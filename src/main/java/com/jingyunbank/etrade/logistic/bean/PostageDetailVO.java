package com.jingyunbank.etrade.logistic.bean;

import java.math.BigDecimal;

public class PostageDetailVO {

	private String ID;
	private String postageID;
	private boolean free;
	private int firstNumber;
	private double firstWeight;
	private double firstVolume;
	private BigDecimal firstCost;
	private int nextNumber;
	private double nextWeight;
	private double nextVolumn;
	private BigDecimal nextCost;
	private String fitArea;
	private boolean valid;
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
