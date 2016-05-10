package com.jingyunbank.etrade.marketing.rankgroup.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jingyunbank.etrade.marketing.rankgroup.entity.RankGroupUserEntity;

public interface RankGroupUserDao {

	public boolean insert(RankGroupUserEntity entity) throws Exception;
	
	public RankGroupUserEntity selectOne(String ID);

	public List<RankGroupUserEntity> selectList(String groupID);

	public boolean updateStatus(@Param("ID")String iD,@Param("status") String status)throws Exception;

	public RankGroupUserEntity selectOneByGroup(@Param("groupID")String groupID, @Param("UID")String uid);

	public List<RankGroupUserEntity> selectListWithStatus(@Param("groupID")String groupID, @Param("status")String status);

	public List<RankGroupUserEntity> selectListUnPay();

	public Integer count(@Param("groupID")String groupID, @Param("status")String status);


}
