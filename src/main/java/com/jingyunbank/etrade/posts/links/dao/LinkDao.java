package com.jingyunbank.etrade.posts.links.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jingyunbank.etrade.posts.links.entity.LinkEntity;

public interface LinkDao {

	public boolean insert(LinkEntity entity) throws Exception;

	public boolean update(LinkEntity entity) throws Exception;

	public boolean delete(@Param("lid") String lid) throws Exception;

	public List<LinkEntity> select() throws Exception;

	public LinkEntity selectOne(@Param("lid") String id) throws Exception;
}
