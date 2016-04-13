package com.jingyunbank.etrade.wap.goods.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.jingyunbank.etrade.api.wap.goods.bo.GoodsImg;

/**
 * 
 * Title: GoodsOperationShow 商品修改对象
 * 
 * @author duanxf
 * @date 2016年4月13日
 */
public class GoodsOperationEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// -----商品属性-------
	private String ID;
	private String name;
	private String code;
	private String MID;
	private String TID;
	private String about;
	private BigDecimal price;
	private BigDecimal salePrice;
	private Date addtime;
	private Date uptime;
	private Date downtime;
	private String PID;
	private String path;
	private String content;
	private boolean status;
	// -------------------
	// 图片信息
	private List<GoodsImgEntity> imgList = new ArrayList<GoodsImgEntity>();
	// attr-value 信息
	private List<GoodsAttrValueEntity> attrValueList = new ArrayList<GoodsAttrValueEntity>();
	// sku 信息
	private List<GoodsSkuEntity> skuList = new ArrayList<GoodsSkuEntity>();

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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
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

	public Date getAddtime() {
		return addtime;
	}

	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}

	public Date getUptime() {
		return uptime;
	}

	public void setUptime(Date uptime) {
		this.uptime = uptime;
	}

	public Date getDowntime() {
		return downtime;
	}

	public void setDowntime(Date downtime) {
		this.downtime = downtime;
	}

	public String getPID() {
		return PID;
	}

	public void setPID(String pID) {
		PID = pID;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public List<GoodsImgEntity> getImgList() {
		return imgList;
	}

	public void setImgList(List<GoodsImgEntity> imgList) {
		this.imgList = imgList;
	}

	public List<GoodsAttrValueEntity> getAttrValueList() {
		return attrValueList;
	}

	public void setAttrValueList(List<GoodsAttrValueEntity> attrValueList) {
		this.attrValueList = attrValueList;
	}

	public List<GoodsSkuEntity> getSkuList() {
		return skuList;
	}

	public void setSkuList(List<GoodsSkuEntity> skuList) {
		this.skuList = skuList;
	}

}
