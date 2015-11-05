package com.jingyunbank.etrade.user.controller;
import java.util.Optional;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.Result;
import com.jingyunbank.core.lang.Patterns;
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

	
	@RequestMapping("/user")
	public String invest(HttpServletRequest request, HttpSession session){
		HttpSession session0 = request.getSession();
		session0.setAttribute("abcdef", "abcdef");
		session.setAttribute("ghijk", "ghijk");
		System.out.println(session0.getAttribute("ghijk"));
		return "{username:mike, password:black mamba}";
	}
	
	/**
	 * 
	 * @todo  用户注册信息
	 * @author guoyuxue
	 * @param request
	 * @param session
	 * @param userVO
	 * @return
	 * @throws DataSavingException
	 */
	@RequestMapping(value="/register",method=RequestMethod.PUT)
	public Result register(HttpServletRequest request,HttpSession session,UserVO userVO) throws DataSavingException{
		Users user=new Users();
		BeanUtils.copyProperties(userVO, user);
		if(userVO.getMobile()!=null){
			Pattern p = Pattern.compile(Patterns.INTERNAL_MOBILE_PATTERN);
			if(p.matcher(userVO.getMobile()).matches()==false){
				return Result.fail("该类手机号不存在");
			}
			if(userVO.getMobile().length()!=11){
				return Result.fail("手机号必须是十一位");
			}
			if(userService.phoneExists(userVO.getMobile())){
				return Result.fail("该手机号已存在。");
			}
		}
		if(userVO.getUsername()==null){
			return Result.fail("用户名不能为空");
		}
		if(userService.unameExists(userVO.getUsername())){
			return Result.fail("该用户名已存在。");
		}
		if(userVO.getEmail()!=null){
			Pattern pattern =Pattern.compile(Patterns.INTERNAL_EMAIL_PATTERN);
			if(pattern.matcher(userVO.getEmail()).matches()==false){
				return Result.fail("邮箱格式不正确！");
			}
			if(userService.emailExists(userVO.getEmail())){
			return Result.fail("该邮箱已存在");
			}
		}
		if(userVO.getPassword().length()<7||userVO.getPassword().length()>21){
			return Result.fail("密码必须是8-20位的");
		}
		if(userVO.getTradepwd().length()<7||userVO.getTradepwd().length()>21){
			return Result.fail("交易密码也应该是8-20位的");
		}
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
				//暂时先不管
				//return Result.fail("用户被锁");
			}
		}else{
			return Result.fail("未找到该用户");
		}
		//3、成功之后
		//用户信息放入session
		session.setAttribute(Constant.SESSION_USER, usersOptional.get());
		session.setAttribute(Constant.SESSION_UID, usersOptional.get().getID());
		//清空错误次数
		session.setAttribute("loginWrongTimes", 0);
		//记录登录历史 未完待续
		
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
