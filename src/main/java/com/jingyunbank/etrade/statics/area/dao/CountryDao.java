package com.jingyunbank.etrade.statics.area.dao;

import java.util.List;

import com.jingyunbank.etrade.statics.area.entity.CountryEntity;

public interface CountryDao {
	
	public List<CountryEntity> select(CountryEntity entity);
	
	public boolean insert(CountryEntity entity) throws Exception;
	
	public boolean delete(CountryEntity entity) throws Exception;
	
	public boolean update(CountryEntity entity) throws Exception;

}
