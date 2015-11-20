package com.jingyunbank.etrade.user.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

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
	public List<AddressEntity> selectListRang(@Param(value = "entity") AddressEntity addressEntity 
			,@Param(value="offset") long offset, @Param(value="size") long size);
	
	/**
	 * 修改是否为默认地址
	 * @param addressEntity
	 * @return
	 * 2015年11月9日 qxs
	 */
	public boolean updateDefault(AddressEntity addressEntity);

	/**
	 * 查询数量
	 * @param entity
	 * @return
	 * 2015年11月13日 qxs
	 */
	public int selectAmount(AddressEntity entity);
	
}
