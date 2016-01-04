package com.jingyunbank.etrade.message.controller;

import java.util.Date;
import java.util.Optional;
import java.util.regex.Pattern;

import javax.annotation.Resource;
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
import com.jingyunbank.core.util.RndBuilder;
import com.jingyunbank.core.util.Times;
import com.jingyunbank.core.web.AuthBeforeOperation;
import com.jingyunbank.core.web.ServletBox;
import com.jingyunbank.etrade.api.message.bo.Message;
import com.jingyunbank.etrade.api.message.service.context.ISyncNotifyService;
import com.jingyunbank.etrade.api.user.bo.Users;
import com.jingyunbank.etrade.api.user.service.IUserService;

@RestController
@RequestMapping("/api/sms")
public class SMSController {

	@Autowired
	private IUserService userService;
	@Resource
	private ISyncNotifyService smsService;
	
	/**
	 * 为传入的手机号发送验证码
	 * @param mobile
	 * @param request
	 * @return
	 * @throws Exception
	 * 2015年11月30日 qxs
	 */
	@RequestMapping(value="/code",method=RequestMethod.GET)
	public Result<String> sendCode(@RequestParam("mobile") String mobile, HttpServletRequest request) throws Exception{
		//验证手机号输入的准确性
		if(!StringUtils.isEmpty(mobile)){
			Pattern p = Pattern.compile(Patterns.INTERNAL_MOBILE_PATTERN);
			if(!p.matcher(mobile).matches()){
				return Result.fail("手机格式不正确");
			}
			//验证码
			String code = new String(new RndBuilder().length(4).hasletter(false).next());
			return sendCodeToMobile(mobile, code, request);
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
	public Result<String> sendCodeByKey(HttpServletRequest request,@RequestParam("key") String key) throws Exception{
		if(StringUtils.isEmpty(key)){
			return Result.fail("参数缺失:手机/邮箱/用户名");
		}
		Optional<Users> usersOptional = userService.singleByKey(key);
		if(usersOptional.isPresent()){
			Users users=usersOptional.get();
			if(!StringUtils.isEmpty(users.getMobile())){
				//验证码
				String code = new String(new RndBuilder().length(4).hasletter(false).next());
				return sendCodeToMobile(users.getMobile(), code, request);
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
	public Result<String> sendCodeToUserMobile(HttpServletRequest request) throws Exception{
		 Optional<Users> userOption = userService.single(ServletBox.getLoginUID(request));
		 //验证码
		 String code = new String(new RndBuilder().length(4).hasletter(false).next());
		 return sendCodeToMobile(userOption.get().getMobile(), code, request);
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
	@RequestMapping(value="/code/check",method=RequestMethod.GET)
	public Result<String> chenckPhoneCode(@RequestParam("code") String code,HttpServletRequest request, HttpSession session) throws Exception{
		
		Result<String>	checkResult = checkCode(code, request, ServletBox.SMS_CODE_KEY_IN_SESSION);
		if(checkResult.isOk()){
			ServletBox.verifyMobile(request);
			return Result.ok("手机验证成功");
		}
		return checkResult;
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
	private Result<String> sendCodeToMobile(String mobile, String code, HttpServletRequest request) throws Exception{
		//距离上次发送时间是否超过1分钟
		if(Times.gone(60L, request.getSession().getAttribute(mobile))){
			request.getSession().setAttribute(ServletBox.SMS_CODE_KEY_IN_SESSION, code);
			Message message = new Message();
			message.setContent("您好，您的验证码是"+code);
			message.getReceiveUser().setMobile(mobile);
			message.setTitle("");
			smsService.inform(message);
			request.getSession().setAttribute(mobile, new Date());
			System.out.println("-----------------"+"您好，您的验证码是"+code);
			return Result.ok();
		}
		return Result.fail("发送过于频繁,请稍后再试");
	}
	
	/**
	 * 验证验证码,成功后清除session
	 * @param code
	 * @param request
	 * @param sessionKey 验证码在session中的name
	 * @return
	 * 2015年11月10日 qxs
	 */
	private Result<String> checkCode(String code, HttpServletRequest request, String sessionName){
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
