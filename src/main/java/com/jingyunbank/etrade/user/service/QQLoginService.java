package com.jingyunbank.etrade.user.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.user.bo.QQLogin;
import com.jingyunbank.etrade.api.user.bo.UserInfo;
import com.jingyunbank.etrade.api.user.bo.Users;
import com.jingyunbank.etrade.api.user.service.IQQLoginService;
import com.jingyunbank.etrade.api.user.service.IUserService;
import com.jingyunbank.etrade.user.dao.QQLoginDao;
import com.jingyunbank.etrade.user.entity.QQLoginEntity;

@Service("qqLoginService")
public class QQLoginService implements IQQLoginService {

	@Autowired
	private QQLoginDao qqLoginDao;
	@Autowired
	private IUserService userService;
	
	@Override
	public QQLogin single(String ID) {
		QQLoginEntity entity = qqLoginDao.selectOne(ID);
		if(entity!=null){
			QQLogin bo = new QQLogin();
			BeanUtils.copyProperties(entity, bo);
			return bo;
		}
		return null;
	}

	@Override
	public boolean save(QQLogin qqlogin) throws DataSavingException {
		QQLoginEntity entity = new QQLoginEntity();
		BeanUtils.copyProperties(qqlogin, entity);
		try {
			return qqLoginDao.insert(entity);
		} catch (Exception e) {
			throw new DataSavingException(e);
		}
	}

	@Override
	public boolean refreshByID(String id, String accessToken, String uid)
			throws DataRefreshingException {
		try {
			return qqLoginDao.update(id, accessToken,  uid);
		} catch (Exception e) {
			throw new DataRefreshingException(e);
		}
	}

	@Override
	public boolean save(QQLogin qq, Users user, UserInfo userInfo) throws DataSavingException {
		save(qq);
		userService.save(user, userInfo, null);
		return true;
	}


}
