package com.jingyunbank.etrade.statics.help.dao;

import java.util.List;

import com.jingyunbank.etrade.statics.help.entity.InformationEntity;

public interface InformationDao {
	
	public boolean insert(InformationEntity informationEntity) throws Exception;
	
	public List<InformationEntity> selectList();
	
}
