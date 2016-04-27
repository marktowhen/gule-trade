package com.jingyunbank.etrade.marketing.group.service.context;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jingyunbank.etrade.api.marketing.group.bo.Group;
import com.jingyunbank.etrade.api.marketing.group.bo.GroupGoods;
import com.jingyunbank.etrade.api.marketing.group.bo.GroupUser;
import com.jingyunbank.etrade.api.marketing.group.service.IGroupGoodsService;
import com.jingyunbank.etrade.api.marketing.group.service.IGroupOrderService;
import com.jingyunbank.etrade.api.marketing.group.service.IGroupService;
import com.jingyunbank.etrade.api.marketing.group.service.IGroupUserService;
import com.jingyunbank.etrade.api.marketing.group.service.context.IGroupPurchaseContextService;
import com.jingyunbank.etrade.api.marketing.group.service.context.IGroupRobotService;
import com.jingyunbank.etrade.api.order.presale.service.IOrderService;
import com.jingyunbank.etrade.api.order.presale.service.context.IOrderContextService;
import com.jingyunbank.etrade.config.PropsConfig;

@Service("groupRobotService")
public class GroupRobotService implements IGroupRobotService{
	private Logger logger = LoggerFactory.getLogger(GroupRobotService.class);
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
	@Autowired
	private IGroupPurchaseContextService groupPurchaseContextService;

	@Override
	@Transactional
	public void closeUnstartGroup() {
		//查出新建团购且团长未支付押金的列表
		List<Group> groupList = groupService.listStartFail();
		if(!groupList.isEmpty()){
			for (Group group : groupList) {
				try {
					groupPurchaseContextService.dismiss(group);
				} catch (Exception e) {
					logger.error("closeUnstartGroup Fail: groupID="+group.getID()+"  reason"+e.getMessage());
				} 
			}
		}
	}
	
	@Override
	@Transactional
	public void startSuccess() {
		List<Group> groupList = groupService.listStartSuccess();
		if(!groupList.isEmpty()){
			for (Group group : groupList) {
				try {
					groupPurchaseContextService.startSuccess(group);
				} catch (Exception e) {
					logger.error("startSuccess Fail: groupID="+group.getID()+"  reason"+e.getMessage());
				} 
			}
		}
		
	}

	@Override
	@Transactional
	public void closeConveneFailGroup()  {
		//查出召集中的团购
		List<Group> groupList = groupService.listConvening();
		if(!groupList.isEmpty()){
			for (Group group : groupList) {
				try {
					GroupGoods groupGoods = group.getGoods();
					if( Objects.nonNull(groupGoods)){
						//如果未支付且支付超时 将团购置为超时状态
						Date timeOut =new Date( group.getStart().getTime()+groupGoods.getDuration());
						if(timeOut.before(new Date()) || new Date().after(groupGoods.getDeadline())){
							//解散
							group.setBuyers(groupUserService.list(group.getID(), GroupUser.STATUS_PAID));
							groupPurchaseContextService.dismiss(group);
						}
					}
				} catch (Exception e) {
					logger.error("closeConveneFailGroup Fail: groupID="+group.getID()+"  reason"+e.getMessage());
				} 
			}
		}
	}

	@Override
	@Transactional
	public void payTimeout()  {
		//查出未支付的用户
		List<GroupUser> groupUserList = groupUserService.listUnPay();
		for (GroupUser groupUser : groupUserList) {
			try {
				//判断如果支付超时
				Date timeOut =new Date( groupUser.getJointime().getTime()+PropsConfig.getLong(PropsConfig.GROUP_PAY_TIME_OUT));
				if(timeOut.before(new Date())){
					//关闭该团购订单
					groupPurchaseContextService.payTimeout(groupUser);
				}
			} catch (Exception e) {
				logger.error("payTimeout Fail: groupUserID="+groupUser.getID()+"  reason"+e.getMessage());
			}
		}
	}

	@Override
	@Transactional
	public void expire()  {
		//3分钟内已到期的团购用户
		List<Group> guList = groupService.listOnDeadline(3);
		for (Group group : guList) {
			try {
				if(group.getBuyers().size()>=group.getGoods().getMinpeople()){
					//如果满团 组团成功
					groupPurchaseContextService.fullMission(group);
				}else{
					//没满团解散
					groupPurchaseContextService.dismiss(group);
				}
			} catch (Exception e) {
				logger.error("deadline Fail: groupID="+group.getID()+"  reason"+e.getMessage());
			}
		}
		
	}

	@Override
	@Transactional
	public void finish() {
		List<Group> guList = groupService.listOnSuccess();
		guList.stream().forEach(group->{
			try {
				groupPurchaseContextService.fullMission(group);
			} catch (Exception e) {
				logger.error("startSuccess Fail: groupID="+group.getID()+"  reason"+e.getMessage());
			}
			
		});

	}

	

}
