package com.jingyunbank.etrade.logistic.bean;

import java.math.BigDecimal;

import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.NotEmpty;

public class PostageDetailVO {

	private String ID;
	private String postageID;
	private boolean free;
	@Min(0)
	private int firstNumber;
	@Min(0)
	private double firstWeight;
	@Min(0)
	private double firstVolume;
	@Min(0)
	private BigDecimal firstCost;
	@Min(0)
	private int nextNumber;
	@Min(0)
	private double nextWeight;
	@Min(0)
	private double nextVolumn;
	@Min(0)
	private BigDecimal nextCost;
	private boolean valid;
	@NotEmpty
	private String transportType;//运送方式
	@NotEmpty
	private String fitArea;
	@NotEmpty
	private String fitAreaName;//适用地区的文字说明 如山东省等
	
	public String getFitAreaName() {
		return fitAreaName;
	}
	public void setFitAreaName(String fitAreaName) {
		this.fitAreaName = fitAreaName;
	}
	
	public String getTransportType() {
		return transportType;
	}
	public void setTransportType(String transportType) {
		this.transportType = transportType;
	}
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getPostageID() {
		return postageID;
	}
	public void setPostageID(String postageID) {
		this.postageID = postageID;
	}
	public boolean isFree() {
		return free;
	}
	public void setFree(boolean free) {
		this.free = free;
	}
	public int getFirstNumber() {
		return firstNumber;
	}
	public void setFirstNumber(int firstNumber) {
		this.firstNumber = firstNumber;
	}
	public double getFirstWeight() {
		return firstWeight;
	}
	public void setFirstWeight(double firstWeight) {
		this.firstWeight = firstWeight;
	}
	public double getFirstVolume() {
		return firstVolume;
	}
	public void setFirstVolume(double firstVolume) {
		this.firstVolume = firstVolume;
	}
	public BigDecimal getFirstCost() {
		return firstCost;
	}
	public void setFirstCost(BigDecimal firstCost) {
		this.firstCost = firstCost;
	}
	public int getNextNumber() {
		return nextNumber;
	}
	public void setNextNumber(int nextNumber) {
		this.nextNumber = nextNumber;
	}
	public double getNextWeight() {
		return nextWeight;
	}
	public void setNextWeight(double nextWeight) {
		this.nextWeight = nextWeight;
	}
	public double getNextVolumn() {
		return nextVolumn;
	}
	public void setNextVolumn(double nextVolumn) {
		this.nextVolumn = nextVolumn;
	}
	public BigDecimal getNextCost() {
		return nextCost;
	}
	public void setNextCost(BigDecimal nextCost) {
		this.nextCost = nextCost;
	}
	public String getFitArea() {
		return fitArea;
	}
	public void setFitArea(String fitArea) {
		this.fitArea = fitArea;
	}
	public boolean isValid() {
		return valid;
	}
	public void setValid(boolean valid) {
		this.valid = valid;
	}
	
	

}
