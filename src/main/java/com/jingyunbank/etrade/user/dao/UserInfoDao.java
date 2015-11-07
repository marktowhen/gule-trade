/**
 * @Title:UserInfoDao.java
@Description:TODO
@author:Administrator
@date:上午10:25:09

 */
package com.jingyunbank.etrade.user.dao;

import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.user.entity.UserInfoEntity;

/**
 * @author guoyuxue
 * @date 2015年11月6日
	@todo 个人资料的信息
 */
public interface UserInfoDao {
	/**
	 * 保存个人资料信息
	 * @param userInfoEntity
	 * @return
	 * @throws DataSavingException
	 */
	public boolean insert(UserInfoEntity userInfoEntity) throws Exception;
	/**
	 * 通过uid查询个人资料信息
	 * @param uid
	 * @return
	 */
	public UserInfoEntity selectByUid(String uid);
	/**
	 * 修改个人信息
	 * @param userInfoEntity
	 * @return
	 * @throws DataUpdatingException
	 */
	public boolean update(UserInfoEntity userInfoEntity) throws Exception;
	
}
