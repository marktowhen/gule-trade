package com.jingyunbank.etrade.user.entity;

import com.jingyunbank.etrade.api.user.bo.BaseAddress;

public class AddressEntity extends BaseAddress{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1602851925715706080L;
	
	private int from = 0;
	private int size = 10;
	
	public int getFrom() {
		return from;
	}
	public void setFrom(int from) {
		this.from = from;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	
	
	
}
