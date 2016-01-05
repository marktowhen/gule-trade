package com.jingyunbank.etrade.goods.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
/**
 * 商品概要信息展示（页面的商品概要图片信息可冗余部分字段）
 * @author liug
 *
 */
public class CommonGoodsVO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String GID;
	private String goodsName; // 商品名
	private BigDecimal price; // 原价
	private BigDecimal promotionPrice; // 促销价格
	private BigDecimal nowPrice; // 现价
	private String specifications;// 规格
	private String weight;// 重量
	private String unit;// 单位
	private String thumbpath1; // 缩略图
	private String goodsTitle; //宝贝标题
	private int comment; //商品评论数
	
	@JsonFormat(pattern="yyyy-MM-dd hh:mm:ss",locale = "zh" , timezone="GMT+8")
	private Date onSaleBeginTime;  //促销开始时间
	@JsonFormat(pattern="yyyy-MM-dd hh:mm:ss",locale = "zh" , timezone="GMT+8")
	private Date onSaleEndTime;	//促销结束时间
	private boolean onSale; //是否促销标志
	
	
	
	/****我的足迹 冗余浏览时间字段***/
	@JsonFormat(pattern="yyyy-MM-dd hh:mm:ss",locale = "zh" , timezone="GMT+8")
	private Date visitTime;
	/****我的收藏 冗余收藏时间字段***/
	@JsonFormat(pattern="yyyy-MM-dd hh:mm:ss",locale = "zh" , timezone="GMT+8")
	private Date collectTime;
	public String getGID() {
		return GID;
	}
	public void setGID(String gID) {
		GID = gID;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public BigDecimal getPromotionPrice() {
		return promotionPrice;
	}
	public void setPromotionPrice(BigDecimal promotionPrice) {
		this.promotionPrice = promotionPrice;
	}
	public BigDecimal getNowPrice() {
		return nowPrice;
	}
	public void setNowPrice(BigDecimal nowPrice) {
		this.nowPrice = nowPrice;
	}
	public String getSpecifications() {
		return specifications;
	}
	public void setSpecifications(String specifications) {
		this.specifications = specifications;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	public String getThumbpath1() {
		return thumbpath1;
	}
	public void setThumbpath1(String thumbpath1) {
		this.thumbpath1 = thumbpath1;
	}
	public String getGoodsTitle() {
		return goodsTitle;
	}
	public void setGoodsTitle(String goodsTitle) {
		this.goodsTitle = goodsTitle;
	}
	public int getComment() {
		return comment;
	}
	public void setComment(int comment) {
		this.comment = comment;
	}
	public Date getOnSaleBeginTime() {
		return onSaleBeginTime;
	}
	public void setOnSaleBeginTime(Date onSaleBeginTime) {
		this.onSaleBeginTime = onSaleBeginTime;
	}
	public Date getOnSaleEndTime() {
		return onSaleEndTime;
	}
	public void setOnSaleEndTime(Date onSaleEndTime) {
		this.onSaleEndTime = onSaleEndTime;
	}
	public boolean isOnSale() {
		return onSale;
	}
	public void setOnSale(boolean onSale) {
		this.onSale = onSale;
	}
	public Date getVisitTime() {
		return visitTime;
	}
	public void setVisitTime(Date visitTime) {
		this.visitTime = visitTime;
	}
	public Date getCollectTime() {
		return collectTime;
	}
	public void setCollectTime(Date collectTime) {
		this.collectTime = collectTime;
	}
	
	
	
	
}
