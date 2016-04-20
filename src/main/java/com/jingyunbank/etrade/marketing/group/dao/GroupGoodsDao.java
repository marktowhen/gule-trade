package com.jingyunbank.etrade.marketing.group.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jingyunbank.etrade.marketing.group.entity.GroupGoodsEntity;


public interface GroupGoodsDao {

	public void insert(GroupGoodsEntity goods) throws Exception;
	
	public GroupGoodsEntity selectOne(String ggid) ;
	
	public List<GroupGoodsEntity> selectMany(@Param("from") long from, 
			@Param("size") int size) ;

}