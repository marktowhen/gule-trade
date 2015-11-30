package com.jingyunbank.etrade.message.controller;

import java.util.Date;
import java.util.Optional;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.Result;
import com.jingyunbank.core.lang.Patterns;
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
	
	
	public static final String MOBILE_CODE_CHECK_DATE="MOBILE_CODE_CHECK_DATE";
	
	/**
	 * 为传入的手机号发送验证码
	 * @param mobile
	 * @param request
	 * @return
	 * @throws Exception
	 * 2015年11月30日 qxs
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/code",method=RequestMethod.GET)
	public Result sendCode(@RequestParam("mobile") String mobile, HttpServletRequest request) throws Exception{
		//验证手机号输入的准确性
		if(!StringUtils.isEmpty(mobile)){
			Pattern p = Pattern.compile(Patterns.INTERNAL_MOBILE_PATTERN);
			if(!p.matcher(mobile).matches()){
				return Result.fail("手机格式不正确");
			}
			return sendCodeToMobile(mobile, EtradeUtil.getRandomCode(), request);
		}

	 	return Result.fail("手机号为空");
		
	}

	/**
	 * 通过key查询出来的手机发送验证码
	 * @param request
	 * @param key 手机/邮箱/用户名
	 * @return
	 * @throws Exception
	 * 2015年11月30日 qxs
	 */
	@RequestMapping(value="/code/bykey",method=RequestMethod.GET)
	public Result sendCodeByKey(HttpServletRequest request, String key) throws Exception{
		if(StringUtils.isEmpty(key)){
			return Result.fail("参数缺失:手机/邮箱/用户名");
		}
		Optional<Users> usersOptional = userService.getByKey(key);
		if(usersOptional.isPresent()){
			Users users=usersOptional.get();
			if(!StringUtils.isEmpty(users.getMobile())){
				return sendCodeToMobile(users.getMobile(), EtradeUtil.getRandomCode(), request);
			}else{
				return Result.fail("用户未绑定手机");
			}
		}else{
			return Result.fail("未找到用户");
		}
	}
	/**
	 * 登录用户的手机号发送验证码
	 * @param request
	 * @return
	 * @throws Exception
	 * 2015年11月11日 qxs
	 */
	//api/sms/code/user
	@AuthBeforeOperation
	@RequestMapping(value="/code/user",method=RequestMethod.GET)
	public Result sendCodeToUserMobile(HttpServletRequest request) throws Exception{
		 Optional<Users> userOption = userService.getByUID(ServletBox.getLoginUID(request));
		 return sendCodeToMobile(userOption.get().getMobile(), EtradeUtil.getRandomCode(), request);
	}
	
	
	/**
	 * 验证
	 * @param mobile
	 * @param code
	 * @param request
	 * @param session
	 * @return
	 */
	//get api/sms/code/check
	@AuthBeforeOperation
	@RequestMapping(value="/code/check",method=RequestMethod.GET)
	public Result chenckPhoneCode(@RequestParam("code") String code,HttpServletRequest request, HttpSession session) throws Exception{
		
		Result	checkResult = checkCode(code, request, ServletBox.SMS_MESSAGE);
		if(checkResult.isOk()){
			session.setAttribute(MOBILE_CODE_CHECK_DATE, new Date());
			return Result.ok("手机验证成功");
		}
		return Result.fail("手机或验证码不一致");
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
	
	
	/**
	 * 验证验证码,成功后清除session
	 * @param code
	 * @param request
	 * @param sessionKey 验证码在session中的name
	 * @return
	 * 2015年11月10日 qxs
	 */
	private Result checkCode(String code, HttpServletRequest request, String sessionName){
		if(StringUtils.isEmpty(code)){
			return Result.fail("验证码不能为空");
		}
		String sessionCode = (String)request.getSession().getAttribute(sessionName);
		if(StringUtils.isEmpty(sessionCode)){
			return Result.fail("验证码未发送或已失效");
		}
		if(code.equals(sessionCode)){
			request.getSession().setAttribute(sessionName, null);
			return Result.ok();
		}
		return Result.fail("验证码错误");
	}
	
}
