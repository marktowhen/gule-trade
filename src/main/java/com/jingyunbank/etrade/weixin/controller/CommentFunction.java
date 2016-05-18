package com.jingyunbank.etrade.weixin.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONObject;

public class CommentFunction {

	/*private static final String appId = "wx84c55bba6fe87f35";//"wx8340070ff03ea6e4";//wx84c55bba6fe87f35
	private static final String appSecret = "d4a04e93a408c1ce83009c47a727ca66";//"9cd2ebf52eb10b44d1151051f84ec857";//d4a04e93a408c1ce83009c47a727ca66
*/	
	/**
	 * 通过appid和appSecret活的access_token
	 **/
	public static String getAccess_token() { // 获得ACCESS_TOKEN15.15
		String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wx0e77cf963bfc785b&secret=d637c27b448c14baeb3a282a77d94356";
		String accessToken = null;
		try {
			URL urlGet = new URL(url);
			HttpURLConnection http = (HttpURLConnection) urlGet.openConnection();
			http.setRequestMethod("GET"); // 必须是get方式请求
			http.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
			http.setDoOutput(true);
			http.setDoInput(true);
			System.setProperty("sun.net.client.defaultConnectTimeout", "30000");// 连接超时30秒
			System.setProperty("sun.net.client.defaultReadTimeout", "30000"); // 读取超时30秒
			http.connect();
			InputStream is = http.getInputStream();
			int size = is.available();
			byte[] jsonBytes = new byte[size];
			is.read(jsonBytes);
			String message = new String(jsonBytes, "UTF-8");
			System.out.println(message);
			JSONObject demoJson = new JSONObject(message);
			accessToken = demoJson.getString("access_token");
			System.out.println(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return accessToken;
	}

	/*public static JSONObject getWeiXinUserInfo(String openid){
		String accessToken = getAccess_token();
		String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + accessToken + "&openid=" + openid;
		JSONObject demoJson = null;
		try {
			URL urlGet = new URL(url);
			HttpURLConnection http = (HttpURLConnection) urlGet.openConnection();
			http.setRequestMethod("GET"); // 必须是get方式请求
			http.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
			http.setDoOutput(true);
			http.setDoInput(true);
			System.setProperty("sun.net.client.defaultConnectTimeout", "30000");// 连接超时30秒
			System.setProperty("sun.net.client.defaultReadTimeout", "30000"); // 读取超时30秒
			http.connect();
			InputStream is = http.getInputStream();
			int size = is.available();
			byte[] jsonBytes = new byte[size];
			is.read(jsonBytes);
			String message = new String(jsonBytes, "UTF-8");
			demoJson = new JSONObject(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return demoJson;
		
	}*/
	
	
	/**
	 * 创建自定义菜单
	 **/
	public static int createMenu() throws IOException {
		//微信底部标题的Title的内容
		String user_define_menu = "{\"button\":[{\"name\":\"跳转首页\",\"type\":\"view\",\"key\":\"1110\",\"url\":\"http://m.jingyunbank.com/contact/aboutme\"},{\"name\":\"会员\",\"sub_button\":[{\"name\":\"我的二维码\",\"type\":\"click\",\"key\":\"1111\",\"url\":null},{\"name\":\"商城\",\"type\":\"view\",\"key\":\"1112\",\"url\":\"http://m.jingyunbank.com/contact/aboutme\"}]},{\"name\":\"关于我们\",\"sub_button\":[{\"name\":\"人工客服\",\"type\":\"view\",\"key\":\"1211\",\"url\":\"http://m.jingyunbank.com/contact/aboutme\"},{\"name\":\"联系我们\",\"type\":\"view\",\"key\":\"1212\",\"url\":\"http://m.jingyunbank.com/contact/aboutme\"},{\"name\":\"公司简介\",\"type\":\"view\",\"key\":\"1213\",\"url\":\"http://m.jingyunbank.com/contact/aboutme\"}]}]}";
		
		
		//此处改为自己想要的结构体，替换即可
		String access_token = getAccess_token();
		System.out.println("token="+access_token);
		String action = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token="+ access_token;
		try {
			URL url = new URL(action);
			HttpURLConnection http = (HttpURLConnection) url.openConnection();
			http.setRequestMethod("POST");
			http.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
			http.setDoOutput(true);
			http.setDoInput(true);
			System.setProperty("sun.net.client.defaultConnectTimeout", "30000");// 连接超时30秒
			System.setProperty("sun.net.client.defaultReadTimeout", "30000"); // 读取超时30秒
			http.connect();
			OutputStream os = http.getOutputStream();
			os.write(user_define_menu.getBytes("UTF-8"));// 传入参数
			os.flush();
			os.close();
			InputStream is = http.getInputStream();
			int size = is.available();
			byte[] jsonBytes = new byte[size];
			is.read(jsonBytes);
			String message = new String(jsonBytes, "UTF-8");
			System.out.println(message);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	/*public static void main(String[] args) {
		try {
			CommentFunction.createMenu();
			System.out.println(CommentFunction.createMenu());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}*/
}
