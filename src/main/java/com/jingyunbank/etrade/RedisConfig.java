package com.jingyunbank.etrade;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
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
		rcf.setHostName("124.128.245.162");
		rcf.setPort(6379);
		return rcf;
	}

	@Bean
	public StringRedisTemplate redisTemplate(){
		return new StringRedisTemplate(jedisConnectionFactory());
	}
}
