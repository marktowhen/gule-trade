package com.jingyunbank.etrade.user.entity;

import com.jingyunbank.etrade.api.user.bo.BaseAddress;

public class AddressEntity extends BaseAddress{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1602851925715706080L;
	
	private long from = 0;
	private long size = 10;
	
	public long getFrom() {
		return from;
	}
	public void setFrom(long from) {
		this.from = from;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	
	
	
}
