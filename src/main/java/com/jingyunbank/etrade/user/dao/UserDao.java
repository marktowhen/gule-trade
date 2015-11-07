package com.jingyunbank.etrade.user.dao;

import java.util.Optional;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.user.entity.UserEntity;

public interface UserDao {
	/**
	 * 注册页面的保存
	 * @param userEntity
	 * @return
	 * @throws DataSavingException
	 */
	public int insert(UserEntity userEntity) throws DataSavingException ;
	/**
	 * 验证手机号是否存在
	 * @param moblie
	 * @return
	 */
	public int phoneExists(String moblie);
	/**
	 * 验证用户名是否存在
	 * @param username
	 * @return
	 */
	
	public int unameExists(String username);
	/**
	 * 验证邮箱是否存在
	 * @param email
	 * @return
	 */
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
	/**
	 *	通过id查询用户（user）的信息
	 * @param id
	 * @return
	 */
	public UserEntity selectUserByid(String id);
	/**
	 * 即使得到当前的id
	 * @return
	 */
	public String queryMaxId();
}
