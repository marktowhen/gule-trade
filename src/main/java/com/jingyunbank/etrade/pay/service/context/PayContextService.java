package com.jingyunbank.etrade.pay.service.context;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jingyunbank.core.KeyGen;
import com.jingyunbank.core.util.UniqueSequence;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.order.bo.Orders;
import com.jingyunbank.etrade.api.pay.bo.OrderPayment;
import com.jingyunbank.etrade.api.pay.bo.PayType;
import com.jingyunbank.etrade.api.pay.service.IPayService;
import com.jingyunbank.etrade.api.pay.service.context.IPayContextService;

@Service("payContextService")
public class PayContextService implements IPayContextService{

	@Autowired
	private IPayService payService;
	
	@Override
	@Transactional
	public void save(List<Orders> orders) throws DataSavingException {
		List<OrderPayment> payments = new ArrayList<OrderPayment>();
		orders.stream().filter(o-> PayType.ONLINE_CODE.equals(o.getPaytypeCode()))
			.forEach(o -> {
				OrderPayment op = new OrderPayment();
				BeanUtils.copyProperties(o, op);
				op.setAddtime(new Date());
				op.setDone(false);
				op.setID(KeyGen.uuid());
				op.setOID(o.getID());
				op.setMoney(o.getPrice());
				op.setTransno(UniqueSequence.next());
				payments.add(op);
			});
		payService.save(payments);
	}

	@Override
	public boolean ifpayable(List<Orders> orders) {
		
		return false;
	}

//	@Override
//	@Transactional
//	public List<OrderPayment> listPayments(List<String> oids) throws DataSavingException {
//		过滤已经过期的订单，过滤已经付款的订单
//		if(orders.stream().anyMatch(x->
//				x.getAddtime().toInstant()
//					.plusSeconds(Orders.VALID_TIME_IN_SECOND)
//					.isBefore(Instant.now())
//				&&
//				x.getStatusCode() != OrderStatusDesc.NEW_CODE
//			)){
//			return new ArrayList<OrderPayment>();
//		}
		//获取订单的支付信息，包括过期的和已经支付的
//		final List<OrderPayment> payments = payService.listPayments(orders.stream()
//									.map(x->x.getID()).collect(Collectors.toList()));
		//找出还没有生成支付信息的订单
//		List<Orders> ordersWhichHaveNoPayments = orders.stream()
//								.filter(o -> !payments.stream().anyMatch(p-> p.getOID() == o.getID()))
//								.collect(Collectors.toList());
//		List<OrderPayment> newpayments = new ArrayList<OrderPayment>();
//		//对于没有支付信息的订单，创建相应的支付记录
//		ordersWhichHaveNoPayments.forEach(o -> {
//			OrderPayment op = new OrderPayment();
//			BeanUtils.copyProperties(o, op);
//			op.setAddtime(new Date());
//			op.setDone(false);
//			op.setID(KeyGen.uuid());
//			op.setOID(o.getID());
//			op.setMoney(o.getPrice());
//			op.setTransno(UniqueSequence.next());
//			newpayments.add(op);
//		});
//		//保存新生成的支付信息
//		//这是整个逻辑的中心点重心点，严格讲save方法对于任意一个订单只会执行一次。
//		if(newpayments.size() > 0) payService.save(newpayments);
//		//合并新生成的支付信息
//		payments.addAll(newpayments);
//
//		if(payments.stream().anyMatch(x->x.isDone())){
//			return new ArrayList<OrderPayment>();
//		}
//		return payments;
//	}

}
