package com.jingyunbank.etrade.order.presale.entity;


public final class OrderStatusDescEntity{

	private String name;
	private String code;
	private int orders;
	private String description;
	private boolean visible;//该状态下的订单买家是否可见
	
	public OrderStatusDescEntity() {
		super();
	}

	public OrderStatusDescEntity(String code, String name, int orders,
			String desc, boolean visible) {
		super();
		this.name = name;
		this.code = code;
		this.orders = orders;
		this.description = desc;
		this.visible = visible;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public int getOrders() {
		return orders;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setOrders(int orders) {
		this.orders = orders;
	}

	public boolean isVisible() {
		return visible;
	}
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
}
