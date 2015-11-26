package com.jingyunbank.etrade.user.controller;

import java.util.Optional;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.Result;
import com.jingyunbank.core.web.ServletBox;
import com.jingyunbank.etrade.api.message.bo.Message;
import com.jingyunbank.etrade.api.user.bo.Users;
import com.jingyunbank.etrade.api.user.service.IUserService;
import com.jingyunbank.etrade.base.util.EtradeUtil;
import com.jingyunbank.etrade.user.bean.UserVO;

@RestController
@RequestMapping("/api/forget")
public class ForgetPassword {
	@Autowired
	private IUserService userService;

	public static final String EMAIL_MESSAGE = "EMAIL_MESSAGE";
	
	
	
	//忘记密码
		/**
		 * 1为输入的邮箱或手机号发送验证码
		 * @param request
		 * @param session
		 * @param loginfo
		 * @return
		 */
		@RequestMapping(value="/forgetpwd/send",method=RequestMethod.GET)
		public Result forgetpwdSend(HttpServletRequest request, HttpSession session,String loginfo) throws Exception{
			if(StringUtils.isEmpty(loginfo)){
				return Result.fail("手机/邮箱");
			}
			Optional<Users> usersOptional = userService.getByKey(loginfo);
			Users users=usersOptional.get();
			if(users.getEmail()!=null){
				return sendCodeToEmail(loginfo, "验证码", EtradeUtil.getRandomCode(), request);
			}
			if(users.getMobile()!=null){
				return sendCodeToMobile(users.getMobile(), EtradeUtil.getRandomCode(), request);
			}
			return Result.fail("发送验证码失败");
		}
		//忘记密码
		/**
		 * 2.验证输入的验证码是否正确并且保存修改后的密码进行保存
		 * @param userVO
		 * @param request
		 * @param session
		 * @param loginfo
		 * @param code
		 * @return
		 * @throws Exception
		 */
		@RequestMapping(value="/forgetpwd/check",method=RequestMethod.POST)
		public Result forgetpwdCheck(UserVO userVO,HttpServletRequest request, HttpSession session,String loginfo,String code) throws Exception{
			if(userVO.getPassword().length()<7||userVO.getPassword().length()>20){
				return Result.fail("登录密码必须是8-20位");
			}
			Result result=null;
			Optional<Users> usersOptionals = userService.getByKey(loginfo);
			Users users=usersOptionals.get();
			if(users.getMobile()!=null){
				result=checkCode(code, request, ServletBox.SMS_MESSAGE);
			}
			if(users.getEmail()!=null){
				result=checkCode(code, request, EMAIL_MESSAGE);
			}
			userVO.setID(users.getID());
			BeanUtils.copyProperties(userVO, users);
			if(userService.refresh(users)&&result.isOk()){
				return Result.ok(userVO);
			}
			
			return Result.fail("验证码出现错误或是修改未成功");
			
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
