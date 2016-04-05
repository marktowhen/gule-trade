package com.jingyunbank.etrade.logistic.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jingyunbank.etrade.logistic.entity.PostageDetailEntity;

public interface PostageDetailDao {

	public boolean insert(PostageDetailEntity postageDetailEntity);
	
	public boolean updateStatus(@Param("ID")String ID,@Param("valid") boolean valid);
	
	public boolean deleteByPostageID(String postageID);
	
	public boolean update(PostageDetailEntity postageDetailEntity);
	
	public PostageDetailEntity selectOne(String ID);
	
	public List<PostageDetailEntity> selectByPostageID(String postageID);
}
