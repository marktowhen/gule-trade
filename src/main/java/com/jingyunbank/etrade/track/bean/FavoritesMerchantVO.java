package com.jingyunbank.etrade.track.bean;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;

import com.jingyunbank.etrade.api.track.bo.FavoritesGoods;
import com.jingyunbank.etrade.goods.bean.CommonGoodsVO;
import com.jingyunbank.etrade.merchant.bean.MerchantVO;

/**
 * 我的收藏店铺的信息
 * @author liug
 *
 */
public class FavoritesMerchantVO {
	/** 商家id */
	private String MID;
	/** 商家信息 */
	private MerchantVO merchantVO;
	/** 商品信息 */
	private List<CommonGoodsVO> goodsList;
	/** id */
	private String ID;
	
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
	/**
	 * 获取4个商品
	 * @return
	 */
	public List<CommonGoodsVO> getGoodsList() {
		/*if(this.goodsList != null && this.goodsList.size()>4){
			List<CommonGoodsVO> rltList = new ArrayList<CommonGoodsVO>();
			for(int i = 0;i<4;i++){
				rltList.add(this.goodsList.get(i));
			}
			return rltList;
		}else{
			return goodsList;
		}*/
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
	public void init(List<FavoritesGoods> goodslist) throws IllegalAccessException, InvocationTargetException, InstantiationException{
		if(goodslist == null || goodslist.size() <= 0){
			return;
		}
		this.merchantVO = new MerchantVO();
		this.goodsList = new ArrayList<CommonGoodsVO>();
		FavoritesGoods goods = goodslist.get(0);
		this.MID = goods.getMID();
		this.ID = goods.getID();
		BeanUtils.copyProperties(goods,this.merchantVO);
		this.merchantVO.setName(goods.getMerchantName());
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
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	
}
