package com.jingyunbank.etrade.goods.bean;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;

import com.jingyunbank.etrade.api.goods.bo.FootprintGoods;

/**
 * 我的足迹
 * @author liug
 *
 */
public class FootprintGoodsVO {
	/** 商品信息 */
	private List<CommonGoodsVO> goodsList;
	
	/**
	 * 
	 * @return
	 */
	public List<CommonGoodsVO> getGoodsList() {
			return goodsList;
	}
	public void setGoodsList(List<CommonGoodsVO> goodsList) {
		this.goodsList = goodsList;
	}
	/**
	 * 接收一个业务对象，并且将业务对象转换初始化为自身使用的对象
	 * @param goods
	 * @throws InstantiationException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public void init(List<FootprintGoods> goodslist) throws IllegalAccessException, InvocationTargetException, InstantiationException{
		if(goodslist == null || goodslist.size() <= 0){
			return;
		}
		this.goodsList = new ArrayList<CommonGoodsVO>();
		this.goodsList = goodslist.stream().map(bo -> {
			CommonGoodsVO vo = new CommonGoodsVO();
			try {
				BeanUtils.copyProperties(bo, vo);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return vo;
		}).collect(Collectors.toList());
	}
}
