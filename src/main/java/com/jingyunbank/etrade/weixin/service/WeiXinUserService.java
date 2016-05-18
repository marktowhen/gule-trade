package com.jingyunbank.etrade.weixin.service;


import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.weixin.bo.SNSUserInfoBo;
import com.jingyunbank.etrade.api.weixin.service.IWeiXinUserService;
import com.jingyunbank.etrade.user.entity.AddressEntity;
import com.jingyunbank.etrade.weixin.dao.WeiXinUserDao;
import com.jingyunbank.etrade.weixin.entity.SNSUserInfo;

@Service
public class WeiXinUserService implements IWeiXinUserService{
	@Autowired
	private WeiXinUserDao weixinUserDao;
	
	@Override
	public boolean addUser(SNSUserInfoBo userInfo) throws DataSavingException, DataRefreshingException {
		
		try {
			int num=weixinUserDao.addUser(getEntityFromBo(userInfo));
			if(num>0){return true;}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new DataSavingException(e);
		}
		return false;
		
	}
	
	@Override
	public Optional<SNSUserInfoBo> getUsers(String openId) {
		SNSUserInfo snsUserInfo=weixinUserDao.selectUserByOpenid(openId);
		if(snsUserInfo!=null){
			return Optional.of(getBoFromEntity(snsUserInfo));
		}else{
			return null;
		}
		
	}


	
	/*@Override
	public List<SNSUserInfoBo> getUsers() {
		List<SNSUserInfoBo> result=null;
		List<SNSUserInfo> entity= weixinUserDao.selectUser();
		if(!entity.isEmpty()&&entity!=null){
			result=new ArrayList<SNSUserInfoBo>();
			for (SNSUserInfo snsUserInfo : entity) {
				result.add(getBoFromEntity(snsUserInfo));
				
			}
		}
		
		return result;
	}*/

	
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
	/**
	 * entity 转 BO
	 * @param addressEntity
	 * @return
	 * 2015年11月5日 qxs
	 */
	private SNSUserInfoBo getBoFromEntity(SNSUserInfo snsUserInfo) {
		SNSUserInfoBo bo = null;
		if(snsUserInfo!=null){
			bo = new SNSUserInfoBo();
			BeanUtils.copyProperties(snsUserInfo, bo);
		}
		return bo;
	}










}
