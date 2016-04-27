package com.jingyunbank.etrade.weixin.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.user.bo.Address;
import com.jingyunbank.etrade.api.weixin.bo.SNSUserInfoBo;
import com.jingyunbank.etrade.api.weixin.service.IWeiXinUserService;
import com.jingyunbank.etrade.weixin.dao.WeiXinUserDao;
import com.jingyunbank.etrade.weixin.entity.SNSUserInfo;

@Service
public class WeiXinUserService implements IWeiXinUserService{
	@Autowired
	private WeiXinUserDao weixinUserDao;
	
	@Override
	public void addUser(SNSUserInfoBo userInfo) throws DataSavingException, DataRefreshingException {
		
		try {
			weixinUserDao.addUser(getEntityFromBo(userInfo));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new DataSavingException(e);
		}
		
	}
	
	/**
	 * bo转entity
	 * @param bo
	 * @return entity
	 * 2015年11月5日 qxs
	 */
	private SNSUserInfo getEntityFromBo(SNSUserInfoBo  bo){
		SNSUserInfo  entity = null;
		if(bo!=null){
			entity = new SNSUserInfo();
			BeanUtils.copyProperties(bo, entity);
		}
		return entity;
	}


}
