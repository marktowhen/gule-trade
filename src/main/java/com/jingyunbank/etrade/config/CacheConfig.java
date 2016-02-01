package com.jingyunbank.etrade.config;

import java.lang.reflect.Method;

import javax.jms.ConnectionFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.listener.MessageListenerContainer;

@Configuration
@EnableCaching
public class CacheConfig {

	@Autowired
	private RedisTemplate<Object, Object> redisTemplate;

	@Autowired
	private ConnectionFactory connectionFactory;
	
	@Bean
	public CacheManager cacheManager(){
		RedisCacheManager mgner = new RedisCacheManager(redisTemplate);
		mgner.setUsePrefix(true);
		return mgner;
	}
	
	@Bean(name="CustomKG")
	public KeyGenerator keygenerator(){
		return new CustomKeyGenerator();
	}
	
	public static class CustomKeyGenerator implements KeyGenerator{

		@Override
		public Object generate(Object target, Method method, Object... params) {
			String signature = target.getClass().getName()+"."+method.getName();
			if (params.length == 0) {
				return signature;
			}
			if (params.length == 1) {
				Object param = params[0];
				if (param != null && !param.getClass().isArray()) {
					return signature + param;
				}
			}
			return signature + new SimpleKey(params);
		}
	}
	
	public DefaultJmsListenerContainerFactory jlcf(){
		DefaultJmsListenerContainerFactory jscf = new DefaultJmsListenerContainerFactory();
		jscf.setPubSubDomain(true);
		jscf.setConnectionFactory(connectionFactory);
		return jscf;
	}
	
	public DefaultJmsListenerContainerFactory jlcf0(){
		DefaultJmsListenerContainerFactory jscf = new DefaultJmsListenerContainerFactory();
		jscf.setPubSubDomain(false);
		jscf.setConnectionFactory(connectionFactory);
		return jscf;
	}
}
