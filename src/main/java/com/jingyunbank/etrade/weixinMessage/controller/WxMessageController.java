package com.jingyunbank.etrade.weixinMessage.controller;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.jingyunbank.etrade.api.weixinMessage.bo.Account;
import com.jingyunbank.etrade.api.weixinMessage.bo.MpAccount;
import com.jingyunbank.etrade.api.weixinMessage.bo.TemplateMessage;
import com.jingyunbank.etrade.weixinMessage.dao.AccountDao;
import com.jingyunbank.etrade.weixinMessage.process.WxApiClient;
import com.jingyunbank.etrade.weixinMessage.process.WxMemoryCacheClient;
import com.jingyunbank.etrade.weixinMessage.service.MyServiceImpl;
import com.jingyunbank.etrade.weixinMessage.util.DateUtil;
import com.jingyunbank.etrade.weixinMessage.util.SpringFreemarkerContextPathUtil;
import com.jingyunbank.etrade.weixinMessage.util.wx.SignUtil;

import net.sf.json.JSONObject;


/**
 * 微信与开发者服务器交互接口
 */
@RestController
@RequestMapping("/wxMessageController")
public class WxMessageController {
	
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
			String token = mpAccount.getToken();//获取token，进行验证；
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
	public ModelAndView getUrl(HttpServletRequest request){
		String path = SpringFreemarkerContextPathUtil.getBasePath(request);
		String url = request.getScheme() + "://" + request.getServerName() + path + "/wxMessageController/wxa31686a39324990e/message.html";
//		String url = request.getScheme() + "://" + request.getServerName() + path + "/wxapi/" + account.getAccount()+"/message.html";
		Account account=new Account();
		if(account.getId() == null){//新增
			account.setUrl(url);
			//account.setToken(UUID.randomUUID().toString().replace("-", ""));
			account.setToken("72597b9628704ab09e8b9e8cbe9b540a");
			account.setCreatetime(new Date());
			account.setAccount("111111111111111111");
			account.setAppid("wxa31686a39324990e");
			account.setAppsecret("9b8bfcbe4a31fe0a78a817c464e930ea");
			accountDao.add(account);
		}else{//更新
			Account tmpAccount = accountDao.getById(account.getId().toString());
			tmpAccount.setUrl(url);
			tmpAccount.setAccount(account.getAccount());
			//tmpAccount.setAppid(account.getAppid());
			tmpAccount.setAppid("wxa31686a39324990e");
			tmpAccount.setAppsecret("9b8bfcbe4a31fe0a78a817c464e930ea");
			tmpAccount.setMsgcount(account.getMsgcount());
			accountDao.update(tmpAccount);
		}
		WxMemoryCacheClient.addMpAccount(account);
		return new ModelAndView("redirect:/wxcms/urltoken.html?save=true");
	}
	
	
	/**
	 * 发送模板消息
	 * @param openId
	 * @param content
	 * @return
	 */
	@RequestMapping(value = "/sendTemplateMessage", method = RequestMethod.GET)
	public void sendTemplateMessage(HttpServletRequest request,HttpServletResponse response){
		MpAccount mpAccount = WxMemoryCacheClient.getSingleMpAccount();//获取缓存中的唯一账号
		TemplateMessage tplMsg = new TemplateMessage();
		String openid="or_Qas3DC2DkZjwKtJymJItYKZzM";
		tplMsg.setOpenid(openid);
		//微信公众号号的template id，开发者自行处理参数
		tplMsg.setTemplateId("7C2VqtmDTLTI-c7kvQsGakktKEkgesRrlv24YSvp9dk"); 
		
		tplMsg.setUrl("http://mp.weixin.qq.com/");
		Map<String, String> dataMap = new HashMap<String,String>();
		dataMap.put("first", "MR 程序员 ");
		dataMap.put("keyword1", "时间：" + DateUtil.COMMON.getDateText(new Date()));
		dataMap.put("keyword2", "100");
		dataMap.put("remark", "感谢您的使用，祝您生活愉快！");
/*		tplMsg.setTemplateId("Wyme6_kKUqv4iq7P4d2NVldw3YxZIql4sL2q8CUES_Y"); 
		
		tplMsg.setUrl("http://www.weixinpy.com");
		Map<String, String> dataMap = new HashMap<String,String>();
		dataMap.put("first", "微信派官方微信模板消息测试");
		dataMap.put("keyword1", "时间：" + DateUtil.COMMON.getDateText(new Date()));
		dataMap.put("keyword2", "关键字二：你好");
		dataMap.put("remark", "备注：感谢您的来访");
*/		tplMsg.setDataMap(dataMap);
		
		JSONObject result = WxApiClient.sendTemplateMessage(tplMsg, mpAccount);
		try {
			if(result.getInt("errcode") != 0){
				response.getWriter().write("send failure");
			}else{
				response.getWriter().write("send success");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}




