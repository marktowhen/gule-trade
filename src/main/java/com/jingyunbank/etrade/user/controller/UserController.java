package com.jingyunbank.etrade.user.controller;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.Result;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.user.IUserService;
import com.jingyunbank.etrade.api.user.bo.Users;
import com.jingyunbank.etrade.base.constant.Constant;
import com.jingyunbank.etrade.base.util.Md5Util;
import com.jingyunbank.etrade.user.bean.UserVO;
@RestController
@RequestMapping("/user")
public class UserController {
  	@Autowired
	private IUserService userService;
	//暂时屏蔽
//	@Resource
//	private UserService userService;
	
	@RequestMapping("/user")
	public String invest(HttpServletRequest request, HttpSession session){
		HttpSession session0 = request.getSession();
		session0.setAttribute("abcdef", "abcdef");
		session.setAttribute("ghijk", "ghijk");
		System.out.println(session0.getAttribute("ghijk"));
		return "{username:mike, password:black mamba}";
	}
	
	@RequestMapping(value="/reginter",method=RequestMethod.POST)
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
	@RequestMapping(value="/login",method=RequestMethod.POST)
	public Result login(HttpServletRequest request, HttpSession session,String loginfo ,String password,String captcha ){
		//1、参数校验
		if(StringUtils.isEmpty(loginfo)){
			return Result.fail("请输入用户名/手机/邮箱");
		}
		if(StringUtils.isEmpty(password)){
			return Result.fail("请输入密码");
		}
		//密码不正确3次后需要验证码
		int loginWrongTimes = 0;
		//session中存放的错误次数
		if(session.getAttribute("loginWrongTimes")!=null){
			loginWrongTimes = Integer.parseInt((String)session.getAttribute("loginWrongTimes"));
			if(loginWrongTimes>=3){
				if(!checkCaptcha(session, captcha)){
					return Result.fail("验证码错误");
				}
			}
		}
		//2、根据用户名/手机号/邮箱查询用户信息
		Optional<Users> usersOptional =  userService.getByKey(loginfo);
		//是否存在该用户
		if(usersOptional.isPresent()){
			Users users = usersOptional.get();
			//密码是否正确
			if(!users.getPassword().equals(Md5Util.getMD5(password))){
				//记录错误次数
				session.setAttribute("loginWrongTimes", ++loginWrongTimes);
				return Result.fail("密码错误");
			}
			//用户被锁
			if(users.isLocked()){
				return Result.fail("用户被锁");
			}
		}else{
			return Result.fail("未找到该用户");
		}
		//3、成功之后
		//用户信息放入session
		session.setAttribute(Constant.SESSION_USER, usersOptional.get());
		//清空错误次数
		session.setAttribute("loginWrongTimes", 0);
		//记录登录历史??
		
		return Result.ok("成功");
	}
	
	
	/**
	 * 校验验证码
	 * @param session
	 * @param captcha
	 * @return
	 */
	private boolean checkCaptcha(HttpSession session, String captcha) {
		return true;
	}
}
