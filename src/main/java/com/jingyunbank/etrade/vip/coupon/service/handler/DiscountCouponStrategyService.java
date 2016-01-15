package com.jingyunbank.etrade.vip.coupon.service.handler;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.jingyunbank.core.Result;
import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.order.presale.bo.OrderGoods;
import com.jingyunbank.etrade.api.order.presale.bo.Orders;
import com.jingyunbank.etrade.api.vip.coupon.bo.BaseCoupon;
import com.jingyunbank.etrade.api.vip.coupon.bo.DiscountCoupon;
import com.jingyunbank.etrade.api.vip.coupon.bo.UserDiscountCoupon;
import com.jingyunbank.etrade.api.vip.coupon.handler.ICouponStrategyService;
import com.jingyunbank.etrade.api.vip.coupon.service.IDiscountCouponService;
import com.jingyunbank.etrade.api.vip.coupon.service.IUserDiscountCouponService;

@Service(BaseCoupon.DISCOUNTCOUPONSTRATEGY)
public class DiscountCouponStrategyService implements ICouponStrategyService {

	@Autowired
	private IDiscountCouponService discountCouponService;
	@Autowired
	private IUserDiscountCouponService userDiscountCouponService;
	
	@Override
	public boolean support(BaseCoupon coupon) {
		return coupon instanceof DiscountCoupon;
	}

	@Override
	public Result<BigDecimal> calculate(String UID, String couponID,
			BigDecimal originprice) throws UnsupportedOperationException {
		Result<UserDiscountCoupon> r = userDiscountCouponService.canConsume(couponID, UID, originprice);
		if(r.isBad()) return Result.fail(r.getMessage());
		DiscountCoupon coupon = r.getBody().getDiscountCoupon();
		BigDecimal discount = coupon.getDiscount();
		BigDecimal discountprice = originprice.multiply(discount).setScale(2, RoundingMode.HALF_UP);
		//若折扣有上限
		if(coupon.getValue()!=null && coupon.getValue().compareTo(new BigDecimal("0"))>0){
			discountprice = discountprice.compareTo(coupon.getValue())<0?discountprice: coupon.getValue();
		}
		return Result.ok(originprice.compareTo(coupon.getThreshhold()) >= 0? originprice.subtract(discountprice): originprice);
	}

	@Override
	public void consume(String UID, String couponID)
			throws DataRefreshingException {
		userDiscountCouponService.consume(couponID, UID);
	}

	@Override
	public void lock(String UID, String couponID)
			throws DataRefreshingException {
		userDiscountCouponService.lock(couponID, UID);
	}

	@Override
	public void unlock(String UID, String couponID)
			throws DataRefreshingException {
		userDiscountCouponService.unlock(couponID, UID);
	}

	@Override
	public boolean calculate(String UID, String couponID, List<Orders> orders)
			throws UnsupportedOperationException {
		if(StringUtils.hasText(couponID)){
			//总订单价格，包括邮费
			BigDecimal origintotalprice = orders.stream()
										.map(x->x.getPrice())
										.reduce(new BigDecimal(0), (x,y)->x.add(y));
			//总订单邮费
			BigDecimal origintotalpostage = orders.stream()
										.map(x->x.getPostage())
										.reduce(new BigDecimal(0), (x,y)->x.add(y));
			//总订单价格，不包括邮费
			BigDecimal originpureprice = origintotalprice.subtract(origintotalpostage);
			
			Result<BigDecimal> finalpricer = calculate(UID, couponID, originpureprice);
			if(finalpricer.isBad()) return false;//illegal coupon
			
			BigDecimal finalprice = finalpricer.getBody();
			orders.forEach(order -> {
				//不包含邮费的订单价格
				BigDecimal orderprice = order.getPrice().subtract(order.getPostage());
				//优惠百分比
				BigDecimal orderpricepercent = orderprice.divide(originpureprice, 6, RoundingMode.HALF_UP);
				BigDecimal neworderprice = finalprice.multiply(orderpricepercent).setScale(2, RoundingMode.HALF_UP);
				order.setPayout(neworderprice.add(order.getPostage()));
				order.setCouponReduce(orderprice.subtract(neworderprice));
				List<OrderGoods> goodses = order.getGoods();
				goodses.forEach(goods -> {
					BigDecimal origingoodspprice = goods.getPprice();//促销价
					BigDecimal origingoodsprice = goods.getPrice();
					BigDecimal count = BigDecimal.valueOf(goods.getCount());
					origingoodsprice = //如果促销价不为空，则使用促销价
							(Objects.nonNull(origingoodspprice) && origingoodspprice.compareTo(new BigDecimal(0)) > 0)?
							origingoodspprice : origingoodsprice;
					BigDecimal origingoodspricepercent = origingoodsprice.divide(orderprice, 6, RoundingMode.HALF_UP);
					BigDecimal finalgoodsprice = origingoodspricepercent.multiply(neworderprice).setScale(2, RoundingMode.HALF_UP);
					BigDecimal payout = finalgoodsprice.multiply(count);
					BigDecimal origintotal = origingoodsprice.multiply(count);
					goods.setPayout(payout);
					goods.setCouponReduce(origintotal.subtract(payout));
				});
			});
			return true;
		}
		
		return false;
	}
}
