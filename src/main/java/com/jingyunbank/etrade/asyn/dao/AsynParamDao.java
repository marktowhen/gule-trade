package com.jingyunbank.etrade.asyn.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jingyunbank.etrade.asyn.entity.AsynParamEntity;

public interface AsynParamDao {

	boolean insertMutl(@Param("list")List<AsynParamEntity> entityList) throws Exception ;

	List<AsynParamEntity> selectBySchedule(String scheduleID);

}
