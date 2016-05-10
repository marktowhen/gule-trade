package com.jingyunbank.etrade.marketing.rankgroup.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jingyunbank.etrade.marketing.rankgroup.entity.RankGroupOrderEntity;

public interface RankGroupOrderDao {

	public boolean insert(RankGroupOrderEntity entity) throws Exception;
	
	public RankGroupOrderEntity selectOne(String ID) ;
	
	public List<RankGroupOrderEntity> selectList(String groupUserID);

	public RankGroupOrderEntity selectByOID(String OID);

	public RankGroupOrderEntity selectListByType(@Param("groupUserID")String groupUserID,@Param("type")String orderType);
}
