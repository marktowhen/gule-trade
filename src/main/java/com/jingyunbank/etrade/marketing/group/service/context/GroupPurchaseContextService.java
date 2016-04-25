package com.jingyunbank.etrade.marketing.group.service.context;

import java.util.ArrayList;
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
import com.jingyunbank.etrade.api.marketing.group.service.IGroupOrderService;
import com.jingyunbank.etrade.api.marketing.group.service.IGroupService;
import com.jingyunbank.etrade.api.marketing.group.service.IGroupUserService;
import com.jingyunbank.etrade.api.marketing.group.service.context.IGroupPurchaseContextService;
import com.jingyunbank.etrade.api.order.presale.bo.Orders;
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
	@Transactional
	public void expire(Group group) {
		List<GroupUser> users = group.getBuyers();
		GroupGoods goods = group.getGoods();
		if(users.size() < goods.getMinpeople()){//未满
			//refund.
			
		}else{//满团
			
		}
	}


	@Override
	public Result<String> startMatch(GroupGoods groupGoods) {
		return Result.ok();
	}


	@Override
	public Result<String> joinMatch(Group group) {
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



}
