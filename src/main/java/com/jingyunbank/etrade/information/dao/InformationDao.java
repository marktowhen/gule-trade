package com.jingyunbank.etrade.information.dao;

import java.util.List;

import com.jingyunbank.etrade.information.entity.InformationEntity;

public interface InformationDao {
	
	public boolean insert(InformationEntity informationEntity) throws Exception;
	
	public List<InformationEntity> selectList();
	
}
