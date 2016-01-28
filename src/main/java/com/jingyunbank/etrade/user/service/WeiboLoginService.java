package com.jingyunbank.etrade.user.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.user.bo.UserInfo;
import com.jingyunbank.etrade.api.user.bo.Users;
import com.jingyunbank.etrade.api.user.bo.WeiboLogin;
import com.jingyunbank.etrade.api.user.service.IUserService;
import com.jingyunbank.etrade.api.user.service.IWeiboLoginService;
import com.jingyunbank.etrade.user.dao.WeiboLoginDao;
import com.jingyunbank.etrade.user.entity.WeiboLoginEntity;

@Service("weiboLoginService")
public class WeiboLoginService implements IWeiboLoginService {

	@Autowired
	private WeiboLoginDao weiboLoginDao;
	@Autowired
	private IUserService userService;
	
	
	@Override
	public WeiboLogin single(String accessTokenOrWeiboUID) {
		WeiboLoginEntity weiboLoginEntity = weiboLoginDao.selectOne(accessTokenOrWeiboUID);
		if(weiboLoginEntity!=null){
			WeiboLogin bo = new WeiboLogin();
			BeanUtils.copyProperties(weiboLoginEntity, bo);
			return bo;
		}
		return null;
	}

	@Override
	public boolean save(WeiboLogin login) throws DataSavingException {
		WeiboLoginEntity entity = new WeiboLoginEntity();
		BeanUtils.copyProperties(login, entity);
		try {
			return weiboLoginDao.insert(entity);
		} catch (Exception e) {
			throw new DataSavingException(e);
		}
	}
	
	@Transactional
	@Override
	public boolean save(WeiboLogin login, Users user, UserInfo info)
			throws DataSavingException {
		save(login);
		userService.save(user, info);
		return true;
	}

	@Override
	public boolean refreshLoginTime(String accessTokenOrWeiboUID)
			throws DataRefreshingException {
		try {
			return weiboLoginDao.updateLoginTime(accessTokenOrWeiboUID);
		} catch (Exception e) {
			throw new DataRefreshingException(e);
		}
	}

	@Override
	public boolean refreshByID(String id, String accessToken, String uid) {
		WeiboLoginEntity entity = new WeiboLoginEntity();
		entity.setID(id);
		entity.setAccessToken(accessToken);
		entity.setUID(uid);
		return weiboLoginDao.updateByID(id, accessToken, uid);
	}

	

}
