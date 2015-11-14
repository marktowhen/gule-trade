package com.jingyunbank.etrade.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.jingyunbank.etrade.api.base.intercepter.AuthIntercepter;

@Configuration
public class MvcConfig {
	
	@Bean
    public WebMvcConfigurerAdapter webMvcConfigurerAdapter() {
        return new WebMvcConfigurerAdapter() {
        	@Override
        	public void addInterceptors(InterceptorRegistry registry) {
        		registry.addInterceptor(new AuthIntercepter());
        	}
		};
    }
}
