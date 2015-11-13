package com.jingyunbank.etrade.goods.entity;

import java.math.BigDecimal;
import java.util.Date;

import org.hibernate.validator.internal.engine.messageinterpolation.parser.BeginState;

import com.jingyunbank.core.Result;

/**
 * 商品表实体
 * @author liug
 *
 */
public class GoodsEntity {
	/**  主键*/
	private String ID;
	/**  商家ID*/
	private String MID;
	/**  品牌ID*/
	private String BID;
	/**  商品名称*/
	private String name;
	/**  商品编码*/
	private String code;
	/**  商品类型ID*/
	private String tid;
	/**  商品价格*/
	private BigDecimal price;
	/**  商品促销价格*/
	private BigDecimal promotionPrice;
	/**  商品现价*/
	private BigDecimal now_price;
	/**  是否上架0否1是*/
	private int state;
	/**  上架时间*/
	private Date upTime;
	/**  下架时间*/
	private Date downTime;
	/**  库存*/
	private int count;
	/**  销量*/
	private int volume;
	//增加时间
	private Date addTime;
	
	//-----11.13新增--------------
	private BigDecimal postage; //邮费
	private int subVolumeType; //减库存方式 (拍下减库存 0 付款减库存1)
	private String barCode; //条形码
	private String goodsDesc; //宝贝描述
	private String goodsTitle; //宝贝标题
	/**  管理员排序*/
	private int adminSort;
	/**  商家排序*/
	private int merchantSort;
	/**  推广排序*/
	private int expandSort;
	
	private int recordSort;//推荐排序
	private Date pro_start;  //促销开始时间
	private Date pro_end;	//促销结束时间
	private int pro_flag; //是否促销标志
	
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getMID() {
		return MID;
	}
	public void setMID(String mID) {
		MID = mID;
	}
	public String getBID() {
		return BID;
	}
	public void setBID(String bID) {
		BID = bID;
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
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
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
	public BigDecimal getNow_price() {
		return now_price;
	}
	public void setNow_price(BigDecimal now_price) {
		this.now_price = now_price;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
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
	public Date getAddTime() {
		return addTime;
	}
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
	
	
	
}
