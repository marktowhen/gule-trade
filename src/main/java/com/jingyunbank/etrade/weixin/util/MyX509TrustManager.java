package com.jingyunbank.etrade.weixin.util;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;
/**
 * 信任管理器
 * @author Administrator 
 * @date 2016年4月21日
	@todo TODO
 */
public class MyX509TrustManager implements X509TrustManager{
	//检查客户端的证书
	@Override
	public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
		// TODO Auto-generated method stub
		
	}
	//检查服务端的证书
	@Override
	public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
		// TODO Auto-generated method stub
		
	}
	//返回受信任的x509证书数组
	@Override
	public X509Certificate[] getAcceptedIssuers() {
		// TODO Auto-generated method stub
		return null;
	}

	

}
