package com.jingyunbank.etrade.posts.dao;

import java.util.List;

import com.jingyunbank.etrade.posts.entity.InformationEntity;

public interface InformationDao {
	
	public boolean insert(InformationEntity informationEntity) throws Exception;
	
	public List<InformationEntity> selectList();
	
}
