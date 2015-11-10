package com.jingyunbank.etrade.user.controller;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.tomcat.util.codec.binary.Base64;
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
  	@Autowired 
  	private IUserService userInfoService;
	
	private static long EMAIL_VILAD_TIME = (1*60*60*1000); //1小时 单位毫秒
	
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
	 * 更换手机操作
	 * @param userVO
	 * @param valid
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/update/phone",method=RequestMethod.POST)
	public Result updatePhone(UserVO userVO,HttpSession session) throws Exception{
	
		//验证手机号是否存在
		if(userVO.getMobile()!=null){
			if(userService.phoneExists(userVO.getMobile())){
				return Result.fail("该手机号已存在。");
			}
			Pattern p = Pattern.compile(Patterns.INTERNAL_MOBILE_PATTERN);
			if(!p.matcher(userVO.getMobile()).matches()){
				return Result.fail("手机格式不正确");
			}
		}
		userVO.setID(session.getAttribute("LOGIN_ID").toString());
		Users users=new Users();
		BeanUtils.copyProperties(userVO, users);
		if(userService.refresh(users)){
			return Result.ok(userVO);
		}
		return Result.ok("修改成功");
		
	}
	/**
	 * 修改密码
	 * @param userVO
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/update/password",method=RequestMethod.POST)
	public Result updatePassword(UserVO userVO,HttpSession session) throws Exception{
	
		//验证手机号是否存在
		if(userVO.getPassword()!=null){
			if(userVO.getPassword().length()<7||userVO.getPassword().length()>20){
				return Result.fail("密码必须是8-20位");
			}
		}
		userVO.setID(session.getAttribute("LOGIN_ID").toString());
		Users users=new Users();
		BeanUtils.copyProperties(userVO, users);
		if(userService.refresh(users)){
			return Result.ok(userVO);
		}
		return Result.ok("修改成功");
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
		if(userService.getByPhone(mobile).isPresent()){
			return Result.fail("该手机号已被使用");
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
	 * 发送邮件（绑定邮箱时使用）
	 * 链接地址 http://ip(:port)?d=1&u=2&m=3
	 * d: uid
	 * u: email+"~"+邮件发送时间 base64编码后字符
	 * m: uid+"_"+username MD5加密后字符串
	 * @param request
	 * @param email
	 * @return
	 */
	@RequestMapping(value="/email",method=RequestMethod.GET)
	public Result sendEmailContentForBindEmail(HttpServletRequest request,
			HttpServletResponse resp, String email) {
		if(userService.getByEmail(email).isPresent()){
			return Result.fail("该邮箱已被使用");
		}
		Optional<Users> userOption = userService.getByUid(ServletBox.getLoginUID(request));
		if(!userOption.isPresent()){
			return Result.fail("请登录");
		}
		if(!StringUtils.isEmpty(userOption.get().getEmail())){
			return Result.fail("您已绑定邮箱");
		}
		
		return sendEmail(request
				,userService.getByUid(ServletBox.getLoginUID(request)).get()
				,email);
	}
	
	/**
	 * 发送绑定邮件
	 * 
	 * @param request
	 * @param user
	 * @param email
	 * @return
	 * 2015年11月10日 qxs
	 */
	private  Result sendEmail(HttpServletRequest request,Users user, String email){
		if(StringUtils.isEmpty(email)){
			return Result.fail("邮箱地址不能为空");
		}
		String basePath = getBasePath(request);
		String msg1 = user.getID() + "_"
				+ user.getUsername();
		// MD5加密（用户id和用户名）
		String msg1Md5 = MD5.digest(msg1);
		// 密钥
		String message = email + "~"
				+ System.currentTimeMillis();
		// 邮箱和当前时间戳进行base64编码
		String verifyCode = new Base64().encodeAsString(message.getBytes());

		String url = basePath
				+ "api/user/ckemail.htm?d="+ user.getID()
				+ "&u=" + verifyCode + "&m=" + msg1Md5;
		StringBuffer content = new StringBuffer();
		
		content.append("验证链接:"+url+"\r\n");
		
		content.append("有效期:"+(EMAIL_VILAD_TIME/1000/60/60)+"小时");
		return MessagerManager.getEmailSender().send(email, "用户验证", content.toString());
	}
	
	private  String getBasePath(HttpServletRequest request){
		String basePath;
		if (request.getServerPort() == 80) {
			basePath = request.getScheme() + "://" + request.getServerName()
					+ "/";
		} else {
			basePath = request.getScheme() + "://" + request.getServerName()
					+ ":" + request.getServerPort() + "/";
		}
		
		return basePath;
	}
	/**
	 * 验证绑定邮箱
	 * @param request
	 * @param m uid+"_"+username MD5加密后字符串
	 * @param u email+"~"+邮件发送时间 base64编码后字符
	 * @param d uid
	 * @return
	 * 2015年11月10日 qxs
	 * @throws DataRefreshingException 
	 */
	@RequestMapping(value="/ckemail",method=RequestMethod.GET)
	public Result checkEmail(HttpServletRequest request,
			String m, String u, String d) throws DataRefreshingException{
		Optional<Users> userOption = userService.getByUid(d);
		if(!userOption.isPresent()){
			return Result.fail("用户不存在");
		}
		
		Users users = userOption.get();
		if(!StringUtils.isEmpty(users.getEmail())){
			return Result.fail("您已绑定邮箱");
		}
		if(!MD5.digest(users.getID()+"_"+users.getUsername()).equals(m)){
			return Result.fail("链接格式错误");
		}
		//emial~time(long)
		String[] emailTime = new String(Base64.decodeBase64(u)).split("~");
		if(emailTime.length!=2){
			return Result.fail("链接格式错误");
		}
		String email = emailTime[0];
		long sendtime = Long.valueOf(emailTime[1]);
		if(new Date(sendtime+EMAIL_VILAD_TIME).before(new Date())){
			return Result.fail("链接已失效");
		}
		if(userService.getByEmail(email).isPresent()){
			return Result.fail("该邮箱已被使用");
		}
		
		//修改用户邮箱
		Users userUpdate = new Users();
		userUpdate.setID(users.getID());
		userUpdate.setEmail(email);
		userService.refresh(userUpdate);
		return Result.ok();
	}
	
	
	
}
