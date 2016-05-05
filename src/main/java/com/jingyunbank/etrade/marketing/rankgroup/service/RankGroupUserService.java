package com.jingyunbank.etrade.marketing.rankgroup.service;

import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.marketing.rankgroup.service.IRankGroupUserService;
import com.jingyunbank.etrade.api.user.bo.Users;

@Service("rankGroupUserService")
public class RankGroupUserService implements IRankGroupUserService{

	@Override
	public Integer count(String groupID, String status) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean sendMessage(Users user, String message) {
		// TODO Auto-generated method stub
		return false;
	}

}
