package com.jingyunbank.etrade.statics.area.dao;

import java.util.List;

import com.jingyunbank.etrade.statics.area.entity.CityEntity;

public interface CityDao {
	
	public List<CityEntity> select(CityEntity entity) ;
	
	public boolean insert(CityEntity entity) throws Exception;
	
	public boolean delete(CityEntity entity) throws Exception;
	
	public boolean update(CityEntity entity) throws Exception;

}
