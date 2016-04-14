package com.jingyunbank.etrade.logistic.bean;

import java.math.BigDecimal;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;


public class PostageCalculateVO {
	@NotNull
	private String postageID;
	private BigDecimal price;
	@Min(1)
	private int city;
	private int number;
	private double weight;
	private double volume;
	@NotEmpty
	private String tansportType;
	
	public String getTansportType() {
		return tansportType;
	}
	public void setTansportType(String tansportType) {
		this.tansportType = tansportType;
	}
	public String getPostageID() {
		return postageID;
	}
	public void setPostageID(String postageID) {
		this.postageID = postageID;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public int getCity() {
		return city;
	}
	public void setCity(int city) {
		this.city = city;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	public double getVolume() {
		return volume;
	}
	public void setVolume(double volume) {
		this.volume = volume;
	}
	
	
}
