package com.jingyunbank.etrade.marketing.flashsale.bean;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class FlashSaleVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 623885158775608048L;

	private String id;
	
	private String gid;
	
	private String skuId;
	
	private int stock;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
	private Date activityTime;
	
	private double currentPrice;
	
	private int flashStatus;
	
	private boolean shows;//是否上架

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}


	/**
	 * @return the shows
	 */
	public boolean isShows() {
		return shows;
	}


	/**
	 * @param shows the shows to set
	 */
	public void setShows(boolean shows) {
		this.shows = shows;
	}


	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the gid
	 */
	public String getGid() {
		return gid;
	}

	/**
	 * @param gid the gid to set
	 */
	public void setGid(String gid) {
		this.gid = gid;
	}

	/**
	 * @return the skuId
	 */
	public String getSkuId() {
		return skuId;
	}

	/**
	 * @param skuId the skuId to set
	 */
	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}

	/**
	 * @return the stock
	 */
	public int getStock() {
		return stock;
	}

	/**
	 * @param stock the stock to set
	 */
	public void setStock(int stock) {
		this.stock = stock;
	}

	/**
	 * @return the currentPrice
	 */
	public double getCurrentPrice() {
		return currentPrice;
	}
	

	/**
	 * @param currentPrice the currentPrice to set
	 */
	public void setCurrentPrice(double currentPrice) {
		this.currentPrice = currentPrice;
	}
	
	/**
	 * @return the activityTime
	 */
	public Date getActivityTime() {
		return activityTime;
	}

	/**
	 * @param activityTime the activityTime to set
	 */
	public void setActivityTime(Date activityTime) {
		this.activityTime = activityTime;
	}

	/**
	 * @return the flashStatus
	 */
	public int getFlashStatus() {
		return flashStatus;
	}

	/**
	 * @param flashStatus the flashStatus to set
	 */
	public void setFlashStatus(int flashStatus) {
		this.flashStatus = flashStatus;
	}

}
