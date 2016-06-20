package com.jingyunbank.etrade.order.presale.service.context;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.jingyunbank.core.KeyGen;
import com.jingyunbank.core.Result;
import com.jingyunbank.core.util.UniqueSequence;
import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.logistic.bo.PostageCalculate;
import com.jingyunbank.etrade.api.logistic.service.context.IPostageCalculateService;
import com.jingyunbank.etrade.api.order.presale.bo.OrderGoods;
import com.jingyunbank.etrade.api.order.presale.bo.OrderLogistic;
import com.jingyunbank.etrade.api.order.presale.bo.OrderStatusDesc;
import com.jingyunbank.etrade.api.order.presale.bo.OrderTrace;
import com.jingyunbank.etrade.api.order.presale.bo.Orders;
import com.jingyunbank.etrade.api.order.presale.service.IOrderGoodsService;
import com.jingyunbank.etrade.api.order.presale.service.IOrderLogisticService;
import com.jingyunbank.etrade.api.order.presale.service.IOrderService;
import com.jingyunbank.etrade.api.order.presale.service.IOrderTraceService;
import com.jingyunbank.etrade.api.order.presale.service.context.IOrderContextService;
import com.jingyunbank.etrade.api.pay.bo.OrderPayment;
import com.jingyunbank.etrade.api.pay.bo.PayType;
import com.jingyunbank.etrade.api.pay.service.IPayService;
import com.jingyunbank.etrade.api.pay.service.context.IPayContextService;
import com.jingyunbank.etrade.api.vip.coupon.bo.BaseCoupon;
import com.jingyunbank.etrade.api.vip.coupon.handler.ICouponStrategyResolver;
import com.jingyunbank.etrade.api.vip.coupon.handler.ICouponStrategyService;
import com.jingyunbank.etrade.api.wap.goods.service.IWapGoodsService;
import com.jingyunbank.etrade.api.weixinMessage.service.WxMessageService;
import com.jingyunbank.etrade.weixinMessage.util.wx.WxConstants;

@Service("orderContextService")
public class OrderContextService implements IOrderContextService {

	@Autowired
	private IOrderService orderService;
	@Autowired
	private IOrderGoodsService orderGoodsService;
	@Autowired
	private IOrderTraceService orderTraceService;
	@Autowired
	private IPayContextService payContextService;
	@Autowired
	private IPayService payService;
	@Autowired
	private IOrderLogisticService orderLogisticService;
	@Autowired
	private ICouponStrategyResolver couponStrategyResolver;
	@Autowired
	private IPostageCalculateService postageCalculateService;
	@Autowired
	private IWapGoodsService wapGoodsService;
	@Autowired
	private WxMessageService wxMessageService;
	
	//校验用户提交的订单价格，邮费以及商品的价格数量等是否相互匹配
	private boolean verifyOrderData(List<Orders> orders) {
		for (Orders order : orders) {
			BigDecimal originorderprice = order.getPrice();//data from user.
			BigDecimal originorderpostage = order.getPostage();//data from user.
			BigDecimal calculatedorderprice = BigDecimal.ZERO;//data calculated based on goods info.
			BigDecimal calculatedorderpostage = BigDecimal.ZERO;//as above.
		
			List<OrderGoods> goods = order.getGoods();
			//邮费 postageID必填 num/weight/volume3选1
			List<PostageCalculate> postList = new ArrayList<PostageCalculate>();
			for (OrderGoods orderGoods : goods) {
				BigDecimal pprice = orderGoods.getPprice();//data from user.
				BigDecimal price = orderGoods.getPrice();//data from user.
				int count = orderGoods.getCount();//data from user.
				BigDecimal actualprice = (Objects.nonNull(pprice) && pprice.compareTo(BigDecimal.ZERO) > 0)?
									pprice : price;
				calculatedorderprice = calculatedorderprice.add(actualprice.multiply(BigDecimal.valueOf(count)).setScale(2, RoundingMode.HALF_UP));
				//查询出商品对应的运费模板id
				try {
					PostageCalculate post = new PostageCalculate();
					post.setPostageID(wapGoodsService.singlePidByGid(orderGoods.getGID()));
					post.setNumber(count);
					//运送方式 前台传过来
					post.setTransportType(order.getDeliveryTypeCode());
					post.setCity(order.getCity());
					postList.add(post);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			//计算邮费 
			if(!order.getType().equals("AUCTION")){
				calculatedorderpostage = postageCalculateService.calculateMuti(postList);
			}
			calculatedorderprice = calculatedorderprice.add(calculatedorderpostage);
			if(calculatedorderprice.compareTo(originorderprice) != 0
					|| calculatedorderpostage.compareTo(originorderpostage) != 0){
				return false;
			}
		}
		return true;
	}
	
	//计算订单，及订单中每件商品的实际支付价格(剔除使用优惠卡券后的价格)
	private boolean calculateCouponReduce(List<Orders> orders){

		//orders 必须是公用一张卡券的或者没有卡券
		long c0 = orders.stream().map(x->x.getCouponID()).distinct().count();
		long c1 = orders.stream().map(x->x.getCouponType()).distinct().count();
		//c0, c1 必须都是1
		if(c0 != c1 || c0 != 1 || c1 != 1){
			return false;
		}
		
		Orders o0 = orders.get(0);
		String couponID = o0.getCouponID();
		String couponType = o0.getCouponType();
		String UID = o0.getUID();
		boolean isemployee = o0.isEmployee();
		
		if(StringUtils.hasText(couponType)){//使用优惠券
			ICouponStrategyService couponStrategyService = couponStrategyResolver.resolve(couponType);
			return couponStrategyService.calculate(UID, couponID, orders);
		}else{//员工通道
			boolean useEmployeeCoupon = false;
			if(useEmployeeCoupon && isemployee){
				orders.forEach(order ->{order.setCouponType(BaseCoupon.EMPLOYEECOUPON);});
				ICouponStrategyService couponStrategyService = couponStrategyResolver.resolve(BaseCoupon.EMPLOYEECOUPON);
				return couponStrategyService.calculate(UID, couponID, orders);
			}
		}
		return !StringUtils.hasText(couponID);
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor={DataSavingException.class, DataRefreshingException.class})
	public Result<List<Orders>> save(List<Orders> orders) throws DataSavingException, DataRefreshingException {
		//订单价格简单校验
		//订单价应担匹配商品总价及邮费计算规则
		boolean goodData = verifyOrderData(orders);
		if(!goodData){
			return Result.fail("订单数据校验失败，请检查订单信息后重新提交。");
		}
		
		//计算优惠券优惠后的价格，以及填充payout
		boolean legalCoupon= calculateCouponReduce(orders);
		if(!legalCoupon){
			return Result.fail("优惠卡券信息不正确，请检查订单信息后重新提交。");
		}
		
		//如果订单支付金额为0则将状态设为已支付
		refreshOrderStatusBasedOnOrderPayout(orders);
		//保存订单信息
		orderService.save(orders);
		//构建详情跟追踪状态
		List<OrderGoods> goods = new ArrayList<OrderGoods>();
		List<OrderTrace> traces = new ArrayList<OrderTrace>();
		List<OrderPayment> payments = new ArrayList<OrderPayment>();
		for (Orders order : orders) {
			goods.addAll(order.getGoods());
			traces.add(createOrderTrace(order, "用户创建订单。"));
			createPayment(order, payments);
		}
		//保存订单的详情（每笔订单的商品信息）
		orderGoodsService.save(goods);
		//保存订单状态追踪信息
		orderTraceService.save(traces);
		//保存订单的支付信息
		payService.save(payments);
		//冻结优惠卡
		List<String> temp = new ArrayList<String>();
		for (Orders order : orders) {
			if(StringUtils.hasText(order.getCouponID()) && StringUtils.hasText(order.getCouponType())){
				if(temp.contains(order.getCouponID())) continue;
				temp.add(order.getCouponID());
				try {
					couponStrategyResolver.resolve(order.getCouponType())
									.lock(order.getUID(), order.getCouponID());
				} catch (IllegalArgumentException | DataRefreshingException e) {
					throw new DataRefreshingException(e);
				}
			}
		}
		
		return Result.ok(orders);
	}

	private void refreshOrderStatusBasedOnOrderPayout(List<Orders> orders) {
		BigDecimal total = orders.stream().map(x->x.getPayout()).reduce(BigDecimal.ZERO, (x,y)->x.add(y));
		if(BigDecimal.ZERO.compareTo(total) == 0){
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

	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor={DataSavingException.class, DataRefreshingException.class})
	public boolean paysuccess(String extransno) throws DataRefreshingException, DataSavingException {
		List<Orders> orders = orderService.listByExtransno(extransno);
		orders = orders.stream()
				.filter(x-> OrderStatusDesc.NEW_CODE.equals(x.getStatusCode()))
				.collect(Collectors.toList());
		if(orders.size() == 0){
			return false;
		}
		
		List<String> oids = orders.stream().map(x->x.getID()).collect(Collectors.toList());
		//刷新订单状态
		orderService.refreshStatus(oids, OrderStatusDesc.PAID);
		//刷新支付记录状态
		payService.refreshStatus(extransno, true);
		List<OrderTrace> traces = new ArrayList<OrderTrace>();
		List<OrderGoods> goods = new ArrayList<OrderGoods>();
		for (Orders order : orders) {
			order.setStatusCode(OrderStatusDesc.PAID_CODE);
			order.setStatusName(OrderStatusDesc.PAID.getName());
			traces.add(createOrderTrace(order, "用户支付成功"));
			//用户支付成功信息推送到用户
			Map<String,String> dataMap=new HashMap<String,String>();
			dataMap.put("first", "尊敬的顾客您好，我们已收到您的付款。");
			dataMap.put("orderMoneySum",order.getPayout()+"");
			dataMap.put("orderProductName",order.getOrderno()+"");
			dataMap.put("Remark", "如有问题请致电400-800-8895或直接在微信留言，我们将第一时间为您服务！");
			//wxMessageService.sendMessageToUser(WxConstants.getString(WxConstants.PAYMENT_SUCCESS), order.getUID(), dataMap);
			
			goods.addAll(order.getGoods());
		}
		//刷新订单商品的状态
		orderGoodsService.refreshStatus(oids, OrderStatusDesc.PAID);
		//保存订单状态追踪信息
		orderTraceService.save(traces);
		
		return true;
	}

	@Override
	@Transactional
	public void payfail(String extransno, String note) throws DataRefreshingException, DataSavingException {
		List<Orders> orders = orderService.listByExtransno(extransno);
		if(orders.size() == 0){
			return;
		}
		List<String> oids = orders.stream().map(x->x.getID()).collect(Collectors.toList());
		//刷新订单状态
		orderService.refreshStatus(oids, OrderStatusDesc.PAYFAIL);
		//刷新支付记录状态
		payService.refreshStatus(extransno, false);
		List<OrderTrace> traces = new ArrayList<OrderTrace>();
		for (Orders order : orders) {
			order.setStatusCode(OrderStatusDesc.PAYFAIL_CODE);
			order.setStatusName(OrderStatusDesc.PAYFAIL.getName());
			traces.add(createOrderTrace(order, note));
			
		}
		//刷新订单商品的状态
		orderGoodsService.refreshStatus(oids, OrderStatusDesc.PAYFAIL);
		//保存订单状态追踪信息
		orderTraceService.save(traces);
		List<String> temp = new ArrayList<String>();
		for (Orders order : orders) {
			if(StringUtils.hasText(order.getCouponID()) && StringUtils.hasText(order.getCouponType())){
				if(temp.contains(order.getCouponID())) continue;
				temp.add(order.getCouponID());
				couponStrategyResolver.resolve(order.getCouponType())
								.unlock(order.getUID(), order.getCouponID());
			}
		}
	}

	@Override
	@Transactional
	public boolean accept(List<String> oids) throws DataRefreshingException, DataSavingException {
		List<Orders> orders = orderService.list(oids);
		if(orders.size() == 0){
			return false;
		}
		if(orders.stream().anyMatch(order -> !OrderStatusDesc.PAID_CODE.equals(order.getStatusCode()))){
			return false;
		}
		orderService.refreshStatus(oids, OrderStatusDesc.ACCEPT);
		List<OrderTrace> traces = new ArrayList<OrderTrace>();
		for (Orders order : orders) {
			order.setStatusCode(OrderStatusDesc.ACCEPT_CODE);
			order.setStatusName(OrderStatusDesc.ACCEPT.getName());
			traces.add(createOrderTrace(order, "卖家接受订单，商品即将出库。"));
		}
		orderTraceService.save(traces);
		//刷新订单商品的状态
		orderGoodsService.refreshStatus(oids, OrderStatusDesc.ACCEPT);
		return true;
	}
	
	@Override
	@Transactional
	public boolean dispatch(OrderLogistic logistic) throws DataRefreshingException, DataSavingException {
		String oid = logistic.getOID();
		Optional<Orders> candidateorder = orderService.single(oid);
		if(!candidateorder.isPresent()){
			return false;
		}
		Orders order = candidateorder.get();
		if(!OrderStatusDesc.ACCEPT_CODE.equals(order.getStatusCode())){
			return false;
		}
		orderLogisticService.save(logistic);
		orderService.refreshStatus(Arrays.asList(oid), OrderStatusDesc.DELIVERED);
		List<OrderTrace> traces = new ArrayList<OrderTrace>();
		order.setStatusCode(OrderStatusDesc.DELIVERED_CODE);
		order.setStatusName(OrderStatusDesc.DELIVERED.getName());
		traces.add(createOrderTrace(order, "卖家已发货"));
		orderTraceService.save(traces);
		//刷新订单商品的状态
		orderGoodsService.refreshStatus(Arrays.asList(oid), OrderStatusDesc.DELIVERED);
		return true;
	}

	@Override
	@Transactional
	public boolean received(List<String> oids, String note) throws DataRefreshingException, DataSavingException{
		List<Orders> orders = orderService.list(oids);
		if(orders.size() == 0){
			return false;
		}
		if(orders.stream().anyMatch(order -> !OrderStatusDesc.DELIVERED_CODE.equals(order.getStatusCode()))){
			return false;
		}
		orderService.refreshStatus(oids, OrderStatusDesc.RECEIVED);
		List<OrderTrace> traces = new ArrayList<OrderTrace>();
		for (Orders order : orders) {
			order.setStatusCode(OrderStatusDesc.RECEIVED_CODE);
			order.setStatusName(OrderStatusDesc.RECEIVED.getName());
			traces.add(createOrderTrace(order, note));
		}
		traces.forEach(trace->trace.setNote(note));
		orderTraceService.save(traces);
		//刷新订单商品的状态
		orderGoodsService.refreshStatus(oids, OrderStatusDesc.RECEIVED);
		
		return true;
	}
	/*, String reason*/
	@Override
	@Transactional
	public boolean cancel(List<String> oids,String reason) throws DataRefreshingException, DataSavingException{
		List<Orders> orders = orderService.list(oids);
		if(orders.size() == 0){
			return false;
		}
		if(orders.stream().anyMatch(order -> !OrderStatusDesc.NEW_CODE.equals(order.getStatusCode()))){
			return false;
		}
		
		orderService.refreshStatus(oids, OrderStatusDesc.CLOSED);
		List<OrderTrace> traces = new ArrayList<OrderTrace>();
		for (Orders order : orders) {
			order.setStatusCode(OrderStatusDesc.CLOSED_CODE);
			order.setStatusName(OrderStatusDesc.CLOSED.getName());
			traces.add(createOrderTrace(order, reason));
		}
		//set note reason
		traces.forEach(trace->trace.setNote(reason));
		orderTraceService.save(traces);
		//刷新订单商品的状态
		orderGoodsService.refreshStatus(oids, OrderStatusDesc.CLOSED);
		//更新优惠卡券状态
		List<String> temp = new ArrayList<String>(); 
		for (Orders order : orders) {
			if(StringUtils.hasText(order.getCouponID()) && StringUtils.hasText(order.getCouponType())){
				if(temp.contains(order.getCouponID())) continue;
				temp.add(order.getCouponID());
				if(!orderService.shareCoupon(order.getCouponID())){
					couponStrategyResolver.resolve(order.getCouponType())
								.unlock(order.getUID(), order.getCouponID());
				}
			}
		}
		return true;
	}
	
	@Override
	@Transactional
	public boolean cancels(List<String> oids) throws DataRefreshingException, DataSavingException{
		List<Orders> orders = orderService.list(oids);
		if(orders.size() == 0){
			return false;
		}
		/*if(orders.stream().anyMatch(order -> !OrderStatusDesc.NEW_CODE.equals(order.getStatusCode()))){
			return false;
		}*/
		
		orderService.refreshStatus(oids, OrderStatusDesc.CLOSED);
		/*List<OrderTrace> traces = new ArrayList<OrderTrace>();
		for (Orders order : orders) {
			order.setStatusCode(OrderStatusDesc.CLOSED_CODE);
			order.setStatusName(OrderStatusDesc.CLOSED.getName());
			traces.add(createOrderTrace(order, reason));
		}
		//set note reason
		traces.forEach(trace->trace.setNote(reason));
		orderTraceService.save(traces);*/
		//刷新订单商品的状态
		orderGoodsService.refreshStatus(oids, OrderStatusDesc.CLOSED);
		//更新优惠卡券状态
		/*List<String> temp = new ArrayList<String>(); 
		for (Orders order : orders) {
			if(StringUtils.hasText(order.getCouponID()) && StringUtils.hasText(order.getCouponType())){
				if(temp.contains(order.getCouponID())) continue;
				temp.add(order.getCouponID());
				if(!orderService.shareCoupon(order.getCouponID())){
					couponStrategyResolver.resolve(order.getCouponType())
								.unlock(order.getUID(), order.getCouponID());
				}
			}
		}*/
		return true;
	}
	@Override
	public boolean remove(String oid) throws DataRefreshingException, DataSavingException {
		Optional<Orders> candidateorder = orderService.single(oid);
		if(!candidateorder.isPresent()){
			return false;
		}
		Orders order = candidateorder.get();
		if(!OrderStatusDesc.CLOSED_CODE.equals(order.getStatusCode())){
			return false;
		}
		
		orderService.refreshStatus(Arrays.asList(oid), OrderStatusDesc.REMOVED);
		/*List<OrderTrace> traces = new ArrayList<OrderTrace>();
		order.setStatusCode(OrderStatusDesc.REMOVED_CODE);
		order.setStatusName(OrderStatusDesc.REMOVED.getName());
		traces.add(createOrderTrace(order, "买家移除订单。"));
		//set note reason
		orderTraceService.save(traces);*/
		//刷新订单商品的状态
		orderGoodsService.refreshStatus(Arrays.asList(oid), OrderStatusDesc.REMOVED);
		return true;
	}

	//创建订单新建追踪状态
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
	private void createPayment(Orders order, List<OrderPayment> payments){
		if(PayType.ONLINE_CODE.equals(order.getPaytypeCode())){
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

	@Override
	public boolean refund(String oid, String ogid)
			throws DataRefreshingException, DataSavingException {
		orderGoodsService.refreshGoodStatus(Arrays.asList(ogid), OrderStatusDesc.REFUNDING);
		return true;
	}

	@Override
	public boolean cancelRefund(String oid, String ogid)
			throws DataRefreshingException, DataSavingException {
		Optional<Orders> candidate = orderService.single(oid);
		if(candidate.isPresent()){
			Orders o = candidate.get();
			orderGoodsService.refreshGoodStatus(Arrays.asList(ogid), OrderStatusDesc.resolve(o.getStatusCode()));
			return true;
		}
		return false;
	}

	@Override
	public void refundDone(List<String> ogids)
			throws DataRefreshingException, DataSavingException {
		orderGoodsService.refreshGoodStatus(ogids, OrderStatusDesc.REFUNDED);
	}
}
