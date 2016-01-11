package com.jingyunbank.etrade.message.service;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.client.producer.SendStatus;
import com.alibaba.rocketmq.common.consumer.ConsumeFromWhere;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.alibaba.rocketmq.common.protocol.heartbeat.MessageModel;
import com.alibaba.rocketmq.remoting.exception.RemotingException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jingyunbank.etrade.api.exception.NoticeDispatchException;
import com.jingyunbank.etrade.api.message.service.context.IAsyncNotifyService;
import com.jingyunbank.etrade.api.message.service.context.ISyncNotifyService;

@Service("asyncNotifyService")
public class AsyncNotifyService implements IAsyncNotifyService {

	@Autowired
	private DefaultMQProducer defaultMQProducer;
	@Autowired
	private DefaultMQPushConsumer defaultMQPushConsumer;
	@Autowired
	private List<ISyncNotifyService> syncNotifyService;

	@Override
	public void dispatch(com.jingyunbank.etrade.api.message.bo.Message message) throws NoticeDispatchException {
		Message msg = null;
		try {
			msg = new Message(MQ_NOTIFY_TOPIC, MQ_NOTIFY_TOPIC_TAG, new ObjectMapper().writeValueAsBytes(message));
		} catch (JsonProcessingException e1) {
			throw new NoticeDispatchException(e1);
		}
        SendResult sendResult = null;
        try {
            sendResult = defaultMQProducer.send(msg);
        } catch (MQClientException | RemotingException | MQBrokerException | InterruptedException e) {
        	throw new NoticeDispatchException(e);
        }
        // 当消息发送失败时如何处理
        if (sendResult == null || sendResult.getSendStatus() != SendStatus.SEND_OK) {
        	throw new NoticeDispatchException(null);
        }
	}
	
	@PostConstruct
	public void consume() throws MQClientException{
        // 订阅指定MyTopic下tags等于MyTag
        defaultMQPushConsumer.subscribe(MQ_NOTIFY_TOPIC, MQ_NOTIFY_TOPIC_TAG);
        // 设置Consumer第一次启动是从队列头部开始消费还是队列尾部开始消费<br>
        // 如果非第一次启动，那么按照上次消费的位置继续消费
        defaultMQPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        // 设置为集群消费(区别于广播消费)
        defaultMQPushConsumer.setMessageModel(MessageModel.CLUSTERING);
        defaultMQPushConsumer.registerMessageListener(new MessageListenerConcurrently() {
            // 默认msgs里只有一条消息，可以通过设置consumeMessageBatchMaxSize参数来批量接收消息
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                MessageExt msg = msgs.get(0);
                if (msg.getTopic().equals(MQ_NOTIFY_TOPIC)) {
                    if (msg.getTags() != null && msg.getTags().equals(MQ_NOTIFY_TOPIC_TAG)) {
                    	System.out.println("----------------------");
                    	System.out.println(new String(msg.getBody()));
                    	syncNotifyService.forEach(service->{
                    		try {
								service.inform(new ObjectMapper().readValue(msg.getBody(), com.jingyunbank.etrade.api.message.bo.Message.class));
							} catch (Exception e) {
								e.printStackTrace();
							}
                    	});
                    }
                }
                System.out.println("----------------------");
                System.out.println(new String(msg.getBody()));
                // 如果没有return success ，consumer会重新消费该消息，直到return success
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });

        // Consumer对象在使用之前必须要调用start初始化，初始化一次即可<br>
        defaultMQPushConsumer.start();
	}

}
