package com.jingyunbank.etrade.user.controller;
import java.util.Optional;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.Result;
import com.jingyunbank.core.web.AuthBeforeOperation;
import com.jingyunbank.core.web.Login;
import com.jingyunbank.core.web.ServletBox;
import com.jingyunbank.etrade.api.message.service.context.ISyncNotifyService;
import com.jingyunbank.etrade.api.user.bo.Users;
import com.jingyunbank.etrade.api.user.service.IUserService;
import com.jingyunbank.etrade.api.vip.coupon.service.IUserCashCouponService;
import com.jingyunbank.etrade.api.vip.coupon.service.IUserDiscountCouponService;
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
	@Autowired
	private IUserCashCouponService userCashCouponService;
	@Autowired
	private IUserDiscountCouponService userDiscountCouponService;
	
	/**
	 * 获得已登录的user
	 * @param userVO
	 * @param request
	 * @return// get /api/user/current
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/current",method=RequestMethod.GET)
	public Result<UserVO> getCurrentUser(UserVO userVO,HttpServletRequest request){
		String id = Login.UID(request);
		Users users=userService.single(id).get();
		BeanUtils.copyProperties(users, userVO);
		return Result.ok(userVO);
	}
	/**
	 * 查询 手机是/邮箱/用户名否存在！
	 * @param phone
	 * @return
	 */
	@RequestMapping(value="/key/exists",method=RequestMethod.GET)
	public Result<Integer> exists(@RequestParam String key){
		int count;
		if(!userService.exists(key)){
			//该手机号不存在
			 count=0;
		}else{
			//该手机号已经存在
			 count=1;
		}
		return Result.ok(count);
		
		
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
	public Result<String> refreshPhone(@RequestParam("mobile") String mobile, @RequestParam("code") String code,HttpServletRequest request) throws Exception{
		//身份验证
		if(ServletBox.checkIfEmailMobileOK(request.getSession())){
			Users users=new Users();
			users.setMobile(mobile);
			users.setID(Login.UID(request));
			//手机验证码
			Result<String> checkResult = checkCode(code, request, ServletBox.SMS_CODE_KEY_IN_SESSION);
			
			
			if(checkResult.isOk()){
				if(userService.singleByKey(mobile).isPresent()){
					return Result.fail("该手机号已被使用");
				}
				userService.refresh(users);
				return Result.ok();
			}
			return Result.fail("验证码错误");
		}
		return Result.fail("验证超时,请重新进行身份验证");
	}
	
	/**
	 * 验证通过后修改邮箱
	 * @param userVO
	 * @param valid
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/email",method=RequestMethod.PUT)
	public Result<String> refreshEmail(@RequestParam("email") String email, @RequestParam("code") String code,HttpServletRequest request) throws Exception{
		//身份验证
		if(ServletBox.checkIfEmailMobileOK(request.getSession())){
			Users users=new Users();
			users.setEmail(email);
			users.setID(Login.UID(request));
			//邮箱验证码
			Result<String> checkResult = checkCode(code, request, ServletBox.EMAIL_CODE_KEY_IN_SESSION);
			
			if(checkResult.isOk()){
				if(userService.singleByKey(email).isPresent()){
					return Result.fail("该邮箱已被使用");
				}
				userService.refresh(users); 
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
	public Result<UserVO> getUserByKey(HttpServletRequest request, HttpSession session,String key) throws Exception{
		if(StringUtils.isEmpty(key)){
			return Result.fail("请输入用户名/手机/邮箱");
		}
		//根据用户名/手机号/邮箱查询用户信息
		Optional<Users> usersOptional =  userService.singleByKey(key);
		if(usersOptional.isPresent()){
			Users users = usersOptional.get();
			return Result.ok(getUserVoFromBo(users));
		}else{
			return Result.fail("未找到该用户");
		}
	}
	
	/**
	 * 安全等级
	 * @param uid
	 * @return
	 * @throws Exception
	 * 2015年11月27日 qxs
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/safety/level/{uid}",method=RequestMethod.GET)
	public Result<Integer> getSafetyLevel(@PathVariable String uid) throws Exception {
		int level = 0;
		Optional<Users> userOption = userService.single(uid);
		if(userOption.isPresent()){
			Users users = userOption.get();
			//已验证邮箱
			if(!StringUtils.isEmpty(users.getEmail())){
				level += 33;
			}
			//已验证手机
			if(!StringUtils.isEmpty(users.getMobile())){
				level += 33;
			}
			//支付密码与登录密码不同
			if(!StringUtils.isEmpty(users.getPassword()) && !users.getPassword().equals(users.getTradepwd())){
				level += 33;
			}
		}
		return Result.ok(level);
	}
	
	/**
	 * 用户未使用的券总数
	 * @param uid
	 * @return
	 * @throws Exception
	 * 2015年11月27日 qxs
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/coupon/amount/{uid}",method=RequestMethod.GET)
	public Result<Integer> getUnusedCouponAmount(@PathVariable String uid) throws Exception {
		return Result.ok(userCashCouponService.countUnusedCoupon(uid)+userDiscountCouponService.countUnusedCoupon(uid));
	}
	
	/**
	 * 修改用户名
	 * @param uid
	 * @return
	 * @throws Exception
	 * 2015年11月27日 qxs
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/username/{username}",method=RequestMethod.PUT)
	public Result<String> refreshUsername(@PathVariable String username,HttpServletRequest request) throws Exception {
		String loginUname = Login.uname(request);
		String uid = Login.UID(request);
		//用户名规则校验 
		Pattern p = Pattern.compile("^([a-zA-Z]+[a-zA-Z0-9]{3,19})$");
		if(StringUtils.isEmpty(username) || !p.matcher(username).matches()){
			return Result.fail("用户名必须以字母开头，并且只能是字母或数字");
		}
		Users oldUser = userService.single(uid).get();
		if(!username.equals(loginUname)
				&& oldUser.getUsername().equals(IUserService.SMS_LOGIN_USERNAME_PREFIX+oldUser.getMobile())){
			if(userService.exists(username)){
				return Result.fail("用户名已存在");
			}
			Users user = new Users();
			user.setID(uid);
			user.setUsername(username);
			userService.refresh(user);
		}
		return Result.ok();
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
