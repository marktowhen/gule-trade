package com.jingyunbank.etrade.infrastructure;

import java.util.Optional;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.jingyunbank.core.Result;
import com.jingyunbank.core.msg.MessagerManager;
import com.jingyunbank.core.msg.sms.SmsSender;
import com.jingyunbank.etrade.api.user.IUserService;
import com.jingyunbank.etrade.api.user.bo.Users;


@Component("smsMessager")
@Scope("singleton")
public class SmsMessager implements IMessageNotify {

	private Logger logger = Logger.getLogger(EmailMessager.class);
	private SmsSender sender;
	@Autowired
	private IUserService userService;
	
	public SmsMessager() {
		try {
			String providername = SystemConfigProperties.getString(SystemConfigProperties.MOBILE_PROVIDER);
			Class.forName(providername);
		} catch (ClassNotFoundException e) {
			logger.error(e);
		}
		sender = MessagerManager.getSmsSender(SystemConfigProperties.toMap());
	}
	
	public void sendMessage(String uid, String subject, String body) throws Exception{
		Optional<Users> usersOptional = userService.getByUid(uid);
		if(usersOptional.isPresent()){
			Result send = sender.send(usersOptional.get().getMobile(), new StringBuilder(subject).append("\n").append(body).toString());
			System.err.println("sendMessage Result:"+send);
		}else{
			throw new Exception("用户不存在");
		}
	}

	@Override
	public void sendMngrMessage(String subject, String body)  throws Exception{
		String[] phones = SystemConfigProperties.getStrings(SystemConfigProperties.ADMIN_MOBILE);
		sender.send(phones, new StringBuilder(subject).append("\n").append(body).toString());
	}
	/**
	 * 向手机号发送短信
	 * @param mobile
	 * @param subject
	 * @param body
	 * @throws Exception
	 * 2015年11月7日 qxs
	 */
	public void sendMessageToMobile(String mobile, String subject, String body) throws Exception{
		sender.send(mobile, new StringBuilder(subject).append("\n").append(body).toString());
	}

}
