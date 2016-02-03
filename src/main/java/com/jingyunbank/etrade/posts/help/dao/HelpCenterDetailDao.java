package com.jingyunbank.etrade.posts.help.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jingyunbank.etrade.posts.help.entity.HelpCenterDetailEntity;

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
	 * 更改有效状态
	 * @param parentID
	 * @param valid
	 * @return
	 * 2015年12月18日 qxs
	 */
	public boolean updateValidByParent(@Param(value="parentID") String parentID,@Param(value="valid") boolean valid);
	
	/**
	 * 查询有效的
	 * @return
	 * 2015年12月10日 qxs
	 */
	public List<HelpCenterDetailEntity> selectValidList(String categoryID) ;
	
	/**
	 * 查询
	 * @param offset
	 * @param size
	 * @return
	 * 2015年12月18日 qxs
	 */
	public List<HelpCenterDetailEntity> selectRange(@Param(value="offset")long offset,@Param(value="size")long size);
	
	/**
	 * 查询单个
	 * @return
	 * 2015年12月10日 qxs
	 */
	public HelpCenterDetailEntity selectSingle(String ID);

	

	
}
