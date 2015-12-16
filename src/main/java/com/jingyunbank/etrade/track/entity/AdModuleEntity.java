package com.jingyunbank.etrade.track.entity;

import java.io.Serializable;
/**
 * 广告模块
 * @author liug
 *
 */
public class AdModuleEntity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**主键 */
	private String ID;
	/**广告模块编码 */
	private String code;
	/**广告模块名称 */
	private String name;
	/**广告模块图片地址 */
	private String imgpath;
	/**描述 */
	private String description;
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
