package com.jingyunbank.etrade.user.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.jingyunbank.core.KeyGen;
import com.jingyunbank.etrade.api.asyn.bo.AsynSchedule;
import com.jingyunbank.etrade.api.asyn.service.IAsynParamService;
import com.jingyunbank.etrade.api.asyn.service.IAsynScheduleService;
import com.jingyunbank.etrade.api.award.bo.SalesUserrelationship;
import com.jingyunbank.etrade.api.award.service.ISalesUserrelationshipService;
import com.jingyunbank.etrade.api.cart.bo.Cart;
import com.jingyunbank.etrade.api.cart.service.ICartService;
import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.user.bo.UserInfo;
import com.jingyunbank.etrade.api.user.bo.Users;
import com.jingyunbank.etrade.api.user.service.IUserService;
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
	@Autowired
	private ICartService cartService;
	@Autowired
	private IAsynScheduleService asynScheduleService;
	@Autowired
	private IAsynParamService asynParamService;
	@Autowired
	private ISalesUserrelationshipService salesUserrelationshipService;
	
	
	
	@Override
	public Optional<Users> single(String id) {
		UserEntity userEntity = new UserEntity();
		userEntity.setID(id);
		userEntity = userDao.selectUser(userEntity);
		return getUsersByEntity(userEntity);
	}

	/**
	 * equals to(phoneExists(key) | unameExists(uname) | emailExists(email))
	 * @param key (username | phone | email)
	 * @return
	 */
	@Override
	public Optional<Users> singleByKey(String key) {
		UserEntity userEntity = userDao.selectOneByKey(key);
		//entity转bo
		return getUsersByEntity(userEntity);
	}

	//保存用户的信息
	@Override
	@Transactional(rollbackFor={DataSavingException.class}, propagation=Propagation.REQUIRED)
	public void save(Users user,UserInfo userInfo, String inviterUID) throws DataSavingException {
		UserEntity userEntity=new UserEntity();
		BeanUtils.copyProperties(user, userEntity);
		
		//密码 前台加密
		userEntity.setTradepwd(userEntity.getPassword());
		try {
			userDao.insert(userEntity);
		} catch (Exception e) {
			throw new DataSavingException(e);
		}
		
		UserInfoEntity userInfoEntity=new UserInfoEntity();
		userInfoEntity.setUID(userEntity.getID());
		userInfoEntity.setRegip(userInfo.getRegip());
		Date date=new Date();
		userInfoEntity.setRegtime(date);
		try {
			userInfoDao.insert(userInfoEntity);
		} catch (Exception e) {
			throw new DataSavingException(e);
		}
		
		//保存用户购物车
		cartService.save(new Cart(KeyGen.uuid(), user.getID()));
		
		//同步三级分销数据
		AsynSchedule schedule = new AsynSchedule();
		schedule.setID(KeyGen.uuid());
		schedule.setServiceName(AsynSchedule.SALES_REGISTER_SERVICE_NAME);
		schedule.setStatus(AsynSchedule.INIT);
		asynScheduleService.save(schedule);
		Map<String, String> params = new HashMap<String, String>();
		params.put("uid",user.getID());//p2p系统用户id
		params.put("username", user.getUsername()); //p2p系统用户名
		params.put("phone", user.getMobile());
		params.put("password", user.getPassword());
		if(!StringUtils.isEmpty(inviterUID)){
			SalesUserrelationship userrelationship = salesUserrelationshipService.singleByUID(inviterUID);
			if(userrelationship!=null){
				params.put("referrer", userrelationship.getSID());//查询推荐人在分销系统中的id
			}
		}
    	asynParamService.saveMutl(schedule.getID(),params);
	}
	

	@Override
	public void refresh(Users user) throws DataRefreshingException {
		UserEntity entity  =  new UserEntity();
		BeanUtils.copyProperties(user, entity);
		try {
			userDao.update(entity);
		} catch (Exception e) {
			throw new DataRefreshingException(e);
		}
	}

	@Override
	public boolean exists(String key) {
		UserEntity userEntity = userDao.selectOneByKey(key);
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

	@Override
	public List<Users> list(List<String> uids) {
		List<UserEntity> entities = userDao.selectMany(uids);
		return entities.stream().map(entity -> {
			Users bo = new Users();
			BeanUtils.copyProperties(entity, bo);
			return bo;
		}).collect(Collectors.toList());
	}

}
