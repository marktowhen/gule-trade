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
import com.jingyunbank.etrade.api.user.bo.Manager;
import com.jingyunbank.etrade.api.user.bo.Seller;
import com.jingyunbank.etrade.api.user.bo.ManagerRole;
import com.jingyunbank.etrade.api.user.bo.Users;
import com.jingyunbank.etrade.api.user.service.IManagerService;
import com.jingyunbank.etrade.api.user.service.ISellerService;
import com.jingyunbank.etrade.api.user.service.IManagerRoleService;
import com.jingyunbank.etrade.api.user.service.IUserService;
import com.jingyunbank.etrade.user.bean.LoginUserVO;
import com.jingyunbank.etrade.user.bean.ManagerVO;
import com.jingyunbank.etrade.user.bean.SellerVO;
import com.jingyunbank.etrade.user.bean.UserVO;

@RestController
@RequestMapping("/api")
public class LoginController {

	@Autowired
	private IUserService userService;
	@Autowired
	private ICartService cartService;
	@Autowired
	private IManagerRoleService userRoleServce;
	
	@Autowired
	private IManagerService managerService;
	@Autowired
	private ISellerService sellerService;
	
	
	public static final String LOGIN_MERCHANT_ID = "LOGIN_MERCHANT_ID";
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
		
		//2、根据用户名/手机号/邮箱查询用户信息
		Optional<Users> usersOptional =  userService.singleByKey(user.getKey());
		//是否存在该用户
		if(usersOptional.isPresent()){
			Users users = usersOptional.get();
			//密码是否正确
			if(!users.getPassword().equals(user.getPassword())){
				//记录错误次数
				return Result.fail("用户名或者密码错误！");
			}
			//用户被锁
			if(users.isLocked()){
				//暂时先不管
				//return Result.fail("用户被锁");
			}
		}else{
			return Result.fail("用户名或者密码错误！");
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
	@RequestMapping(value="/login/back",method=RequestMethod.POST,
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
				return Result.fail("用户名或者密码错误！");
			}
			//用户被锁
			if(users.isLocked()){
				//暂时先不管
				//return Result.fail("用户被锁");
			}
			
		}else{
			return Result.fail("用户名或者密码错误！");
		}
		//3、成功之后
		//用户信息放入session
		Users users = usersOptional.get();
		
		Login.UID(session, users.getID());
		Login.Uname(session, users.getUsername());
		Security.authenticate(session);
		
		//查询用户拥有的权限
		List<ManagerRole> list = userRoleServce.list(users.getID());
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
	@RequestMapping(value="/login/seller",method=RequestMethod.POST,
				consumes="application/json;charset=UTF-8")
	public Result<SellerVO> sellerLogin(@Valid @RequestBody LoginUserVO loginUser, 
						BindingResult valid, HttpSession session,
						HttpServletResponse response) throws Exception{
		if(valid.hasErrors()){
			return Result.fail("用户名或者密码错误！");
		}
		
		//2、根据用户名/手机号/邮箱查询用户信息
		Optional<Seller> sellerOptional =  sellerService.singleByMname(loginUser.getKey());
		//是否存在该用户
		if(sellerOptional.isPresent()){
			Seller seller = sellerOptional.get();
			//密码是否正确
			if(!seller.getPassword().equals(loginUser.getPassword())){
				return Result.fail("用户名或者密码错误！");
			}
		}else{
			return Result.fail("用户名或者密码错误！");
		}
		//3、成功之后
		//用户信息放入session
		Seller seller = sellerOptional.get();
		Login.UID(session, seller.getID());
		Login.Uname(session, seller.getSname());
		//商铺id放入session
		session.setAttribute(LOGIN_MERCHANT_ID, seller.getMid());
		Security.authenticate(session);
		
		//将uid写入cookie
		Cookie cookie = new Cookie(Login.LOGIN_ID, seller.getID());
		cookie.setPath("/");
		cookie.setMaxAge(session.getMaxInactiveInterval());
		response.addCookie(cookie);
		
		SellerVO vo = new SellerVO();
		BeanUtils.copyProperties(seller, vo);
		return Result.ok(vo);
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
	@RequestMapping(value="/login/manager",method=RequestMethod.POST,
				consumes="application/json;charset=UTF-8")
	public Result<ManagerVO> managerLogin(@Valid @RequestBody LoginUserVO loginUser, 
						BindingResult valid, HttpSession session,
						HttpServletResponse response) throws Exception{
		if(valid.hasErrors()){
			return Result.fail("用户名或者密码错误！");
		}
		
		//2、根据用户名/手机号/邮箱查询用户信息
		Optional<Manager> managerOptional =  managerService.singleByMname(loginUser.getKey());
		//是否存在该用户
		if(managerOptional.isPresent()){
			Manager manager = managerOptional.get();
			//密码是否正确
			if(!manager.getPassword().equals(loginUser.getPassword())){
				return Result.fail("用户名或者密码错误！");
			}
		}else{
			return Result.fail("用户名或者密码错误！");
		}
		//3、成功之后
		Manager manager = managerOptional.get();
		//查询用户拥有的权限
		List<ManagerRole> list = userRoleServce.list(manager.getID());
		if(list.isEmpty()){
			return Result.fail("您没有权限");
		}
		String [] roles = new String [list.size()];
		for (int i = 0; i < list.size(); i++) {
			roles[i] = list.get(i).getRole().getCode();
		}
		Security.authorize(session, roles);
		//用户信息放入session
		Login.UID(session, manager.getID());
		Login.Uname(session, manager.getMname());
		Security.authenticate(session);
		
		
		//将uid写入cookie
		Cookie cookie = new Cookie(Login.LOGIN_ID, manager.getID());
		cookie.setPath("/");
		cookie.setMaxAge(session.getMaxInactiveInterval());
		response.addCookie(cookie);
		
		ManagerVO vo = new ManagerVO();
		BeanUtils.copyProperties(manager, vo);
		return Result.ok(vo);
	}
	
}
