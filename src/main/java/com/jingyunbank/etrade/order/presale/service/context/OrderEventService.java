package com.jingyunbank.etrade.order.presale.service.context;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.jms.JMSException;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.ReferenceType;
import com.jingyunbank.core.KeyGen;
import com.jingyunbank.etrade.api.exception.NoticeDispatchException;
import com.jingyunbank.etrade.api.message.service.context.ISyncNotifyService;
import com.jingyunbank.etrade.api.order.presale.bo.Orders;
import com.jingyunbank.etrade.api.order.presale.service.context.IOrderEventService;
import com.jingyunbank.etrade.api.user.bo.Users;
import com.jingyunbank.etrade.api.user.service.IUserService;
import com.jingyunbank.etrade.api.vip.point.service.context.IPointContextService;

@Service("orderEventService")
public class OrderEventService implements IOrderEventService {

	@Autowired
	private JmsTemplate jmsTemplate;
	@Autowired
	private List<ISyncNotifyService> syncNotifyService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IPointContextService pointContextService;
	
	@Override
	public void broadcast(List<Orders> event, String queue)throws NoticeDispatchException{
		jmsTemplate.send(queue, new MessageCreator() {
			@Override
			public javax.jms.Message createMessage(Session session) throws JMSException {
				try {
					return session.createTextMessage(new ObjectMapper().writeValueAsString(event));
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
				return session.createTextMessage();
			}
		});
	}
	//计算积分
	@JmsListener(destination=MQ_ORDER_QUEUE_PAYSUCC)
	public void calculatePoint(String content){
		List<Orders> orders = convert(content);
		try {
			pointContextService.addPoint(orders.get(0).getUID(), 
					orders.stream().map(x->x.getPayout())
						.reduce(BigDecimal.ZERO, (x,y)->x.add(y)).divide(BigDecimal.valueOf(100)).intValue(), "消费赚取积分");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//发送消息提醒。
	@JmsListener(destination=MQ_ORDER_QUEUE_PAYSUCC)
	public void paysuccnotify(String content){
		List<Orders> orders = convert(content);
		
    	Optional<Users> ucandidate = userService.single(orders.get(0).getUID());
    	syncNotifyService.forEach(service->{
    		try {
    			com.jingyunbank.etrade.api.message.bo.Message imsg = new com.jingyunbank.etrade.api.message.bo.Message();
    			imsg.setTitle("支付成功提醒");
    			imsg.setID(KeyGen.uuid());
    			imsg.setContent("您好，您有一笔订单支付成功，如有疑问，请登录网站查询详情。");
    			imsg.setReceiveUser(ucandidate.get());
    			imsg.setReceiveUID(ucandidate.get().getID());
				service.inform(imsg);
			} catch (Exception e) {
				e.printStackTrace();
			}
    	});
	}

	private List<Orders> convert(String content) {
		List<Orders> orders = new ArrayList<Orders>();
		try {
			orders = new ObjectMapper().readValue(content, CollectionType.construct(List.class, ReferenceType.construct(Orders.class)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return orders;
	}
	

}
