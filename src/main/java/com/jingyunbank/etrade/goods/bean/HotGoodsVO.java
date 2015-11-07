package com.jingyunbank.etrade.goods.bean;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.jingyunbank.core.util.CollectionUtils;
import com.jingyunbank.etrade.api.goods.bo.Goods;

/**
 * 首页热门推荐
 * @author liug
 *
 */
public class HotGoodsVO {
	/** 商家id */
	private String MID;
	/** 商家信息 */
	private MerchantVO merchantVO;
	/** 商品信息 */
	private List<CommonGoodsVO> goodsList;
	
	public String getMID() {
		return MID;
	}
	public void setMID(String mID) {
		MID = mID;
	}
	public MerchantVO getMerchantVO() {
		return merchantVO;
	}
	public void setMerchantVO(MerchantVO merchantVO) {
		this.merchantVO = merchantVO;
	}
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
	public void init(List<Goods> goodslist) throws IllegalAccessException, InvocationTargetException, InstantiationException{
		if(goodslist == null || goodslist.size() <= 0){
			return;
		}
		this.merchantVO = new MerchantVO();
		this.goodsList = new ArrayList<CommonGoodsVO>();
		Goods goods = goodslist.get(0);
		this.MID = goods.getMerchant_id();
		BeanUtils.copyProperties(goods,this.merchantVO);
		this.goodsList = CollectionUtils.copyTo(goodslist, CommonGoodsVO.class);
	}
}
