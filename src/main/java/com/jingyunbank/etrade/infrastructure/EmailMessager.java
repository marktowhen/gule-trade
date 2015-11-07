package com.jingyunbank.etrade.infrastructure;

import java.util.Optional;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.jingyunbank.core.msg.MessagerManager;
import com.jingyunbank.core.msg.email.EmailSender;
import com.jingyunbank.etrade.api.user.IUserService;
import com.jingyunbank.etrade.api.user.bo.Users;

@Component("emailMessager")
@Scope("singleton")
public class EmailMessager implements IMessageNotify{

	private Logger logger = Logger.getLogger(EmailMessager.class);
	private EmailSender sender;
	@Resource private IUserService userService;
	
	
	public EmailMessager() {
		try {
			String providername = SystemConfigProperties.getString(SystemConfigProperties.EMAIL_PROVIDER);
			Class.forName(providername);
		} catch (ClassNotFoundException e) {
			logger.error(e);
		}
		sender = MessagerManager.getEmailSender(SystemConfigProperties.toMap());
	}
	
	@Override
	public void sendMessage(String uid, String subject, String body)  throws Exception{
		
		Optional<Users> usersOptional = userService.getByUid(uid);
		if(usersOptional.isPresent()){
			Users user = usersOptional.get();
			if(StringUtils.hasText(user.getEmail())){
				sender.send(user.getEmail(), subject, body);
			}else{
				throw new Exception("邮箱不存在");
			}
		}else{
			throw new Exception("用户不存在");
		}
	}

	@Override
	public void sendMngrMessage(String subject, String body) throws Exception{
		String[] emails = SystemConfigProperties.getStrings(SystemConfigProperties.ADMIN_EMAIL);
		sender.send(emails, subject, body);
	}

}
