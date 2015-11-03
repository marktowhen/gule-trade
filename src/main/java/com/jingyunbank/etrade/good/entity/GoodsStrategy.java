package com.jingyunbank.etrade.good.entity;

import java.io.Serializable;
import java.util.Date;
/**
 * 
* Title: 商品策略实体
* @author    duanxf
* @date      2015年11月3日
 */
public class GoodsStrategy implements Serializable {

	private static final long serialVersionUID = 1L;
	private String ID;
	/**商品ID*/
	private String GID;
	/**策略ID*/
	private String SID;
	/**开始时间*/
	private Date startData;
	/**结束时间*/
	private Date endData;
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getGID() {
		return GID;
	}
	public void setGID(String gID) {
		GID = gID;
	}
	public String getSID() {
		return SID;
	}
	public void setSID(String sID) {
		SID = sID;
	}
	public Date getStartData() {
		return startData;
	}
	public void setStartData(Date startData) {
		this.startData = startData;
	}
	public Date getEndData() {
		return endData;
	}
	public void setEndData(Date endData) {
		this.endData = endData;
	}
	
	
	
}
