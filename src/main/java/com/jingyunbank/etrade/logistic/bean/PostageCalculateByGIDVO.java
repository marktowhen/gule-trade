package com.jingyunbank.etrade.logistic.bean;

import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.NotEmpty;


public class PostageCalculateByGIDVO {
	@NotEmpty
	private String GID;
	@Min(1)
	private int city;
	private int number;
	private double weight;
	private double volume;
	@NotEmpty
	private String transportType;
	
	public String getGID() {
		return GID;
	}
	public void setGID(String gID) {
		GID = gID;
	}
	public String getTransportType() {
		return transportType;
	}
	public void setTransportType(String transportType) {
		this.transportType = transportType;
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
