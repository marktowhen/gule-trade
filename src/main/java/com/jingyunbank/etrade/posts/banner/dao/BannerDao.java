package com.jingyunbank.etrade.posts.banner.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jingyunbank.etrade.posts.banner.entity.BannerEntity;

public interface BannerDao {

	boolean insert(BannerEntity entity) throws Exception;

	boolean update(BannerEntity entity)throws Exception;

	boolean updateValidStatus(@Param("ID")String ID,@Param("valid") boolean valid) throws Exception;

	List<BannerEntity> select(@Param("type")String type);

	int count(String type);
	
	BannerEntity selectSingle(String ID);

	List<BannerEntity> selectRange(@Param("type")String type, @Param("offset")long offset, @Param("size")long size);


}
