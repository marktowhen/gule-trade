package com.jingyunbank.etrade.user.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.core.KeyGen;
import com.jingyunbank.core.Range;
import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.user.IUserService;
import com.jingyunbank.etrade.api.user.bo.UserInfo;
import com.jingyunbank.etrade.api.user.bo.Users;
import com.jingyunbank.etrade.base.util.Md5Util;
import com.jingyunbank.etrade.user.dao.UserDao;
import com.jingyunbank.etrade.user.dao.UserInfoDao;
import com.jingyunbank.etrade.user.entity.UserEntity;
import com.jingyunbank.etrade.user.entity.UserInfoEntity;

@Service("userService")
public class UserService implements IUserService{
	@Autowired
	private UserDao userDao;
	@Autowired
	private UserInfoDao userInfoDao;
	@Override
	public Optional<Users> getByUid(String id) {
		UserEntity userEntity = new UserEntity();
		userEntity.setID(id);
		userEntity = userDao.selectUser(userEntity);
		return getUsersByEntity(userEntity);
	}

	@Override
	public Optional<Users> getByPhone(String phone) {
		UserEntity userEntity = new UserEntity();
		userEntity.setMobile(phone);
		userEntity = userDao.selectUser(userEntity);
		return getUsersByEntity(userEntity);
	}

	@Override
	public Optional<Users> getByUname(String username) {
		UserEntity userEntity = new UserEntity();
		userEntity.setUsername(username);
		userEntity = userDao.selectUser(userEntity);
		return getUsersByEntity(userEntity);
	}

	@Override
	public Optional<Users> getByEmail(String email) {
		UserEntity user = new UserEntity();
		user.setEmail(email);
		UserEntity userEntity = userDao.selectUser(user);
		return getUsersByEntity(userEntity);
	}
	/**
	 * equals to(phoneExists(key) | unameExists(uname) | emailExists(email))
	 * @param key (username | phone | email)
	 * @return
	 */
	@Override
	public Optional<Users> getByKey(String key) {
		UserEntity userEntity = userDao.selectUserByLoginKey(key);
		//entity转bo
		return getUsersByEntity(userEntity);
	}

	//保存用户的信息
	@Override
	public boolean save(Users user,UserInfo userInfo) throws DataSavingException {
		UserEntity userEntity=new UserEntity();
		BeanUtils.copyProperties(user, userEntity);
		
		/*info.getRegip();*/
			int result=0;
			boolean flag=false;
			//密码加密
			userEntity.setPassword(Md5Util.getMD5(user.getPassword()));
			userEntity.setTradepwd(Md5Util.getMD5(user.getTradepwd()));
			userEntity.setID(KeyGen.uuid());
			try {
				result=userDao.insert(userEntity);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				throw new DataSavingException();
			}
			
			UserInfoEntity userInfoEntity=new UserInfoEntity();
			userInfoEntity.setUid(userEntity.getID());
			userInfoEntity.setRegip(userInfo.getRegip());
			Date date=new Date();
			userInfoEntity.setRegtime(date);
			try {
				if(result>0 && userInfoDao.insert(userInfoEntity)){
				flag=true;
				}else{
				flag=false;
				}
			} catch (Exception e) {
				throw new DataSavingException();
				}
			return flag;
			
	}
	

	@Override
	public boolean refresh(Users user) throws DataRefreshingException {
		UserEntity entity  =  new UserEntity();
		BeanUtils.copyProperties(user, entity);
		try {
			return userDao.update(entity);
		} catch (Exception e) {
			throw new DataRefreshingException();
		}
	}

	@Override
	public List<Users> list(Range range) {
		return null;
	}

	@Override
	public List<Users> list(Date start, Date end) {
		return null;
	}
    //判断用户是否存在
	@Override
	public boolean phoneExists(String phone) {
		int result=0;
		boolean flag=false;
		result=userDao.phoneExists(phone);
		if(result>0){
			flag=true;
		}else{
			flag=false;
		}
		return flag;
	}
	//判断用户名是否存在
	@Override
	public boolean unameExists(String uname) {
		int result=0;
		boolean flag=false;
		result=userDao.unameExists(uname);
		if(result>0){
			flag=true;
		}else{
			flag=false;
		}
		return flag;
	}
   //判断邮箱是否存在
	@Override
	public boolean emailExists(String email) {
		int result=0;
		boolean flag=false;
		result=userDao.emailExists(email);
		if(result>0){
			flag=true;
		}else{
			flag=false;
		}
		return flag;
	}

	@Override
	public boolean exists(String key) {
		UserEntity userEntity = userDao.selectUserByLoginKey(key);
		if(userEntity!=null){
			return true;
		}
		return false;
	}
	/**
	 * userEntity转为Optional<Users>
	 * @param userEntity
	 * @return
	 */
	private Optional<Users> getUsersByEntity(UserEntity userEntity){
		if(userEntity!=null){
			Users users = new Users();
			BeanUtils.copyProperties(userEntity, users);
			return Optional.of(users);
		}
		return Optional.empty();
	}

	/* (non-Javadoc)
	 * @see com.jingyunbank.etrade.api.user.IUserService#queryMaxId()
	 */

	

}
