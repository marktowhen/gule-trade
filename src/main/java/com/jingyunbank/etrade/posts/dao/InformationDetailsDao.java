package com.jingyunbank.etrade.posts.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jingyunbank.etrade.posts.entity.InformationDetailsEntity;




public interface InformationDetailsDao {
	
	public boolean insert(InformationDetailsEntity informationDetailsEntity) throws Exception;
	
	public void delete(String id) throws Exception;
	
	public boolean update(InformationDetailsEntity informationDetailsEntity) throws Exception;
	
	public List<InformationDetailsEntity> selectDetailsBySid(@Param(value="SID") String sid,@Param(value="from") long from,@Param(value="size") long size);
	
	public List<InformationDetailsEntity> selectDetailBySid(String sid);
	
	public InformationDetailsEntity selectDetailByid(String id);
	
	public List<InformationDetailsEntity> selectDetail(@Param(value="from") long from,@Param(value="size") long size);
	
}
