package com.jingyunbank.etrade.message.service;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.jingyunbank.core.msg.MessagerManager;
import com.jingyunbank.core.msg.email.EmailSender;
import com.jingyunbank.etrade.api.message.bo.Message;
import com.jingyunbank.etrade.api.message.service.context.ISyncNotifyService;
import com.jingyunbank.etrade.config.PropsConfig;

@Service("emailService")
public class EmailService implements ISyncNotifyService {
	
	private EmailSender sender;
	

	public EmailService() {
		try {
			String providername = PropsConfig.getString(PropsConfig.EMAIL_PROVIDER);
			Class.forName(providername);
		} catch (ClassNotFoundException e) {
//			logger.error(e);
		}
		sender = MessagerManager.getEmailSender(PropsConfig.toMap());
	}
	
	@Override
	public void inform(Message msg) throws Exception {
		if(msg.getReceiveUser()!=null && !StringUtils.isEmpty(msg.getReceiveUser().getEmail())){
			sender.send(msg.getReceiveUser().getEmail(), msg.getTitle(), msg.getContent());
		}
	}
}
