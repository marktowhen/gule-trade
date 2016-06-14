package com.jingyunbank.etrade.weixin.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.jingyunbank.etrade.weixin.util.JsonUtil;
import com.jingyunbank.etrade.weixin.entity.AccessToken;


public class WechatKit {

	/**
	 * 获取AccessToken
	 * @return
	 */
	public static String getAccess_token() {
		HttpResponse response = null;
		CloseableHttpClient httpclient = null;
		HttpGet get = null;
		String access_token = "";
		try {
			get = new HttpGet("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wx3f0da4d18b8e3ff8&secret=eca90c942400a6a3c6a70099bb7958d7");
			httpclient = HttpClients.createDefault();
			response = httpclient.execute(get);
			int statusCode = response.getStatusLine().getStatusCode();
			if(statusCode >= 200 && statusCode <= 300){
				HttpEntity entity = response.getEntity();
				if(null != entity){
					String content = EntityUtils.toString(entity);
					AccessToken accessToken = (AccessToken)JsonUtil.getInstance().json2obj(content, AccessToken.class);
					if(accessToken != null){
						access_token = accessToken.getAccess_token();
					}
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			get.releaseConnection();
		}
		return access_token;
	}
	
	/*public static void main(String[] args) {
		String access_token = getAccess_token();
		//OXNulWitJRms4_J7qXkhPj_xMfpwqkHeK8t2sRi0bV4_pRujICcOFt8CapjY-UD3bx_722cGYNtTgJOJs1H3OcO9kQ2H9w-i7vaAdVz2pF4
		//OXNulWitJRms4_J7qXkhPj_xMfpwqkHeK8t2sRi0bV4hV_iA4jnEuS0qYyYLfv0QK0QKK2DVm5Mw5Dl27C8Bm8cYio9Xe-ctWvjKOWFLYlQ
		System.out.println(access_token);
	}*/
	
	/**
	 * 将微信消息中的CreateTime转换成标准格式的时间（yyyy-MM-dd HH:mm:ss）
	 * 
	 * @param createTime 消息创建时间
	 * @return
	 */
	public static String formatTime(String createTime) {
		// 将微信传入的CreateTime转换成long类型，再乘以1000
		long msgCreateTime = Long.parseLong(createTime) * 1000L;
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(new Date(msgCreateTime));
	}
}
