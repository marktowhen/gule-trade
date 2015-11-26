package com.jingyunbank.etrade.user.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.Result;
import com.jingyunbank.core.lang.Patterns;
import com.jingyunbank.core.web.ServletBox;
import com.jingyunbank.etrade.api.message.bo.Message;
import com.jingyunbank.etrade.api.user.bo.UserInfo;
import com.jingyunbank.etrade.api.user.bo.Users;
import com.jingyunbank.etrade.api.user.service.IUserService;
import com.jingyunbank.etrade.user.bean.UserVO;

@RestController
public class RegisterController {
 	@Autowired
	private IUserService userService;
 	
	public static final String EMAIL_MESSAGE = "EMAIL_MESSAGE";
	
	
/**
 * 用户注册信息及其发送手机或邮箱验证码
 * @param userVO
 * @param valid
 * @param request
 * @param session
 * @return
 * @throws Exception
 */
	@RequestMapping(value="/api/user/register/send",method=RequestMethod.PUT)
	public Result register(@RequestBody UserVO userVO,HttpServletRequest request,HttpSession session) throws Exception{
		//验证邮箱是否存在
		if(!StringUtils.isEmpty(userVO.getMobile())){
			Pattern p = Pattern.compile(Patterns.INTERNAL_MOBILE_PATTERN);
			if(!p.matcher(userVO.getMobile()).matches()){
				return Result.fail("手机格式不正确");
			}
			if(userService.phoneExists(userVO.getMobile())){
				return Result.fail("该手机号已存在。");
			}
			return sendCodeToMobile(userVO.getMobile(), getCheckCode(), request);
			
		}
		//验证邮箱是否存在
		if(!StringUtils.isEmpty(userVO.getEmail())){
			Pattern p1 = Pattern.compile(Patterns.INTERNAL_EMAIL_PATTERN);
			if(!p1.matcher(userVO.getEmail()).matches()){
				return Result.fail("手机格式不正确");
			}
			if(userService.emailExists(userVO.getEmail())){
				return Result.fail("该邮箱已存在");
			}
			return sendCodeToEmail(userVO.getEmail(), "验证码", getCheckCode(), request);
			
		}
		return Result.fail("发送验证码失败");
	}
	/**
	 * 判断验证码是否输入正确
	 * @param userVO
	 * @param request
	 * @param session
	 * @param mobile
	 * @param code
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/api/user/register/checkcode",method=RequestMethod.POST)
	public Result registerCheckCode(@Valid @RequestBody UserVO userVO,BindingResult valid,HttpServletRequest request, HttpSession session) throws Exception{
		if(valid.hasErrors()){
			List<ObjectError> errors = valid.getAllErrors();
			return Result.fail(errors.stream()
						.map(oe -> Arrays.asList(oe.getDefaultMessage()).toString())
						.collect(Collectors.joining(" ; ")));
		}
		//验证用户名是否已存在
		if(userService.unameExists(userVO.getUsername())){
			return Result.fail("该用户名已存在。");
		}
		Result checkResult = null;
		if(StringUtils.isEmpty(userVO.getMobile())&&StringUtils.isEmpty(userVO.getEmail())){
			return Result.fail("邮箱和手机号至少有一个不为空");
		}
		//验证手机号是否存在
		if(!StringUtils.isEmpty(userVO.getMobile())){
			if(userService.phoneExists(userVO.getMobile())){
				return Result.fail("该手机号已存在。");
			}
			checkResult = checkCode(userVO.getCode(), request, ServletBox.SMS_MESSAGE);
		}
		//验证邮箱是否存在
		if(!StringUtils.isEmpty(userVO.getEmail())){
			if(userService.emailExists(userVO.getEmail())){
			return Result.fail("该邮箱已存在");
			}
			checkResult = checkCode(userVO.getCode(), request, EMAIL_MESSAGE);
		}
		Users user=new Users();
		BeanUtils.copyProperties(userVO, user);
		UserInfo userInfo=new UserInfo();
		userInfo.setRegip(request.getRemoteAddr());
		//保存用户信息和个人资料信息
		if(checkResult.isOk()){
			if(userService.save(user, userInfo)){
			return	Result.ok("注册信息成功");
			}
		}
		return Result.fail("验证失败或是保存失败");
	}
	/**
	 * 获取4位随机数
	 * @return
	 * 2015年11月7日 qxs
	 */
	private String getCheckCode(){
		return String.valueOf(Math.abs(new Random().nextInt()%10000));
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
