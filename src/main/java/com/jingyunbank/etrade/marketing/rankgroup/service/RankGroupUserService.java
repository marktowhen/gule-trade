package com.jingyunbank.etrade.marketing.rankgroup.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.marketing.rankgroup.bo.RankGroupUser;
import com.jingyunbank.etrade.api.marketing.rankgroup.service.IRankGroupUserService;
import com.jingyunbank.etrade.api.user.bo.Users;
import com.jingyunbank.etrade.marketing.rankgroup.dao.RankGroupUserDao;
import com.jingyunbank.etrade.marketing.rankgroup.entity.RankGroupUserEntity;

@Service("rankGroupUserService")
public class RankGroupUserService implements IRankGroupUserService{
	@Autowired
	RankGroupUserDao rankGroupUserDao;

	@Override
	public Integer count(String groupID, String status) {
		
		return rankGroupUserDao.count(groupID, status);
	}

	@Override
	public boolean sendMessage(Users user, String message) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean save(RankGroupUser groupUser) throws DataSavingException {
		RankGroupUserEntity entity = new RankGroupUserEntity();
		BeanUtils.copyProperties(groupUser, entity);
		try {
			return rankGroupUserDao.insert(entity);
		} catch (Exception e) {
			throw new DataSavingException(e);
		}
	}

	@Override
	public Optional<RankGroupUser> single(String ID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<RankGroupUser> list(String groupID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<RankGroupUser> list(String groupID, String status) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean refreshStatus(String ID, String status) throws DataRefreshingException {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	

}
