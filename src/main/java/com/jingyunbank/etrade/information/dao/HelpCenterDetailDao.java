package com.jingyunbank.etrade.information.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jingyunbank.etrade.information.entity.HelpCenterDetailEntity;

public interface HelpCenterDetailDao {

	/**
	 * 新增
	 * @param entity
	 * @return
	 * 2015年12月10日 qxs
	 */
	public boolean insert(HelpCenterDetailEntity entity) throws Exception;
	
	/**
	 * 修改
	 * @param entity
	 * @return
	 * @throws Exception
	 * 2015年12月10日 qxs
	 */
	public boolean update(HelpCenterDetailEntity entity) throws Exception;
	/**
	 * 更改有效状态
	 * @param ID
	 * @param valid
	 * @return
	 * 2015年12月10日 qxs
	 */
	public boolean updateValid(@Param(value="ID")String ID, @Param(value="valid") boolean valid) throws Exception;
	
	/**
	 * 查询有效的
	 * @return
	 * 2015年12月10日 qxs
	 */
	public List<HelpCenterDetailEntity> selectValidList(String categoryID) ;
	
	/**
	 * 查询单个
	 * @return
	 * 2015年12月10日 qxs
	 */
	public HelpCenterDetailEntity selectSingle(String ID);
}
