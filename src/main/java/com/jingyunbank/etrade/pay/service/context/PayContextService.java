package com.jingyunbank.etrade.pay.service.context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jingyunbank.core.util.UniqueSequence;
import com.jingyunbank.etrade.api.pay.bo.OrderPayment;
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
	public Map<String, String> refreshAndResolvePipeline(
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
		payService.refreshNOAndPipeline(payments);
		PayHandler handler = payHandlerResolver.resolve(pipelineCode);
		return handler.prepare(payments, bankCode);
	}
}
