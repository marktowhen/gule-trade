package com.jingyunbank.etrade.resource.entity;

/**
 * ueditor上传封装
* Title: UeditorImg
* @author    duanxf
* @date      2015年12月9日
 */
public class UeditorImg {
	private String state;//上传状态SUCCESS 一定要大写
	private String url;//上传地址
	private String title;//图片名称demo.jpg
	private String original;//图片名称demo.jpg
	private String name;
	private String size;
	private String type;
	
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getOriginal() {
		return original;
	}
	public void setOriginal(String original) {
		this.original = original;
	}
}
