package com.jingyunbank.etrade.marketing.flashsale.bean;

import java.io.Serializable;

public class FlashSaleOrderVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -142254927140304285L;

	private String id;
	
	private String flashId;
	
	private String flashUserId;
	
	private String oid;
	
	private long orderno;
	
	private String type;
	
	
	

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
	

}
