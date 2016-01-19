package com.jingyunbank.etrade.order.presale.dao;

import java.util.List;

import com.jingyunbank.etrade.order.presale.entity.OrderStatusDescEntity;

public interface OrderStatusDescDao {

	public List<OrderStatusDescEntity> selectVisible();

}
