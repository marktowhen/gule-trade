package com.jingyunbank.etrade.user.service;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.user.IUserInfoService;
import com.jingyunbank.etrade.api.user.bo.UserInfo;
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

	
	@Override
	public Optional<UserInfo> getByPhone(String phone) {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public Optional<UserInfo> getByUname(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public Optional<UserInfo> getByEmail(String email) {
		// TODO Auto-generated method stub
		return null;
	}


	/* (non-Javadoc)
	 * @see com.jingyunbank.etrade.api.user.IUserInfoService#userInfoVO(java.lang.String)
	 */

	public int UidExists(String uid) {
		

		return userInfoDao.UidExists(uid);
	}

}
