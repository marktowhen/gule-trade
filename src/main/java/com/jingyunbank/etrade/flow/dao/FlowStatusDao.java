package com.jingyunbank.etrade.flow.dao;

import org.apache.ibatis.annotations.Param;

import com.jingyunbank.etrade.flow.entity.FlowStatusEntity;

public interface FlowStatusDao {
	
	public FlowStatusEntity selectOne(@Param("flowType")String flowType,@Param("currentStatus") String currentStatus,
			@Param("flag")String flag);

}
