package com.jingyunbank.etrade.user.controller;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.web.bind.annotation.RestController;
/*https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect
*/
@RestController
public class WeiXinLoginController {
	
	public static void WeiXinGetOpenid(){
		HttpPost httpPost = null;
		CloseableHttpClient httpclient = null;
		HttpResponse response = null;
		String url="https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";
		try {
			httpclient = HttpClients.createDefault();
			httpPost = new HttpPost(url);
			response=httpclient.execute(httpPost);
			
		}  catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
