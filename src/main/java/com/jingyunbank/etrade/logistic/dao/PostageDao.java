package com.jingyunbank.etrade.logistic.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jingyunbank.etrade.logistic.entity.PostageEntity;

public interface PostageDao {

	boolean insert(PostageEntity postageEntity);
	
	boolean updateStatus(@Param("ID")String ID,@Param("valid") boolean valid);
	
	boolean update(PostageEntity postageEntity);
	
	PostageEntity selectOne(String ID);
	
	List<PostageEntity> selectByMID(String MID);
}