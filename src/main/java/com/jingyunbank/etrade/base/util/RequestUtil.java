package com.jingyunbank.etrade.base.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import com.jingyunbank.etrade.base.constant.Constant;
/**
 * 测试用 等待正式工具类
 * qxs
 */
public class RequestUtil {

	
	/**
	 * 获取唯一存在的id
	 * @return uuid
	 * 2015年11月5日 qxs
	 */
	public static String getUiid(){
		SimpleDateFormat f = new SimpleDateFormat("yyyyMMddHHmmss");
		Random random = new Random();
		int num = random.nextInt()%10000;
		if(num<0) num = -num;
		return f.format(new Date())+num;
	}
	
	/**
	 * 获取已登录的userid
	 * @param request
	 * @return
	 * 2015年11月5日 qxs
	 */
	public static String getLoginUserid(HttpServletRequest request){
		return (String) request.getAttribute(Constant.SESSION_UID);
	}
	
	public static void main(String[] args) {
		for (int i = 0; i < 100; i++) {
			System.out.println(getUiid());
		}
	}
}
