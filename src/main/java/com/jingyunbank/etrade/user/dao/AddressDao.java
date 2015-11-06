package com.jingyunbank.etrade.user.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jingyunbank.core.Range;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.exception.DataUpdatingException;
import com.jingyunbank.etrade.user.entity.AddressEntity;

public interface AddressDao {
	
	/**
	 * 新增
	 * @param addressEntity
	 * @return
	 * @throws DataSavingException
	 * 2015年11月5日 qxs
	 */
	public boolean insert(AddressEntity addressEntity) throws DataSavingException ;
	
	/**
	 * 修改基本信息
	 * @param addressEntity
	 * @return
	 * @throws DataUpdatingException
	 * 2015年11月5日 qxs
	 */
	public boolean update(AddressEntity addressEntity) throws DataUpdatingException;
	
	/**
	 * 修改地址的状态
	 * @param addressEntity
	 * @return
	 * @throws DataUpdatingException
	 * 2015年11月5日 qxs
	 */
	public boolean updateStatus(AddressEntity addressEntity) throws DataUpdatingException;
	
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
	
}
