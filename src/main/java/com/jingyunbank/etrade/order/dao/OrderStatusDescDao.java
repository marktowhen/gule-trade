package com.jingyunbank.etrade.order.dao;

import java.util.List;

import com.jingyunbank.etrade.order.entity.OrderStatusDescEntity;

public interface OrderStatusDescDao {

	public List<OrderStatusDescEntity> selectVisible();

}
