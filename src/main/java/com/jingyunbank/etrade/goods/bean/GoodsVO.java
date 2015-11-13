package com.jingyunbank.etrade.goods.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 商品VO
 * @author liug
 *
 */
public class GoodsVO  implements Serializable{

	private static final long serialVersionUID = 1L;
	// --------------商家属性----------------//
		private String MID;
		private String merchantName; // 商家
		private String merchantDesc; // 商家简介
		private String merchantImg; // 商家图片

		// --------------品牌属性----------------//
		private String BID; // 品牌id
		private String brandName; // 所属品牌
		private String brandDesc; // 所属品牌描述

		// --------------类型属性----------------//
		private String TID; // 所属属性id
		private String typeName; // 所属类型

		// --------------商品属性----------------//
		private String GID; // 商品ID
		private String goodsName; // 商品名
		private String goodsCode; // 商品编码
		private BigDecimal price; // 原价
		private BigDecimal promotionPrice; // 促销价格
		private BigDecimal nowPrice; // 现价
		private String disCount; // 折扣
		private BigDecimal discountPrice; // 折扣价
		private String count; // 库存
		private String volume; // 销量
		@JsonFormat(pattern="yyyy-MM-dd hh:mm:ss",locale = "zh" , timezone="GMT+8")
		private Date addTime;// 商品添加时间
		@JsonFormat(pattern="yyyy-MM-dd hh:mm:ss",locale = "zh" , timezone="GMT+8")
		private Date pro_start;  //促销开始时间
		@JsonFormat(pattern="yyyy-MM-dd hh:mm:ss",locale = "zh" , timezone="GMT+8")
		private Date pro_end;	//促销结束时间
		private int pro_flag; //是否促销标志
		@JsonFormat(pattern="yyyy-MM-dd hh:mm:ss",locale = "zh" , timezone="GMT+8")
		private Date upTime; //上架时间
		@JsonFormat(pattern="yyyy-MM-dd hh:mm:ss",locale = "zh" , timezone="GMT+8")
		private Date downTime;//下架时间
		
		
		//----------------商品图片属性-----------------//
		private String show_path_1; // 展示图片
		private String show_path_2; // 展示图片
		private String show_path_3; // 展示图片
		private String show_path_4; // 展示图片
		private String thumb_path_1; // 缩略图
		private String thumb_path_2; // 缩略图
		private String thumb_path_3; // 缩略图
		private String thumb_path_4; // 缩略图
		// --------------商品详细属性----------------//
		private String standardNo;// 产品标准号
		private String shelfLife; // 保质期
		private String approveNo;// 批准文号
		private String usage;// 食用方法和食用量
		private String commendedUser;// 适宜人群
		private String notCommendedUser;// 不适宜人群
		private String ingredients;// 配料表
		private String foodAdditives;// 食品添加剂
		private String specifications;// 具体规格
		private String ingredient;// 成分含量
		private String functions;// 功能及功效
		private String note;// 注意事项
		private String storageMethods;// 储藏方法
		private String isGiftBox;// 是否礼盒装
		@JsonFormat(pattern="yyyy-MM-dd hh:mm:ss",locale = "zh" , timezone="GMT+8")
		private Date ProductionDate;//生产日期
		private String weight;// 重量
		private String unit;// 单位
		public String getMID() {
			return MID;
		}
		public void setMID(String mID) {
			MID = mID;
		}
		public String getMerchantName() {
			return merchantName;
		}
		public void setMerchantName(String merchantName) {
			this.merchantName = merchantName;
		}
		public String getMerchantDesc() {
			return merchantDesc;
		}
		public void setMerchantDesc(String merchantDesc) {
			this.merchantDesc = merchantDesc;
		}
		public String getMerchantImg() {
			return merchantImg;
		}
		public void setMerchantImg(String merchantImg) {
			this.merchantImg = merchantImg;
		}
		public String getBID() {
			return BID;
		}
		public void setBID(String bID) {
			BID = bID;
		}
		public String getBrandName() {
			return brandName;
		}
		public void setBrandName(String brandName) {
			this.brandName = brandName;
		}
		public String getBrandDesc() {
			return brandDesc;
		}
		public void setBrandDesc(String brandDesc) {
			this.brandDesc = brandDesc;
		}
		public String getTID() {
			return TID;
		}
		public void setTID(String tID) {
			TID = tID;
		}
		public String getTypeName() {
			return typeName;
		}
		public void setTypeName(String typeName) {
			this.typeName = typeName;
		}
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
		public String getGoodsCode() {
			return goodsCode;
		}
		public void setGoodsCode(String goodsCode) {
			this.goodsCode = goodsCode;
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
		public String getDisCount() {
			return disCount;
		}
		public void setDisCount(String disCount) {
			this.disCount = disCount;
		}
		public BigDecimal getDiscountPrice() {
			return discountPrice;
		}
		public void setDiscountPrice(BigDecimal discountPrice) {
			this.discountPrice = discountPrice;
		}
		public String getCount() {
			return count;
		}
		public void setCount(String count) {
			this.count = count;
		}
		public String getVolume() {
			return volume;
		}
		public void setVolume(String volume) {
			this.volume = volume;
		}
		public Date getAddTime() {
			return addTime;
		}
		public void setAddTime(Date addTime) {
			this.addTime = addTime;
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
		public int getPro_flag() {
			return pro_flag;
		}
		public void setPro_flag(int pro_flag) {
			this.pro_flag = pro_flag;
		}
		public Date getUpTime() {
			return upTime;
		}
		public void setUpTime(Date upTime) {
			this.upTime = upTime;
		}
		public Date getDownTime() {
			return downTime;
		}
		public void setDownTime(Date downTime) {
			this.downTime = downTime;
		}
		public String getShow_path_1() {
			return show_path_1;
		}
		public void setShow_path_1(String show_path_1) {
			this.show_path_1 = show_path_1;
		}
		public String getShow_path_2() {
			return show_path_2;
		}
		public void setShow_path_2(String show_path_2) {
			this.show_path_2 = show_path_2;
		}
		public String getShow_path_3() {
			return show_path_3;
		}
		public void setShow_path_3(String show_path_3) {
			this.show_path_3 = show_path_3;
		}
		public String getShow_path_4() {
			return show_path_4;
		}
		public void setShow_path_4(String show_path_4) {
			this.show_path_4 = show_path_4;
		}
		public String getThumb_path_1() {
			return thumb_path_1;
		}
		public void setThumb_path_1(String thumb_path_1) {
			this.thumb_path_1 = thumb_path_1;
		}
		public String getThumb_path_2() {
			return thumb_path_2;
		}
		public void setThumb_path_2(String thumb_path_2) {
			this.thumb_path_2 = thumb_path_2;
		}
		public String getThumb_path_3() {
			return thumb_path_3;
		}
		public void setThumb_path_3(String thumb_path_3) {
			this.thumb_path_3 = thumb_path_3;
		}
		public String getThumb_path_4() {
			return thumb_path_4;
		}
		public void setThumb_path_4(String thumb_path_4) {
			this.thumb_path_4 = thumb_path_4;
		}
		public String getStandardNo() {
			return standardNo;
		}
		public void setStandardNo(String standardNo) {
			this.standardNo = standardNo;
		}
		public String getShelfLife() {
			return shelfLife;
		}
		public void setShelfLife(String shelfLife) {
			this.shelfLife = shelfLife;
		}
		public String getApproveNo() {
			return approveNo;
		}
		public void setApproveNo(String approveNo) {
			this.approveNo = approveNo;
		}
		public String getUsage() {
			return usage;
		}
		public void setUsage(String usage) {
			this.usage = usage;
		}
		public String getCommendedUser() {
			return commendedUser;
		}
		public void setCommendedUser(String commendedUser) {
			this.commendedUser = commendedUser;
		}
		public String getNotCommendedUser() {
			return notCommendedUser;
		}
		public void setNotCommendedUser(String notCommendedUser) {
			this.notCommendedUser = notCommendedUser;
		}
		public String getIngredients() {
			return ingredients;
		}
		public void setIngredients(String ingredients) {
			this.ingredients = ingredients;
		}
		public String getFoodAdditives() {
			return foodAdditives;
		}
		public void setFoodAdditives(String foodAdditives) {
			this.foodAdditives = foodAdditives;
		}
		public String getSpecifications() {
			return specifications;
		}
		public void setSpecifications(String specifications) {
			this.specifications = specifications;
		}
		public String getIngredient() {
			return ingredient;
		}
		public void setIngredient(String ingredient) {
			this.ingredient = ingredient;
		}
		public String getFunctions() {
			return functions;
		}
		public void setFunctions(String functions) {
			this.functions = functions;
		}
		public String getNote() {
			return note;
		}
		public void setNote(String note) {
			this.note = note;
		}
		public String getStorageMethods() {
			return storageMethods;
		}
		public void setStorageMethods(String storageMethods) {
			this.storageMethods = storageMethods;
		}
		public String getIsGiftBox() {
			return isGiftBox;
		}
		public void setIsGiftBox(String isGiftBox) {
			this.isGiftBox = isGiftBox;
		}
		public Date getProductionDate() {
			return ProductionDate;
		}
		public void setProductionDate(Date productionDate) {
			ProductionDate = productionDate;
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
		
}
