package com.jingyunbank.etrade.weixinMessage.controller;

import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.jingyunbank.etrade.api.weixinMessage.bo.Account;
import com.jingyunbank.etrade.api.weixinMessage.bo.MpAccount;
import com.jingyunbank.etrade.weixinMessage.dao.AccountDao;
import com.jingyunbank.etrade.weixinMessage.process.WxMemoryCacheClient;
import com.jingyunbank.etrade.weixinMessage.service.MyServiceImpl;
import com.jingyunbank.etrade.weixinMessage.util.SpringFreemarkerContextPathUtil;
import com.jingyunbank.etrade.weixinMessage.util.wx.SignUtil;


/**
 * 微信与开发者服务器交互接口
 */
@RestController
@RequestMapping("/wxapi")
public class WxApController {
	
	@Autowired
	private MyServiceImpl myService;
	@Autowired
	AccountDao accountDao;
	/**
	 * GET请求：进行URL、Tocken 认证；
	 * 1. 将token、timestamp、nonce三个参数进行字典序排序
	 * 2. 将三个参数字符串拼接成一个字符串进行sha1加密
	 * 3. 开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
	 */
	@RequestMapping(value = "/{account}/message",  method = RequestMethod.GET)
	public @ResponseBody String doGet(HttpServletRequest request,@PathVariable String account) {
		//如果是多账号，根据url中的account参数获取对应的MpAccount处理即可
		
		MpAccount mpAccount = WxMemoryCacheClient.getSingleMpAccount();//获取缓存中的唯一账号
		if(mpAccount != null){
			//String token = mpAccount.getToken();//获取token，进行验证；
			String token="72597b9628704ab09e8b9e8cbe9b540a";
			String signature = request.getParameter("signature");// 微信加密签名
			String timestamp = request.getParameter("timestamp");// 时间戳
			String nonce = request.getParameter("nonce");// 随机数
			String echostr = request.getParameter("echostr");// 随机字符串
			// 校验成功返回  echostr，成功成为开发者；否则返回error，接入失败
			if (SignUtil.validSign(signature, token, timestamp, nonce)) {
				return echostr;
			}
		}
		return "error";
	}
	
	@RequestMapping(value = "/getUrl")
	public ModelAndView getUrl(HttpServletRequest request ,@ModelAttribute Account account){
		String path = SpringFreemarkerContextPathUtil.getBasePath(request);
		String url = request.getScheme() + "://" + request.getServerName() + path + "/wxapi/" + account.getAccount()+"/message.html";
		
		if(account.getId() == null){//新增
			account.setUrl(url);
			account.setToken(UUID.randomUUID().toString().replace("-", ""));
			account.setCreatetime(new Date());
			accountDao.add(account);
		}else{//更新
			Account tmpAccount = accountDao.getById(account.getId().toString());
			tmpAccount.setUrl(url);
			tmpAccount.setAccount(account.getAccount());
			tmpAccount.setAppid(account.getAppid());
			tmpAccount.setAppsecret(account.getAppsecret());
			tmpAccount.setMsgcount(account.getMsgcount());
			accountDao.update(tmpAccount);
		}
		WxMemoryCacheClient.addMpAccount(account);
		return new ModelAndView("redirect:/wxcms/urltoken.html?save=true");
	}
	
}




