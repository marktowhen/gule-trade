package com.jingyunbank.etrade.message.service;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.jingyunbank.core.msg.MessagerManager;
import com.jingyunbank.core.msg.sms.SmsSender;
import com.jingyunbank.etrade.api.message.bo.Message;
import com.jingyunbank.etrade.api.message.service.context.ISyncNotifyService;
import com.jingyunbank.etrade.base.util.SystemConfigProperties;

/**
 * 手机短信服务
 */
@Service("smsService")
public class SmsService implements ISyncNotifyService {

	private SmsSender sender;
	
	public SmsService() {
		try {
			String providername = SystemConfigProperties.getString(SystemConfigProperties.MOBILE_PROVIDER);
			Class.forName(providername);
		} catch (ClassNotFoundException e) {
//			logger.error(e);
		}
		sender = MessagerManager.getSmsSender(SystemConfigProperties.toMap());
	}
	
	@Override
	public void inform(Message msg) throws Exception {
		
		if(msg.getReceiveUser()!=null && !StringUtils.isEmpty(msg.getReceiveUser().getMobile()))
			sender.send(msg.getReceiveUser().getMobile(), 
					new StringBuilder(StringUtils.isEmpty(msg.getTitle())?"":msg.getTitle())
						.append("\n").append(msg.getContent()).toString());
	}

}
