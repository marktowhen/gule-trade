package com.jingyunbank.etrade.logistic.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jingyunbank.etrade.logistic.entity.PostageDetailEntity;

public interface PostageDetailDao {

	public boolean insert(PostageDetailEntity postageDetailEntity) throws Exception;
	
	public boolean updateStatus(@Param("ID")String ID,@Param("valid") boolean valid)  throws Exception;
	
	public boolean updateStatusBatch( @Param("postageID")String postageID,@Param("valid") boolean valid)  throws Exception;
	
	public boolean update(PostageDetailEntity postageDetailEntity)  throws Exception;
	
	public PostageDetailEntity selectOne(String ID);
	
	public List<PostageDetailEntity> selectByPostageID(String postageID);

	public PostageDetailEntity selectByFitArea(@Param("postageID")String postageID, @Param("fitArea")String fitArea);
}
