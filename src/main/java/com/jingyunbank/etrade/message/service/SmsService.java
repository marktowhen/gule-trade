package com.jingyunbank.etrade.message.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.jingyunbank.core.msg.MessagerManager;
import com.jingyunbank.core.msg.sms.SmsSender;
import com.jingyunbank.etrade.api.message.bo.Message;
import com.jingyunbank.etrade.api.message.service.context.ISyncNotifyService;
import com.jingyunbank.etrade.api.user.bo.Users;
import com.jingyunbank.etrade.api.user.service.IUserService;
import com.jingyunbank.etrade.base.util.SystemConfigProperties;

/**
 * 手机短信服务
 */
//@Service("smsService")
public class SmsService implements ISyncNotifyService {

	private SmsSender sender;
	@Autowired private IUserService userService;
	
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
		Optional<Users> user = userService.getByUid(msg.getReceiveUID());
		if(user.isPresent())
			sender.send(user.get().getMobile(), 
					new StringBuilder(msg.getTitle()).append("\n").append(msg.getContent()).toString());
	}

}
