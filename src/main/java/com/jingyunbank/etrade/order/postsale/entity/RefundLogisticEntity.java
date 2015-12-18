package com.jingyunbank.etrade.order.postsale.entity;

import java.util.Date;

/**
 * 退单物流信息，卖家将发送的快递单号填入该
 */
public class RefundLogisticEntity{

	private String ID;
	private String RID;//退单ID
	private String expressno;
	private String expressName;
	private Date addtime;
	private String receiver;
	
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getRID() {
		return RID;
	}
	public void setRID(String rID) {
		RID = rID;
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
	public String getExpressName() {
		return expressName;
	}
	public void setExpressName(String expressName) {
		this.expressName = expressName;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	
}
