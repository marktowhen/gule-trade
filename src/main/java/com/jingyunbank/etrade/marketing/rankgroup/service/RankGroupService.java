package com.jingyunbank.etrade.marketing.rankgroup.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.marketing.rankgroup.bo.RankGroup;
import com.jingyunbank.etrade.api.marketing.rankgroup.service.IRankGroupService;
import com.jingyunbank.etrade.api.order.presale.bo.Orders;
import com.jingyunbank.etrade.api.user.bo.Users;

@Service("rankGroupService")
public class RankGroupService implements IRankGroupService{

	@Override
	public void start(Users leader, RankGroup group, Orders orders)
			throws DataSavingException, DataRefreshingException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void join(Users user, RankGroup group, Orders orders) throws DataSavingException, DataRefreshingException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<RankGroup> listOnDeadline() {
		// TODO Auto-generated method stub
		return null;
	}
	

}
