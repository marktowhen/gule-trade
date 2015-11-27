package com.jingyunbank.etrade.message.controller;

import java.util.Optional;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
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
import com.jingyunbank.etrade.user.bean.UserVO;

@RestController
@RequestMapping("/api/sms")
public class SMSController {

	@Autowired
	private IUserService userService;
	
	
	/**
	 * 用户注册信息及其发送手机验证码
	 * @param userVO
	 * @param valid
	 * @param request
	 * @param session
	 * @return
	 * @throws Exception
	 */
		@RequestMapping(value="register/sendcode",method=RequestMethod.PUT)
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
				return sendCodeToMobile(userVO.getMobile(), EtradeUtil.getRandomCode(), request);
				
			}
			return Result.fail("发送验证码失败");
		}
	/**
	 * 当前手机号发送验证
	 * @param request
	 * @param session
	 * @param userVO
	 * @return
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/sendcode/message",method=RequestMethod.GET)
	public Result currentPhone(HttpServletRequest request, HttpSession session) throws Exception{
		String id = ServletBox.getLoginUID(request);
		
		Users users=userService.getByUid(id).get();
		if(users.getMobile()!=null){
			return sendCodeToMobile(users.getMobile(), EtradeUtil.getRandomCode(), request);
		}
		
		return Result.fail("重试");
	}
	//修改手机号的操作
	/**
	 * 1更换手机发送验证码的过程操作
	 * @param userVO
	 * @param valid
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/phone",method=RequestMethod.GET)
	public Result sendUpdatePhone(@RequestParam("mobile") String mobile,HttpSession session,HttpServletRequest request) throws Exception{
		//验证手机号输入的准确性
		if(mobile!=null){
				Pattern p = Pattern.compile(Patterns.INTERNAL_MOBILE_PATTERN);
				if(!p.matcher(mobile).matches()){
					return Result.fail("手机格式不正确");
				}
				//检验手机号是否已经存在
				if(userService.phoneExists(mobile)){
					return Result.fail("该手机号已存在。");
				}
			}

			 return sendCodeToMobile(mobile, EtradeUtil.getRandomCode(), request);
		
		
		
	}
	//忘记密码
		/**
		 * 1手机号发送验证码
		 * @param request
		 * @param session
		 * @param loginfo
		 * @return
		 */
	@RequestMapping(value="/forgetpwd/code",method=RequestMethod.GET)
	public Result forgetpwdSend(HttpServletRequest request, HttpSession session,String loginfo) throws Exception{
		if(StringUtils.isEmpty(loginfo)){
			return Result.fail("手机/邮箱");
		}
		Optional<Users> usersOptional = userService.getByKey(loginfo);
		Users users=usersOptional.get();
		
		if(users.getMobile()!=null){
			return sendCodeToMobile(users.getMobile(), EtradeUtil.getRandomCode(), request);
		}
		return Result.fail("发送验证码失败");
	}
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
