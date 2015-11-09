package com.jingyunbank.etrade.user.controller;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.Result;
import com.jingyunbank.core.lang.Patterns;
import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.user.IUserService;
import com.jingyunbank.etrade.api.user.bo.UserInfo;
import com.jingyunbank.etrade.api.user.bo.Users;
import com.jingyunbank.etrade.base.constant.Constant;
import com.jingyunbank.etrade.base.util.Md5Util;
import com.jingyunbank.etrade.base.util.RequestUtil;
import com.jingyunbank.etrade.user.bean.UserInfoVO;
import com.jingyunbank.etrade.infrastructure.SmsMessager;
import com.jingyunbank.etrade.user.bean.UserVO;
@RestController
@RequestMapping("/api/user")
public class UserController {
  	@Autowired
	private IUserService userService;
  	@Autowired 
  	private IUserService userInfoService;
  	
  	@Autowired
	private SmsMessager smsMessager;
	
	@RequestMapping("/user")
	public String invest(HttpServletRequest request, HttpSession session){
		HttpSession session0 = request.getSession();
		session0.setAttribute("abcdef", "abcdef");
		session.setAttribute("ghijk", "ghijk");
		System.out.println(session0.getAttribute("ghijk"));
		return "{username:mike, password:black mamba}";
	}
	
/**
 * 用户注册信息的保存
 * @param userVO
 * @param valid
 * @param request
 * @param session
 * @return
 * @throws Exception
 */
	@RequestMapping(value="/register",method=RequestMethod.PUT)
	public Result register(@Valid UserVO userVO,BindingResult valid,HttpServletRequest request,HttpSession session) throws Exception{
		if(valid.hasErrors()){
			List<ObjectError> errors = valid.getAllErrors();
			return Result.fail(errors.stream()
						.map(oe -> Arrays.asList(oe.getCodes()).toString())
						.collect(Collectors.joining(" ; ")));
		}
		//验证用户名是否已存在
		if(userService.unameExists(userVO.getUsername())){
			return Result.fail("该用户名已存在。");
		}
		//验证手机号是否存在
		if(userVO.getMobile()!=null){
			if(userService.phoneExists(userVO.getMobile())){
				return Result.fail("该手机号已存在。");
			}
		}
		//验证邮箱是否存在
		if(userVO.getEmail()!=null){
			if(userService.emailExists(userVO.getEmail())){
			return Result.fail("该邮箱已存在");
			}
		}
		Users user=new Users();
		BeanUtils.copyProperties(userVO, user);
		UserInfo userInfo=new UserInfo();
		userInfo.setRegip(request.getRemoteAddr());
		//保存用户信息和个人资料信息
		if(userService.save(user, userInfo)){
			return Result.ok("保存成功");
		}
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
		/*if(StringUtils.isEmpty(password)){
			return Result.fail("请输入密码");
		}*/
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
		Users users = usersOptional.get();
		session.setAttribute(Constant.LOGIN_ID, users.getID());
		session.setAttribute(Constant.LOGIN_USERNAME, users.getUsername());
		//清空错误次数
		session.setAttribute("loginWrongTimes", 0);
		//记录登录历史 未完待续
		
		return Result.ok("成功");
	}
	/**
	 * 根据用户名/手机/邮箱查询用户信息
	 * @param request
	 * @param session
	 * @param loginfo 用户名/手机/邮箱
	 * 
	 * @return
	 * qxs
	 */
	@RequestMapping(value="/query",method=RequestMethod.GET)
	public Result query(HttpServletRequest request, HttpSession session,String loginfo  ){
		//1、参数校验
		if(StringUtils.isEmpty(loginfo)){
			return Result.fail("请输入用户名/手机/邮箱");
		}
		//2、根据用户名/手机号/邮箱查询用户信息
		Optional<Users> usersOptional =  userService.getByKey(loginfo);
		//是否存在该用户
		if(usersOptional.isPresent()){
			Users users = usersOptional.get();
			return Result.ok(getUserVoFromBo(users));
		}else{
			return Result.fail("未找到该用户");
		}
	}
	
	
	/**
	 * 获取已登录的用户
	 * @param request
	 * @param session
	 * @return
	 * 2015年11月6日 qxs
	 */
	@RequestMapping(value="/loginuser",method=RequestMethod.GET)
	public Result queryLoginUser(HttpServletRequest request, HttpSession session){
		String id = RequestUtil.getLoginId(request);
		if(!StringUtils.isEmpty(id)){
			Optional<Users> users = userService.getByUid(id);
			if(users.isPresent()){
				return Result.ok(getUserVoFromBo(users.get()));
			}
		}
		return Result.fail("未登录");
	}
	
	/**
	 * 发送手机验证码
	 * @param request
	 * @param session
	 * @param mobile 
	 * @return
	 * 2015年11月6日 qxs
	 * @throws Exception 
	 */
	@RequestMapping(value="/message",method=RequestMethod.GET)
	public Result getMessage(HttpServletRequest request, HttpSession session,String mobile) throws Exception{
		if(StringUtils.isEmpty(mobile)){
			return Result.fail("请输入手机号");
		}
		/*Pattern p = Pattern.compile(Patterns.INTERNAL_MOBILE_PATTERN);
		if(!p.matcher(mobile).matches()){
			return Result.fail("手机号格式错误");
		}*/
		String id = RequestUtil.getLoginId(request);
		if(!StringUtils.isEmpty(userService.getByUid(id).get().getMobile())){
			return Result.fail("您已经绑定过手机了");
		}
		String code  = getCheckCode();
		System.out.println("验证码是-------"+code);
		if(!StringUtils.isEmpty(id)){
			//如何设置验证码的有效期限--待解决
			smsMessager.sendMessageToMobile(mobile, "您的验证码是:", code);
			session.setAttribute(Constant.SMS_MESSAGE, code);
			session.setAttribute("UNCHECK_MOBILE", mobile);
			return Result.ok("成功");
		}
		return Result.fail("未登录");
	}
	
	/**
	 * 验证手机号
	 * @param request
	 * @param session
	 * @param mobile 
	 * @return
	 * 2015年11月6日 qxs
	 * @throws DataRefreshingException 
	 * @throws Exception 
	 */
	@RequestMapping(value="/message",method=RequestMethod.POST)
	public Result checkMobile(HttpServletRequest request, HttpSession session,String mobile,String code) throws DataRefreshingException {
		String uid = RequestUtil.getLoginId(request);
		String sessionCode  = (String)session.getAttribute(Constant.SMS_MESSAGE);
		if(StringUtils.isEmpty(sessionCode)){
			return Result.fail("未发送短信或短信已失效");
		}
		if(!StringUtils.isEmpty(uid)){
			//验证发送短信的手机号与最后提交的手机号是否一致
			if(session.getAttribute("UNCHECK_MOBILE").equals(mobile)){
				//判断是否成功
				if(sessionCode.equals(code)){
					//成功后修改用户手机号
					Users users = new Users();
					users.setID(uid);
					users.setMobile(mobile);
					userService.refresh(users);
					return Result.ok("验证成功");
				}else{
					return Result.fail("验证码错误");
				}
			}else{
				return Result.fail("请确认手机号是否正确");
			}
		}
		return Result.fail("未登录");
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
	 * 校验验证码
	 * @param session
	 * @param captcha
	 * @return
	 */
	private boolean checkCaptcha(HttpSession session, String captcha) {
		return true;
	}
	
	/**
	 * 获取4位随机数
	 * @return
	 * 2015年11月7日 qxs
	 */
	private String getCheckCode(){
		
		return String.valueOf(new Random().nextInt()%10000);
	}
}
