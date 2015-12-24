package com.jingyunbank.etrade.vip.point.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jingyunbank.etrade.vip.point.entity.PointLogEntity;

public interface PointLogDao {

	public List<PointLogEntity> select(@Param(value="UID")String uid) ;

	public List<PointLogEntity> selectList(@Param(value="UID")String uid, @Param(value="offset")long offset,@Param(value="size")long size) ;
	
	public boolean insert(PointLogEntity entity) throws Exception;
}
