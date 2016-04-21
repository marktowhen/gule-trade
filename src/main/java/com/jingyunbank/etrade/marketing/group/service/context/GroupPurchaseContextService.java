package com.jingyunbank.etrade.marketing.group.service.context;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.core.Result;
import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.marketing.group.bo.Group;
import com.jingyunbank.etrade.api.marketing.group.bo.GroupGoods;
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

	@Autowired
	private IGroupService groupService;
	@Autowired
	private IGroupUserService groupUserService;
	@Autowired
	private IGroupOrderService groupOrderService;
	@Autowired
	private IOrderContextService orderContextService;
	
	@Override
	public void start(Users leader, Group group, List<Orders> orders) throws DataSavingException, DataRefreshingException {
		GroupGoods goods = group.getGoods();
		group.setLeaderUID(leader.getID());
		group.setStart(new Date());
		//save group.
		groupService.save(group);
		GroupUser user = new GroupUser();
		user.setGroup(group);
		user.setJointime(new Date());
		user.setUID(leader.getID());
		user.setPaid(goods.getDeposit());
		//save group user
		groupUserService.save(user);
		
		//保存订单
		orderContextService.save(orders);
		
	}

	
	@Override
	public void join(Users user, Group group, List<Orders> orders) throws DataSavingException, DataRefreshingException {
		GroupGoods goods = group.getGoods();
		GroupUser guser = new GroupUser();
		guser.setGroup(group);
		guser.setJointime(new Date());
		guser.setUser(user);
		guser.setPaid(goods.getDeposit());
		//save group user
		groupUserService.save(guser);
		//保存订单
		orderContextService.save(orders);
		
	}

	@Override
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

}
