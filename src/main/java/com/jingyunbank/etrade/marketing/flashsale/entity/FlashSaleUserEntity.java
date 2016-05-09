package com.jingyunbank.etrade.marketing.flashsale.entity;

import java.io.Serializable;
import java.util.Date;

public class FlashSaleUserEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7225332180837827022L;
	
	private String id;
	
	private String flashId;
	
	private String uid;
	
	private String orderStatus;
	
	private String paid;
	
	private FlashSaleEntity flashSaleEntity;
	
	private Date orderTime;
	
	
	/**
	 * @return the orderTime
	 */
	public Date getOrderTime() {
		return orderTime;
	}

	/**
	 * @param orderTime the orderTime to set
	 */
	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}

	/**
	 * @return the flashSaleEntity
	 */
	public FlashSaleEntity getFlashSaleEntity() {
		return flashSaleEntity;
	}

	/**
	 * @param flashSaleEntity the flashSaleEntity to set
	 */
	public void setFlashSaleEntity(FlashSaleEntity flashSaleEntity) {
		this.flashSaleEntity = flashSaleEntity;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the flashId
	 */
	public String getFlashId() {
		return flashId;
	}

	/**
	 * @param flashId the flashId to set
	 */
	public void setFlashId(String flashId) {
		this.flashId = flashId;
	}

	/**
	 * @return the uid
	 */
	public String getUid() {
		return uid;
	}

	/**
	 * @param uid the uid to set
	 */
	public void setUid(String uid) {
		this.uid = uid;
	}

	/**
	 * @return the orderStatus
	 */
	public String getOrderStatus() {
		return orderStatus;
	}

	/**
	 * @param orderStatus the orderStatus to set
	 */
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	/**
	 * @return the paid
	 */
	public String getPaid() {
		return paid;
	}

	/**
	 * @param paid the paid to set
	 */
	public void setPaid(String paid) {
		this.paid = paid;
	}
	
	

	

}
