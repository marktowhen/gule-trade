package com.jingyunbank.etrade.marketing.group.schedule;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.marketing.group.bo.Group;
import com.jingyunbank.etrade.api.marketing.group.bo.GroupGoods;
import com.jingyunbank.etrade.api.marketing.group.bo.GroupOrder;
import com.jingyunbank.etrade.api.marketing.group.bo.GroupUser;
import com.jingyunbank.etrade.api.marketing.group.service.IGroupGoodsService;
import com.jingyunbank.etrade.api.marketing.group.service.IGroupOrderService;
import com.jingyunbank.etrade.api.marketing.group.service.IGroupService;
import com.jingyunbank.etrade.api.marketing.group.service.IGroupUserService;
import com.jingyunbank.etrade.api.order.presale.service.IOrderService;
import com.jingyunbank.etrade.api.order.presale.service.context.IOrderContextService;
import com.jingyunbank.etrade.config.PropsConfig;

@Component
public class GroupSchedule {
	
	@Autowired
	private IGroupService groupService;
	@Autowired
	private IGroupUserService groupUserService;
	@Autowired
	private IGroupGoodsService groupGoodsService;
	@Autowired
	private IOrderContextService orderContextService;
	@Autowired
	private IGroupOrderService groupOrderService;
	@Autowired
	private IOrderService orderService;
	
	//团长未支付定金 组团失败
	@Scheduled(fixedDelay=(60*1000))//
	public void checkStart() throws DataRefreshingException{
		//查出新建团购且团长未支付押金的列表
		List<Group> groupList = groupService.list(Group.STATUS_NEW);
		if(!groupList.isEmpty()){
			for (Group group : groupList) {
				Optional<GroupUser> gu = groupUserService.single(group.getID(), group.getLeaderUID());
				if(gu.isPresent()){
					//如果未支付且支付超时 将团购置为超时状态
					if(GroupUser.STATUS_CLOSED.equals(gu.get().getStatus())){
						groupService.refreshStatus(group.getID(), group.getStatus(), "TIME_OUT");
					}
				}
			}
		}
	}
	
	
	//到期未组团成功的 退还押金
	@Scheduled(fixedDelay=(60*1000))//
	public void checkConvening() throws DataRefreshingException{
		//查出召集中的团购
		List<Group> groupList = groupService.list(Group.STATUS_CONVENING);
		if(!groupList.isEmpty()){
			for (Group group : groupList) {
				Optional<GroupGoods> groupGoods = groupGoodsService.single(group.getGroupGoodsID());
				if( groupGoods.isPresent()){
					
					//如果未支付且支付超时 将团购置为超时状态
					Date timeOut =new Date( group.getStart().getTime()+groupGoods.get().getDuration());
					if(timeOut.before(new Date()) || new Date().after(groupGoods.get().getDeadline())){
						groupService.refreshStatus(group.getID(), group.getStatus(), "TIME_OUT");
						//退还定金   更改order状态->退款
						
						//发消息通知
						List<GroupUser> groupUserList = groupUserService.list(group.getID(), GroupUser.STATUS_DEPOSIT_PAID);
						
						
					}
				}
			}
		}
	}
	
	//定金逾期未支付 退团
	@Scheduled(fixedDelay=(60*1000))//
	public void checkDeposit() throws DataRefreshingException, DataSavingException{
		//查出召集中的团购
		List<Group> groupList = groupService.list(Group.STATUS_CONVENING);
		if(!groupList.isEmpty()){
			for (Group group : groupList) {
				List<String> timeOutOIDs = new ArrayList<String>();
				//查出未支付定金的用户
				List<GroupUser> groupUserList = groupUserService.listUnPayDeposit(group.getID());
				for (GroupUser groupUser : groupUserList) {
					//判断如果支付超时
					Date timeOut =new Date( groupUser.getJointime().getTime()+PropsConfig.getLong(PropsConfig.GROUP_DEPOSIT_TIME_OUT));
					if(timeOut.before(new Date())){
						//关闭该团购订单
						groupUserService.refreshStatus(groupUser.getID(), groupUser.getStatus(), "TIME_OUT");
						//收集要关闭的订单id
						Optional<GroupOrder> groupOrder = groupOrderService.single(groupUser.getID(), GroupOrder.TYPE_DEPOSIT);
						if(groupOrder.isPresent()){
							timeOutOIDs.add(groupOrder.get().getOID());
						}
					}
				}
				//为防止用户支付已关闭的团购订单 更改Orders状态
				orderContextService.cancel(timeOutOIDs, "超时关闭");
			}
		}
	}
	
	//尾款逾期未支付
	@Scheduled(fixedDelay=(60*1000))//
	public void checkBalancePayment() throws DataRefreshingException, DataSavingException{
		//查出召集中的团购
		List<Group> groupList = groupService.list(Group.STATUS_DEPOSIT_PAID);
		if(!groupList.isEmpty()){
			for (Group group : groupList) {
				List<String> timeOutOIDs = new ArrayList<String>();
				//查出未支付尾款的用户
				List<GroupUser> groupUserList = groupUserService.listUnPayBalance(group.getID());
				for (GroupUser groupUser : groupUserList) {
					//判断如果支付超时
					Optional<GroupOrder> groupOrder = groupOrderService.single(groupUser.getID(), GroupOrder.TYPE_BALANCE_PAYMENT);
					if(groupOrder.isPresent()){
						//下单时间是否超时
						Date timeOut =new Date( orderService.single(groupOrder.get().getOID()).get().getAddtime().getTime()+PropsConfig.getLong(PropsConfig.GROUP_BALANCE_PAYMENT_TIME_OUT));
						if(timeOut.before(new Date())){
							//关闭该团购订单
							groupUserService.refreshStatus(groupUser.getID(), groupUser.getStatus(), "TIME_OUT");
							//收集要关闭的订单id
							timeOutOIDs.add(groupOrder.get().getOID());
						}
					}
				}
				//为防止用户支付已关闭的团购订单 更改Orders状态
				orderContextService.cancel(timeOutOIDs, "超时关闭");
			}
		}
	}
	
	//groupGoods deadline
	
	
	

}
