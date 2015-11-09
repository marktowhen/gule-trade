package com.jingyunbank.etrade.user.dao;

import java.util.List;

import com.jingyunbank.etrade.user.entity.AddressEntity;

public interface AddressDao {
	
	/**
	 * 新增
	 * @param addressEntity
	 * @return
	 * @throws Exception
	 * 2015年11月5日 qxs
	 */
	public boolean insert(AddressEntity addressEntity) throws Exception ;
	
	/**
	 * 修改基本信息
	 * @param addressEntity
	 * @return
	 * @throws Exception
	 * 2015年11月5日 qxs
	 */
	public boolean update(AddressEntity addressEntity) throws Exception;
	
	/**
	 * 修改地址的状态
	 * @param addressEntity
	 * @return
	 * @throws Exception
	 * 2015年11月5日 qxs
	 */
	public boolean updateStatus(AddressEntity addressEntity) throws Exception;
	
	/**
	 * 列表查询
	 * @param addressEntity
	 * @return
	 * 2015年11月5日 qxs
	 */
	public List<AddressEntity> selectList(AddressEntity addressEntity);
	
	/**
	 * 分页查询
	 * @param addressEntity
	 * @param range 
	 * @return
	 * 2015年11月5日 qxs
	 */
	public List<AddressEntity> selectListRang(AddressEntity addressEntity);
	
	/**
	 * 修改是否为默认地址
	 * @param addressEntity
	 * @return
	 * 2015年11月9日 qxs
	 */
	public boolean updateDefault(AddressEntity addressEntity);
	
}
