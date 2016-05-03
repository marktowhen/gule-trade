package com.jingyunbank.etrade.marketing.group.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jingyunbank.etrade.marketing.group.entity.GroupGoodsEntity;
import com.jingyunbank.etrade.marketing.group.entity.GroupGoodsShowEntity;


public interface GroupGoodsDao {

	public void insert(GroupGoodsEntity goods) throws Exception;
	
	public GroupGoodsEntity selectOne(String ggid) ;
	
	public List<GroupGoodsShowEntity> selectMany(@Param("MID")String MID,@Param("goodsName")String goodsName, @Param("from") long from, 
			@Param("size") int size) ;

	public GroupGoodsEntity selectOneByGroupID(String groupID);

	public void update(GroupGoodsEntity goods) throws Exception;

	public int count(@Param("MID")String MID,@Param("goodsName")String goodsName);

}