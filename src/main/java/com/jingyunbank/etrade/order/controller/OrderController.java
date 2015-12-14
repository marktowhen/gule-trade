package com.jingyunbank.etrade.order.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.KeyGen;
import com.jingyunbank.core.Result;
import com.jingyunbank.core.util.UniqueSequence;
import com.jingyunbank.core.web.AuthBeforeOperation;
import com.jingyunbank.core.web.ServletBox;
import com.jingyunbank.etrade.api.order.bo.OrderGoods;
import com.jingyunbank.etrade.api.order.bo.OrderStatusDesc;
import com.jingyunbank.etrade.api.order.bo.Orders;
import com.jingyunbank.etrade.api.order.service.context.IOrderContextService;
import com.jingyunbank.etrade.api.vip.handler.ICouponStrategyResolver;
import com.jingyunbank.etrade.api.vip.handler.ICouponStrategyService;
import com.jingyunbank.etrade.order.bean.PurchaseGoodsVO;
import com.jingyunbank.etrade.order.bean.PurchaseOrderVO;
import com.jingyunbank.etrade.order.bean.PurchaseRequestVO;

@RestController
public class OrderController {

	@Autowired
	private IOrderContextService orderContextService;
	@Autowired
	private ICouponStrategyResolver couponStrategyResolver;
	
	/**
	 * 订单确认并提交<br>
	 * uri: put /api/order {"addressID":"XXXXX", "":"", "orders":[{}, {}, {}]}
	 * @param purchase
	 * @param valid
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@AuthBeforeOperation
	@RequestMapping(
			value="/api/order",
			method=RequestMethod.POST,
			consumes=MediaType.APPLICATION_JSON_UTF8_VALUE,
			produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Result<PurchaseRequestVO> submit(@Valid @RequestBody PurchaseRequestVO purchase,
			BindingResult valid, HttpSession session) throws Exception{
		if(valid.hasErrors()){
			List<ObjectError> errors = valid.getAllErrors();
			return Result.fail(errors.stream()
						.map(oe -> Arrays.asList(oe.getCodes()).toString())
						.collect(Collectors.joining(" ; ")));
		}
		
		String UID = ServletBox.getLoginUID(session);
		purchase.setUID(UID);

		String couponID = purchase.getCouponID();
		String couponType = purchase.getCouponType();
		
		List<PurchaseOrderVO> ordervos = purchase.getOrders();
		List<Orders> orders = new ArrayList<Orders>();
		
		for (PurchaseOrderVO ordervo : ordervos) {
			ordervo.setID(KeyGen.uuid());
			ordervo.setOrderno(UniqueSequence.next18());
			ordervo.setAddtime(new Date());
			
			Orders order = new Orders();
			BeanUtils.copyProperties(ordervo, order);
			BeanUtils.copyProperties(purchase, order);
			
			order.setStatusCode(OrderStatusDesc.NEW.getCode());
			order.setStatusName(OrderStatusDesc.NEW.getName());
			
			List<PurchaseGoodsVO> goodses = ordervo.getGoods();
			List<OrderGoods> orderGoodses = new ArrayList<OrderGoods>();
			for (PurchaseGoodsVO goods : goodses) {
				OrderGoods orderGoods = new OrderGoods();
				BeanUtils.copyProperties(goods, orderGoods);
				orderGoods.setID(KeyGen.uuid());
				orderGoods.setOID(order.getID());
				orderGoods.setOrderno(order.getOrderno());
				orderGoods.setStatusCode(OrderStatusDesc.NEW_CODE);
				orderGoods.setAddtime(new Date());
				orderGoods.setUID(order.getUID());
				orderGoodses.add(orderGoods);
			}
			order.setPrice(orderGoodses.stream()
					.map(x->x.getPrice())
					.reduce(new BigDecimal(0), (x,y)->x.add(y))
					);
			order.setGoods(orderGoodses);
			orders.add(order);
		}
		
		setOrdersGoodsFinalPrice(couponID, couponType, UID, orders);
		
		orderContextService.save(orders);
		return Result.ok(purchase);
	}

	private void setOrdersGoodsFinalPrice(String couponID, String couponType, String UID,
			List<Orders> orders) throws Exception{
		if(StringUtils.hasText(couponType) && StringUtils.hasText(couponID)){
			BigDecimal originprice = orders.stream()
										.map(x->x.getPrice())
										.reduce(new BigDecimal(0), (x,y)->x.add(y));
			ICouponStrategyService couponStrategyService = couponStrategyResolver.resolve(couponType);
			Result<BigDecimal> finalpricer = couponStrategyService.calculate(UID, couponID, originprice);
			if(finalpricer.isBad()) return;//illegal coupon
			
			BigDecimal finalprice = finalpricer.getBody();
			orders.forEach(order -> {
				BigDecimal orderprice = order.getPrice();
				BigDecimal orderpricepercent = orderprice.divide(originprice, 2, RoundingMode.HALF_UP);
				BigDecimal neworderprice = finalprice.multiply(orderpricepercent);
				order.setPayout(neworderprice);
				List<OrderGoods> goodses = order.getGoods();
				goodses.forEach(goods -> {
					BigDecimal origingoodsprice = goods.getPrice();
					BigDecimal origingoodspricepercent = origingoodsprice.divide(orderprice, 2, RoundingMode.HALF_UP);
					BigDecimal finalgoodsprice = origingoodspricepercent.multiply(neworderprice);
					goods.setPayout(finalgoodsprice);
					goods.setReduce(origingoodsprice.subtract(finalgoodsprice));
				});
			});
		}
	}
	
	@AuthBeforeOperation
	@RequestMapping(
			value="/api/orders/cancellation",
			method=RequestMethod.PUT,
			consumes=MediaType.APPLICATION_JSON_UTF8_VALUE,
			produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Result<String> cancel(@Valid @RequestBody OIDWithNoteVO cancellation,
			BindingResult valid, HttpSession session) throws Exception{
		if(valid.hasErrors()){
			return Result.fail("您提交的订单信息有误！");
		}
		if(!orderContextService.cancel(cancellation.getOid(), cancellation.getNote())){
			return Result.fail("您提交的订单信息有误，请检查后重新尝试！");
		}
		return Result.ok();
	}
	
	private static class OIDWithNoteVO{
		@NotNull
		private String oid;
		@NotNull
		private String note;
		public String getOid() {
			return oid;
		}
		@SuppressWarnings("unused")
		public void setOid(String oid) {
			this.oid = oid;
		}
		public String getNote() {
			return note;
		}
		@SuppressWarnings("unused")
		public void setNote(String note) {
			this.note = note;
		}
	}
	
	@AuthBeforeOperation
	@RequestMapping(value="/api/orders/{id}", method=RequestMethod.DELETE)
	public Result<String> remove(@PathVariable String id) throws Exception{
		
		orderContextService.remove(id);
		
		return Result.ok(id);
	}
}
