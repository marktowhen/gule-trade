package com.jingyunbank.etrade.base.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
/**
 * 加载短信邮箱服务类 
 * @author qxs
 *
 */
@Component("systemConfigProperties")
@Scope("singleton")
public final class SystemConfigProperties {

	private Logger logger = Logger.getLogger(SystemConfigProperties.class);
	private SystemConfigProperties() {
		try {
			//加载短信服务类
			Class.forName(getString(MOBILE_PROVIDER));
			//加载邮箱服务类
			Class.forName(getString(EMAIL_PROVIDER));
		} catch (ClassNotFoundException e) {
			logger.error(e);
		}
	}
	/**
	 * 图片等文件的存放目录
	 */
	public final static String ROOT_FILE_PATH = "root.file.path";
	
	public final static String HEAD_PICTURE_SIZE = "head.picture.size";
	
	private final static String PROPERTIES_FILE_LOCATION = "system.properties";
	
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
	
	public final static String MAX_LEVEL_OF_HIERARCHY = "max.level.of.hierarchy";
	
	public final static String REGISTER_BONUS_ENABLED = "register.bonus.enabled";
	public final static String REGISTER_BONUS_LEVEL = "register.bonus.level";
	public final static String REGISTER_BONUS_MONEY = "register.bonus.money";
	
	public final static String REALNAME_BONUS_ENABLED = "realname.bonus.enabled";
	public final static String REALNAME_BONUS_LEVEL = "realname.bonus.level";
	public final static String REALNAME_BONUS_MONEY = "realname.bonus.money.";
	
	public final static String PURCHASE_BONUS_ENABLED = "purchase.bonus.enabled";
	public final static String PURCHASE_BONUS_LEVEL = "purchase.bonus.level";
	
	public final static String DEFAULT_1ST_LEVEL_RATE = "default.first.level.rate";
	public final static String DEFAULT_2ND_LEVEL_RATE = "default.second.level.rate";
	public final static String DEFAULT_3RD_LEVEL_RATE = "default.third.level.rate";
	
	public final static ClassLoader RESOURCE_LOADER = SystemConfigProperties.class.getClassLoader();
	
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
