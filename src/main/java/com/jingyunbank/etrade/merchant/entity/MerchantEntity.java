package com.jingyunbank.etrade.merchant.entity;

import java.io.Serializable;
import java.util.Date;
/**
 * 
* Title: 商家实体表
* @author    duanxf
* @date      2015年11月3日
 */
public class MerchantEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	/**主键*/
	private String ID;
	/**商家名称*/
	private String name;
	/**商家英文名称*/
	private String ename;
	/**商家编码*/
	private String code;
	/**商家地址*/
	private String address;
	/**商家规模*/
	private String scale;
	/**员工人数（人）*/
	private int employeeNum;
	/**电话*/
	private String tel;
	/**邮编*/
	private String zipcode;
	/**qq*/
	private String qq ;
	/**二维码路径*/
	private String twoDimensionCode ;
	/**注册时间*/
	private Date registerDate ;
	/**管理员排序*/
	private int adminSortNum ;
	/**商家描述*/
	private String  merchantDesc ;
	/**商家图片路径*/
	private String imgPath ;
	/** 是否开具发票 0否1是  */
	private String invoiceFlag;
	/** 商家的等级1-5  */
	private String level;
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
	public String getEname() {
		return ename;
	}
	public void setEname(String ename) {
		this.ename = ename;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getScale() {
		return scale;
	}
	public void setScale(String scale) {
		this.scale = scale;
	}
	public int getEmployeeNum() {
		return employeeNum;
	}
	public void setEmployeeNum(int employeeNum) {
		this.employeeNum = employeeNum;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getZipcode() {
		return zipcode;
	}
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public String getTwoDimensionCode() {
		return twoDimensionCode;
	}
	public void setTwoDimensionCode(String twoDimensionCode) {
		this.twoDimensionCode = twoDimensionCode;
	}
	public Date getRegisterDate() {
		return registerDate;
	}
	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}
	public int getAdminSortNum() {
		return adminSortNum;
	}
	public void setAdminSortNum(int adminSortNum) {
		this.adminSortNum = adminSortNum;
	}
	public String getMerchantDesc() {
		return merchantDesc;
	}
	public void setMerchantDesc(String merchantDesc) {
		this.merchantDesc = merchantDesc;
	}
	public String getImgPath() {
		return imgPath;
	}
	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}
	public static MerchantEntity getInstance(){
		return new MerchantEntity();
	}
	public String getInvoiceFlag() {
		return invoiceFlag;
	}
	public void setInvoiceFlag(String invoiceFlag) {
		this.invoiceFlag = invoiceFlag;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	
}
