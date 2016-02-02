package com.jingyunbank.etrade.user.service;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.user.bo.UserInfo;
import com.jingyunbank.etrade.api.user.service.IUserInfoService;
import com.jingyunbank.etrade.api.vip.point.service.context.IPointContextService;
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
	@Autowired
	private IPointContextService pointContextService;
	
	@Override
	public boolean save(UserInfo uinfo) throws DataSavingException {
		UserInfoEntity userInfoEntity=new UserInfoEntity();
		BeanUtils.copyProperties(uinfo, userInfoEntity);
		
		try {
			return userInfoDao.insert(userInfoEntity);
		} catch (Exception e) {
			throw new DataSavingException(e);
		}
	}

	
	@Override
	@Transactional
	public boolean refresh(UserInfo uinfo) throws DataRefreshingException{
		UserInfoEntity userInfoEntity=new UserInfoEntity();
		BeanUtils.copyProperties(uinfo, userInfoEntity);
		try {
			 userInfoDao.update(userInfoEntity);
			 if(ifGivePoint(uinfo.getUID())){
				 pointContextService.addPoint(uinfo.getUID(), 50, "信息完善啦！");
				 refreshIsPoint(uinfo.getUID(), true);
			 }
			 return true;
		} catch (Exception e) {
			throw new DataRefreshingException(e);
		}
	}
	
	private boolean refreshIsPoint(String uid, boolean isPoint){
		return userInfoDao.updateIsPoint(uid, isPoint);
	}
	
	/**
	 * 判断是否要奖励用户 50积分
	 * @param uid
	 * @return
	 * 2016年2月2日 qxs
	 */
	private boolean ifGivePoint(String uid){
		Optional<UserInfo> optional = this.getByUid(uid);
		if(optional.isPresent()){
			UserInfo userInfo = optional.get();
			if(!userInfo.isPoint() && !StringUtils.isEmpty(userInfo.getBirthday()) && !StringUtils.isEmpty(userInfo.getAddress())&& !StringUtils.isEmpty(userInfo.getCity()) 
					&&!StringUtils.isEmpty(userInfo.getCountry())&& !StringUtils.isEmpty(userInfo.getProvince())&&!StringUtils.isEmpty(userInfo.getEducation())
					&& !StringUtils.isEmpty(userInfo.getJob())&& !StringUtils.isEmpty(userInfo.getIncome())){
				return true;
			}
		}
		return false;
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
