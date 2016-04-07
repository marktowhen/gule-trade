package com.jingyunbank.etrade.config;

import java.io.IOException;
import java.util.Properties;

import javax.sql.DataSource;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.alibaba.druid.pool.DruidDataSource;
import com.jingyunbank.etrade.asyn.util.SpringContextUtils;

@Configuration
@EnableTransactionManagement
@PropertySource("classpath:application.properties")
public class DataConfig {

	private String get(String key) throws IOException{
		Properties prop = new Properties();
    	prop.load(DataConfig.class.getClassLoader().getResourceAsStream("application.properties"));
    	return prop.getProperty(key);
	}
	
	@Bean
	public SqlSessionFactoryBean mybatisSqlSession() throws IOException{
		SqlSessionFactoryBean ssfb = new SqlSessionFactoryBean();
		ssfb.setDataSource(dataSource());
		ssfb.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:com/jingyunbank/etrade/**/mapper/*Mapper.xml"));
		return ssfb;
	}
	
	@Bean
	public MapperScannerConfigurer mapperScanner(){
		MapperScannerConfigurer msc = new MapperScannerConfigurer();
		msc.setBasePackage("com.jingyunbank.etrade.**.dao");
		return msc;
	}
	
	@Bean
	@Scope(value=ConfigurableBeanFactory.SCOPE_SINGLETON)
	public DataSource dataSource() throws IOException{
		DruidDataSource ds = new DruidDataSource();
		System.out.println(get("spring.datasource.url"));
		ds.setUrl(get("spring.datasource.url"));
		ds.setUsername(get("spring.datasource.username"));
		ds.setPassword(get("spring.datasource.password"));
		ds.setDriverClassName(get("spring.datasource.driver-class-name"));
		ds.setMaxActive(Integer.parseInt(get("druid.max.active")));
		ds.setDefaultAutoCommit(false);
		return ds;
	}
	
	@Bean
	public PlatformTransactionManager transactionManager() throws IOException{
		return new DataSourceTransactionManager(dataSource());
	}
	//通过beanid获取实例
	@Bean
	@Scope(value=ConfigurableBeanFactory.SCOPE_SINGLETON)
	public SpringContextUtils getSpringContextUtils(){
		return new SpringContextUtils();
	}
}
