/**
 * @Title:UserDao.java
@Description:TODO
@author:Administrator
@date:上午9:35:08

 */
package com.jingyunbank.etrade.user.dao;

import java.util.Optional;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.user.entity.UserEntity;

public interface UserDao {
	public int insert(UserEntity userEntity) throws DataSavingException ;
	public int phoneExists(String mobile);
	public int unameExists(String username);
	public int emailExists(String email);
	
	/**
	 * 根据查询条件查询
	 * @param user
	 * @return
	 */
	public UserEntity selectUser(UserEntity user);

	/**
	 * 根据登录名查询用户
	 * @param key loginName/email/phone
	 * @return
	 */
	public UserEntity selectUserByLoginKey(String key);
}
