package com.jingyunbank.etrade.weixin.util;

import java.awt.List;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import com.jingyunbank.etrade.weixin.bean.SNSUserInfo;
import com.jingyunbank.etrade.weixin.bean.WeixinOauth2Token;
import com.jingyunbank.etrade.weixin.util.MyX509TrustManager;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;



/**
 * 获取微信授权的静态方法
 * @author Administrator 
 * @date 2016年4月22日
	@todo TODO
 */
public class GetAccessToken{
	
	
	public static String GetCodeRequest = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect"; 

	/**
	 * 用户同意授权获取code值
	 * @param appId
	 * @param redirect_uri
	 * @param scope
	 * @return
	 */
	public static String getCodeRequest(String appId,String redirect_uri,String scope){
		
		String result=null;
		GetCodeRequest = GetCodeRequest.replace("APPID", appId);
		GetCodeRequest = GetCodeRequest.replace("REDIRECT_URI",redirect_uri);
		GetCodeRequest = GetCodeRequest.replace("SCOPE",scope);
		result=GetCodeRequest;
		return result;
		
	}
	
	
	/**
	 * 获取网页授权的access_token
	 * @param appId
	 * @param appSecret
	 * @param code
	 * @return
	 */
	public static WeixinOauth2Token getOauth2AccessToken(String appId,String appSecret,String code){
		WeixinOauth2Token wet=null;
		//拼接请求地址
		String requestUrl="https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
		 requestUrl = requestUrl.replace("APPID", appId);
	     requestUrl = requestUrl.replace("SECRET", appSecret);
	     requestUrl = requestUrl.replace("CODE", code);
	     //获取网页授权凭证
	     JSONObject jsonObject = GetAccessToken.httpsRequest(requestUrl, "GET", null);
	     if(null!=jsonObject){
	    	 wet = new WeixinOauth2Token();
	    	 wet.setAccessToken(jsonObject.getString("access_token"));
	    	 wet.setExpiresIn(jsonObject.getInt("expires_in"));
	    	 wet.setRefreshToken(jsonObject.getString("refresh_token"));
	    	 wet.setOpenId(jsonObject.getString("openid"));
	    	 wet.setScope(jsonObject.getString("scope"));
	     }
	     
	     return wet;
		
	}
	
	/**
	 * 发送https请求
	 * @param requestUrl 请求地址
	 * @param requestMethod 请求方式
	 * @param outputStr
	 * @return
	 */
	public static JSONObject httpsRequest(String requestUrl, String requestMethod, String outputStr){
		JSONObject jsonObject=null;
		
		  try {
	            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
	            TrustManager[] tm = { new MyX509TrustManager() };
	            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
	            sslContext.init(null, tm, new java.security.SecureRandom());
	            // 从上述SSLContext对象中得到SSLSocketFactory对象
	            SSLSocketFactory ssf = sslContext.getSocketFactory();

	            URL url = new URL(requestUrl);
	            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
	            conn.setSSLSocketFactory(ssf);
	            
	            conn.setDoOutput(true);
	            conn.setDoInput(true);
	            conn.setUseCaches(false);
	            // 设置请求方式（GET/POST）
	            conn.setRequestMethod(requestMethod);

	            // 当outputStr不为null时向输出流写数据
	            if (null != outputStr) {
	                OutputStream outputStream = conn.getOutputStream();
	                // 注意编码格式
	                outputStream.write(outputStr.getBytes("UTF-8"));
	                outputStream.close();
	            }

	            // 从输入流读取返回内容
	            InputStream inputStream = conn.getInputStream();
	            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
	            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
	            String str = null;
	            StringBuffer buffer = new StringBuffer();
	            while ((str = bufferedReader.readLine()) != null) {
	                buffer.append(str);
	            }

	            // 释放资源
	            bufferedReader.close();
	            inputStreamReader.close();
	            inputStream.close();
	            inputStream = null;
	            conn.disconnect();
	           jsonObject = JSONObject.fromObject(buffer.toString());
	        } catch (ConnectException ce) {
	        	ce.printStackTrace();
	            /*log.error("连接超时：{}", ce);*/
	        } catch (Exception e) {
	        	e.printStackTrace();
	            /*log.error("https请求异常：{}", e);*/
	        }
	        return jsonObject;
		
	}
	/**
	 * 得到用户的基本信息
	 * @param accessToken
	 * @param openId
	 * @return
	 */
	public static SNSUserInfo  getUserInfo(String accessToken,String openId){
		SNSUserInfo snsuserInfo=null;
		//拼接请求地址
		String requestUrl="https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID";
		requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken).replace("OPENID", openId);
		
		JSONObject jsonObject = GetAccessToken.httpsRequest(requestUrl, "GET", null);
		if(null!=jsonObject){
			snsuserInfo=new SNSUserInfo();
			snsuserInfo.setOpenId(jsonObject.getString("openid"));
			snsuserInfo.setUsername(jsonObject.getString("nickname"));
			snsuserInfo.setSex(jsonObject.getInt("sex"));
			snsuserInfo.setCountry(jsonObject.getString("country"));
			snsuserInfo.setProvice(jsonObject.getString("provice"));
			snsuserInfo.setCity(jsonObject.getString("city"));
			snsuserInfo.setHeadImgUrl(jsonObject.getString("headimgurl"));
			snsuserInfo.setPrivilegeList(JSONArray.toList(jsonObject.getJSONArray("privilege"),List.class));
		}
		return snsuserInfo;
		
	}
	
	
	/**
	 * url编码
	 * @param source
	 * @return
	 */
	public static String urlEncodeUTF8(String source){
		String result = source;
		  try {
			result = java.net.URLEncoder.encode(source, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
		
	}
	
	public static void main(String[] args){
		String source="https://guoxiaoxue.tunnel.qydev.com";
		System.out.println(GetAccessToken.urlEncodeUTF8(source));
	}

}
