package com.jingyunbank.etrade.statics.area.dao;

import java.util.List;

import com.jingyunbank.etrade.statics.area.entity.ProvinceEntity;

public interface ProvinceDao {
	
	public List<ProvinceEntity> select(ProvinceEntity entity);
	
	public boolean insert(ProvinceEntity entity) throws Exception;
	
	public boolean delete(ProvinceEntity entity) throws Exception;
	
	public boolean update(ProvinceEntity entity) throws Exception;

}
