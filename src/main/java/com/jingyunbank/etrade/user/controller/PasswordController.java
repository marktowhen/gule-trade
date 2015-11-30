package com.jingyunbank.etrade.user.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.Result;
import com.jingyunbank.core.web.AuthBeforeOperation;
import com.jingyunbank.core.web.ServletBox;
import com.jingyunbank.etrade.api.user.bo.Users;
import com.jingyunbank.etrade.api.user.service.IUserService;
import com.jingyunbank.etrade.base.util.SystemConfigProperties;
import com.jingyunbank.etrade.message.controller.SMSController;
import com.jingyunbank.etrade.user.bean.UserVO;

@RestController
@RequestMapping("/api/pwd")
public class PasswordController {
	@Autowired
	private IUserService userService;

	public static final String EMAIL_MESSAGE = "EMAIL_MESSAGE";
	
	
	/**
	 * 2修改登录密码
	 * @param userVO
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/password",method=RequestMethod.PUT)
	public Result updatePassword(@RequestBody UserVO userVO,HttpSession session,HttpServletRequest request) throws Exception{
		if(effectiveTime(session)){
		//验证登录密码有效性
			if(userVO.getPassword()!=null){
				if(userVO.getPassword().length()<7||userVO.getPassword().length()>20){
					return Result.fail("密码必须是8-20位");
				}
			}
			String uid = ServletBox.getLoginUID(request);
			userVO.setID(uid);
			Users users=new Users();
			BeanUtils.copyProperties(userVO, users);
			if(userService.refresh(users)){
				return Result.ok(userVO);
			}
			
		}
		return Result.fail("修改登录密码失败");
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
		@RequestMapping(value="/forgetpwd/checkcode",method=RequestMethod.POST)
		public Result forgetpwdCheck(UserVO userVO,HttpServletRequest request, HttpSession session,String loginfo,String code) throws Exception{
			if(userVO.getPassword().length()<7||userVO.getPassword().length()>20){
				return Result.fail("登录密码必须是8-20位");
			}
			Result result=null;
			Optional<Users> usersOptionals = userService.getByKey(loginfo);
			Users users=usersOptionals.get();
		/*	if(users.getMobile()!=null){
				result=checkCode(code, request, ServletBox.SMS_MESSAGE);
			}
			if(users.getEmail()!=null){
				result=checkCode(code, request, EMAIL_MESSAGE);
			}*/
			userVO.setID(users.getID());
			BeanUtils.copyProperties(userVO, users);
			if(userService.refresh(users)/*&&result.isOk()*/){
				return Result.ok(userVO);
			}
			
			return Result.fail("验证码出现错误或是修改未成功");
			
		}
		private boolean effectiveTime(HttpSession session){
			Calendar now=Calendar.getInstance();
			now.setTime(new Date());
			Object sessionDate=session.getAttribute(SMSController.MOBILE_CODE_CHECK_DATE);
			if(sessionDate!=null && sessionDate instanceof Date ){
				Calendar checkDate  = Calendar.getInstance();
				checkDate.setTime((Date)sessionDate);
				//+2
				checkDate.add(Calendar.MINUTE, SystemConfigProperties.getInt("effective.time") );
				checkDate.getTime();
				now.getTime();
				if(checkDate.after(now)){
					return true;
				}
			}
			return false;
			
		}
	
}