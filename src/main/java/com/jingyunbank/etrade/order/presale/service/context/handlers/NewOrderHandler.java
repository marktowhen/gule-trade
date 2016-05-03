package com.jingyunbank.etrade.order.presale.service.context.handlers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.jingyunbank.core.KeyGen;
import com.jingyunbank.core.Result;
import com.jingyunbank.core.util.UniqueSequence;
import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.logistic.bo.PostageCalculate;
import com.jingyunbank.etrade.api.logistic.service.context.IPostageCalculateService;
import com.jingyunbank.etrade.api.order.presale.bo.OrderGoods;
import com.jingyunbank.etrade.api.order.presale.bo.OrderStatusDesc;
import com.jingyunbank.etrade.api.order.presale.bo.OrderTrace;
import com.jingyunbank.etrade.api.order.presale.bo.Orders;
import com.jingyunbank.etrade.api.order.presale.service.IOrderGoodsService;
import com.jingyunbank.etrade.api.order.presale.service.IOrderService;
import com.jingyunbank.etrade.api.order.presale.service.IOrderTraceService;
import com.jingyunbank.etrade.api.order.presale.service.context.IOrderStatusHandler;
import com.jingyunbank.etrade.api.pay.bo.OrderPayment;
import com.jingyunbank.etrade.api.pay.bo.PayType;
import com.jingyunbank.etrade.api.pay.service.IPayService;
import com.jingyunbank.etrade.api.vip.coupon.bo.BaseCoupon;
import com.jingyunbank.etrade.api.vip.coupon.handler.ICouponStrategyResolver;
import com.jingyunbank.etrade.api.vip.coupon.handler.ICouponStrategyService;
import com.jingyunbank.etrade.api.wap.goods.service.IWapGoodsService;

public class NewOrderHandler implements IOrderStatusHandler {

	@Autowired
	private IOrderService orderService;
	@Autowired
	private IOrderGoodsService orderGoodsService;
	@Autowired
	private IOrderTraceService orderTraceService;
	@Autowired
	private IPayService payService;
	@Autowired
	private ICouponStrategyResolver couponStrategyResolver;
	@Autowired
	private IPostageCalculateService postageCalculateService;
	@Autowired
	private IWapGoodsService wapGoodsService;

	@Override
	public Result<String> handle(List<Orders> orders) throws Exception {
		// 订单价格简单校验
		// 订单价应担匹配商品总价及邮费计算规则
		boolean goodData = verifyOrderData(orders);
		if (!goodData) {
			return Result.fail("订单数据校验失败，请检查订单信息后重新提交。");
		}

		// 计算优惠券优惠后的价格，以及填充payout
		boolean legalCoupon = calculateCouponReduce(orders);
		if (!legalCoupon) {
			return Result.fail("优惠卡券信息不正确，请检查订单信息后重新提交。");
		}

		// 如果订单支付金额为0则将状态设为已支付
		refreshOrderStatusBasedOnOrderPayout(orders);
		// 保存订单信息
		orderService.save(orders);
		// 构建详情跟追踪状态
		List<OrderGoods> goods = new ArrayList<OrderGoods>();
		List<OrderTrace> traces = new ArrayList<OrderTrace>();
		List<OrderPayment> payments = new ArrayList<OrderPayment>();
		for (Orders order : orders) {
			goods.addAll(order.getGoods());
			traces.add(createOrderTrace(order, "用户创建订单。"));
			createPayment(order, payments);
		}
		// 保存订单的详情（每笔订单的商品信息）
		orderGoodsService.save(goods);
		// 保存订单状态追踪信息
		orderTraceService.save(traces);
		// 保存订单的支付信息
		payService.save(payments);
		// 冻结优惠卡
		List<String> temp = new ArrayList<String>();
		for (Orders order : orders) {
			if (StringUtils.hasText(order.getCouponID())
					&& StringUtils.hasText(order.getCouponType())) {
				if (temp.contains(order.getCouponID()))
					continue;
				temp.add(order.getCouponID());
				try {
					couponStrategyResolver.resolve(order.getCouponType()).lock(
							order.getUID(), order.getCouponID());
				} catch (IllegalArgumentException | DataRefreshingException e) {
					throw new DataRefreshingException(e);
				}
			}
		}

		return Result.ok();
	}

	// 创建订单新建追踪状态
	private OrderTrace createOrderTrace(Orders order, String note) {
		OrderTrace trace = new OrderTrace();
		trace.setAddtime(new Date());
		trace.setID(KeyGen.uuid());
		trace.setOID(order.getID());
		trace.setOrderno(order.getOrderno());
		trace.setOperator(order.getUID());
		trace.setStatusCode(order.getStatusCode());
		trace.setStatusName(order.getStatusName());
		trace.setNote(note);
		return trace;
	}

	private void createPayment(Orders order, List<OrderPayment> payments) {
		if (PayType.ONLINE_CODE.equals(order.getPaytypeCode())) {
			OrderPayment op = new OrderPayment();
			BeanUtils.copyProperties(order, op);
			op.setAddtime(new Date());
			op.setDone(false);
			op.setID(KeyGen.uuid());
			op.setOID(order.getID());
			op.setMoney(order.getPayout());
			op.setTransno(UniqueSequence.next());
			payments.add(op);
		}
	}

	private void refreshOrderStatusBasedOnOrderPayout(List<Orders> orders) {
		BigDecimal total = orders.stream().map(x -> x.getPayout())
				.reduce(BigDecimal.ZERO, (x, y) -> x.add(y));
		if (BigDecimal.ZERO.compareTo(total) == 0) {
			for (Orders order : orders) {
				order.setStatusCode(OrderStatusDesc.PAID.getCode());
				order.setStatusName(OrderStatusDesc.PAID.getName());
				List<OrderGoods> goods = order.getGoods();
				for (OrderGoods orderGoods : goods) {
					orderGoods.setStatusCode(order.getStatusCode());
					orderGoods.setStatusName(order.getStatusName());
				}
			}
		}
	}

	// 校验用户提交的订单价格，邮费以及商品的价格数量等是否相互匹配
	private boolean verifyOrderData(List<Orders> orders) {
		for (Orders order : orders) {
			BigDecimal originorderprice = order.getPrice();// data from user.
			BigDecimal originorderpostage = order.getPostage();// data from
																// user.
			BigDecimal calculatedorderprice = BigDecimal.ZERO;// data calculated
																// based on
																// goods info.
			BigDecimal calculatedorderpostage = BigDecimal.ZERO;// as above.

			List<OrderGoods> goods = order.getGoods();
			// 邮费 postageID必填 num/weight/volume3选1
			List<PostageCalculate> postList = new ArrayList<PostageCalculate>();
			for (OrderGoods orderGoods : goods) {
				BigDecimal pprice = orderGoods.getPprice();// data from user.
				BigDecimal price = orderGoods.getPrice();// data from user.
				int count = orderGoods.getCount();// data from user.
				BigDecimal actualprice = (Objects.nonNull(pprice) && pprice
						.compareTo(BigDecimal.ZERO) > 0) ? pprice : price;
				calculatedorderprice = calculatedorderprice.add(actualprice
						.multiply(BigDecimal.valueOf(count)).setScale(2,
								RoundingMode.HALF_UP));
				// 查询出商品对应的运费模板id
				try {
					PostageCalculate post = new PostageCalculate();
					post.setPostageID(wapGoodsService.singlePidByGid(orderGoods
							.getGID()));
					post.setNumber(count);
					// 运送方式 前台传过来
					post.setTransportType(order.getDeliveryTypeCode());
					post.setCity(order.getCity());
					postList.add(post);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			// 计算邮费
			calculatedorderpostage = postageCalculateService
					.calculateMuti(postList);

			calculatedorderprice = calculatedorderprice
					.add(calculatedorderpostage);
			if (calculatedorderprice.compareTo(originorderprice) != 0
					|| calculatedorderpostage.compareTo(originorderpostage) != 0) {
				return false;
			}
		}
		return true;
	}

	// 计算订单，及订单中每件商品的实际支付价格(剔除使用优惠卡券后的价格)
	private boolean calculateCouponReduce(List<Orders> orders) {

		// orders 必须是公用一张卡券的或者没有卡券
		long c0 = orders.stream().map(x -> x.getCouponID()).distinct().count();
		long c1 = orders.stream().map(x -> x.getCouponType()).distinct()
				.count();
		// c0, c1 必须都是1
		if (c0 != c1 || c0 != 1 || c1 != 1) {
			return false;
		}

		Orders o0 = orders.get(0);
		String couponID = o0.getCouponID();
		String couponType = o0.getCouponType();
		String UID = o0.getUID();
		boolean isemployee = o0.isEmployee();

		if (StringUtils.hasText(couponType)) {// 使用优惠券
			ICouponStrategyService couponStrategyService = couponStrategyResolver
					.resolve(couponType);
			return couponStrategyService.calculate(UID, couponID, orders);
		} else {// 员工通道
			boolean useEmployeeCoupon = false;
			if (useEmployeeCoupon && isemployee) {
				orders.forEach(order -> {
					order.setCouponType(BaseCoupon.EMPLOYEECOUPON);
				});
				ICouponStrategyService couponStrategyService = couponStrategyResolver
						.resolve(BaseCoupon.EMPLOYEECOUPON);
				return couponStrategyService.calculate(UID, couponID, orders);
			}
		}
		return !StringUtils.hasText(couponID);
	}

	@Override
	public Result<String> handle(Orders order) throws Exception {
		return Result.fail("");
	}

}
