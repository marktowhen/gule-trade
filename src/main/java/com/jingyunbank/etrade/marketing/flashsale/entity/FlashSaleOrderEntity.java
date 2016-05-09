package com.jingyunbank.etrade.marketing.flashsale.entity;

import java.io.Serializable;

public class FlashSaleOrderEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3878766125887117859L;

	private String id;
	
	private String flashId;
	
	private String flashUserId;
	
	private String oid;
	
	private long orderno;
	
	private String type;
	
	
	
	/**
	 * @return the flashUserId
	 */
	public String getFlashUserId() {
		return flashUserId;
	}

	/**
	 * @param flashUserId the flashUserId to set
	 */
	public void setFlashUserId(String flashUserId) {
		this.flashUserId = flashUserId;
	}

	/**
	 * @return the orderno
	 */
	public long getOrderno() {
		return orderno;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @param orderno the orderno to set
	 */
	public void setOrderno(long orderno) {
		this.orderno = orderno;
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
	 * @return the oid
	 */
	public String getOid() {
		return oid;
	}

	/**
	 * @param oid the oid to set
	 */
	public void setOid(String oid) {
		this.oid = oid;
	}

	

}
