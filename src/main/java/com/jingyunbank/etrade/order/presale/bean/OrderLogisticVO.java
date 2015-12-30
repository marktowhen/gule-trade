package com.jingyunbank.etrade.order.presale.bean;

import java.util.Date;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

/**
 * 订单物流信息，卖家将发送的快递单号填入该
 *
 */
public class OrderLogisticVO {
	
	private String ID;
	@NotBlank
	@Size(min=22, max=22)
	private String OID;
	@NotBlank
	@Size(max=20, min=10)
	private String expressno;
	@NotBlank
	private String expressName;
	private Date addtime;
	@NotBlank
	private String typeCode;
	@NotBlank
	private String typeName;
	
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getOID() {
		return OID;
	}
	public void setOID(String oID) {
		OID = oID;
	}
	public String getExpressno() {
		return expressno;
	}
	public void setExpressno(String expressno) {
		this.expressno = expressno;
	}
	public Date getAddtime() {
		return addtime;
	}
	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getExpressName() {
		return expressName;
	}
	public void setExpressName(String expressName) {
		this.expressName = expressName;
	}
	public String getTypeCode() {
		return typeCode;
	}
	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}
}
