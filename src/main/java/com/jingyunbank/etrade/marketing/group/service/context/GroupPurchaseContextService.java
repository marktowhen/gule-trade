package com.jingyunbank.etrade.marketing.group.service.context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jingyunbank.core.KeyGen;
import com.jingyunbank.core.Result;
import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.flow.service.IFlowStatusService;
import com.jingyunbank.etrade.api.marketing.group.bo.Group;
import com.jingyunbank.etrade.api.marketing.group.bo.GroupGoods;
import com.jingyunbank.etrade.api.marketing.group.bo.GroupOrder;
import com.jingyunbank.etrade.api.marketing.group.bo.GroupUser;
import com.jingyunbank.etrade.api.marketing.group.service.IGroupGoodsService;
import com.jingyunbank.etrade.api.marketing.group.service.IGroupOrderService;
import com.jingyunbank.etrade.api.marketing.group.service.IGroupService;
import com.jingyunbank.etrade.api.marketing.group.service.IGroupUserService;
import com.jingyunbank.etrade.api.marketing.group.service.context.IGroupPurchaseContextService;
import com.jingyunbank.etrade.api.order.presale.bo.Orders;
import com.jingyunbank.etrade.api.order.presale.service.IOrderGoodsService;
import com.jingyunbank.etrade.api.order.presale.service.context.IOrderContextService;
import com.jingyunbank.etrade.api.user.bo.Users;

@Service("groupPurchaseContextService")
public class GroupPurchaseContextService implements IGroupPurchaseContextService{

	//private Logger logger = LoggerFactory.getLogger(GroupPurchaseContextService.class);
	@Autowired
	private IGroupService groupService;
	@Autowired
	private IGroupUserService groupUserService;
	@Autowired
	private IGroupOrderService groupOrderService;
	@Autowired
	private IOrderContextService orderContextService;
	@Autowired
	private IFlowStatusService flowStatusService;
	@Autowired
	private IOrderGoodsService orderGoodsService;
	@Autowired
	private IGroupGoodsService groupGoodsService;
	
	@Override
	@Transactional
	public void start(Users leader, Group group, Orders orders) throws DataSavingException, DataRefreshingException {
		group.setLeaderUID(leader.getID());
		group.setStart(new Date());
		//save group.
		groupService.save(group);
		GroupUser user = new GroupUser();
		user.setID(KeyGen.uuid());
		user.setGroupID(group.getID());
		user.setJointime(new Date());
		user.setUID(leader.getID());
		user.setPaid(group.getGoods().getDeposit());
		user.setStatus(GroupUser.STATUS_NEW);
		//save group user
		groupUserService.save(user);
		
		//保存订单
		List<Orders> orderList = new ArrayList<Orders>();
		orderList.add(orders);
		orderContextService.save(orderList);
		
		GroupOrder go = new GroupOrder();
		go.setGroupID(group.getID());
		go.setGroupUserID(user.getID());
		go.setID(KeyGen.uuid());
		go.setOID(orders.getID());
		go.setOrderno(orders.getOrderno());
		go.setType(GroupOrder.TYPE_FULL);
		groupOrderService.save(go);
		
	}

	
	@Override
	@Transactional
	public void join(Users user, Group group, Orders orders) throws DataSavingException, DataRefreshingException {
		GroupUser guser = new GroupUser();
		guser.setID(KeyGen.uuid());
		guser.setGroupID(group.getID());
		guser.setJointime(new Date());
		guser.setUID(user.getID());
		guser.setPaid(group.getGoods().getDeposit());
		guser.setStatus(GroupUser.STATUS_NEW);
		//save group user
		groupUserService.save(guser);
		//保存订单
		List<Orders> orderList = new ArrayList<Orders>();
		orderList.add(orders);
		orderContextService.save(orderList);
		
		GroupOrder go = new GroupOrder();
		go.setGroupID(group.getID());
		go.setGroupUserID(user.getID());
		go.setID(KeyGen.uuid());
		go.setOID(orders.getID());
		go.setOrderno(orders.getOrderno());
		go.setType(GroupOrder.TYPE_FULL);
		groupOrderService.save(go);
		
	}

	@Override
	public Result<String> startMatch(GroupGoods groupGoods) {
		if(groupGoods.getDeadline().before(new Date())){
			return Result.fail("该团购已结束,请及时参加其他活动");
		}
		
		return Result.ok();
	}

	@Override
	public Result<String> joinMatch(Group group) {
		if(!Group.STATUS_CONVENING.equals(group.getStatus())){
			return Result.fail("团购非召集中,请选择其他团购");
		}
		//团购人数
		Optional<GroupGoods> gg = groupGoodsService.single(group.getGroupGoodsID());
		if(!gg.isPresent()){
			return Result.fail("团购商品不存在,请选择其他团购");
		}
		
		if(gg.get().getDeadline().before(new Date())){
			return Result.fail("该团购已结束,请及时参加其他活动");
		}
		return Result.ok();
	}

	@Override
	public void payFinish(Orders order) throws DataRefreshingException {
		Optional<GroupOrder> go = groupOrderService.single( order.getID());
		if(go.isPresent()){
			Optional<GroupUser> gu = groupUserService.single(go.get().getGroupUserID());
			if(gu.isPresent()){
				groupUserService.refreshStatus(gu.get().getID(), gu.get().getStatus(), order.getStatusCode());
			}
		}
	}
	
	@Override
	public void refound(List<GroupUser> groupUserList) throws DataRefreshingException, DataSavingException {
		//关闭团购订单
		for(GroupUser user : groupUserList){
			if(GroupUser.STATUS_PAID.equals(user.getStatus())){
				groupUserService.refreshStatus(user.getID(), GroupUser.STATUS_REFUNED);
				Optional<GroupOrder> go = groupOrderService.single(user.getID(), GroupOrder.TYPE_FULL);
				if(go.isPresent()){
					String gid = groupGoodsService.singleByGroupID(user.getGroupID()).get().getGID();
					//ogid 应该由skuid获取
					String ogid =  orderGoodsService.singleOrderGoods(go.get().getOID(), gid).get().getID();
					orderContextService.refund(go.get().getOID(),ogid);
				}
				//通知用户
				groupUserService.notice(user, "退款通知");
				//订单状态修改？
			}
		}
	}


	@Override
	public void fullMission(Group group) throws DataRefreshingException {
		groupService.refreshStatus(group.getID(), Group.STATUS_PAID);
		//通知用户
		groupUserService.list(group.getID(), GroupUser.STATUS_PAID).stream().forEach(user->{
			groupUserService.notice(user, "组团成功");
		});
	}


	@Override
	public void dismiss(Group group) throws DataRefreshingException, DataSavingException {
		groupService.refreshStatus(group.getID(), Group.STATUS_CLOSED);
		this.refound(group.getBuyers());
	}

	@Override
	public void payTimeout(GroupUser groupUser) throws DataRefreshingException, DataSavingException {
		//通知用户
		groupUserService.notice(groupUser, "支付超时退出");
		groupUserService.refreshStatus(groupUser.getID(), GroupUser.STATUS_CLOSED);
		
		//收集要关闭的订单id
		Optional<GroupOrder> groupOrder = groupOrderService.single(groupUser.getID(), GroupOrder.TYPE_FULL);
		//为防止用户支付已关闭的团购订单 更改Orders状态
		orderContextService.cancel(Arrays.asList(groupOrder.get().getOID()), "超时关闭");
	}


	@Override
	public void startSuccess(Group group) throws DataRefreshingException {
		groupService.refreshStatus(group.getID(), Group.STATUS_CONVENING);
		GroupUser user = new GroupUser();
		user.setUID(group.getLeaderUID());
		user.setGroupID(group.getID());
		groupUserService.notice(user, "创建成功");
	}



}
