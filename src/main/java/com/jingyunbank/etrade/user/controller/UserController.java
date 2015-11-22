package com.jingyunbank.etrade.user.controller;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.tomcat.util.codec.binary.Base64;
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

import com.jingyunbank.core.Result;
import com.jingyunbank.core.lang.Patterns;
import com.jingyunbank.core.util.MD5;
import com.jingyunbank.core.web.AuthBeforeOperation;
import com.jingyunbank.core.web.ServletBox;
import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.message.bo.Message;
import com.jingyunbank.etrade.api.message.service.context.ISyncNotifyService;
import com.jingyunbank.etrade.api.user.bo.UserInfo;
import com.jingyunbank.etrade.api.user.bo.Users;
import com.jingyunbank.etrade.api.user.service.IUserInfoService;
import com.jingyunbank.etrade.api.user.service.IUserService;
import com.jingyunbank.etrade.user.bean.LoginUserVO;
import com.jingyunbank.etrade.user.bean.UserVO;
@RestController
@RequestMapping("/api/user")
public class UserController {
  	@Autowired
	private IUserService userService;
	@Autowired 
  	private IUserInfoService userInfoService;
	@Resource
	private ISyncNotifyService emailService;
	@Resource
	private ISyncNotifyService smsService;
  	
	private static long EMAIL_VILAD_TIME = (1*60*60*1000); //1小时 单位毫秒
	
	public static final String EMAIL_MESSAGE = "EMAIL_MESSAGE";
	
	
/**
 * 用户注册信息及其发送手机或邮箱验证码
 * @param userVO
 * @param valid
 * @param request
 * @param session
 * @return
 * @throws Exception
 */
	@RequestMapping(value="/register/send",method=RequestMethod.PUT)
	public Result register(UserVO userVO,HttpServletRequest request,HttpSession session) throws Exception{
		//验证邮箱是否存在
		if(!StringUtils.isEmpty(userVO.getMobile())){
			Pattern p = Pattern.compile(Patterns.INTERNAL_MOBILE_PATTERN);
			if(!p.matcher(userVO.getMobile()).matches()){
				return Result.fail("手机格式不正确");
			}
			if(userService.phoneExists(userVO.getMobile())){
				return Result.fail("该手机号已存在。");
			}
			return sendCodeToMobile(userVO.getMobile(), getCheckCode(), request);
		}
		//验证邮箱是否存在
		if(!StringUtils.isEmpty(userVO.getEmail())){
			Pattern p1 = Pattern.compile(Patterns.INTERNAL_EMAIL_PATTERN);
			if(!p1.matcher(userVO.getEmail()).matches()){
				return Result.fail("手机格式不正确");
			}
			if(userService.emailExists(userVO.getEmail())){
				return Result.fail("该邮箱已存在");
			}
			return sendCodeToEmail(userVO.getEmail(), "验证码", getCheckCode(), request);
		}
		return Result.fail("发送验证码失败");
	}
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
	@RequestMapping(value="/register/checkcode",method=RequestMethod.POST)
	public Result registerCheckCode(@Valid UserVO userVO,BindingResult valid,HttpServletRequest request, HttpSession session,String mobile,String code) throws Exception{
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
		Result checkResult = null;
		if(userVO.getMobile()==null&&userVO.getEmail()==null){
			return Result.fail("邮箱和手机号至少有一个不为空");
		}
		//验证手机号是否存在
		if(userVO.getMobile()!=null){
			if(userService.phoneExists(userVO.getMobile())){
				return Result.fail("该手机号已存在。");
			}
			checkResult = checkCode(code, request, ServletBox.SMS_MESSAGE);
		}
		//验证邮箱是否存在
		if(userVO.getEmail()!=null){
			if(userService.emailExists(userVO.getEmail())){
			return Result.fail("该邮箱已存在");
			}
			checkResult = checkCode(code, request, EMAIL_MESSAGE);
		}
		Users user=new Users();
		BeanUtils.copyProperties(userVO, user);
		UserInfo userInfo=new UserInfo();
		userInfo.setRegip(request.getRemoteAddr());
		//保存用户信息和个人资料信息
		if(checkResult.isOk()){
			if(userService.save(user, userInfo)){
			return	Result.ok("注册信息成功");
			}
		}
		return Result.fail("验证失败或是保存失败");
	}
	/**
	 * 当前手机号发送验证
	 * @param request
	 * @param session
	 * @param userVO
	 * @return
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/send/message",method=RequestMethod.GET)
	public Result currentPhone(UserVO userVO,String code,HttpServletRequest request, HttpSession session) throws Exception{
		String id = ServletBox.getLoginUID(request);
		
		Users users=userService.getByUid(id).get();
		if(users.getMobile()!=null){
			return sendCodeToMobile(users.getMobile(), getCheckCode(), request);
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
	@AuthBeforeOperation
	@RequestMapping(value="/send/message",method=RequestMethod.POST)
	public Result chenckPhoneCode(String mobile,String code,HttpServletRequest request, HttpSession session) throws Exception{
		Result	checkResult = checkCode(code, request, ServletBox.SMS_MESSAGE);
			if(checkResult.isOk()){
				return Result.ok("手机验证成功");
			}
			/*if(checkCodeCommon(mobile,code,request,session)){
				return Result.ok("手机验证成功");
				//只有当前手机号验证成功了，才会进入到修改手机号阶段！
				//只有当前手机号验证成功了，才会进行修改登录密码！
				//只有当前手机号验证成功了，才可以进行修改支付密码！
				//只有当前手机号验证成功了，才可以进行设置支付密码！
			}*/
		
		return Result.fail("手机或验证码不一致");
	}
	//修改手机号的操作
	/**
	 * 1更换手机发送验证码的过程操作
	 * @param userVO
	 * @param valid
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/update/phone",method=RequestMethod.GET)
	public Result sendUpdatePhone(UserVO userVO,HttpSession session,HttpServletRequest request) throws Exception{
		//验证手机号输入的准确性
		if(userVO.getMobile()!=null){
				Pattern p = Pattern.compile(Patterns.INTERNAL_MOBILE_PATTERN);
				if(!p.matcher(userVO.getMobile()).matches()){
					return Result.fail("手机格式不正确");
				}
				//检验手机号是否已经存在
				if(userService.phoneExists(userVO.getMobile())){
					return Result.fail("该手机号已存在。");
				}
			}
			 return sendCodeToMobile(userVO.getMobile(), getCheckCode(), request);
		
		
		
	}
	//修改手机号的操作
	/**
	 * 1更换手机之后的验证码是否输入正确呢
	 * @param userVO
	 * @param valid
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/update/phone",method=RequestMethod.POST)
	public Result checkCodeUpdatePhone(UserVO userVO,String code,HttpServletRequest request, HttpSession session) throws Exception{
		String uid = ServletBox.getLoginUID(request);
			Users users=new Users();
			userVO.setID(uid);
			BeanUtils.copyProperties(userVO, users);
		Result	checkResult = checkCode(code, request, ServletBox.SMS_MESSAGE);
			if(checkResult.isOk() && userService.refresh(users)){
				return Result.ok(userVO);
			}
		
		return Result.fail("手机或验证码不一致,");
	}
	/**
	 * 2修改登录密码
	 * @param userVO
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@AuthBeforeOperation
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
			return Result.ok(userVO);
		}
		return Result.fail("修改登录密码失败");
	}
	/**
	 * 3修改交易密码
	 * @param userVO
	 * @param session
	 * @return
	 */
	@AuthBeforeOperation
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
			return Result.ok(userVO);
		}
		return Result.fail("修改交易密码失败");
		
	}
	/**
	 * 4设置交易密码(通过id查询出这个对象看看那有没有交易密码，没有的情况下进行添加)
	 * @param userVO
	 * @param session
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/install/tradepwd",method=RequestMethod.POST)
	public Result installTradepwd(UserVO userVO,HttpSession session,HttpServletRequest request) throws Exception{
		
		if(userVO.getTradepwd()!=null){
			if(userVO.getTradepwd().length()<7||userVO.getTradepwd().length()>20){
				return Result.fail("交易密码必须是8-20位");
			}
		}
		String uid = ServletBox.getLoginUID(request);
		Optional<Users> optional=userService.getByUid(uid);
		Users users=optional.get();
		if(StringUtils.isEmpty(users.getTradepwd())){
				userVO.setID(uid);
				BeanUtils.copyProperties(userVO, users);
				if(userService.refresh(users)){
					return Result.ok(userVO);
				}
		}
		return Result.fail("设置交易密码失败");
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
	@RequestMapping(value="/login",method=RequestMethod.POST,
				consumes="application/json;charset=UTF-8")
	public Result login(@Valid @RequestBody LoginUserVO user, 
						BindingResult valid, HttpSession session) throws Exception{
		if(valid.hasErrors()){
			return Result.fail("用户名或者密码错误！");
		}
		
		//密码不正确3次后需要验证码
		int loginWrongTimes = 0;
		//session中存放的错误次数
		Object objLoginTimes = session.getAttribute("loginWrongTimes");
		if(objLoginTimes != null && objLoginTimes instanceof Integer){
			loginWrongTimes = (int)session.getAttribute("loginWrongTimes");
			if(loginWrongTimes>=3){
				if(!checkCaptcha(session, user.getCaptcha())){
					return Result.fail("验证码错误");
				}
			}
		}
		//2、根据用户名/手机号/邮箱查询用户信息
		Optional<Users> usersOptional =  userService.getByKey(user.getKey());
		//是否存在该用户
		if(usersOptional.isPresent()){
			Users users = usersOptional.get();
			//密码是否正确
			if(!users.getPassword().equals(MD5.digest(user.getPassword()))){
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
	 * 用户注销登录
	 * @param session
	 * @return
	 * 2015年11月11日 qxs
	 */
	@RequestMapping(value="/logout",method=RequestMethod.GET)
	public Result logout(HttpSession session) throws Exception{
		session.invalidate();
		return Result.ok("成功");
	}
	
	/**
	 * 根据用户名/手机/邮箱查询用户信息
	 * @param request
	 * @param session
	 * @param key 用户名/手机/邮箱
	 * 
	 * @return
	 * qxs
	 */
	@RequestMapping(value="/query",method=RequestMethod.GET)
	public Result getUserByKey(HttpServletRequest request, HttpSession session,String key  ) throws Exception{
		//1、参数校验
		if(StringUtils.isEmpty(key)){
			return Result.fail("请输入用户名/手机/邮箱");
		}
		//2、根据用户名/手机号/邮箱查询用户信息
		Optional<Users> usersOptional =  userService.getByKey(key);
		//是否存在该用户
		if(usersOptional.isPresent()){
			Users users = usersOptional.get();
			return Result.ok(getUserVoFromBo(users));
		}else{
			return Result.fail("未找到该用户");
		}
	}
	//忘记密码
	/**
	 * 1为输入的邮箱或手机号发送验证码
	 * @param request
	 * @param session
	 * @param loginfo
	 * @return
	 */
	@RequestMapping(value="/forgetpwd/send",method=RequestMethod.GET)
	public Result forgetpwdSend(HttpServletRequest request, HttpSession session,String loginfo) throws Exception{
		if(StringUtils.isEmpty(loginfo)){
			return Result.fail("手机/邮箱");
		}
		Optional<Users> usersOptional = userService.getByKey(loginfo);
		Users users=usersOptional.get();
		if(users.getEmail()!=null){
			return sendCodeToEmail(loginfo, "验证码", getCheckCode(), request);
		}
		if(users.getMobile()!=null){
			return sendCodeToMobile(users.getMobile(), getCheckCode(), request);
		}
		return Result.fail("发送验证码失败");
	}
	//忘记密码
	/**
	 * 2.验证输入的验证码是否正确并且保存修改后的密码进行保存
	 * @param userVO
	 * @param request
	 * @param session
	 * @param loginfo
	 * @param code
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/forgetpwd/check",method=RequestMethod.POST)
	public Result forgetpwdCheck(UserVO userVO,HttpServletRequest request, HttpSession session,String loginfo,String code) throws Exception{
		if(userVO.getPassword().length()<7||userVO.getPassword().length()>20){
			return Result.fail("登录密码必须是8-20位");
		}
		Result result=null;
		Optional<Users> usersOptionals = userService.getByKey(loginfo);
		Users users=usersOptionals.get();
		if(users.getMobile()!=null){
			result=checkCode(code, request, ServletBox.SMS_MESSAGE);
		}
		if(users.getEmail()!=null){
			result=checkCode(code, request, EMAIL_MESSAGE);
		}
		userVO.setID(users.getID());
		BeanUtils.copyProperties(userVO, users);
		if(userService.refresh(users)&&result.isOk()){
			return Result.ok(userVO);
		}
		
		return Result.fail("验证码出现错误或是修改未成功");
		
	}
	
	/**
	 * 获取已登录的用户
	 * @param request
	 * @param session
	 * @return
	 * 2015年11月6日 qxs
	 */
	@RequestMapping(value="/loginuser",method=RequestMethod.GET)
	public Result getLoginUser(HttpServletRequest request, HttpSession session) throws Exception{
		String id = ServletBox.getLoginUID(request);
		if(!StringUtils.isEmpty(id)){
			Optional<Users> users = userService.getByUid(id);
			if(users.isPresent()){
				return Result.ok(getUserVoFromBo(users.get()));
			}
		}
		return Result.fail("未登录");
	}
	
	
	
	//------------------------------qxs 验证/修改邮箱  start-----------------------------------------------
	
	//1、
	/**
	 * 发送验证码到注册手机 
	 * @param request
	 * @return
	 * @throws Exception
	 * 2015年11月11日 qxs
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/smsMessage",method=RequestMethod.GET)
	public Result sendCodeToRegistMobile(HttpServletRequest request) throws Exception{
		 Optional<Users> userOption = userService.getByUid(ServletBox.getLoginUID(request));
		 return sendCodeToMobile(userOption.get().getMobile(), getCheckCode(), request);
	}
	
	//2、
	/**
	 * 校验短信验证码
	 * @param request
	 * @param code
	 * @return
	 * 2015年11月11日 qxs
	 */
	@RequestMapping(value="/cksmsMessage",method=RequestMethod.GET)
	public Result checkSmsMassage(HttpServletRequest request, String code){
		return checkCode(code, request, ServletBox.SMS_MESSAGE);
	}
	//3、
	/**
	 * 校验图形验证码，校验邮箱格式,通过后发送验证链接到用户输入的邮箱
	 * @param request
	 * @param code
	 * @param email
	 * @return
	 * 2015年11月11日 qxs
	 * @throws Exception 
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/email-link",method=RequestMethod.GET)
	public Result checkCodeAndSendEamil(HttpServletRequest request, String code, String email) throws Exception{
		if(!checkCaptcha(request.getSession(), code)){
			return Result.fail("验证码错误");
		}
		if(StringUtils.isEmpty(email)){
			return Result.fail("邮箱地址不能为空");
		}
		Pattern p = Pattern.compile(Patterns.INTERNAL_EMAIL_PATTERN);
		if(!p.matcher(email).matches()){
			return Result.fail("邮箱格式错误");
		}
		Optional<Users> userOption = userService.getByUid(ServletBox.getLoginUID(request));
		if(userService.getByEmail(email).isPresent()){
			return Result.fail("该邮箱已被使用");
		}
		return sendLinkToEmail(request,userOption.get(), email);
	}
	
	
	//4、验证邮箱链接，通过后绑定邮箱
	/**
	 * 验证绑定邮箱的链接
	 * @param request
	 * @param m uid+"_"+username MD5加密后字符串
	 * @param u email+"~"+邮件发送时间 base64编码后字符
	 * @param d uid
	 * @return
	 * 2015年11月10日 qxs
	 * @throws DataRefreshingException 
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/ckemail-link",method=RequestMethod.GET)
	public Result checkEmailLink(HttpServletRequest request,
			String m, String u, String d) throws DataRefreshingException{
		Optional<Users> userOption = userService.getByUid(d);
		
		Users users = userOption.get();
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
	
	
	
	//------------------------------验证邮箱end-----------------------------------------------
	
	
	//------------------------------qxs 验证手机  start-----------------------------------------------
		//1、发送邮箱验证码
	/**
	 * 发送验证码到注册邮箱
	 * @param request
	 * @param resp
	 * @param email
	 * @return
	 * 2015年11月10日 qxs
	 * @throws Exception 
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/email-message",method=RequestMethod.GET)
	public Result sendCodeToEmail(HttpServletRequest request) throws Exception {
		 Optional<Users> userOption = userService.getByUid(ServletBox.getLoginUID(request));
		return  sendCodeToEmail(userOption.get().getEmail(), "验证码", getCheckCode(), request);
	}
	//2、验证邮箱验证码
	/**
	 * 验证邮箱验证码
	 * @param request
	 * @param resp
	 * @param email
	 * @return
	 * 2015年11月10日 qxs
	 */
	@RequestMapping(value="/email-message",method=RequestMethod.POST)
	public Result checkEmailCode(HttpServletRequest request,@RequestBody String code) {
		return  checkCode(code, request, EMAIL_MESSAGE);
	}
	//3、
	/**
	 * 发送手机验证码 
	 * @param request
	 * @param session
	 * @param mobile 
	 * @return
	 * 2015年11月6日 qxs
	 * @throws Exception 
	 */
	@AuthBeforeOperation
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
		//获取验证码
		String code  = getCheckCode();
			//如何设置验证码的有效期限--待解决
			Result result = sendCodeToMobile(mobile, code, request);
			if(result.isBad()){
				return result;
			}
			session.setAttribute("UNCHECK_MOBILE", mobile);
			return Result.ok("成功");
		
	}
	
	//4、
	/**
	 * 验证手机验证码并绑定手机
	 * @param request
	 * @param session
	 * @param mobile 
	 * @return
	 * 2015年11月6日 qxs
	 * @throws DataRefreshingException 
	 * @throws Exception 
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/message/{mobile}",method=RequestMethod.POST)
	public Result checkBindingMobile(HttpServletRequest request, HttpSession session,@PathVariable String mobile,@RequestBody String code) throws Exception {
		String uid = ServletBox.getLoginUID(request);
		String sessionCode  = (String)session.getAttribute(ServletBox.SMS_MESSAGE);
		if(StringUtils.isEmpty(sessionCode)){
			return Result.fail("未发送短信或短信已失效");
		}
			//验证发送短信的手机号与最后提交的手机号是否一致
			if(session.getAttribute("UNCHECK_MOBILE").equals(mobile)){
				//判断是否成功
				Result checkResult = checkCode(code, request, ServletBox.SMS_MESSAGE);
				if(checkResult.isOk()){
					//成功后修改用户手机号
					Users users = new Users();
					users.setID(uid);
					users.setMobile(mobile);
					userService.refresh(users);
					//清除session
					session.setAttribute("UNCHECK_MOBILE", null);
					return Result.ok("验证成功");
				}else{
					return Result.fail("验证码错误");
				}
			}else{
				return Result.fail("请确认手机号是否正确");
			}
	}
	
	//------------------------------qxs 验证手机  end-----------------------------------------------
  
	
	
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
	 * 校验图形验证码
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
	 * 发送邮箱验证码,将code放入session  EMAIL_MESSAGE
	 * @param email
	 * @param subTitle
	 * @param code
	 * @param request
	 * @return
	 * 2015年11月10日 qxs
	 * @throws Exception 
	 */
	private Result sendCodeToEmail(String email, String subTitle, String code, HttpServletRequest request) throws Exception{
		request.getSession().setAttribute(EMAIL_MESSAGE, code);
		Message message = new Message();
		message.setTitle(subTitle);
		message.setContent("您的验证码是:"+code);
		message.getReceiveUser().setEmail(email);
		System.out.println("-----------------"+"您的验证码是:"+code);
		//emailService.inform(message);
		return Result.ok();
	}
	/**
	 * 发送手机验证码 将code放入session  SMS_MESSAGE
	 * @param mobile
	 * @param code
	 * @param request
	 * @return
	 * @throws Exception
	 * 2015年11月10日 qxs
	 */
	private Result sendCodeToMobile(String mobile, String code, HttpServletRequest request) throws Exception{
		request.getSession().setAttribute(ServletBox.SMS_MESSAGE, code);
		Message message = new Message();
		message.setContent("您的验证码是:"+code);
		message.getReceiveUser().setMobile(mobile);
		message.setTitle("");
		System.out.println("-----------------"+"您的验证码是:"+code);
		//smsService.inform(message);
		return Result.ok();
	}
	/**
	 * 验证验证码,成功后清除session
	 * @param code
	 * @param request
	 * @param sessionKey 验证码在session中的name
	 * @return
	 * 2015年11月10日 qxs
	 */
	private Result checkCode(String code, HttpServletRequest request, String sessionName){
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
	 * 发送绑定邮箱的链接 点击链接验证后绑定
	 * 链接地址 http://ip(:port)?d=1&u=2&m=3
	 * d: uid
	 * u: email+"~"+邮件发送时间 base64编码后字符
	 * m: uid+"_"+username MD5加密后字符串
	 * @param request
	 * @param email
	 * @return
	 * @throws Exception 
	 */
	private  Result sendLinkToEmail(HttpServletRequest request,Users user, String email) throws Exception{
		
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
		//验证链接的地址
		String url = basePath
				+ "api/user/ckemail.htm?d="+ user.getID()
				+ "&u=" + verifyCode + "&m=" + msg1Md5;
		//编辑邮箱内容
		StringBuffer content = new StringBuffer();
		content.append("验证链接:"+url+"\r\n");
		content.append("有效期:"+(EMAIL_VILAD_TIME/1000/60/60)+"小时");
		
		Message emailMessage = new Message();
		emailMessage.setTitle("用户验证");
		emailMessage.setContent(content.toString());
		emailMessage.getReceiveUser().setEmail(email);
		emailService.inform(emailMessage);
		return Result.ok();
	}
	/**
	 * 获取项目根目录
	 * @param request
	 * @return
	 * 2015年11月10日 qxs
	 */
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
	
}
