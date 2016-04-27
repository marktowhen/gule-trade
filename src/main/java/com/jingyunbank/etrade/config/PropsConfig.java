package com.jingyunbank.etrade.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("systemConfigProperties")
@Scope("singleton")
public final class PropsConfig {

	
	private final static String PROPERTIES_FILE_LOCATION = "config.properties";
	
	public final static String GOBAL_SEPARATOR = "global.separator";
	//The key name of the values which specify the mobiles of the admin.
	//the mobiles are separated by <code>GOBAL_SEPARATOR</code>
	public final static String ADMIN_MOBILE = "admin.mobile";
	//The key name of the values which specify the emails of the admin.
	//the emails are separated by <code>GOBAL_SEPARATOR</code>
	public final static String ADMIN_EMAIL = "admin.email";
	
	public final static String MOBILE_REMINDER_ENABLED = "mobile.reminder.enabled";
	public final static String MOBILE_PROVIDER = "mobile.provider";
	public final static String EMAIL_REMINDER_ENABLED = "email.reminder.enabled";
	public final static String EMAIL_PROVIDER = "email.provider";
	
	
	// 用户三级好友
	public static final String SALES_USERREFERRAL_URL = "url.sales.level.detail";
	public static final String SALES_USERREFERRALCOUNT_URL = "url.sales.level.count";
	public static final String SALES_USERREFERRAL_QUERY = "url.sales.level.query";
	// 三级总收益
	public static final String SALES_LEVEL_SUMMARY_URL = "url.sales.level.summary";

	public static final String SALES_PURCHASE_LOG = "url.sales.purchase.log";
	public static final String SALES_PURCHASE_LOGCOUNT = "url.sales.purchase.count";

	public static final String SALES_PROFIT_TOTAL = "url.sales.profit.total";

	//在职用户投资收益
	public static final String EMP_PROFIT_TOTAL = "url.emp.profit.total";
	//在职用户投资收益
	public static final String EMP_PROFIT_DETAIL = "url.emp.profit.detail";
	public static final String EMP_PROFIT_COUNT = "url.emp.profit.count";
	

	/* 同步到三级分销的内容 以下 */

	// 注册信息
	public static final String SALES_REGISTER_URL = "url.sales.register";
	// 投标信息
	public static final String SALES_PURCHASE_URL = "url.sales.purchase";
	// 更新用户信息
	public static final String SALES_USERINFO_URL = "url.sales.updateuser";
	// 实名
	public static final String SALES_REALNAME_URL = "url.sales.realname";
	
	//转入佣金
	public static final String SALES_THREE_COMMISSION = "url.three.commission";
	//团购支付超时时间 秒
	public static final String GROUP_PAY_TIME_OUT = "group.pay.time.out";
	
	

	
	public final static ClassLoader RESOURCE_LOADER = PropsConfig.class.getClassLoader();
	
	public static String getString(String key){
		return refresh().getProperty(key, "");
	}
	
	public static String[] getStrings(String key){
		Properties p = refresh();
		String r = p.getProperty(key, "");
		return r.split(p.getProperty(GOBAL_SEPARATOR, ","));
	}
	
	public static int getInt(String key){
		Properties p = refresh();
		String r = p.getProperty(key, "0");
		return Integer.parseInt(r.trim());
	}
	
	public static long getLong(String key){
		Properties p = refresh();
		String r = p.getProperty(key, "0");
		return Long.parseLong(r.trim());
	}
	
	public static double getDouble(String key){
		Properties p = refresh();
		String r = p.getProperty(key, "0.0");
		return Double.parseDouble(r.trim());
	}
	
	public static boolean getBoolean(String key){
		Properties p = refresh();
		String r = p.getProperty(key, "false");
		return r.trim().equals("true");
	}
	
	public static Map<String, String> toMap(){
		Properties pr = refresh();
		Map<String, String> rm = new HashMap<>();
		pr.forEach((k,v) -> {
			rm.put((String)k, (String)v);
		});
		return rm;
	}
	
	private static Properties refresh(){
		Properties props = new Properties();
		InputStream is = RESOURCE_LOADER.getResourceAsStream(PROPERTIES_FILE_LOCATION); //RESOURCE_LOADER.getResourceAsStream(PROPERTIES_FILE_LOCATION);
		try {
			props.load(is);
			is.close();
		} catch (IOException e) {
			//consume the exception silently.
		}
		
		return props;
	}
	
	
	
	
}
