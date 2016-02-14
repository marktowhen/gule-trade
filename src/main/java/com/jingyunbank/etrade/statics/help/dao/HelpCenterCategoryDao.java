package com.jingyunbank.etrade.statics.help.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jingyunbank.etrade.statics.help.entity.HelpCenterCategoryEntity;


public interface HelpCenterCategoryDao {

	/**
	 * 新增
	 * @param entity
	 * @return
	 * 2015年12月10日 qxs
	 */
	public boolean insert(HelpCenterCategoryEntity entity) throws Exception;
	
	/**
	 * 修改
	 * @param entity
	 * @return
	 * @throws Exception
	 * 2015年12月10日 qxs
	 */
	public boolean update(HelpCenterCategoryEntity entity) throws Exception;
	/**
	 * 更改有效状态
	 * @param ID
	 * @param valid
	 * @return
	 * 2015年12月10日 qxs
	 */
	public boolean updateValid(@Param(value="ID")String ID,@Param(value="valid") boolean valid) throws Exception;
	
	/**
	 * 查询有效的
	 * @param offset
	 * @param size
	 * @return
	 * 2015年12月17日 qxs
	 */
	public List<HelpCenterCategoryEntity> selectValidListPage(@Param(value="offset")long offset,@Param(value="size")long size);
	
	/**
	 * 根据id查询
	 * @param ids
	 * @return
	 * 2015年12月18日 qxs
	 */
	public List<HelpCenterCategoryEntity> selectListByCondition(@Param(value="ids") String [] ids);
	
	/**
	 * 查询有效的
	 * @return
	 * 2015年12月17日 qxs
	 */
	public List<HelpCenterCategoryEntity> selectValidList();
	
	/**
	 * 查询单个
	 * @return
	 * 2015年12月10日 qxs
	 */
	public HelpCenterCategoryEntity selectSingle(String ID);
}
