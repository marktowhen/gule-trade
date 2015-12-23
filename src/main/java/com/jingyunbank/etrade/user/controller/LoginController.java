package com.jingyunbank.etrade.user.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.KeyGen;
import com.jingyunbank.core.Result;
import com.jingyunbank.core.util.MD5;
import com.jingyunbank.core.web.ServletBox;
import com.jingyunbank.etrade.api.cart.bo.Cart;
import com.jingyunbank.etrade.api.cart.service.ICartService;
import com.jingyunbank.etrade.api.user.bo.UserInfo;
import com.jingyunbank.etrade.api.user.bo.Users;
import com.jingyunbank.etrade.api.user.service.IUserService;
import com.jingyunbank.etrade.base.util.EtradeUtil;
import com.jingyunbank.etrade.user.bean.LoginUserPwdVO;
import com.jingyunbank.etrade.user.bean.LoginUserSmsVO;

@RestController
@RequestMapping("/api")
public class LoginController {

	@Autowired
	private IUserService userService;
	@Autowired
	private ICartService cartService;
	
	/**
	 * 登录
	 * @param user
	 * @param valid
	 * @param session
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/login",method=RequestMethod.POST,
				consumes="application/json;charset=UTF-8")
	public Result<String> login(@Valid @RequestBody LoginUserPwdVO user, 
						BindingResult valid, HttpSession session,
						HttpServletResponse response) throws Exception{
		if(valid.hasErrors()){
			return Result.fail("用户名或者密码错误！");
		}
		
		//密码不正确3次后需要验证码
		int loginWrongTimes = 0;
		//session中存放的错误次数
		Object objLoginTimes = session.getAttribute("loginWrongTimes");
		if(objLoginTimes != null && objLoginTimes instanceof Integer){
			loginWrongTimes = (int)session.getAttribute("loginWrongTimes");
			if(loginWrongTimes>=3){
				if(!checkCaptcha(session, user.getCaptcha())){
					return Result.fail("验证码错误");
				}
			}
		}
		//2、根据用户名/手机号/邮箱查询用户信息
		Optional<Users> usersOptional =  userService.getByKey(user.getKey());
		//是否存在该用户
		if(usersOptional.isPresent()){
			Users users = usersOptional.get();
			//密码是否正确
			if(!users.getPassword().equalsIgnoreCase(MD5.digest(user.getPassword()))){
				//记录错误次数
				session.setAttribute("loginWrongTimes", ++loginWrongTimes);
				return Result.fail("密码错误");
			}
			//用户被锁
			if(users.isLocked()){
				//暂时先不管
				//return Result.fail("用户被锁");
			}
		}else{
			return Result.fail("未找到该用户");
		}
		//3、成功之后
		doAfterCheck(usersOptional.get(), session, response);
		
		return Result.ok("成功");
	}
	/**
	 * 用户注销登录
	 * @param session
	 * @return
	 * 2015年11月11日 qxs
	 */
	@RequestMapping(value="/logout",method=RequestMethod.GET)
	public Result<String> logout(HttpSession session) throws Exception{
		session.invalidate();
		return Result.ok("成功");
	}
	
	/**
	 * 短信登录   
	 * @param request
	 * @param session
	 * @param loginfo 用户名/手机/邮箱
	 * @param password 密码
	 * @param checkCode 验证码
	 * 
	 * @return
	 */
	@RequestMapping(value="/login/sms",method=RequestMethod.POST,
				consumes="application/json;charset=UTF-8")
	public Result<String> loginBySms(@Valid @RequestBody LoginUserSmsVO user, 
						BindingResult valid, HttpSession session,
						HttpServletResponse response, HttpServletRequest request) throws Exception{
		if(valid.hasErrors()){
			List<ObjectError> errors = valid.getAllErrors();
			return Result.fail(errors.stream()
					.map(oe -> Arrays.asList(oe.getDefaultMessage()).toString())
					.collect(Collectors.joining(" ; ")));
		}
		//验证短信验证码是否正确
		String sessionCode = (String)session.getAttribute(ServletBox.SMS_MESSAGE);
		if(StringUtils.isEmpty(sessionCode)){
			return Result.fail("验证码未发送或已失效");
		}
		if(!sessionCode.equals(user.getCode())){
			return Result.fail("验证码错误");
		}
		session.setAttribute(ServletBox.SMS_MESSAGE, null);
		Optional<Users> userOption = userService.getByPhone(user.getMobile());
		//如果未注册自动注册
		if(!userOption.isPresent()){
			Users registerUser = new Users();
			registerUser.setMobile(user.getMobile());
			registerUser.setUsername(IUserService.SMS_LOGIN_USERNAME_PREFIX+user.getMobile());
			registerUser.setID(KeyGen.uuid());
			UserInfo userInfo=new UserInfo();
			userInfo.setRegip(EtradeUtil.getIpAddr(request));
			userService.save(registerUser, userInfo);
			userOption = Optional.of(registerUser);
		}
		//成功之后
		doAfterCheck(userOption.get(), session, response);
		
		return Result.ok();
	}
	
	/**
	 * 登录成功后的操作
	 * @param users
	 * @param session
	 * @param response
	 * 2015年12月21日 qxs
	 */
	private void doAfterCheck(Users users, HttpSession session, HttpServletResponse response){
		//用户信息放入session
		Optional<Cart> candidatecart = cartService.singleCart(users.getID());
		candidatecart.ifPresent(cart->{
			ServletBox.setLoginCartID(session, cart.getID());
		});
		
		ServletBox.setLoginUID(session, users.getID());
		ServletBox.setLoginUname(session, users.getUsername());
		//清空错误次数
		session.setAttribute("loginWrongTimes", 0);
		//记录登录历史 未完待续
		
		//将uid写入cookie
		Cookie cookie = new Cookie(ServletBox.LOGIN_ID, users.getID());
		cookie.setPath("/");
		response.addCookie(cookie);
	}
	
	
	
	/**
	 * 校验图形验证码
	 * @param session
	 * @param captcha
	 * @return
	 */
	private boolean checkCaptcha(HttpSession session, String captcha) {
		return true;
	}
	
}
