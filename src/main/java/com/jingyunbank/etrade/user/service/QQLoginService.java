package com.jingyunbank.etrade.user.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.user.bo.QQLogin;
import com.jingyunbank.etrade.api.user.service.IQQLoginService;
import com.jingyunbank.etrade.user.dao.QQLoginDao;
import com.jingyunbank.etrade.user.entity.QQLoginEntity;

@Service("qqLoginService")
public class QQLoginService implements IQQLoginService {

	@Autowired
	private QQLoginDao qqLoginDao;
	
	@Override
	public QQLogin single(String accessToken) {
		QQLoginEntity entity = qqLoginDao.selectOne(accessToken);
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
	public boolean refreshLoginTime(String accessToken)
			throws DataRefreshingException {
		try {
			return qqLoginDao.updateLoginTime(accessToken);
		} catch (Exception e) {
			throw new DataRefreshingException(e);
		}
	}

}
