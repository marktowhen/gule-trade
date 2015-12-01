package com.jingyunbank.etrade.user.controller;
import java.util.Optional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.Result;
import com.jingyunbank.core.web.AuthBeforeOperation;
import com.jingyunbank.core.web.ServletBox;
import com.jingyunbank.etrade.api.message.service.context.ISyncNotifyService;
import com.jingyunbank.etrade.api.user.bo.Users;
import com.jingyunbank.etrade.api.user.service.IUserService;
import com.jingyunbank.etrade.base.util.EtradeUtil;
import com.jingyunbank.etrade.user.bean.UserVO;
@RestController
@RequestMapping("/api/user")
public class UserController {
  	@Autowired
	private IUserService userService;
	@Resource
	private ISyncNotifyService emailService;
	@Resource
	private ISyncNotifyService smsService;
	/**
	 * 邮箱/短信 成功验证身份的时间
	 */
	public static final String CHECK_CODE_PASS_DATE="CHECK_CODE_PASS_DATE";
	/**
	 * 邮箱验证码在session中的key
	 */
	public static final String EMAIL_MESSAGE = "EMAIL_MESSAGE";
	
	/**
	 * 通过id查出对应的对象
	 * @param userVO
	 * @param request
	 * @return// get /api/user/current
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/current",method=RequestMethod.GET)
	public Result selectPhone(UserVO userVO,HttpServletRequest request){
		String id = ServletBox.getLoginUID(request);
		Users users=userService.getByUID(id).get();
		BeanUtils.copyProperties(users, userVO);
		return Result.ok(userVO);
	}
	/**
	 * 验证通过后修改手机号
	 * @param userVO
	 * @param valid
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/phone",method=RequestMethod.PUT)
	public Result refreshPhone(@RequestParam("mobile") String mobile, @RequestParam("code") String code,HttpServletRequest request) throws Exception{
		//身份验证
		if(EtradeUtil.effectiveTime(request.getSession())){
			Users users=new Users();
			users.setMobile(mobile);
			users.setID(ServletBox.getLoginUID(request));
			//手机验证码
			Result checkResult = checkCode(code, request, ServletBox.SMS_MESSAGE);
			if(checkResult.isOk() && userService.refresh(users) ){
				return Result.ok();
			}
			return Result.fail("验证码错误");
		}
		return Result.fail("验证超时,请重新进行身份验证");
	}
	
	/**
	 * 验证通过后修改手机号
	 * @param userVO
	 * @param valid
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/email",method=RequestMethod.PUT)
	public Result refreshEmail(@RequestParam("email") String email, @RequestParam("code") String code,HttpServletRequest request) throws Exception{
		//身份验证
		if(EtradeUtil.effectiveTime(request.getSession())){
			Users users=new Users();
			users.setEmail(email);
			users.setID(ServletBox.getLoginUID(request));
			//邮箱验证码
			Result checkResult = checkCode(code, request, UserController.EMAIL_MESSAGE);
			if(checkResult.isOk() && userService.refresh(users) ){
				return Result.ok();
			}
			return Result.fail("验证码错误");
		}
		return Result.fail("验证超时,请重新进行身份验证");
	}
	/**
	 * 根据用户名/手机/邮箱查询用户信息
	 * @param request
	 * @param session
	 * @param key 用户名/手机/邮箱
	 * 
	 * @return
	 * 
	 */
	///get /api/user?key=email/phone/username
	@RequestMapping(value="/",method=RequestMethod.GET)
	public Result getUserByKey(HttpServletRequest request, HttpSession session,String key) throws Exception{
		if(StringUtils.isEmpty(key)){
			return Result.fail("请输入用户名/手机/邮箱");
		}
		//根据用户名/手机号/邮箱查询用户信息
		Optional<Users> usersOptional =  userService.getByKey(key);
		if(usersOptional.isPresent()){
			Users users = usersOptional.get();
			return Result.ok(getUserVoFromBo(users));
		}else{
			return Result.fail("未找到该用户");
		}
	}
	
	/**
	 * user bo转vo
	 * @param users
	 * @return
	 * 2015年11月5日 qxs
	 */
	private UserVO getUserVoFromBo(Users users){
		UserVO vo = null;
		
		if(users!=null){
			vo = new UserVO();
			BeanUtils.copyProperties(users, vo);
		}
		return vo;
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
