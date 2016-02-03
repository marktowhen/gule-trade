package com.jingyunbank.etrade.posts.banner.bean;

import org.hibernate.validator.constraints.NotEmpty;

public class BannerVO {
	private String ID;
	
	private String type; //类型
	@NotEmpty(message="PC端图片不能为空")
	private String image; //图片
	@NotEmpty(message="PC端跳转地址不能为空")
	private String targetUrl; //跳转地址
	private String wechatImage; //微信图片
	private String wechatTargetUrl; //微站点跳转地址
	private String alt;
	private int order;
	private boolean valid;
	private String remark;
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getTargetUrl() {
		return targetUrl;
	}
	public void setTargetUrl(String targetUrl) {
		this.targetUrl = targetUrl;
	}
	public String getWechatImage() {
		return wechatImage;
	}
	public void setWechatImage(String wechatImage) {
		this.wechatImage = wechatImage;
	}
	public String getWechatTargetUrl() {
		return wechatTargetUrl;
	}
	public void setWechatTargetUrl(String wechatTargetUrl) {
		this.wechatTargetUrl = wechatTargetUrl;
	}
	public String getAlt() {
		return alt;
	}
	public void setAlt(String alt) {
		this.alt = alt;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public boolean isValid() {
		return valid;
	}
	public void setValid(boolean valid) {
		this.valid = valid;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
	
}
