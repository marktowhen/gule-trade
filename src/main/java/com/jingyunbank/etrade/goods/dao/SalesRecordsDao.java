package com.jingyunbank.etrade.goods.dao;

import java.util.List;
import java.util.Map;

import com.jingyunbank.etrade.goods.entity.SalesRecordEntity;

public interface SalesRecordsDao {

	public void insert(SalesRecordEntity entity) throws Exception;
	
	public List<SalesRecordEntity> select(Map<String, Object> params);
}
