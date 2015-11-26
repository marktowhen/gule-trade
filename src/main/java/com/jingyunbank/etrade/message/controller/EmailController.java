package com.jingyunbank.etrade.message.controller;

import java.util.Optional;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.Result;
import com.jingyunbank.core.lang.Patterns;
import com.jingyunbank.core.util.MD5;
import com.jingyunbank.core.web.AuthBeforeOperation;
import com.jingyunbank.core.web.ServletBox;
import com.jingyunbank.etrade.api.message.bo.Message;
import com.jingyunbank.etrade.api.message.service.context.ISyncNotifyService;
import com.jingyunbank.etrade.api.user.bo.Users;
import com.jingyunbank.etrade.api.user.service.IUserService;
import com.jingyunbank.etrade.base.util.EtradeUtil;
import com.jingyunbank.etrade.base.util.SystemConfigProperties;


@RestController
@RequestMapping("/api/email")
public class EmailController {
	
	@Autowired
	private IUserService userService;
	@Resource
	private ISyncNotifyService emailService;
	/**
	 * 邮箱验证码在session中的key
	 */
	public static final String EMAIL_MESSAGE = "EMAIL_MESSAGE";
	
	/**
	 * 发送验证码到注册邮箱
	 * @param request
	 * @param resp
	 * @param email
	 * @return
	 * 2015年11月10日 qxs
	 * @throws Exception 
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/user-email",method=RequestMethod.GET)
	public Result sendCodeToEmail(HttpServletRequest request) throws Exception {
		 Optional<Users> userOption = userService.getByUid(ServletBox.getLoginUID(request));
		return  sendCodeToEmail(userOption.get().getEmail(), "验证码", EtradeUtil.getRandomCode(), request);
	}
	
	/**
	 * 校验图形验证码，校验邮箱格式,通过后发送验证链接到用户输入的邮箱
	 * @param request
	 * @param code
	 * @param email
	 * @return
	 * 2015年11月11日 qxs
	 * @throws Exception 
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/email-link/{code}",method=RequestMethod.PUT)
	public Result checkCodeAndSendEamil(HttpServletRequest request,@PathVariable String code,@RequestBody String email) throws Exception{
		if(!checkCaptcha(request.getSession(), code)){
			return Result.fail("验证码错误");
		}
		if(StringUtils.isEmpty(email)){
			return Result.fail("邮箱地址不能为空");
		}
		Pattern p = Pattern.compile(Patterns.INTERNAL_EMAIL_PATTERN);
		if(!p.matcher(email).matches()){
			return Result.fail("邮箱格式错误");
		}
		Optional<Users> userOption = userService.getByUid(ServletBox.getLoginUID(request));
		if(userService.getByEmail(email).isPresent()){
			return Result.fail("该邮箱已被使用");
		}
		return sendLinkToEmail(request,userOption.get(), email);
	}
	/**
	 * 发送绑定邮箱的链接 点击链接验证后绑定
	 * 链接地址 http://ip(:port)?d=1&u=2&m=3
	 * d: uid
	 * u: email+"~"+邮件发送时间 base64编码后字符
	 * m: uid+"_"+username MD5加密后字符串
	 * @param request
	 * @param email
	 * @return
	 * @throws Exception 
	 */
	private  Result sendLinkToEmail(HttpServletRequest request,Users user, String email) throws Exception{
		
		String basePath = EtradeUtil.getBasePath(request);
		String msg1 = user.getID() + "_"
				+ user.getUsername();
		// MD5加密（用户id和用户名）
		String msg1Md5 = MD5.digest(msg1);
		// 密钥
		String message = email + "~"
				+ System.currentTimeMillis();
		// 邮箱和当前时间戳进行base64编码
		String verifyCode = new Base64().encodeAsString(message.getBytes());
		//验证链接的地址
		String url = basePath
				+ "api/user/ckemail-link.htm?d="+ user.getID()
				+ "&u=" + verifyCode + "&m=" + msg1Md5;
		//编辑邮箱内容
		StringBuffer content = new StringBuffer();
		content.append("验证链接:"+url+"\r\n");
		content.append("有效期:"+(SystemConfigProperties.getLong(SystemConfigProperties.EMAIL_VERIFY_VALID_TIME)/1000/60/60)+"小时");
		
		Message emailMessage = new Message();
		emailMessage.setTitle("用户验证");
		emailMessage.setContent(content.toString());
		emailMessage.getReceiveUser().setEmail(email);
		emailService.inform(emailMessage);
		return Result.ok();
	}
	
	/**
	 * 发送邮箱验证码,将code放入session  EMAIL_MESSAGE
	 * @param email
	 * @param subTitle
	 * @param code
	 * @param request
	 * @return
	 * 2015年11月10日 qxs
	 * @throws Exception 
	 */
	private Result sendCodeToEmail(String email, String subTitle, String code, HttpServletRequest request) throws Exception{
		request.getSession().setAttribute(EMAIL_MESSAGE, code);
		Message message = new Message();
		message.setTitle(subTitle);
		message.setContent("您的验证码是:"+code);
		message.getReceiveUser().setEmail(email);
		System.out.println("-----------------"+"您的验证码是:"+code);
		//emailService.inform(message);
		return Result.ok();
	}
	
	/**
	 * 校验图形验证码
	 * @param session
	 * @param captcha
	 * @return
	 */
	private boolean checkCaptcha(HttpSession session, String captcha) {
		String sessionCaptcha = session.getAttribute(ServletBox.SIMPLE_CAPTCHA_CODE)==null ? "" : (String)session.getAttribute(ServletBox.SIMPLE_CAPTCHA_CODE);
		if(!StringUtils.isEmpty(captcha) && captcha.equalsIgnoreCase(sessionCaptcha )){
			return true;
		}
		return false;
	}
	
	
}
