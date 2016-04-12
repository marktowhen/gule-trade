package com.jingyunbank.etrade.wap.goods.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * Title: GoodsSkuVO skuVO
 * 
 * @author duanxf
 * @date 2016年4月6日
 */
public class GoodsSkuVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String ID;
	private String GID;
	private String properties;
	private String propertiesValue;
	private int stock;
	private int volume;
	private BigDecimal price;
	private BigDecimal salePrice;
	private boolean status;

	private List<GoodsImgVO> imgList = new ArrayList<GoodsImgVO>();

	public List<GoodsImgVO> getImgList() {
		return imgList;
	}

	public void setImgList(List<GoodsImgVO> imgList) {
		this.imgList = imgList;
	}

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

	public String getProperties() {
		return properties;
	}

	public void setProperties(String properties) {
		this.properties = properties;
	}

	public String getPropertiesValue() {
		return propertiesValue;
	}

	public void setPropertiesValue(String propertiesValue) {
		this.propertiesValue = propertiesValue;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public int getVolume() {
		return volume;
	}

	public void setVolume(int volume) {
		this.volume = volume;
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

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

}
