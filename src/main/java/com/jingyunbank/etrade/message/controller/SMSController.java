package com.jingyunbank.etrade.message.controller;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.Result;
import com.jingyunbank.core.web.AuthBeforeOperation;
import com.jingyunbank.core.web.ServletBox;
import com.jingyunbank.etrade.api.message.bo.Message;
import com.jingyunbank.etrade.api.user.bo.Users;
import com.jingyunbank.etrade.api.user.service.IUserService;
import com.jingyunbank.etrade.base.util.EtradeUtil;

@RestController
@RequestMapping("/api/sms")
public class SMSController {

	@Autowired
	private IUserService userService;
	
	/**
	 * 发送验证码到注册手机 
	 * @param request
	 * @return
	 * @throws Exception
	 * 2015年11月11日 qxs
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/user-mobile",method=RequestMethod.GET)
	public Result sendCodeToRegistMobile(HttpServletRequest request) throws Exception{
		 Optional<Users> userOption = userService.getByUid(ServletBox.getLoginUID(request));
		 return sendCodeToMobile(userOption.get().getMobile(), EtradeUtil.getRandomCode(), request);
	}
	
	/**
	 * 发送手机验证码 
	 * @param request
	 * @param session
	 * @param mobile 
	 * @return
	 * 2015年11月6日 qxs
	 * @throws Exception 
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/code",method=RequestMethod.GET)
	public Result getMessage(HttpServletRequest request, HttpSession session,String mobile) throws Exception{
		if(StringUtils.isEmpty(mobile)){
			return Result.fail("请输入手机号");
		}
		String id = ServletBox.getLoginUID(request);
		if(!StringUtils.isEmpty(userService.getByUid(id).get().getMobile())){
			return Result.fail("您已经绑定过手机了");
		}
		if(userService.getByPhone(mobile).isPresent()){
			return Result.fail("该手机号已被使用");
		}
		//如何设置验证码的有效期限--待解决
		Result result = sendCodeToMobile(mobile, EtradeUtil.getRandomCode(), request);
		if(result.isBad()){
			return result;
		}
		session.setAttribute("UNCHECK_MOBILE", mobile);
		return Result.ok("成功");
		
	}
	
	
	/**
	 * 发送手机验证码 将code放入session  SMS_MESSAGE
	 * @param mobile
	 * @param code
	 * @param request
	 * @return
	 * @throws Exception
	 * 2015年11月10日 qxs
	 */
	private Result sendCodeToMobile(String mobile, String code, HttpServletRequest request) throws Exception{
		request.getSession().setAttribute(ServletBox.SMS_MESSAGE, code);
		Message message = new Message();
		message.setContent("您的验证码是:"+code);
		message.getReceiveUser().setMobile(mobile);
		message.setTitle("");
		System.out.println("-----------------"+"您的验证码是:"+code);
		//smsService.inform(message);
		return Result.ok();
	}
	
	
	
	
}
