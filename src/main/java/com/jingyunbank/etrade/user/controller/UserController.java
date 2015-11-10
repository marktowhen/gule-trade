package com.jingyunbank.etrade.user.controller;
import java.util.Arrays;
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
import com.jingyunbank.core.msg.MessagerManager;
import com.jingyunbank.core.msg.sms.SmsMessage;
import com.jingyunbank.core.util.MD5;
import com.jingyunbank.core.web.ServletBox;
import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.user.IUserService;
import com.jingyunbank.etrade.api.user.bo.UserInfo;
import com.jingyunbank.etrade.api.user.bo.Users;
import com.jingyunbank.etrade.user.bean.UserVO;
@RestController
@RequestMapping("/api/user")
public class UserController {
  	@Autowired
	private IUserService userService;
/*  	@Autowired 
  	private IUserService userInfoService;*/
  	
	
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
						.map(oe -> Arrays.asList(oe.getDefaultMessage()).toString())
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
	 * 当前手机号发送验证
	 * @param request
	 * @param session
	 * @param userVO
	 * @return
	 */
	@RequestMapping(value="/send/message",method=RequestMethod.GET)
	public Result currentPhone(UserVO userVO,String code,HttpServletRequest request, HttpSession session) throws Exception{
		String id = ServletBox.getLoginUID(request);
		if(!StringUtils.isEmpty(id)){
		Users users=userService.getByUid(id).get();
		if(sendCodeCommon(users.getMobile(),session,request)){
			
			return Result.ok("已经收到当前手机号的验证码了！");
		}
		return Result.fail("请重新登录");
		}
		return Result.fail("重试");
	}
	/**
	 * 当前手机号检验手机号或验证码是否输入正确
	 * @param mobile
	 * @param code
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/send/message",method=RequestMethod.POST)
	public Result chenckPhoneCode(String mobile,String code,HttpServletRequest request, HttpSession session){
		String uid = ServletBox.getLoginUID(request);
		if(!StringUtils.isEmpty(uid)){
			if(checkCodeCommon(mobile,code,request,session)){
				return Result.ok("手机验证成功");
				//只有当前手机号验证成功了，才会进入到修改手机号阶段！
				//只有当前手机号验证成功了，才会进行修改登录密码！
				//只有当前手机号验证成功了，才可以进行修改支付密码！
				//只有当前手机号验证成功了，才可以进行设置支付密码！
			}
		}
		return Result.fail("手机或验证码不一致,没有登录");
	}
	
	/**
	 * 1更换手机发送验证码的过程操作
	 * @param userVO
	 * @param valid
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/update/phone",method=RequestMethod.GET)
	public Result sendUpdatePhone(UserVO userVO,HttpSession session,HttpServletRequest request) throws Exception{
		String uid = ServletBox.getLoginUID(request);
		if(!StringUtils.isEmpty(uid)){
			
			if(userVO.getMobile()!=null){
				Pattern p = Pattern.compile(Patterns.INTERNAL_MOBILE_PATTERN);
				if(!p.matcher(userVO.getMobile()).matches()){
					return Result.fail("手机格式不正确");
				}
				if(userService.phoneExists(userVO.getMobile())){
					return Result.fail("该手机号已存在。");
				}
			
			}
			if(sendCodeCommon(userVO.getMobile(),session,request)){
				return Result.ok("已经修改的手机号发送了验证码");
		}
		}
		return Result.fail("手机修改失败或是没能发送验证码");
	}
	/**
	 * 1更换手机之后的验证码是否输入正确呢
	 * @param userVO
	 * @param valid
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/update/phone",method=RequestMethod.POST)
	public Result checkCodeUpdatePhone(UserVO userVO,String code,HttpServletRequest request, HttpSession session) throws Exception{
		String uid = ServletBox.getLoginUID(request);
		if(!StringUtils.isEmpty(uid)){
			Users users=new Users();
			userVO.setID(uid);
			BeanUtils.copyProperties(userVO, users);
			if(checkCodeCommon(userVO.getMobile(),code,request,session) && userService.refresh(users)){
				return Result.ok("手机验证成功");
			}
		}
		return Result.fail("手机或验证码不一致,没有登录");
	}
	/**
	 * 2修改登录密码
	 * @param userVO
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/update/password",method=RequestMethod.POST)
	public Result updatePassword(UserVO userVO,HttpSession session,HttpServletRequest request) throws Exception{
	
		//验证登录密码有效性
		if(userVO.getPassword()!=null){
			if(userVO.getPassword().length()<7||userVO.getPassword().length()>20){
				return Result.fail("密码必须是8-20位");
			}
		}
		String uid = ServletBox.getLoginUID(request);
		userVO.setID(uid);
		Users users=new Users();
		BeanUtils.copyProperties(userVO, users);
		if(userService.refresh(users)){
			return Result.ok("修改登录密码成功");
		}
		return Result.ok(userVO);
	}
	/**
	 * 3修改交易密码
	 * @param userVO
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/update/tradepwd",method=RequestMethod.POST)
	public Result updateTradePassword(UserVO userVO,HttpSession session,HttpServletRequest request) throws Exception{
		//验证交易密码的有效性
		if(userVO.getTradepwd()!=null){
			if(userVO.getTradepwd().length()<7||userVO.getTradepwd().length()>20){
				return Result.fail("交易密码必须是8-20位");
			}
		}
		String uid = ServletBox.getLoginUID(request);
		userVO.setID(uid);
		Users users=new Users();
		BeanUtils.copyProperties(userVO, users);
		if(userService.refresh(users)){
			return Result.ok("修改交易密码成功");
		}
		return Result.ok(userVO);
		
	}
	/*public Result installTradepwd(){
		
	}*/
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
			if(!users.getPassword().equals(MD5.digest(password))){
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
		ServletBox.setLoginUID(session, users.getID());
		ServletBox.setLoginUname(session, users.getUsername());
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
		String id = ServletBox.getLoginUID(request);
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
		
		String id = ServletBox.getLoginUID(request);
		if(!StringUtils.isEmpty(userService.getByUid(id).get().getMobile())){
			return Result.fail("您已经绑定过手机了");
		}
		String code  = getCheckCode();
		if(!StringUtils.isEmpty(id)){
			//如何设置验证码的有效期限--待解决
			SmsMessage message = new SmsMessage();
			message.setMobile(mobile);
			message.setBody("您的验证码是:"+code);
			Result result = null;
			result = MessagerManager.getSmsSender().send(message);
			if(result.isBad()){
				return result;
			}
			session.setAttribute(ServletBox.SMS_MESSAGE, code);
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
		String uid = ServletBox.getLoginUID(request);
		String sessionCode  = (String)session.getAttribute(ServletBox.SMS_MESSAGE);
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
					//清除session
					session.setAttribute(ServletBox.SMS_MESSAGE, null);
					session.setAttribute("UNCHECK_MOBILE", null);
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
		return String.valueOf(Math.abs(new Random().nextInt()%10000));
	}
	/**
	 * 发送验证码的公共方法
	 * @param mobile
	 * @param session
	 * @param request
	 * @return
	 * @throws Exception
	 */
	private boolean sendCodeCommon(String mobile,HttpSession session,HttpServletRequest request) throws Exception{
		boolean flag=false;
		String code  = getCheckCode();
		SmsMessage message = new SmsMessage();
		message.setMobile(mobile);
		message.setBody("您的验证码是:"+code);
		Result result = null;
		result = MessagerManager.getSmsSender().send(message);
		if(result.isBad()){
			return flag;
		}
		session.setAttribute(ServletBox.SMS_MESSAGE, code);
		session.setAttribute("UNCHECK_MOBILE", mobile);
		return flag=true;
		
	}
	/**
	 * 验证手机号和验证码是否输入正确！
	 * @param request
	 * @param session
	 * @param mobile
	 * @param code
	 * @return
	 */
	private boolean checkCodeCommon(String mobile,String code,HttpServletRequest request, HttpSession session){
		boolean flag=false;
		
		String sessionCode  = (String)session.getAttribute(ServletBox.SMS_MESSAGE);
		if(StringUtils.isEmpty(sessionCode)){
			return flag=false;
		}
			//验证发送短信的手机号与最后提交的手机号是否一致
			if(session.getAttribute("UNCHECK_MOBILE").equals(mobile)){
				//判断是否成功
				if(sessionCode.equals(code)){
					//成功后修改用户手机号
				/*	Users users = new Users();
					users.setID(uid);
					users.setMobile(mobile);
					userService.refresh(users);*/
					//清除session
					session.setAttribute(ServletBox.SMS_MESSAGE, null);
					session.setAttribute("UNCHECK_MOBILE", null);
					return flag=true;
				}
			}
			return flag;
	}
}
