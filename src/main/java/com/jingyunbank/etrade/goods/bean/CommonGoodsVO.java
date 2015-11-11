package com.jingyunbank.etrade.goods.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
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
	private BigDecimal promotion_price; // 特价
	private BigDecimal nowPrice; // 现价
	private String weight;// 重量
	private String unit;// 单位
	private String thumb_path_1; // 缩略图
	private Date pro_start;  //促销开始时间
	private Date pro_end;	//促销结束时间
	private String pro_flag; //是否促销标志
	
	
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

	public BigDecimal getPromotion_price() {
		return promotion_price;
	}
	public void setPromotion_price(BigDecimal promotion_price) {
		this.promotion_price = promotion_price;
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
	public Date getPro_start() {
		return pro_start;
	}
	public void setPro_start(Date pro_start) {
		this.pro_start = pro_start;
	}
	public Date getPro_end() {
		return pro_end;
	}
	public void setPro_end(Date pro_end) {
		this.pro_end = pro_end;
	}
	public String getPro_flag() {
		return pro_flag;
	}
	public void setPro_flag(String pro_flag) {
		this.pro_flag = pro_flag;
	}
	
	
	
	
}
