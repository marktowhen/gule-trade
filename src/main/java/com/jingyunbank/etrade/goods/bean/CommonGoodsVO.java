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
	private BigDecimal specialPrice; // 特价
	private BigDecimal nowPrice; // 现价
	private String weight;// 重量
	private String unit;// 单位
	private String thumb_path_1; // 缩略图
	/****我的足迹 冗余浏览时间字段***/
	@JsonFormat(pattern="yyyy-MM-dd hh:mm:ss",locale = "zh" , timezone="GMT+8")
	private Date visitTime;
	
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
	public BigDecimal getSpecialPrice() {
		return specialPrice;
	}
	public void setSpecialPrice(BigDecimal specialPrice) {
		this.specialPrice = specialPrice;
	}
	public BigDecimal getNowPrice() {
		return nowPrice;
	}
	public void setNowPrice(BigDecimal nowPrice) {
		this.nowPrice = nowPrice;
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
	public String getThumb_path_1() {
		return thumb_path_1;
	}
	public void setThumb_path_1(String thumb_path_1) {
		this.thumb_path_1 = thumb_path_1;
	}
	public Date getVisitTime() {
		return visitTime;
	}
	public void setVisitTime(Date visitTime) {
		this.visitTime = visitTime;
	}
	
}
