package com.jingyunbank.etrade.user.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.jingyunbank.core.Range;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.exception.DataUpdatingException;
import com.jingyunbank.etrade.api.user.IUserService;
import com.jingyunbank.etrade.api.user.bo.Users;
import com.jingyunbank.etrade.user.dao.UserDao;
import com.jingyunbank.etrade.user.entity.UserEntity;

@Service("userService")
public class UserService implements IUserService{
	@Autowired
	private UserDao userDao;
	
	
	
	
	@Override
	public Optional<Users> getByUid(String id) {
		return null;
	}

	@Override
	public Optional<Users> getByPhone(String phone) {
		return null;
	}

	@Override
	public Optional<Users> getByUname(String username) {
		return null;
	}

	@Override
	public Optional<Users> getByEmail(String email) {
		UserEntity user = new UserEntity();
		user.setEmail(email);
		UserEntity userEntity = userDao.selectUser(user);
		if(userEntity!=null){
			//entityתΪBo
			try {
				Users userBo = new Users();
				BeanUtils.copyProperties( userEntity,userBo);
				return Optional.of(userBo);
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		return Optional.empty();
	}
	/**
	 * equals to(phoneExists(key) | unameExists(uname) | emailExists(email))
	 * @param key (username | phone | email)
	 * @return
	 */
	@Override
	public Optional<Users> getByKey(String key) {
		UserEntity userEntity = userDao.selectUserByLoginKey(key);
		//entityתΪBo
		if(userEntity!=null){
			Users user = new Users();
			try {
				BeanUtils.copyProperties(userEntity, user);
				return Optional.of(user);
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		return Optional.empty();
	}

	@Override
	public boolean save(Users user) throws DataSavingException {
		UserEntity userEntity=new UserEntity();
		BeanUtils.copyProperties(user, userEntity);
			return userDao.insert(userEntity);
	}
	

	@Override
	public boolean update(Users user) throws DataUpdatingException {
		return false;
	}

	@Override
	public List<Users> list(Range range) {
		return null;
	}

	@Override
	public List<Users> list(Date start, Date end) {
		return null;
	}

	@Override
	public boolean phoneExists(String phone) {
		return userDao.phoneExists(phone);
	}

	@Override
	public boolean unameExists(String uname) {
		return userDao.unameExists(uname);
	}

	@Override
	public boolean emailExists(String email) {
		return userDao.emailExists(email);
	}

	@Override
	public boolean exists(String key) {
		return false;
	}

}
