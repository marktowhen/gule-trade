package com.jingyunbank.etrade.vip.coupon.service.handler;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.core.Result;
import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.goods.service.IGoodsService;
import com.jingyunbank.etrade.api.order.presale.bo.OrderGoods;
import com.jingyunbank.etrade.api.order.presale.bo.Orders;
import com.jingyunbank.etrade.api.vip.coupon.bo.BaseCoupon;
import com.jingyunbank.etrade.api.vip.coupon.handler.ICouponStrategyService;

@Service(BaseCoupon.EMPLOYEECOUPONSTRATEGY)
public class EmployeeOnsaleStrategyService implements ICouponStrategyService {

	@Autowired
	private IGoodsService goodsService;
	
	@Override
	public boolean support(BaseCoupon coupon) {
		return true;
	}

	@Override
	public Result<BigDecimal> calculate(String UID, String couponID,
			BigDecimal originprice) {
		return Result.ok(originprice.multiply(BigDecimal.valueOf(0.8)).setScale(2));
	}

	@Override
	public boolean calculate(String UID, String couponID, List<Orders> orders) {
			orders.forEach(order -> {
				List<OrderGoods> goodses = order.getGoods();
				Map<String, BigDecimal> emprices = goodsService.emprice(goodses.stream().map(x->x.getGID()).collect(Collectors.toList()));
				goodses.forEach(goods -> {
					BigDecimal origingoodsprice = goods.getPrice();
					BigDecimal count = BigDecimal.valueOf(goods.getCount());
					BigDecimal finalgoodsprice = origingoodsprice;
					BigDecimal emprice = emprices.get(goods.getGID());
					if(Objects.nonNull(emprice) && emprice.compareTo(BigDecimal.ZERO) > 0){
						finalgoodsprice = emprice;//商品设定的员工价
					}else{//商品未设员工价按8折计算
						finalgoodsprice = origingoodsprice.multiply(BigDecimal.valueOf(0.8)).setScale(2);
					}
					BigDecimal payout = finalgoodsprice.multiply(count);
					BigDecimal origintotal = origingoodsprice.multiply(count);
					goods.setPayout(payout);
					goods.setCouponReduce(origintotal.subtract(payout));
				});
				BigDecimal orderprice = order.getPrice();
				BigDecimal finalorderprice = BigDecimal.valueOf(goodses.stream().map(x->x.getPayout().doubleValue()).collect(Collectors.summingDouble(x->x)).doubleValue());
				order.setPayout(finalorderprice);
				order.setCouponReduce(orderprice.subtract(finalorderprice));
			});
			return true;
	}

	@Override
	public void consume(String UID, String couponID)
			throws DataRefreshingException {
		//do nothing
	}

	@Override
	public void lock(String UID, String couponID)
			throws DataRefreshingException {
		//do nothing
	}

	@Override
	public void unlock(String UID, String couponID)
			throws DataRefreshingException {
		//do nothing
	}

}
