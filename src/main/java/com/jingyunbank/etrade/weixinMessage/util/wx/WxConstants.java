package com.jingyunbank.etrade.weixinMessage.util.wx;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:system.properties")
public class WxConstants {

	public static final String OKCODE = "200";

	public static final String FAILCODE = "500";
	public final static ClassLoader RESOURCE_LOADER = WxConstants.class.getClassLoader();
	private static final String CONFIG_FILE_LOCATION = "system.properties";

	// 用户三级好友
	
	public static final String PAYMENT_SUCCESS = "weixinMesage.payment.success";
	public static final String REFUND_REMINDER = "weixinMesage.payment.refund";
	public static final String  ORDER_SUCCESS= "weixinMesage.payment.commit";
	public static final String ORDER_CANCEL = "weixinMesage.payment.cancel";
	public static final String MAKE_GROUP = "weixinMesage.payment.group";
	/*public static final String REFUND_REMINDER = "weixinMesage.payment.refund";
	public static final String REFUND_REMINDER = "weixinMesage.payment.refund";
	public static final String REFUND_REMINDER = "weixinMesage.payment.refund";*/


	public static String getString(String key) {
		return refresh().getProperty(key, "");
	}

	private static Properties refresh() {
		Properties props = new Properties();
		InputStream is = RESOURCE_LOADER.getResourceAsStream(CONFIG_FILE_LOCATION);
		try {
			props.load(is);
			is.close();
		} catch (IOException e) {
			// consume the exception silently.
		}
		return props;
	}

}
