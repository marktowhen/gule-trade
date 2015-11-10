package com.jingyunbank.etrade.goods.bean;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;

import com.jingyunbank.core.util.CollectionUtils;
import com.jingyunbank.etrade.api.goods.bo.Hot24Goods;

/**
 * 阿胶后台 24小时热卖推荐
 * @author liug
 *
 */
public class Hot24GoodsVO {
	/** 商家id */
	private String MID;
	/** 商品信息 */
	private List<CommonGoodsVO> goodsList;
	
	public String getMID() {
		return MID;
	}
	public void setMID(String mID) {
		MID = mID;
	}
	/**
	 * 获取4个商品
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
	public void init(List<Hot24Goods> goodslist) throws IllegalAccessException, InvocationTargetException, InstantiationException{
		if(goodslist == null || goodslist.size() <= 0){
			return;
		}
		this.goodsList = new ArrayList<CommonGoodsVO>();
		Hot24Goods goods = goodslist.get(0);
		this.MID = goods.getMID();
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
