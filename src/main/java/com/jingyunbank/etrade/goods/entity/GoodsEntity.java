package com.jingyunbank.etrade.goods.entity;

import java.math.BigDecimal;
import java.util.Date;

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
	/**  商品特价*/
	private BigDecimal special_price;
	
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
	
	private Date addtime;
	/**  管理员排序*/
	private int adminSort;
	/**  商家排序*/
	private int merchantSort;
	/**  推广排序*/
	private int expandSort;
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
	public BigDecimal getSpecial_price() {
		return special_price;
	}
	public void setSpecial_price(BigDecimal special_price) {
		this.special_price = special_price;
	}
	public BigDecimal getNow_price() {
		return now_price;
	}
	public void setNow_price(BigDecimal now_price) {
		this.now_price = now_price;
	}
	public Date getAddtime() {
		return addtime;
	}
	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}
	
	
}
