package com.jingyunbank.etrade.marketing.flashsale.bean;

import java.io.Serializable;

import com.jingyunbank.etrade.api.user.bo.Users;

public class FlashSaleUserVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4567116521035724035L;

	private String id;
	
	private String flashId;
	
	private String uid;
	
	private String orderStatus;
	
	private String paid;
	
	private Users user;
	
	private FlashSaleVo flashSaleVo;
	
	

	/**
	 * @return the user
	 */
	public Users getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(Users user) {
		this.user = user;
	}

	/**
	 * @return the flashSaleVo
	 */
	public FlashSaleVo getFlashSaleVo() {
		return flashSaleVo;
	}

	/**
	 * @param flashSaleVo the flashSaleVo to set
	 */
	public void setFlashSaleVo(FlashSaleVo flashSaleVo) {
		this.flashSaleVo = flashSaleVo;
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
