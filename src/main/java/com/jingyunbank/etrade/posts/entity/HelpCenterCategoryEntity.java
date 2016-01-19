package com.jingyunbank.etrade.posts.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * 帮助中心 类别
 * 
 */
public class HelpCenterCategoryEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5213386966008706263L;
	private String ID;
	//名称
	private String name;
	//是否有效
	private boolean valid;
	//排序
	private int sort;
	//添加时间
	private Date addTime;
	//修改时间
	private Date updateTime;
	//修改人
	private String updateUID;
	
	private List<HelpCenterDetailEntity> helpCenterDetailList = new ArrayList<HelpCenterDetailEntity>();
	
	public List<HelpCenterDetailEntity> getHelpCenterDetailList() {
		return helpCenterDetailList;
	}
	public void setHelpCenterDetailList(
			List<HelpCenterDetailEntity> helpCenterDetailList) {
		this.helpCenterDetailList = helpCenterDetailList;
	}
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isValid() {
		return valid;
	}
	public void setValid(boolean valid) {
		this.valid = valid;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	public Date getAddTime() {
		return addTime;
	}
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getUpdateUID() {
		return updateUID;
	}
	public void setUpdateUID(String updateUID) {
		this.updateUID = updateUID;
	}
	
	
}
