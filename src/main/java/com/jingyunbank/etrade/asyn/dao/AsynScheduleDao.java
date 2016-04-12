package com.jingyunbank.etrade.asyn.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jingyunbank.etrade.asyn.entity.AsynScheduleEntity;


public interface AsynScheduleDao {

	boolean insert(AsynScheduleEntity entity) throws Exception ;

	boolean updateStatus(@Param("ID")String id,@Param("status") String status)throws Exception ;

	boolean delete(String ID) throws Exception ;

	AsynScheduleEntity selectOne(String ID);

	List<AsynScheduleEntity> selectList(@Param("offset")long offset,@Param("size") long size);

	

}
