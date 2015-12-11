package com.jingyunbank.etrade.information.bean;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

public class HelpCenterDetailVO implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4165205781518992976L;
	private String ID;
	//父级菜单id
	@NotEmpty
	private String parentID;
	//名称
	@NotEmpty
	@Size(min=1, max=40)
	private String name;
	//内容
	@NotEmpty
	private String content;
	//是否有效
	private boolean valid;
	//排序
	@Min(value=1)
	private int sort;
	//添加时间
	private Date addTime;
	//修改时间
	private Date updateTime;
	//修改人
	private Date updateUID;
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getParentID() {
		return parentID;
	}
	public void setParentID(String parentID) {
		this.parentID = parentID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
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
	public Date getUpdateUID() {
		return updateUID;
	}
	public void setUpdateUID(Date updateUID) {
		this.updateUID = updateUID;
	}
	
	

}
