package com.jingyunbank.etrade.weixin.entity;

public class Constants {
	// 项目密钥
	public static String ADDSTRING = "sxbank";
	// 项目中用户身份ID在session中的key
	public static final String IDBYSESSION = TEA.Encrypt("UID_" + ADDSTRING);
	// 项目中用户身份username在session中的key
	public static final String USERNAMEBYSESSION = TEA.Encrypt("UNM_"
			+ ADDSTRING);
	// 项目中用户身份username在session中的key
	public static final String CARTIDBYSESSION = TEA.Encrypt("CARTID_"
				+ ADDSTRING);

}
