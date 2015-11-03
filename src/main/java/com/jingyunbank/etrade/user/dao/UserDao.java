/**
 * @Title:UserDao.java
@Description:TODO
@author:Administrator
@date:上午9:35:08

 */
package com.jingyunbank.etrade.user.dao;

import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.user.entity.UserEntity;

public interface UserDao {
	public boolean insert(UserEntity userEntity) throws DataSavingException ;
	public boolean phoneExists(String mobile);
	public boolean unameExists(String username);
	public boolean emailExists(String email);
}