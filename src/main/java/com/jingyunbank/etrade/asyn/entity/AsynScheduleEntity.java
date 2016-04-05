package com.jingyunbank.etrade.asyn.entity;

import java.util.Date;

public class AsynScheduleEntity {


	private String ID;
	private String definedID;
	private String status;
	private Date addtime;
	private Date updatetime;
	private String remark;
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getDefinedID() {
		return definedID;
	}
	public void setDefinedID(String definedID) {
		this.definedID = definedID;
	}
	public Date getAddtime() {
		return addtime;
	}
	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}
	
}
