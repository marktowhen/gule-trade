package com.jingyunbank.etrade.pay.service.context;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jingyunbank.core.KeyGen;
import com.jingyunbank.core.util.UniqueSequence;
import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.order.bo.Orders;
import com.jingyunbank.etrade.api.pay.bo.OrderPayment;
import com.jingyunbank.etrade.api.pay.bo.PayType;
import com.jingyunbank.etrade.api.pay.handler.PayHandler;
import com.jingyunbank.etrade.api.pay.handler.PayHandlerResolver;
import com.jingyunbank.etrade.api.pay.service.IPayService;
import com.jingyunbank.etrade.api.pay.service.context.IPayContextService;

@Service("payContextService")
public class PayContextService implements IPayContextService{

	@Autowired
	private IPayService payService;
	@Autowired
	private PayHandlerResolver payHandlerResolver;
	
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

	@Override
	@Transactional
	public Map<String, String> refreshAndComposite(
						List<OrderPayment> payments, String pipelineCode, String pipelineName,
						String bankCode)
			throws Exception {
		if(payService.anyDone(payments.stream().map(x->x.getID()).collect(Collectors.toList()))){
			Map<String, String> map = new HashMap<String, String>();
			map.put("error", "订单信息已过期！");
			return map;
		}
		long newExtransno = UniqueSequence.next18();
		payments.forEach(x->{
			x.setExtransno(newExtransno);
			x.setPipelineCode(pipelineCode);
			x.setPipelineName(pipelineName);
		});
		payService.refresh(payments);
		PayHandler handler = payHandlerResolver.resolve(pipelineCode);
		return handler.prepare(payments, bankCode);
	}

	@Override
	public void paydone(String extransno) throws DataRefreshingException {
		payService.finish(extransno);
		//List<OrderPayment> payments = payService.listPayments(extransno);
	}

	@Override
	public void payfail(String extransno) throws DataRefreshingException {
		payService.fail(extransno);
		//List<OrderPayment> payments = payService.listPayments(extransno);
	}
}
