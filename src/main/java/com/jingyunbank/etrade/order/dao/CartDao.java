package com.jingyunbank.etrade.order.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jingyunbank.etrade.order.entity.CartEntity;

public interface CartDao{

	public void insert(CartEntity order) throws Exception ;

	public void update(CartEntity order) throws Exception ;

	public void delete(String id) throws Exception;
	
	public List<CartEntity> selectRange(@Param(value="uid") String uid, @Param(value="offset") long offset, @Param(value="size") long size) ;

	public List<CartEntity> selectByUID(String uid);

}
