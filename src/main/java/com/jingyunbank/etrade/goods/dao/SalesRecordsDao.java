package com.jingyunbank.etrade.goods.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.jingyunbank.etrade.goods.entity.SalesRecordEntity;

public interface SalesRecordsDao {

	public void insert(SalesRecordEntity entity) throws Exception;
	
	public List<SalesRecordEntity> select(Map<String, Object> params);

	public Integer count(@Param("gid")String gid, @Param("year")String year, @Param("month")String month);
}
