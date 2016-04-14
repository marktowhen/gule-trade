package com.jingyunbank.etrade.logistic.entity;

import java.math.BigDecimal;

public class PostageDetailEntity {

	private String ID;
	private String postageID;
	private boolean free;
	private int firstNumber;
	private double firstWeight;
	private double firstVolume;
	private BigDecimal firstCost;
	private int nextNumber;
	private double nextWeight;
	private double nextVolume;
	private BigDecimal nextCost;
	private String fitArea;
	private boolean valid;
	private String transportType;//运送方式
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
	public boolean isFree() {
		return free;
	}
	public void setFree(boolean free) {
		this.free = free;
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
	public double getNextVolume() {
		return nextVolume;
	}
	public void setNextVolume(double nextVolume) {
		this.nextVolume = nextVolume;
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
