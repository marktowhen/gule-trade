package com.jingyunbank.etrade.track.entity;

import java.io.Serializable;
/**
 * 广告明细
 * @author liug
 *
 */
public class AdDetailEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**主键 */
	private String ID;
	/**外键ID */
	private String adModuleId;
	/**模块名称 */
	private String adModuleName;
	/**广告名称 */
	private String name;
	/**广告图片地址 */
	private String imgpath;
	/**广告链接地址 */
	private String linkpath;
	/**描述 */
	private String description;
	/**排序 */
	private int sortNum;
	/**属性1 */
	private String attr1;
	/**属性2 */
	private String attr2;
	/**属性3 */
	private String attr3;
	/**属性4 */
	private String attr4;
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getAdModuleId() {
		return adModuleId;
	}
	public void setAdModuleId(String adModuleId) {
		this.adModuleId = adModuleId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImgpath() {
		return imgpath;
	}
	public void setImgpath(String imgpath) {
		this.imgpath = imgpath;
	}
	public String getLinkpath() {
		return linkpath;
	}
	public void setLinkpath(String linkpath) {
		this.linkpath = linkpath;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getSortNum() {
		return sortNum;
	}
	public void setSortNum(int sortNum) {
		this.sortNum = sortNum;
	}
	public String getAttr1() {
		return attr1;
	}
	public void setAttr1(String attr1) {
		this.attr1 = attr1;
	}
	public String getAttr2() {
		return attr2;
	}
	public void setAttr2(String attr2) {
		this.attr2 = attr2;
	}
	public String getAttr3() {
		return attr3;
	}
	public void setAttr3(String attr3) {
		this.attr3 = attr3;
	}
	public String getAttr4() {
		return attr4;
	}
	public void setAttr4(String attr4) {
		this.attr4 = attr4;
	}
	public String getAdModuleName() {
		return adModuleName;
	}
	public void setAdModuleName(String adModuleName) {
		this.adModuleName = adModuleName;
	}
	
}
