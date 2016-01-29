package com.jingyunbank.etrade.config;

import java.nio.charset.Charset;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import redis.clients.jedis.JedisPoolConfig;

@Configuration
@EnableRedisHttpSession
public class RedisConfig {

	@Bean
	public RedisConnectionFactory jedisConnectionFactory(){
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(40);
		config.setMaxIdle(5);
		config.setMinIdle(1);
		config.setTestOnBorrow(true);
		config.setTestOnReturn(true);
		config.setTestWhileIdle(true);
		JedisConnectionFactory rcf = new JedisConnectionFactory(config);
		rcf.setHostName("101.200.215.25");
		rcf.setPort(6379);
		rcf.setPassword("testredisserver");
		return rcf;
	}

	@Bean
	public StringRedisTemplate stringRedisTemplate(){
		StringRedisTemplate redis = new StringRedisTemplate(jedisConnectionFactory());
		return redis;
	}
	
	@Bean
	public RedisTemplate<Object, Object> redisTemplate(){
		RedisTemplate<Object, Object> redis = new RedisTemplate<Object, Object>();
		redis.setConnectionFactory(jedisConnectionFactory());
		//redis.setDefaultSerializer(new StringRedisSerializer(Charset.forName("UTF-8")));
		
		return redis;
	}
}
