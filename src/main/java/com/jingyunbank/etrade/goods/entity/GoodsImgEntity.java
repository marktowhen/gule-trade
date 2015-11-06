package com.jingyunbank.etrade.goods.entity;

import java.io.Serializable;

/**
 * 
* Title: 商品图片实体
* @author    duanxf
* @date      2015年11月3日
 */
public class GoodsImgEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	/**主键*/
	private String ID;
	/**商品id*/
	private String GID;
	/**商品缩略图*/
	private String thumbpath1;
	/**商品缩略图*/
	private String thumbpath2;
	/**商品缩略图*/
	private String thumbpath3;
	/**商品缩略图*/
	private String thumbpath4;

	/**商品展示图*/
	private String showpath1;
	/**商品展示图*/
	private String showpath2;
	/**商品展示图*/
	private String showpath3;
	/**商品展示图*/
	private String showpath4;
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
	public String getThumbpath1() {
		return thumbpath1;
	}
	public void setThumbpath1(String thumbpath1) {
		this.thumbpath1 = thumbpath1;
	}
	public String getThumbpath2() {
		return thumbpath2;
	}
	public void setThumbpath2(String thumbpath2) {
		this.thumbpath2 = thumbpath2;
	}
	public String getThumbpath3() {
		return thumbpath3;
	}
	public void setThumbpath3(String thumbpath3) {
		this.thumbpath3 = thumbpath3;
	}
	public String getThumbpath4() {
		return thumbpath4;
	}
	public void setThumbpath4(String thumbpath4) {
		this.thumbpath4 = thumbpath4;
	}
	public String getShowpath1() {
		return showpath1;
	}
	public void setShowpath1(String showpath1) {
		this.showpath1 = showpath1;
	}
	public String getShowpath2() {
		return showpath2;
	}
	public void setShowpath2(String showpath2) {
		this.showpath2 = showpath2;
	}
	public String getShowpath3() {
		return showpath3;
	}
	public void setShowpath3(String showpath3) {
		this.showpath3 = showpath3;
	}
	public String getShowpath4() {
		return showpath4;
	}
	public void setShowpath4(String showpath4) {
		this.showpath4 = showpath4;
	}
	
	
	
	
}
