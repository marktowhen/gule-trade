package com.jingyunbank.etrade.goods.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 *  用于商品修改的展示VO
* Title: GoodsOperationShowVO
* @author    duanxf
* @date      2015年12月15日
 */
public class GoodsOperationShowVO implements Serializable{

	private static final long serialVersionUID = 1L;
	private String GID;//商品ID
	private String MID;//商家ID
	private String merchantName;
	private String BID;//品牌ID
	private String brandName;
	private String name;//商品名称
	private String code;//商品编码
	private String TID;//商品类型ID
	private BigDecimal price;//商品价格
	private BigDecimal promotionPrice;//商品促销价格
	private BigDecimal nowprice;//商品现价
	private boolean state;//是否上架0否1是
	@JsonFormat(pattern="yyyy-MM-dd hh:mm:ss",locale = "zh" , timezone="GMT+8")
	private Date upTime;//上架时间
	@JsonFormat(pattern="yyyy-MM-dd hh:mm:ss",locale = "zh" , timezone="GMT+8")
	private Date downTime;//下架时间
	private int count;//库存
	private int volume;//销量
	@JsonFormat(pattern="yyyy-MM-dd hh:mm:ss",locale = "zh" , timezone="GMT+8")
	private Date addTime;//添加时间
	@JsonFormat(pattern="yyyy-MM-dd hh:mm:ss",locale = "zh" , timezone="GMT+8")
	private Date onSaleBeginTime;  //促销开始时间
	@JsonFormat(pattern="yyyy-MM-dd hh:mm:ss",locale = "zh" , timezone="GMT+8")
	private Date onSaleEndTime;	//促销结束时间
	private boolean onSale; //是否促销标志
	
	
	private BigDecimal postage; //邮费
	private int subVolumeType; //减库存方式 (拍下减库存 0 付款减库存1)
	private String barCode; //条形码
	private String goodsDesc; //宝贝描述
	private String goodsTitle; //宝贝标题
	private int adminSort;//管理员排序字段
	private int merchantSort;//商家排序字段
	private int expandSort;//推广排序字段
	private int recordSort;//推荐排序字段
	
	//--------图片信息-----------
	private String thumbpath1;//商品 缩略图1
	private String thumbpath2;//商品 缩略图2
	private String thumbpath3;//商品 缩略图3
	private String thumbpath4;//商品 缩略图4
	private String thumbpath5;//商品展示图1
	private String content;//商品展示图2
	
	//---------详细信息-----------
	private String standardNo;//产品标准号
	private String shelfLife;//保质期
	private String approveNo;// 批准文号
	private String usage;//食用方法和食用量
	private String commendedUser;//适宜人群
	private String notCommendedUser;// 不适宜人群
	private String ingredients;//配料表
	private String foodAdditives;//食品添加剂
	private String specifications;//具体规格
	private String functions;//功能及功效
	private String ingredient; //成分含量
	private String note;//注意事项
	private String storageMethods;//储藏方法
	private String isGiftBox;//是否礼盒装
	@JsonFormat(pattern="yyyy-MM-dd hh:mm:ss",locale = "zh" , timezone="GMT+8")
	private Date productionDate;//生产日期
	private String weight;//重量
	private String unit;//单位
	private String factoryName; //厂名
	private String factoryAddr; //厂址
	private String factoryTel;  //联系方式
	public String getGID() {
		return GID;
	}
	public void setGID(String gID) {
		GID = gID;
	}
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getTID() {
		return TID;
	}
	public void setTID(String tID) {
		TID = tID;
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
	
	public BigDecimal getNowprice() {
		return nowprice;
	}
	public void setNowprice(BigDecimal nowprice) {
		this.nowprice = nowprice;
	}
	public boolean isState() {
		return state;
	}
	public void setState(boolean state) {
		this.state = state;
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
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getVolume() {
		return volume;
	}
	public void setVolume(int volume) {
		this.volume = volume;
	}
	public Date getAddTime() {
		return addTime;
	}
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
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
	public BigDecimal getPostage() {
		return postage;
	}
	public void setPostage(BigDecimal postage) {
		this.postage = postage;
	}
	public int getSubVolumeType() {
		return subVolumeType;
	}
	public void setSubVolumeType(int subVolumeType) {
		this.subVolumeType = subVolumeType;
	}
	public String getBarCode() {
		return barCode;
	}
	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}
	public String getGoodsDesc() {
		return goodsDesc;
	}
	public void setGoodsDesc(String goodsDesc) {
		this.goodsDesc = goodsDesc;
	}
	public String getGoodsTitle() {
		return goodsTitle;
	}
	public void setGoodsTitle(String goodsTitle) {
		this.goodsTitle = goodsTitle;
	}
	public int getAdminSort() {
		return adminSort;
	}
	public void setAdminSort(int adminSort) {
		this.adminSort = adminSort;
	}
	public int getMerchantSort() {
		return merchantSort;
	}
	public void setMerchantSort(int merchantSort) {
		this.merchantSort = merchantSort;
	}
	public int getExpandSort() {
		return expandSort;
	}
	public void setExpandSort(int expandSort) {
		this.expandSort = expandSort;
	}
	public int getRecordSort() {
		return recordSort;
	}
	public void setRecordSort(int recordSort) {
		this.recordSort = recordSort;
	}
	public String getThumbpath1() {
		return thumbpath1;
	}
	public void setThumbpath1(String thumbpath1) {
		this.thumbpath1 = thumbpath1;
	}
	public String getThumbpath2() {
		return thumbpath2;
	}
	public void setThumbpath2(String thumbpath2) {
		this.thumbpath2 = thumbpath2;
	}
	public String getThumbpath3() {
		return thumbpath3;
	}
	public void setThumbpath3(String thumbpath3) {
		this.thumbpath3 = thumbpath3;
	}
	public String getThumbpath4() {
		return thumbpath4;
	}
	public void setThumbpath4(String thumbpath4) {
		this.thumbpath4 = thumbpath4;
	}
	public String getThumbpath5() {
		return thumbpath5;
	}
	public void setThumbpath5(String thumbpath5) {
		this.thumbpath5 = thumbpath5;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
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
	public String getFunctions() {
		return functions;
	}
	public void setFunctions(String functions) {
		this.functions = functions;
	}
	public String getIngredient() {
		return ingredient;
	}
	public void setIngredient(String ingredient) {
		this.ingredient = ingredient;
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
		return productionDate;
	}
	public void setProductionDate(Date productionDate) {
		this.productionDate = productionDate;
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
	public String getFactoryName() {
		return factoryName;
	}
	public void setFactoryName(String factoryName) {
		this.factoryName = factoryName;
	}
	public String getFactoryAddr() {
		return factoryAddr;
	}
	public void setFactoryAddr(String factoryAddr) {
		this.factoryAddr = factoryAddr;
	}
	public String getFactoryTel() {
		return factoryTel;
	}
	public void setFactoryTel(String factoryTel) {
		this.factoryTel = factoryTel;
	}
	
	
	
}
