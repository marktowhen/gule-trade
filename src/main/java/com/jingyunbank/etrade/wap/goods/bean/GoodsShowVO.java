package com.jingyunbank.etrade.wap.goods.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 
 * Title: GoodsShowVO 商品展示VO
 * 
 * @author duanxf
 * @date 2016年4月6日
 */
public class GoodsShowVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String GID; // 商品ID
	private String MID; // 店铺ID
	private String TID; // 类型ID
	private String name; // 商品名
	private String path; // 展示图片
	private BigDecimal price; // 价格
	private BigDecimal salePrice; // 折扣价
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
	private Date addTime; // 添加时间
	private int volume; // 总销量
	private int comment; // 总评论

	public String getGID() {
		return GID;
	}

	public void setGID(String gID) {
		GID = gID;
	}

	public String getMID() {
		return MID;
	}

	public void setMID(String mID) {
		MID = mID;
	}

	public String getTID() {
		return TID;
	}

	public void setTID(String tID) {
		TID = tID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(BigDecimal salePrice) {
		this.salePrice = salePrice;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public int getVolume() {
		return volume;
	}

	public void setVolume(int volume) {
		this.volume = volume;
	}

	public int getComment() {
		return comment;
	}

	public void setComment(int comment) {
		this.comment = comment;
	}

	@Override
	public String toString() {
		return "GoodsShowVO [getGID()=" + getGID() + ", getMID()=" + getMID() + ", getTID()=" + getTID()
				+ ", getName()=" + getName() + ", getPath()=" + getPath() + ", getPrice()=" + getPrice()
				+ ", getSalePrice()=" + getSalePrice() + ", getAddTime()=" + getAddTime() + ", getVolume()="
				+ getVolume() + ", getComment()=" + getComment() + "]";
	}

}
