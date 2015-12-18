package com.jingyunbank.etrade.order.postsale.entity;

import java.util.Date;

/**
 * 追踪退单变更的业务对象<br>
 * 该类描述了退款（退单）的生命周期，包括退单申请，批准，完成等
 */
public class RefundTraceEntity{

	private String ID;
	private String RID;//退单id
	private String operator;//操作者（区分买家卖家）
	private String statusCode;//
	private String statusName;//
	private Date addtime;//发生时间
	private String note;//
	
	
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
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	public Date getAddtime() {
		return addtime;
	}
	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
}
