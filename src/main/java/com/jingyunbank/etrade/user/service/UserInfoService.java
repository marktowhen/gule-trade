package com.jingyunbank.etrade.user.service;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.user.bo.UserInfo;
import com.jingyunbank.etrade.api.user.service.IUserInfoService;
import com.jingyunbank.etrade.user.dao.UserInfoDao;
import com.jingyunbank.etrade.user.entity.UserInfoEntity;

/**
 * @author Administrator 
 * @date 2015年11月6日
	@todo TODO
 */
@Service("userInfoService")
public class UserInfoService implements IUserInfoService{
	
	@Autowired 
	private UserInfoDao userInfoDao;
	
	@Override
	public boolean save(UserInfo uinfo) throws DataSavingException {
		boolean flag=false;
		
		UserInfoEntity userInfoEntity=new UserInfoEntity();
		BeanUtils.copyProperties(uinfo, userInfoEntity);
		
		try {
			if(userInfoDao.insert(userInfoEntity)){
				flag=true;
			}else{
				flag=false;
			}
		} catch (Exception e) {
			throw new DataSavingException(e);
		}
		return flag;
		
	}

	
	@Override
	public boolean refresh(UserInfo uinfo) throws DataRefreshingException{
		boolean flag=false;
		
		UserInfoEntity userInfoEntity=new UserInfoEntity();
		BeanUtils.copyProperties(uinfo, userInfoEntity);
		
		try {
			if(userInfoDao.update(userInfoEntity)){
				flag=true;
			}else{
				flag=false;
			}
		} catch (Exception e) {
			throw new DataRefreshingException(e);
		}
		
		return flag;
	}

	
	@Override
	public Optional<UserInfo> getByUid(String id) {
		// TODO Auto-generated method stub
		UserInfoEntity userInfoEntity=new UserInfoEntity();
		userInfoEntity=userInfoDao.selectByUid(id);
		UserInfo userInfo=new UserInfo();
		BeanUtils.copyProperties(userInfoEntity, userInfo);
		return Optional.of(userInfo);
	}

	

	public int UidExists(String uid) {
		

		return userInfoDao.UidExists(uid);
	}


	@Override
	public boolean refreshPicture(UserInfo uinfo)	throws DataRefreshingException {
		 UserInfoEntity userInfoEntity = new UserInfoEntity();
		 userInfoEntity.setUID(uinfo.getUID());
		 userInfoEntity.setPicture(uinfo.getPicture());
		try {
			return userInfoDao.updatePicture(userInfoEntity);
		} catch (Exception e) {
			throw new DataRefreshingException(e);
		}
	}

}
