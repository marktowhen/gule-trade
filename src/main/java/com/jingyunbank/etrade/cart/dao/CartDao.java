package com.jingyunbank.etrade.cart.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jingyunbank.etrade.cart.entity.CartEntity;
import com.jingyunbank.etrade.cart.entity.GoodsInCartEntity;

public interface CartDao{

	public void insertCart(CartEntity cart) throws Exception ;

	public CartEntity selectOne(String uid) ;
	
	public void insertOneGoods(GoodsInCartEntity goods) throws Exception;
	
	public void insertManyGoods(List<GoodsInCartEntity> goods) throws Exception;
	
	public void deleteOne(String gidInCart) throws Exception;
	
	public void deleteMany(@Param(value="gidsInCart") List<String> gidsInCart) throws Exception;
	
	public void deleteByUID(String uid) throws Exception;
	
	public void deleteUserGoods(@Param(value="uid") String uid, @Param(value="gids") List<String> gids) throws Exception;
	
	public void update(GoodsInCartEntity goods) throws Exception ;

	public GoodsInCartEntity selectOneGoods(@Param(value="uid") String uid, @Param(value="gid") String gid,@Param(value="skuid") String skuid);
	
	public List<GoodsInCartEntity> selectRangeByUID(@Param(value="uid") String uid, @Param(value="offset") long offset, @Param(value="size") int size) ;

	public List<GoodsInCartEntity> selectAllByUID(String uid);
	
	public List<GoodsInCartEntity> selectAllByCartID(String cartID);

	public int count(String uid);

}
