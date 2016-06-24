package com.jingyunbank.etrade.weixinMessage.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.user.bo.Users;
import com.jingyunbank.etrade.api.user.service.IUserService;
import com.jingyunbank.etrade.api.weixinMessage.bo.MpAccount;
import com.jingyunbank.etrade.api.weixinMessage.bo.TemplateMessage;
import com.jingyunbank.etrade.api.weixinMessage.service.IWxMessageService;
import com.jingyunbank.etrade.weixinMessage.process.WxApiClient;
import com.jingyunbank.etrade.weixinMessage.process.WxMemoryCacheClient;

import net.sf.json.JSONObject;
/**
 * 业务消息处理
 * 开发者根据自己的业务自行处理消息的接收与回复；
 */

@Service("wxMessageService")
public class WxMessageServiceImpl implements IWxMessageService{
	
	@Autowired
	private IUserService userService;
   
	@Override
	public boolean sendMessageToUser(String templateId,String userId, Map<String, String> dataMap) {
		//根据userId查询用户openid
		boolean flag=false;
		String openid=null;
		MpAccount mpAccount = WxMemoryCacheClient.getSingleMpAccount();//获取缓存中的唯一账号
		TemplateMessage tplMsg = new TemplateMessage();
		Users user=userService.selUserByUserId(userId);
		if(user!=null){
			openid=user.getOpenId();
			if(openid!=null&&openid!=""){
				tplMsg.setOpenid(openid);
				//微信公众号号的template id，开发者自行处理参数
				tplMsg.setTemplateId(templateId); 
				tplMsg.setUrl("https://api.weixin.qq.com/");
				tplMsg.setDataMap(dataMap);
				JSONObject result = WxApiClient.sendTemplateMessage(tplMsg, mpAccount);
				try {
					if(result.getInt("errcode") != 0){
						System.out.println("send fail");
					}else{
						flag=true;
						System.out.println("send success");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		}
		return flag;
	}


	


	
}


