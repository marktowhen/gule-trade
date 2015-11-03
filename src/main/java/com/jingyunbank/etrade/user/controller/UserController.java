package com.jingyunbank.etrade.user.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.Result;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.user.bo.Users;
import com.jingyunbank.etrade.user.bean.UserVO;
import com.jingyunbank.etrade.user.service.UserService;

@RestController
public class UserController {
  
	@Resource
	private UserService userService;
	
	@RequestMapping("/user")
	public String invest(HttpServletRequest request, HttpSession session){
		HttpSession session0 = request.getSession();
		session0.setAttribute("abcdef", "abcdef");
		session.setAttribute("ghijk", "ghijk");
		System.out.println(session0.getAttribute("ghijk"));
		return "{username:mike, password:black mamba}";
	}
	
	@RequestMapping(value="/user/reginter",method=RequestMethod.POST)
	public Result register(HttpServletRequest request,HttpSession session,UserVO userVO) throws DataSavingException{
		if(userVO.getMobile()!=null&&userVO.getMobile().length()!=11){
			return Result.fail("手机号必须是11位。");
		}
		if(userService.phoneExists(userVO.getMobile())){
			return Result.fail("该手机号已存在。");
		}
		if(userService.unameExists(userVO.getUsername())){
			return Result.fail("该用户名已存在。");
		}
		if(userService.emailExists(userVO.getEmail())){
			return Result.fail("该邮箱已存在");
		}
		Users user=new Users();
		BeanUtils.copyProperties(userVO, user);
		userService.save(user);
		return Result.ok(userVO);
	}
}
