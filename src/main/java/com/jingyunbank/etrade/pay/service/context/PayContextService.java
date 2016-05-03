package com.jingyunbank.etrade.pay.service.context;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.jms.JMSException;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jingyunbank.core.util.UniqueSequence;
import com.jingyunbank.etrade.api.pay.bo.OrderPayment;
import com.jingyunbank.etrade.api.pay.bo.PayPipeline;
import com.jingyunbank.etrade.api.pay.handler.IPayHandler;
import com.jingyunbank.etrade.api.pay.handler.IPayHandlerResolver;
import com.jingyunbank.etrade.api.pay.service.IPayService;
import com.jingyunbank.etrade.api.pay.service.context.IPayContextService;

@Service("payContextService")
public class PayContextService implements IPayContextService{

	@Autowired
	private IPayService payService;
	@Autowired
	private IPayHandlerResolver payHandlerResolver;
	@Autowired
	private JmsTemplate jmsTemplate;
	
//	@Override
//	@Transactional
//	public Map<String, String> refreshAndResolvePipeline(
//						List<OrderPayment> payments, String pipelineCode, String pipelineName,
//						String bankCode)
//			throws Exception {
//		if(payService.anyDone(payments.stream().map(x->x.getID()).collect(Collectors.toList()))){
//			Map<String, String> map = new HashMap<String, String>();
//			map.put("error", "订单信息已过期！");
//			return map;
//		}
//		long newExtransno = UniqueSequence.next18();
//		payments.forEach(x->{
//			x.setExtransno(newExtransno);
//			x.setPipelineCode(pipelineCode);
//			x.setPipelineName(pipelineName);
//			x.setBankCode(bankCode);
//			
//		});
//		payService.refreshNOAndPipeline(payments);
//		IPayHandler handler = payHandlerResolver.resolve(pipelineCode);
//		return handler.prepare(payments);
//	}

	@Override
	@Transactional
	public Map<String, String> prepay(List<String> oids, PayPipeline pipeline)
			throws Exception {
		List<OrderPayment> payments = payService.listPayable(oids);
		if(payService.anyDone(payments.stream().map(x->x.getID()).collect(Collectors.toList()))){
			Map<String, String> map = new HashMap<String, String>();
			map.put("error", "订单信息已过期！");
			return map;
		}
		long newExtransno = UniqueSequence.next18();
		payments.forEach(x->{
			x.setExtransno(newExtransno);
			x.setPipelineCode(pipeline.getCode());
			x.setPipelineName(pipeline.getName());
			x.setBankCode(pipeline.getBankcode());
		});
		payService.refreshNOAndPipeline(payments);
		IPayHandler handler = payHandlerResolver.resolve(pipeline.getCode());
		return handler.prepay(payments);
	}

	@Override
	public Map<String, String> prefund(String oids) throws Exception {
		List<OrderPayment> payments = payService.listPaid(Arrays.asList(oids));
		if(!payService.allDone(payments.stream().map(x->x.getID()).collect(Collectors.toList()))){
			Map<String, String> map = new HashMap<String, String>();
			map.put("error", "存在未支付的订单，无法退款！");
			return map;
		}
		if(payments.size() != 1){
			Map<String, String> map = new HashMap<String, String>();
			map.put("error", "订单信息有误！");
			return map;
		}
		OrderPayment payment = payments.get(0);
		String pipeline = payments.get(0).getPipelineCode();
		IPayHandler handler = payHandlerResolver.resolve(pipeline);
		return handler.prefund(payment);
	}

	@Override
	public boolean paysucc(String extrano) throws Exception {
		
		
		
		jmsTemplate.setPubSubDomain(true);
		jmsTemplate.send("PAYSUCCESS_CALLBACK", new MessageCreator() {
			@Override
			public javax.jms.Message createMessage(Session session) throws JMSException {
					return session.createTextMessage(extrano);
			}
		});
		
		return false;
	}

	@Override
	public void payfail(String extrano, String reason) throws Exception {
		

		jmsTemplate.setPubSubDomain(true);
		jmsTemplate.send("PAYFAILURE_CALLBACK", new MessageCreator() {
			@Override
			public javax.jms.Message createMessage(Session session) throws JMSException {
					return session.createTextMessage(extrano);
			}
		});
	}
	
}
