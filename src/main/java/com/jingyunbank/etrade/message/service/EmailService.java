package com.jingyunbank.etrade.message.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.jingyunbank.core.msg.MessagerManager;
import com.jingyunbank.core.msg.email.EmailSender;
import com.jingyunbank.etrade.api.message.bo.Message;
import com.jingyunbank.etrade.api.message.service.context.ISyncNotifyService;
import com.jingyunbank.etrade.api.user.service.IUserService;
import com.jingyunbank.etrade.base.util.SystemConfigProperties;

@Service("emailService")
public class EmailService implements ISyncNotifyService {
	
	private EmailSender sender;
	@Autowired private IUserService userService;

	public EmailService() {
		try {
			String providername = SystemConfigProperties.getString(SystemConfigProperties.EMAIL_PROVIDER);
			Class.forName(providername);
		} catch (ClassNotFoundException e) {
//			logger.error(e);
		}
		sender = MessagerManager.getEmailSender(SystemConfigProperties.toMap());
	}
	
	@Override
	public void inform(Message msg) throws Exception {
		if(msg.getReceiveUser()!=null && !StringUtils.isEmpty(msg.getReceiveUser().getEmail())){
			sender.send(msg.getReceiveUser().getEmail(), msg.getTitle(), msg.getContent());
		}
	}
}
