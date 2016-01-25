package com.jingyunbank.etrade.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
@EnableCaching
public class CacheConfig {

	@Autowired
	private RedisTemplate<Object, Object> redisTemplate;

	@Bean
	public CacheManager cacheManager(){
		return new RedisCacheManager(redisTemplate);
	}
}
