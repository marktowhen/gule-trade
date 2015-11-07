package com.jingyunbank.etrade.user.service;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.exception.DataUpdatingException;
import com.jingyunbank.etrade.api.user.IUserInfoService;
import com.jingyunbank.etrade.api.user.bo.UserInfo;
import com.jingyunbank.etrade.user.dao.UserDao;
import com.jingyunbank.etrade.user.dao.UserInfoDao;
import com.jingyunbank.etrade.user.entity.UserEntity;
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
		
		if(userInfoDao.insert(userInfoEntity)){
			flag=true;
		}else{
			flag=false;
		}
		return flag;
		
	}

	
	@Override
	public boolean update(UserInfo uinfo) throws DataUpdatingException {
		boolean flag=false;
		int result=0;
		UserInfoEntity userInfoEntity=new UserInfoEntity();
		BeanUtils.copyProperties(uinfo, userInfoEntity);
		result=userInfoDao.update(userInfoEntity);
		if(result>0){
			flag=true;
		}else{
			flag=false;
		}
		
		return flag;
	}

	
	@Override
	public Optional<UserInfo> getByUid(String id) {
		// TODO Auto-generated method stub
		return null;
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

}
