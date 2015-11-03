package com.jingyunbank.etrade.good.entity;

import java.io.Serializable;

/**
 * 
* Title: 商品图片实体
* @author    duanxf
* @date      2015年11月3日
 */
public class GoodImgEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	/**主键*/
	private String ID;
	/**商品id*/
	private String GID;
	/**商品缩略图*/
	private String thumbpath;
	/**商品展示图*/
	private String showpath;
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
	public String getThumbpath() {
		return thumbpath;
	}
	public void setThumbpath(String thumbpath) {
		this.thumbpath = thumbpath;
	}
	public String getShowpath() {
		return showpath;
	}
	public void setShowpath(String showpath) {
		this.showpath = showpath;
	}
	
	
	
	
}
