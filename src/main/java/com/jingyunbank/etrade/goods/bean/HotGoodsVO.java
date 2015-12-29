package com.jingyunbank.etrade.goods.bean;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;

import com.jingyunbank.etrade.api.goods.bo.HotGoods;
import com.jingyunbank.etrade.merchant.bean.MerchantVO;

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
	/** 全图片list */
	private List<String> fullImgList = new ArrayList<String>();
	/** 空图片list */
	private List<String> emptyImgList = new ArrayList<String>();
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
		if(this.goodsList != null && this.goodsList.size()>4){
			List<CommonGoodsVO> rltList = new ArrayList<CommonGoodsVO>();
			for(int i = 0;i<4;i++){
				rltList.add(this.goodsList.get(i));
			}
			return rltList;
		}else{
			return goodsList;
		}
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
	public void init(List<HotGoods> goodslist) throws IllegalAccessException, InvocationTargetException, InstantiationException{
		if(goodslist == null || goodslist.size() <= 0){
			return;
		}
		this.merchantVO = new MerchantVO();
		this.goodsList = new ArrayList<CommonGoodsVO>();
		HotGoods goods = goodslist.get(0);
		this.MID = goods.getMID();
		BeanUtils.copyProperties(goods,this.merchantVO);
		this.merchantVO.setID(goods.getMID());
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
		String levelstr = this.merchantVO.getLevel();
		int level = 0;
		if(levelstr!=null && !"".equals(levelstr)){
			level = Integer.parseInt(levelstr);
		}
		for(int i =0;i<level;i++){
			this.fullImgList.add("" + i);
		}
		for(int i =0;i<(5-level);i++){
			this.emptyImgList.add("" + i);
		}
	}
	public List<String> getFullImgList() {
		return fullImgList;
	}
	public void setFullImgList(List<String> fullImgList) {
		this.fullImgList = fullImgList;
	}
	public List<String> getEmptyImgList() {
		return emptyImgList;
	}
	public void setEmptyImgList(List<String> emptyImgList) {
		this.emptyImgList = emptyImgList;
	}
	
}
