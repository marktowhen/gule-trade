package com.jingyunbank.etrade.goods.entity;

import java.util.Date;

/**
 * 商品详情表
 * @author liug
 *
 */
public class GoodsDetailEntity {
	/** 主键 ID*/
	private String ID;
	/** 商品ID*/
	private String GID;
	/** 产品标准号*/
	private String standardNo;
	/** 保质期*/
	private String shelfLife;
	/** 生产许可批准文号*/
	private String approveNo;
	/** 食用方法和食用量*/
	private String usage;
	/** 适宜人群*/
	private String commendedUser;
	/** 不适宜人群*/
	private String notCommendedUser;
	/** 配料表*/
	private String ingredients;
	/** 食品添加剂*/
	private String foodAdditives;
	/** 具体规格*/
	private String specifications;
	/**口味*/
	private String taste;
	/** 成分含量*/
	private String ingredient;
	/** 功能及功效*/
	private String functions;
	/** 注意事项*/
	private String note;
	/** 储藏方法*/
	private String storageMethods;
	/** 包装方式*/
	private String isGiftBox;
	/**生产日期*/
	private Date ProductionDate;
	/** 重量*/
	private String weight;
	/** 单位*/
	private String unit;
	//-----11.13--------------
	private String factoryName; //厂名
	private String factoryAddr; //厂址
	private String factoryTel;  //联系方式
	
	
	
	
	
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
	public String getStandardNo() {
		return standardNo;
	}
	public void setStandardNo(String standardNo) {
		this.standardNo = standardNo;
	}
	public String getShelfLife() {
		return shelfLife;
	}
	public void setShelfLife(String shelfLife) {
		this.shelfLife = shelfLife;
	}
	public String getApproveNo() {
		return approveNo;
	}
	public void setApproveNo(String approveNo) {
		this.approveNo = approveNo;
	}
	public String getUsage() {
		return usage;
	}
	public void setUsage(String usage) {
		this.usage = usage;
	}
	public String getCommendedUser() {
		return commendedUser;
	}
	public void setCommendedUser(String commendedUser) {
		this.commendedUser = commendedUser;
	}
	public String getNotCommendedUser() {
		return notCommendedUser;
	}
	public void setNotCommendedUser(String notCommendedUser) {
		this.notCommendedUser = notCommendedUser;
	}
	public String getIngredients() {
		return ingredients;
	}
	public void setIngredients(String ingredients) {
		this.ingredients = ingredients;
	}
	public String getFoodAdditives() {
		return foodAdditives;
	}
	public void setFoodAdditives(String foodAdditives) {
		this.foodAdditives = foodAdditives;
	}
	public String getSpecifications() {
		return specifications;
	}
	public void setSpecifications(String specifications) {
		this.specifications = specifications;
	}
	public String getIngredient() {
		return ingredient;
	}
	public void setIngredient(String ingredient) {
		this.ingredient = ingredient;
	}
	public String getFunctions() {
		return functions;
	}
	public void setFunctions(String functions) {
		this.functions = functions;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getStorageMethods() {
		return storageMethods;
	}
	public void setStorageMethods(String storageMethods) {
		this.storageMethods = storageMethods;
	}
	public String getIsGiftBox() {
		return isGiftBox;
	}
	public void setIsGiftBox(String isGiftBox) {
		this.isGiftBox = isGiftBox;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public Date getProductionDate() {
		return ProductionDate;
	}
	public void setProductionDate(Date productionDate) {
		ProductionDate = productionDate;
	}
	public String getFactoryName() {
		return factoryName;
	}
	public void setFactoryName(String factoryName) {
		this.factoryName = factoryName;
	}
	public String getFactoryAddr() {
		return factoryAddr;
	}
	public void setFactoryAddr(String factoryAddr) {
		this.factoryAddr = factoryAddr;
	}
	public String getFactoryTel() {
		return factoryTel;
	}
	public void setFactoryTel(String factoryTel) {
		this.factoryTel = factoryTel;
	}
	public String getTaste() {
		return taste;
	}
	public void setTaste(String taste) {
		this.taste = taste;
	}
	
	
	
}
