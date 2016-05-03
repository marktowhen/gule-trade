package com.jingyunbank.etrade.order.presale.service.context.handlers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.jingyunbank.core.Result;
import com.jingyunbank.etrade.api.order.presale.bo.OrderGoods;
import com.jingyunbank.etrade.api.order.presale.bo.OrderStatusDesc;
import com.jingyunbank.etrade.api.order.presale.bo.Orders;
import com.jingyunbank.etrade.api.order.presale.service.IOrderGoodsService;
import com.jingyunbank.etrade.api.order.presale.service.context.IOrderStatusHandler;

public class RefundingOrderHandler implements IOrderStatusHandler {

	@Autowired
	private IOrderGoodsService orderGoodsService;

	@Override
	public Result<String> handle(List<Orders> orders) throws Exception {
		
		if(orders.isEmpty()){
			return Result.fail("orders is empty.");
		}
		List<String> ogids = new ArrayList<String>();
		orders.forEach(x->{
			List<OrderGoods> gs = x.getGoods();
			gs.forEach(y->{
				ogids.add(y.getID());
			});
		});
		orderGoodsService.refreshGoodStatus(ogids, OrderStatusDesc.REFUNDING);
		return Result.ok();
	}

	@Override
	public Result<String> handle(Orders order) throws Exception {
		return null;
	}

}
