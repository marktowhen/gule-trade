package com.jingyunbank.etrade.marketing.rankgroup.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jingyunbank.etrade.marketing.rankgroup.entity.RankGroupEntity;

public interface RankGroupDao {

	public boolean insert(RankGroupEntity entity) throws Exception;

	public RankGroupEntity selectOne(String ID);
	
	public RankGroupEntity selectOneDetail(String ID);
	
	public RankGroupEntity singleByGroupGoodID(String ID);

	public boolean updateStatus(@Param("ID")String ID, @Param("status")String status) throws Exception;
	
	public List<RankGroupEntity> selectListOnDeadline();
	

}
