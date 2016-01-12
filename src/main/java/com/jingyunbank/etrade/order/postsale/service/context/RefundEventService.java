package com.jingyunbank.etrade.order.postsale.service.context;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.jms.JMSException;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.ReferenceType;
import com.jingyunbank.core.KeyGen;
import com.jingyunbank.etrade.api.exception.NoticeDispatchException;
import com.jingyunbank.etrade.api.message.service.context.ISyncNotifyService;
import com.jingyunbank.etrade.api.order.postsale.bo.Refund;
import com.jingyunbank.etrade.api.order.postsale.service.context.IRefundEventService;
import com.jingyunbank.etrade.api.user.bo.Users;
import com.jingyunbank.etrade.api.user.service.IUserService;

public class RefundEventService implements IRefundEventService{

	@Autowired
	private JmsTemplate jmsTemplate;
	@Autowired
	private List<ISyncNotifyService> syncNotifyService;
	@Autowired
	private IUserService userService;
	
	@Override
	public void broadcast(List<Refund> event, String status)
			throws NoticeDispatchException {
		jmsTemplate.send(status, new MessageCreator() {
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
	//发送消息提醒。
	@JmsListener(destination=MQ_REFUND_QUEUE_DONE)
	public void paysuccnotify(String content){
		List<Refund> refunds = convert(content);
		
    	Optional<Users> ucandidate = userService.single(refunds.get(0).getUID());
    	syncNotifyService.forEach(service->{
    		try {
    			com.jingyunbank.etrade.api.message.bo.Message imsg = new com.jingyunbank.etrade.api.message.bo.Message();
    			imsg.setTitle("退单接受通知");
    			imsg.setID(KeyGen.uuid());
    			imsg.setContent("您好，您有一笔退单被卖家同意，请登录网站查询详情。");
    			imsg.setReceiveUser(ucandidate.get());
    			imsg.setReceiveUID(ucandidate.get().getID());
				service.inform(imsg);
			} catch (Exception e) {
				e.printStackTrace();
			}
    	});
	}
	
	private List<Refund> convert(String content) {
		List<Refund> refunds = new ArrayList<Refund>();
		try {
			refunds = new ObjectMapper().readValue(content, CollectionType.construct(List.class, ReferenceType.construct(Refund.class)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return refunds;
	}
}
