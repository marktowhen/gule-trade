package com.jingyunbank.etrade.marketing.group.service.context;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.marketing.group.bo.Group;
import com.jingyunbank.etrade.api.marketing.group.bo.GroupGoods;
import com.jingyunbank.etrade.api.marketing.group.bo.GroupUser;
import com.jingyunbank.etrade.api.marketing.group.service.context.IGroupPurchaseContextService;
import com.jingyunbank.etrade.api.user.bo.Users;

@Service("groupPurchaseContextService")
public class GroupPurchaseContextService implements IGroupPurchaseContextService{

	@Override
	public void start(Users leader, Group group) {
		GroupGoods goods = group.getGoods();
		group.setLeader(leader);
		group.setStart(new Date());
		//save group.
		GroupUser user = new GroupUser();
		user.setGroup(group);
		user.setJointime(new Date());
		user.setUser(leader);
		user.setPaid(goods.getDeposit());
		//save group user
		
	}

	@Override
	public void join(Users user, Group group) {
		GroupGoods goods = group.getGoods();
		GroupUser guser = new GroupUser();
		guser.setGroup(group);
		guser.setJointime(new Date());
		guser.setUser(user);
		guser.setPaid(goods.getDeposit());
		//save group user
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

}
