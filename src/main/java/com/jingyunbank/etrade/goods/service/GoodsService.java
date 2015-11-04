package com.jingyunbank.etrade.goods.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.goods.bo.Goods;
import com.jingyunbank.etrade.api.goods.service.IGoodsService;
/**
 * 
* Title: 商品接口
* @author    duanxf
* @date      2015年11月4日
 */
@Service("goodsService")
public class GoodsService implements IGoodsService{

	public List<Goods> queryByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}



}
