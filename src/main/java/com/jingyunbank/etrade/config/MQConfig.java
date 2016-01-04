package com.jingyunbank.etrade.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;

@Configuration
public class MQConfig {

	@Bean(destroyMethod="shutdown")
	@Scope("singleton")
	public DefaultMQProducer producer() throws MQClientException{
		DefaultMQProducer producer = new DefaultMQProducer("etrade-producer-group");
		producer.setNamesrvAddr("101.200.215.25:9876");
		producer.setInstanceName(String.valueOf(System.currentTimeMillis()));
		producer.start();
		return producer;
	}
	
	@Bean(destroyMethod="shutdown")
	@Scope("prototype")
	public DefaultMQPushConsumer pushConsumer() throws MQClientException{
		DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("etrade-consumer-group");
		consumer.setNamesrvAddr("101.200.215.25:9876");
		consumer.setInstanceName(String.valueOf(System.currentTimeMillis()));
		return consumer;
	}
	
}
