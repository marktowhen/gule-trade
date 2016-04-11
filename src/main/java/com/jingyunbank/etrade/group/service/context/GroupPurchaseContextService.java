package com.jingyunbank.etrade.group.service.context;

import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.group.bo.Group;
import com.jingyunbank.etrade.api.group.service.context.IGroupPurchaseContextService;
import com.jingyunbank.etrade.api.user.bo.Users;

@Service("groupPurchaseContextService")
public class GroupPurchaseContextService implements IGroupPurchaseContextService{

	@Override
	public void start(Users leader, Group group) {
		
	}

	@Override
	public void join(Users user, Group group) {
		
	}

	@Override
	public void expire(Group group) {
		
	}

}
