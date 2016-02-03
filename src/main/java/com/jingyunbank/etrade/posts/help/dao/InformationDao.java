package com.jingyunbank.etrade.posts.help.dao;

import java.util.List;

import com.jingyunbank.etrade.posts.help.entity.InformationEntity;

public interface InformationDao {
	
	public boolean insert(InformationEntity informationEntity) throws Exception;
	
	public List<InformationEntity> selectList();
	
}
