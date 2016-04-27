package com.jingyunbank.etrade.weixin.controller;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.jingyunbank.etrade.weixin.entity.ComplexMenu;
import com.jingyunbank.etrade.weixin.entity.Menu;
import com.jingyunbank.etrade.weixin.entity.Menus;
import com.jingyunbank.etrade.weixin.entity.SingleMenu;
import com.jingyunbank.etrade.weixin.util.JsonUtil;
import com.jingyunbank.etrade.weixin.util.WechatKit;


public class CreateMenuController {
	
	private static Logger logger = Logger.getLogger(CreateMenuController.class);
	/**
	 * 创建菜单
	 * @param menu
	 * @return
	 */
	public static String createMenu(String menus){
		String result = "";
		CloseableHttpClient httpclient = null;
		HttpPost httpPost = null;
		HttpResponse response = null;
		String url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=" + WechatKit.getAccess_token();
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
		
		SingleMenu singleMenu1 = new SingleMenu();
		singleMenu1.setName("跳转首页");
		singleMenu1.setType("view");
		singleMenu1.setKey("");
		singleMenu1.setUrl("");
		
		
		SingleMenu singleMenu21 = new SingleMenu();
		singleMenu21.setName("第二个菜单的第一的菜单页");
		singleMenu21.setType("");
		singleMenu21.setKey("");
		singleMenu21.setUrl("");
		
		SingleMenu singleMenu22 = new SingleMenu();
		singleMenu22.setName("第二个菜单的第二的菜单页");
		singleMenu22.setType("");
		singleMenu22.setKey("");
		singleMenu22.setUrl("");
		
		
		ComplexMenu complexMenu2 = new ComplexMenu();
		complexMenu2.setName("第二个菜单");
		complexMenu2.setSingle(new SingleMenu[]{singleMenu21,singleMenu22});
		
		
		//所有的菜单
		Menus menus = new Menus();
		menus.setSingle(new Menu[]{singleMenu1,complexMenu2});
		String menuJson = JsonUtil.getInstance().obj2json(menus);
		return menuJson;
		
	}
}
