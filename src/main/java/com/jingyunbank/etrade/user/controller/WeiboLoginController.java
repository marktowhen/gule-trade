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

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.KeyGen;
import com.jingyunbank.core.Result;
import com.jingyunbank.core.web.Login;
import com.jingyunbank.core.web.Security;
import com.jingyunbank.core.web.ServletBox;
import com.jingyunbank.etrade.api.cart.bo.Cart;
import com.jingyunbank.etrade.api.cart.service.ICartService;
import com.jingyunbank.etrade.api.user.bo.UserInfo;
import com.jingyunbank.etrade.api.user.bo.Users;
import com.jingyunbank.etrade.api.user.bo.WeiboLogin;
import com.jingyunbank.etrade.api.user.service.IUserService;
import com.jingyunbank.etrade.api.user.service.IWeiboLoginService;
import com.jingyunbank.etrade.user.bean.ThirdLoginBindVO;
import com.jingyunbank.etrade.user.bean.ThirdLoginRegistVO;
import com.jingyunbank.etrade.user.bean.UserVO;

@RestController
@RequestMapping("/api")
public class WeiboLoginController {

	@Autowired
	private IWeiboLoginService weiboLoginService;
	@Autowired
	private IUserService userService;
	@Autowired
	private ICartService cartService;
	/**
	 * 登录   
	 * @param request
	 * @param session
	 * @param loginfo 用户名/手机/邮箱
	 * @param password 密码
	 * @param checkCode 验证码
	 * 
	 * @return
	 */
	@RequestMapping(value="/login/weibo/{weiboUID}",method=RequestMethod.GET)
	public Result<Object> weiboLogin(@PathVariable String weiboUID, HttpSession session,
						HttpServletResponse response) throws Exception{
		
		//2、根据用户名/手机号/邮箱查询用户信息
		WeiboLogin weiboLogin = weiboLoginService.single(weiboUID);
		if(weiboLogin==null){
			return Result.ok().setCode("400");
		}
		
		Optional<Users> usersOptional = userService.single(weiboLogin.getUID());
		//是否存在该用户
		if(!usersOptional.isPresent()){
			return Result.fail("未找到该用户");
		}
		//3、成功之后
		Security.authenticate(session);
		//用户信息放入session 写入cookie
		Users users = usersOptional.get();
		//购物车
		loginSuccessCar(users.getID(), session, response);
		//用户信息
		loginSuccess(users.getID(), users.getUsername(), session, response);
		
		
		UserVO vo = new UserVO();
		BeanUtils.copyProperties(users, vo);
		return Result.ok(vo);
	}
	
	/**
	 * 微博绑定已有账号
	 * @param user
	 * @param valid
	 * @param session
	 * @param response
	 * @return
	 * @throws Exception
	 * 2016年1月27日 qxs
	 */
	@RequestMapping(value="/login/bind/weibo",method=RequestMethod.POST,
			consumes="application/json;charset=UTF-8")
	public Result<UserVO> weiboBind(@Valid @RequestBody ThirdLoginBindVO user, 
					BindingResult valid, HttpSession session,
					HttpServletResponse response) throws Exception{
		if(valid.hasErrors()){
			return Result.fail("用户名或者密码错误！");
		}
		
		//2、根据用户名/手机号/邮箱查询用户信息
		Optional<Users> usersOptional =  userService.singleByKey(user.getLoginKey());
		//是否存在该用户
		if(usersOptional.isPresent()){
			Users users = usersOptional.get();
			//密码是否正确
			if(!users.getPassword().equals(user.getLoginPassword())){
				return Result.fail("用户名或者密码错误！");
			}
		}else{
			return Result.fail("用户名或者密码错误！");
		}
		//3、成功之后
		//进行绑定
		WeiboLogin weiboLogin = weiboLoginService.single(user.getThirdLoginKey());
		if(weiboLogin!=null){
			//刷新绑定信息
			weiboLogin.setAccessToken(user.getAccessToken());
			weiboLogin.setUID(usersOptional.get().getID());
			weiboLoginService.refreshByID(weiboLogin.getID(),user.getAccessToken(),usersOptional.get().getID());
		}else{
			weiboLogin = new WeiboLogin();
			weiboLogin.setAccessToken(user.getAccessToken());
			weiboLogin.setID(KeyGen.uuid());
			weiboLogin.setWeiboUID(user.getThirdLoginKey());
			weiboLogin.setUID(usersOptional.get().getID());
			weiboLoginService.save(weiboLogin);
		}
		Security.authenticate(session);
		//用户信息放入session 写入cookie
		Users users = usersOptional.get();
		//购物车
		loginSuccessCar(users.getID(), session, response);
		//用户信息
		loginSuccess(users.getID(), users.getUsername(), session, response);
		
		
		UserVO vo = new UserVO();
		BeanUtils.copyProperties(users, vo);
		return Result.ok(vo);
	}
	/**
	 * 微博注册
	 * @param userVO
	 * @param valid
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * 2016年1月21日 qxs
	 */
	@RequestMapping(value="/register/weibo",method=RequestMethod.POST)
	public Result<String> weiboRegist(@Valid @RequestBody ThirdLoginRegistVO userVO,BindingResult valid,HttpServletRequest request,HttpServletResponse response) throws Exception{
		if(valid.hasErrors()){
			List<ObjectError> errors = valid.getAllErrors();
			return Result.fail(errors.stream()
						.map(oe -> Arrays.asList(oe.getDefaultMessage()).toString())
						.collect(Collectors.joining(" ; ")));
		}
		//验证用户名是否已存在
		if(userService.exists(userVO.getUsername())){
			return Result.fail("该用户名已存在。");
		}
		if(userService.exists(userVO.getMobile())){
			return Result.fail("该手机号已存在。");
		}
		Result<String> checkResult = checkCode(userVO.getCode(), request, ServletBox.SMS_CODE_KEY_IN_SESSION);
		//保存用户信息和个人资料信息
		if(checkResult.isOk()){
			Users user=new Users();
			BeanUtils.copyProperties(userVO, user);
			user.setID(KeyGen.uuid());//generate uid here to make view visible
			
			UserInfo userInfo=new UserInfo();
			userInfo.setRegip(ServletBox.ip(request));
			WeiboLogin weibo = new WeiboLogin();
			BeanUtils.copyProperties(userVO, weibo);
			weibo.setID(KeyGen.uuid());
			weibo.setUID(user.getID());
			weibo.setWeiboUID(userVO.getThirdLoginKey());
			weiboLoginService.save(weibo, user, userInfo);
			registSuccess(user, request.getSession(), response);
			return	Result.ok("注册信息成功");
		}
		return checkResult;
	}
	
	
	
	public void loginSuccess(String uid, String username, HttpSession session,
			HttpServletResponse response){
		Login.UID(session, uid);
		Login.uname(session, username);
		
		//将uid写入cookie
		Cookie cookie = new Cookie(Login.LOGIN_USER_ID, uid);
		cookie.setPath("/");
		cookie.setMaxAge(session.getMaxInactiveInterval());
		response.addCookie(cookie);
	}
	
	public void loginSuccessCar(String uid ,HttpSession session, HttpServletResponse response){
		Optional<Cart> candidatecart = cartService.singleCart(uid);
		candidatecart.ifPresent(cart->{
			Login.cartID(session, cart.getID());
		});
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
	
	/**
	 * 注册成功后的操作
	 * @param user
	 * @param session
	 * @param response
	 * 2016年1月21日 qxs
	 */
	private void registSuccess(Users user, HttpSession session,HttpServletResponse response){
		Optional<Cart> candidatecart = cartService.singleCart(user.getID());
		candidatecart.ifPresent(cart->{
			Login.cartID(session, cart.getID());
		});
		
		Login.UID(session, user.getID());
		Login.uname(session, user.getUsername());
		Security.authenticate(session);
		
		//将uid写入cookie
		Cookie cookie = new Cookie(Login.LOGIN_USER_ID, user.getID());
		cookie.setPath("/");
		response.addCookie(cookie);
	}
	
	
}
