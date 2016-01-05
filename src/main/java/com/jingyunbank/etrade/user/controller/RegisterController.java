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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.KeyGen;
import com.jingyunbank.core.Result;
import com.jingyunbank.core.web.ServletBox;
import com.jingyunbank.etrade.api.cart.bo.Cart;
import com.jingyunbank.etrade.api.cart.service.ICartService;
import com.jingyunbank.etrade.api.user.bo.UserInfo;
import com.jingyunbank.etrade.api.user.bo.Users;
import com.jingyunbank.etrade.api.user.service.IUserService;
import com.jingyunbank.etrade.user.bean.UserVO;

@RestController
public class RegisterController {
 	@Autowired
	private IUserService userService;
 	@Autowired
	private ICartService cartService;
 	
	
	

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
	@RequestMapping(value="/api/register",method=RequestMethod.PUT)
	public Result<String> registerCheckCode(@Valid @RequestBody UserVO userVO,BindingResult valid,HttpServletRequest request, HttpSession session,HttpServletResponse response) throws Exception{
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
		Result<String> checkResult = null;
		if(StringUtils.isEmpty(userVO.getMobile())&&StringUtils.isEmpty(userVO.getEmail())){
			return Result.fail("邮箱和手机号至少有一个不为空");
		}
		//验证手机号是否存在
		if(!StringUtils.isEmpty(userVO.getMobile())){
			if(userService.exists(userVO.getMobile())){
				return Result.fail("该手机号已存在。");
			}
			checkResult = checkCode(userVO.getCode(), request, ServletBox.SMS_CODE_KEY_IN_SESSION);
		}
		//验证邮箱是否存在
		if(!StringUtils.isEmpty(userVO.getEmail())){
			if(userService.exists(userVO.getEmail())){
			return Result.fail("该邮箱已存在");
			}
			checkResult = checkCode(userVO.getCode(), request, ServletBox.EMAIL_CODE_KEY_IN_SESSION);
		}
		Users user=new Users();
		BeanUtils.copyProperties(userVO, user);
		user.setID(KeyGen.uuid());//generate uid here to make view visible
		
		UserInfo userInfo=new UserInfo();
		userInfo.setRegip(request.getRemoteAddr());
		//保存用户信息和个人资料信息
		if(checkResult.isOk()){
			userService.save(user, userInfo);
			
			
			Optional<Cart> candidatecart = cartService.singleCart(user.getID());
			candidatecart.ifPresent(cart->{
				ServletBox.setLoginCartID(session, cart.getID());
			});
			
			ServletBox.setLoginUID(session, user.getID());
			ServletBox.setLoginUname(session, user.getUsername());
			//清空错误次数
			session.setAttribute("loginWrongTimes", 0);
			//记录登录历史 未完待续
			
			//将uid写入cookie
			Cookie cookie = new Cookie(ServletBox.LOGIN_ID, user.getID());
			cookie.setPath("/");
			response.addCookie(cookie);
			
			UserVO vo = new UserVO();
			BeanUtils.copyProperties(user, vo);
			/*return Result.ok(vo);*/
			return	Result.ok("注册信息成功");
		}else{
			return	checkResult;
		}
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
