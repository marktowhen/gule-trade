package com.jingyunbank.etrade.user.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.Result;
import com.jingyunbank.core.web.Login;
import com.jingyunbank.core.web.Security;
import com.jingyunbank.etrade.api.cart.bo.Cart;
import com.jingyunbank.etrade.api.cart.service.ICartService;
import com.jingyunbank.etrade.api.user.bo.UserRole;
import com.jingyunbank.etrade.api.user.bo.Users;
import com.jingyunbank.etrade.api.user.service.IUserRoleService;
import com.jingyunbank.etrade.api.user.service.IUserService;
import com.jingyunbank.etrade.user.bean.LoginUserVO;
import com.jingyunbank.etrade.user.bean.UserVO;

@RestController
@RequestMapping("/api")
public class LoginController {

	@Autowired
	private IUserService userService;
	@Autowired
	private ICartService cartService;
	@Autowired
	private IUserRoleService userRoleServce;
	
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
	@RequestMapping(value="/login",method=RequestMethod.POST,
				consumes="application/json;charset=UTF-8")
	public Result<UserVO> login(@Valid @RequestBody LoginUserVO user, 
						BindingResult valid, HttpSession session,
						HttpServletResponse response) throws Exception{
		if(valid.hasErrors()){
			return Result.fail("用户名或者密码错误！");
		}
		
		//密码不正确3次后需要验证码
		int loginWrongTimes = 0;
		//session中存放的错误次数
//		Object objLoginTimes = session.getAttribute("loginWrongTimes");
//		if(objLoginTimes != null && objLoginTimes instanceof Integer){
//			loginWrongTimes = (int)session.getAttribute("loginWrongTimes");
//			if(loginWrongTimes>=3){
//				if(!checkCaptcha(session, user.getCaptcha())){
//					return Result.fail("验证码错误");
//				}
//			}
//		}
		//2、根据用户名/手机号/邮箱查询用户信息
		Optional<Users> usersOptional =  userService.singleByKey(user.getKey());
		//是否存在该用户
		if(usersOptional.isPresent()){
			Users users = usersOptional.get();
			//密码是否正确
			if(!users.getPassword().equals(user.getPassword())){
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
			return Result.fail("未找到该用户,请确认输入是否正确");
		}
		//3、成功之后
		//用户信息放入session
		Users users = usersOptional.get();
		Optional<Cart> candidatecart = cartService.singleCart(users.getID());
		candidatecart.ifPresent(cart->{
			Login.CartID(session, cart.getID());
		});
		
		Login.UID(session, users.getID());
		Login.Uname(session, users.getUsername());
		Security.authenticate(session);
		//清空错误次数
		session.setAttribute("loginWrongTimes", 0);
		//记录登录历史 未完待续
		
		//将uid写入cookie
		Cookie cookie = new Cookie(Login.LOGIN_ID, users.getID());
		cookie.setPath("/");
		cookie.setMaxAge(session.getMaxInactiveInterval());
		response.addCookie(cookie);
		
		
		UserVO vo = new UserVO();
		BeanUtils.copyProperties(users, vo);
		return Result.ok(vo);
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
	 * users登录   暂时不用
	 * @param request
	 * @param session
	 * @param loginfo 用户名/手机/邮箱
	 * @param password 密码
	 * @param checkCode 验证码
	 * 
	 * @return
	 */
	@RequestMapping(value="/back/login",method=RequestMethod.POST,
				consumes="application/json;charset=UTF-8")
	public Result<UserVO> loginBack(@Valid @RequestBody LoginUserVO user, 
						BindingResult valid, HttpSession session,
						HttpServletResponse response) throws Exception{
		if(valid.hasErrors()){
			return Result.fail("用户名或者密码错误！");
		}
		
		//密码不正确3次后需要验证码
		int loginWrongTimes = 0;
		//2、根据用户名/手机号/邮箱查询用户信息
		Optional<Users> usersOptional =  userService.singleByKey(user.getKey());
		//是否存在该用户
		if(usersOptional.isPresent()){
			Users users = usersOptional.get();
			//密码是否正确
			if(!users.getPassword().equals(user.getPassword())){
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
			return Result.fail("未找到该用户,请确认输入是否正确");
		}
		//3、成功之后
		//用户信息放入session
		Users users = usersOptional.get();
		
		Login.UID(session, users.getID());
		Login.Uname(session, users.getUsername());
		Security.authenticate(session);
		
		//查询用户拥有的权限
		List<UserRole> list = userRoleServce.list(users.getID());
		if(list.isEmpty()){
			//暂时屏蔽
			//return Result.fail("您没有权限");
		}
		String [] roles = new String [list.size()];
		for (int i = 0; i < list.size(); i++) {
			roles[i] = list.get(i).getRole().getCode();
		}
		Security.authorize(session, roles);
		//清空错误次数
		session.setAttribute("loginWrongTimes", 0);
		//记录登录历史 未完待续
		
		//将uid写入cookie
		Cookie cookie = new Cookie(Login.LOGIN_ID, users.getID());
		cookie.setPath("/");
		cookie.setMaxAge(session.getMaxInactiveInterval());
		response.addCookie(cookie);
		
		
		UserVO vo = new UserVO();
		BeanUtils.copyProperties(users, vo);
		return Result.ok(vo);
	}
	
	/**
	 * 卖家登录   
	 * @param request
	 * @param session
	 * @param loginfo 用户名/手机/邮箱
	 * @param password 密码
	 * @param checkCode 验证码
	 * 
	 * @return
	 */
	@RequestMapping(value="/seller/login",method=RequestMethod.POST,
				consumes="application/json;charset=UTF-8")
	public Result<UserVO> sellerLogin(@Valid @RequestBody LoginUserVO user, 
						BindingResult valid, HttpSession session,
						HttpServletResponse response) throws Exception{
		
		
		return Result.ok();
	}
	
	/**
	 * 管理员登录   
	 * @param request
	 * @param session
	 * @param loginfo 用户名/手机/邮箱
	 * @param password 密码
	 * @param checkCode 验证码
	 * 
	 * @return
	 */
	@RequestMapping(value="/manager/login",method=RequestMethod.POST,
				consumes="application/json;charset=UTF-8")
	public Result<UserVO> managerLogin(@Valid @RequestBody LoginUserVO user, 
						BindingResult valid, HttpSession session,
						HttpServletResponse response) throws Exception{
		return Result.ok();
	}
	
}
