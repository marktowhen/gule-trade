package com.jingyunbank.etrade.weixin.controller;

import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.jingyunbank.etrade.weixin.entity.ComplexButton;
import com.jingyunbank.etrade.weixin.entity.Button;
import com.jingyunbank.etrade.weixin.entity.Menu;
import com.jingyunbank.etrade.weixin.entity.CommonButton;
import com.jingyunbank.etrade.weixin.util.JsonUtil;
import com.jingyunbank.etrade.weixin.util.WechatKit;


public class CreateMenuController {
	
	private static Logger logger = Logger.getLogger(CreateMenuController.class);
	//https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=
	
	/**
	 * 删除菜单
	 * @return
	 */
	public static String deleteMenu(){
		String result = "";
		String url = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token="+ WechatKit.getAccess_token();
		HttpResponse response = null;
		CloseableHttpClient httpclient = null;
		HttpGet get = null;
		try {
			httpclient = HttpClients.createDefault();
			get = new HttpGet(url);
			response = httpclient.execute(get);
			int statusCode = response.getStatusLine().getStatusCode();
			if(statusCode >= 200 && statusCode <= 300){
				HttpEntity entity = response.getEntity();
				if(null != entity){
					result = EntityUtils.toString(entity);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}finally{
			get.releaseConnection();
		}
		return result;
	}
	
	/**
	 * 创建菜单
	 * @param menu
	 * @return
	 */
	public static String createMenu(String menus){
		String result = "";
		CloseableHttpClient httpclient = null;
		HttpPost httpPost = null;
		HttpResponse response = null;// 
		String url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token="+ WechatKit.getAccess_token();
		try {
			httpclient = HttpClients.createDefault();
			httpPost = new HttpPost(url);
			httpPost.setEntity(new StringEntity(menus, "UTF-8"));
			response = httpclient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			result = EntityUtils.toString(entity);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}finally{
			httpPost.releaseConnection();
		}
		return result;
		
	}
	public static String getMenu(){
		//一级菜单
		CommonButton mainBtn10 = new CommonButton();
		mainBtn10.setName("跳转首页");
		mainBtn10.setType("view");
		mainBtn10.setKey("1110");
		mainBtn10.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx3f0da4d18b8e3ff8&redirect_uri=https%3A%2F%2Fguoyuxue.tunnel.qydev.com%2Fapi%2Fget%2Fuser&response_type=code&scope=snsapi_userinfo&state=123#wechat_redirect");
		/*https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx3f0da4d18b8e3ff8&redirect_uri=https%3a%2f%2fguoyuxue.tunnel.qydev.com%2fapi%2fget%2fuser&response_type=code&scope=snsapi_userinfo&state=123#wechat_redirect
*/		//二级菜单
		CommonButton btn1111 = new CommonButton();
		btn1111.setName("我的二维码");
		btn1111.setType("click");
		btn1111.setKey("1111");
		//二级菜单
		CommonButton btn1112 = new CommonButton();
		btn1112.setName("商城");
		btn1112.setType("view");
		btn1112.setKey("1112");
		btn1112.setUrl("http://m.jingyunbank.com/contact/aboutme");
		//一级菜单
		ComplexButton mainBtn11 = new ComplexButton();
		mainBtn11.setName("会员");
		mainBtn11.setSub_button(new CommonButton[] { btn1111, btn1112});
		//二级菜单
		CommonButton btn1211 = new CommonButton();
		btn1211.setName("人工客服");
		btn1211.setType("view");
		btn1211.setKey("1211");
		btn1211.setUrl("http://m.jingyunbank.com/contact/aboutme");
		//二级菜单
		CommonButton btn1212 = new CommonButton();
		btn1212.setName("联系我们");
		btn1212.setType("view");
		btn1212.setKey("1212");
		btn1212.setUrl("http://m.jingyunbank.com/contact/aboutme");
		//二级菜单
		CommonButton btn1213 = new CommonButton();
		btn1213.setName("公司简介");
		btn1213.setType("view");
		btn1213.setKey("1213");
		btn1213.setUrl("http://m.jingyunbank.com/contact/aboutme");
		//一级菜单
		ComplexButton mainBtn12 = new ComplexButton();
		mainBtn12.setName("关于我们");
		mainBtn12.setSub_button(new CommonButton[] {btn1211, btn1212, btn1213});
		
		//所有菜单的一级菜单   下边显示用的
		Menu menu = new Menu();
		menu.setButton(new Button[]{mainBtn10, mainBtn11, mainBtn12});
		String menuJson = JsonUtil.getInstance().obj2json(menu);
		return menuJson;
		
	}
	public static void main(String[] args) throws UnsupportedEncodingException {
		String deleteMenu = deleteMenu();
		System.out.println(deleteMenu);
		String createMenu = createMenu(getMenu());
		System.out.println(createMenu);
		System.out.println(getMenu());
	}
}
