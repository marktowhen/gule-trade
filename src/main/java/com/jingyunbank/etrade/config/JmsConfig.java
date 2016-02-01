package com.jingyunbank.etrade.config;

import javax.jms.ConnectionFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;

@Configuration
public class JmsConfig {

	@Autowired
	private ConnectionFactory connectionFactory;
	
	//订阅发布模式监听器容器
	@Bean(name="topicJmsListnerContainer")
	public DefaultJmsListenerContainerFactory topic(){
		DefaultJmsListenerContainerFactory jscf = new DefaultJmsListenerContainerFactory();
		jscf.setPubSubDomain(true);
		jscf.setConnectionFactory(connectionFactory);
		return jscf;
	}
	
	//队列模式监听器容器
	@Bean(name="queueJmsListnerContainer")
	public DefaultJmsListenerContainerFactory queue(){
		DefaultJmsListenerContainerFactory jscf = new DefaultJmsListenerContainerFactory();
		jscf.setPubSubDomain(false);
		jscf.setConnectionFactory(connectionFactory);
		return jscf;
	}
}
