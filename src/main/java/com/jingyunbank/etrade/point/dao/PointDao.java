package com.jingyunbank.etrade.point.dao;

import org.apache.ibatis.annotations.Param;

import com.jingyunbank.etrade.point.entity.PointEntity;

public interface PointDao {
	
	public PointEntity select(@Param(value = "UID")String uid);
	
	public boolean insert(PointEntity entity) throws Exception;
	
	/**
	 * 将积分修改成指定值
	 * @param uid
	 * @param point 
	 * @return
	 * 2015年12月1日 qxs
	 */
	public boolean update(@Param(value = "UID") String uid, @Param(value = "point") int point) throws Exception;
	
	/**
	 * 增加或减少积分
	 * @param uid
	 * @param point 正数则增加 负数则减少
	 * @return
	 * 2015年12月1日 qxs
	 */
	public boolean calculatePoint(@Param(value = "UID") String uid, @Param(value = "point") int point) throws Exception;

}
