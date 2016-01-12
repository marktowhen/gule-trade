package com.jingyunbank.etrade.message.service;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.exception.NoticeDispatchException;
import com.jingyunbank.etrade.api.message.service.context.IAsyncNotifyService;
import com.jingyunbank.etrade.api.message.service.context.ISyncNotifyService;

@Service("asyncNotifyService")
public class AsyncNotifyService implements IAsyncNotifyService {

	@Autowired
	private JmsTemplate jmsTemplate;
	@Resource
	private ISyncNotifyService emailService;
	@Resource
	private ISyncNotifyService inboxService;
	@Resource
	private ISyncNotifyService smsService;

	@Override
	public void dispatch(com.jingyunbank.etrade.api.message.bo.Message message) throws NoticeDispatchException {
		jmsTemplate.send(MQ_NOTIFY_TOPIC, new MessageCreator() {
			@Override
			public javax.jms.Message createMessage(Session session) throws JMSException {
				return session.createObjectMessage(message);
			}
		});
	}
	
	@JmsListener(destination=MQ_NOTIFY_TOPIC)
	public void consumesms(@Payload com.jingyunbank.etrade.api.message.bo.Message message) throws Exception {
		smsService.inform(message);
	}
	
	@JmsListener(destination=MQ_NOTIFY_TOPIC)
	public void consumeemail(@Payload com.jingyunbank.etrade.api.message.bo.Message message) throws Exception {
		emailService.inform(message);
	}
	
	@JmsListener(destination=MQ_NOTIFY_TOPIC)
	public void consumeinbox(@Payload com.jingyunbank.etrade.api.message.bo.Message message) throws Exception {
		inboxService.inform(message);
	}

}
