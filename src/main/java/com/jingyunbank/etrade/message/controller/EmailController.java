package com.jingyunbank.etrade.message.controller;


import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.Result;
import com.jingyunbank.core.lang.Patterns;
import com.jingyunbank.core.util.RndBuilder;
import com.jingyunbank.core.web.AuthBeforeOperation;
import com.jingyunbank.core.web.ServletBox;
import com.jingyunbank.etrade.api.message.bo.Message;
import com.jingyunbank.etrade.api.message.service.context.ISyncNotifyService;
import com.jingyunbank.etrade.api.user.bo.Users;
import com.jingyunbank.etrade.api.user.service.IUserService;
import com.jingyunbank.etrade.user.controller.UserController;


@RestController
@RequestMapping("/api/email")
public class EmailController {
	
	@Autowired
	private IUserService userService;
	@Resource
	private ISyncNotifyService emailService;
	/**
	 * 邮箱验证码在session中的key
	 */
	public static final String EMAIL_MESSAGE = "EMAIL_MESSAGE";
	

	/**
	 * 为传入的邮箱发送验证码
	 * @param userVO
	 * @param valid
	 * @return
	 * @throws Exception
	 */
	//get /api/sms/code?mail=1234454@qq.com
	@RequestMapping(value="/code",method=RequestMethod.GET)
	public Result<String> sendCodeToEmail(@RequestParam("email") String email,HttpServletRequest request) throws Exception{
		if(!StringUtils.isEmpty(email)){
			Pattern p = Pattern.compile(Patterns.INTERNAL_EMAIL_PATTERN);
			if(!p.matcher(email).matches()){
				return Result.fail("邮箱格式不正确");
			}
			String code = new String(new RndBuilder().length(4).hasletter(false).next());
			return sendCodeToEmail(email,"验证码", code, request);
		}
		return Result.fail("邮箱不能为空");
	}

	
	/**
	 * 1通过key查询出来的邮箱发送验证码
	 * @param request
	 * @param session
	 * @param key
	 * @return
	 */
	@RequestMapping(value="/code/bykey",method=RequestMethod.GET)
	public Result<String> sendCodeByKey(HttpServletRequest request, HttpSession session,@RequestParam("key") String key) throws Exception{
		if(StringUtils.isEmpty(key)){
			return Result.fail("手机/邮箱");
		}
		Optional<Users> usersOptional = userService.getByKey(key);
		Users users=usersOptional.get();
		if(users.getEmail()!=null){
			String code = new String(new RndBuilder().length(4).hasletter(false).next());
			return sendCodeToEmail(users.getEmail(), "验证码", code, request);
		}
		return Result.fail("发送验证码失败");
	}
	
	/**
	 * 为用户绑定邮箱发送验证码
	 * @param request
	 * @param resp
	 * @param email
	 * @return
	 * 2015年11月10日 qxs
	 * @throws Exception 
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/code/user",method=RequestMethod.GET)
	public Result<String> sendCodeToEmail(HttpServletRequest request) throws Exception {
		Optional<Users> userOption = userService.getByUID(ServletBox.getLoginUID(request));
		String code = new String(new RndBuilder().length(4).hasletter(false).next());
		return  sendCodeToEmail(userOption.get().getEmail(), "验证码", code, request);
	}
	

	/**
	 * 验证邮箱验证码
	 * @param request
	 * @param resp
	 * @param email
	 * @return
	 * 2015年11月10日 qxs
	 */
	@RequestMapping(value="/code/check",method=RequestMethod.GET)
	public Result<String> checkEmailCode(HttpServletRequest request,@RequestParam String code,HttpSession session) {
		
		Result<String> checkResul = checkCode(code, request, EMAIL_MESSAGE);
		if(checkResul.isOk()){
			session.setAttribute(UserController.CHECK_CODE_PASS_DATE, new Date());
		}
		return checkResul;
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
	private Result<String> sendCodeToEmail(String email, String subTitle, String code, HttpServletRequest request) throws Exception{
		//距离上次发送时间是否超过1分钟
		if(checkSendTime(request.getSession(), email)){
			request.getSession().setAttribute(EMAIL_MESSAGE, code);
			Message message = new Message();
			message.setTitle(subTitle);
			message.getReceiveUser().setEmail(email);
			
			StringBuffer sb = new StringBuffer();
			sb.append("<div style=\"margin:0 40px;  padding:0;font-size:14px; color:#6a6a6a; line-height:28px; font-family:'微软雅黑';width: 700px;\">");
			sb.append("<p style='margin:0; padding:8px 0;'><strong>尊敬的用户您好:</strong></p>");
			sb.append("<p style='margin:0; padding:8px 0;'>您的验证码： <strong style='color:#c00'>"+code+"</strong> 与邮箱绑定后，可以用邮箱地址直接登录</p>");
			sb.append("<p style='margin:0; padding:8px 0;' align='right'>中华阿胶网<br>");
			sb.append("<span style='border-bottom:1px dashed #ccc;' t='5' times=' 11:08'>通讯地址： 山东省济南市历下区泉城路世茂国际A座1010室</span> <br>");
			//手机号码 qq邮箱会把它处理，标签撑破了
			//sb.append("<span style='border-bottom:1px dashed #ccc;' t='5' times=' 11:08'>联系电话：400-800-0815</span> ");
			sb.append("</p>");
			sb.append("</div>");
			message.setContent(sb.toString());
			emailService.inform(message);
			request.getSession().setAttribute(email, new Date());
			System.out.println("-----------------"+"您的验证码是:"+code);
			return Result.ok();
		}
		return Result.fail("发送过于频繁,请稍后再试");
	}
	
	/**
	 * 验证发送间隔是否超过1分钟
	 * @param session
	 * @return
	 * 2015年12月8日 qxs
	 */
	private boolean checkSendTime(HttpSession session, String email){
		Object objLastTime = session.getAttribute(email);
		if(objLastTime!=null && objLastTime instanceof Date){
			Date lastTime = (Date)objLastTime;
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(lastTime);
			calendar.add(Calendar.MINUTE, 1);
			if(calendar.after(Calendar.getInstance())){
				return false;
			}
		}
		return true;
	}
	
	
	/**
	 * 发送验证链接到用户输入的邮箱
	 * @param request
	 * @param email
	 * @return
	 * 2015年11月11日 qxs
	 * @throws Exception 
	 *//*
	@AuthBeforeOperation
	@RequestMapping(value="/email-link",method=RequestMethod.PUT)
	public Result checkCodeAndSendEamil(HttpServletRequest request,@RequestBody String email) throws Exception{
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
	
	*//**
	 * 发送绑定邮箱的链接 点击链接验证后绑定
	 * 链接地址 http://ip(:port)?d=1&u=2&m=3
	 * d: uid
	 * u: email+"~"+邮件发送时间 base64编码后字符
	 * m: uid+"_"+username MD5加密后字符串
	 * @param request
	 * @param email
	 * @return
	 * @throws Exception 
	 *//*
	private  Result sendLinkToEmail(HttpServletRequest request,Users user, String email) throws Exception{
		
		String basePath = EtradeUtil.getBasePath(request);
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
				+ "api/user/ckemail-link.htm?d="+ user.getID()
				+ "&u=" + verifyCode + "&m=" + msg1Md5;
		//编辑邮箱内容
		StringBuffer content = new StringBuffer();
		content.append("验证链接:"+url+"\r\n");
		content.append("有效期:"+(SystemConfigProperties.getLong(SystemConfigProperties.EMAIL_VERIFY_VALID_TIME)/1000/60/60)+"小时");
		
		Message emailMessage = new Message();
		emailMessage.setTitle("用户验证");
		emailMessage.setContent(content.toString());
		emailMessage.getReceiveUser().setEmail(email);
		emailService.inform(emailMessage);
		return Result.ok();
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
	 
	@AuthBeforeOperation
	@RequestMapping(value="/ckemail-link",method=RequestMethod.GET)
	public ModelAndView checkEmailLink(HttpServletRequest request,
			String m, String u, String d) throws DataRefreshingException{
		Optional<Users> userOption = userService.getByUid(d);
		Users users = userOption.get();
		int result = 1;
		if(!MD5.digest(users.getID()+"_"+users.getUsername()).equals(m)){
//			return Result.fail("链接格式错误");
			result = 2;
		}
		//emial~time(long)
		String[] emailTime = new String(Base64.decodeBase64(u)).split("~");
		if(emailTime.length!=2){
//			return Result.fail("链接格式错误");
			result = 2;
		}
		String email = emailTime[0];
		long sendtime = Long.valueOf(emailTime[1]);
		if(new Date(sendtime+SystemConfigProperties.getLong(SystemConfigProperties.EMAIL_VERIFY_VALID_TIME)).before(new Date())){
//			return Result.fail("链接已失效");
			result = 3;
		}
		if(userService.getByEmail(email).isPresent()){
//			return Result.fail("该邮箱已被使用");
			result = 4;
		}
		if(result == 1 ){
			//修改用户邮箱
			Users userUpdate = new Users();
			userUpdate.setID(users.getID());
			userUpdate.setEmail(email);
			userService.refresh(userUpdate);
		}
		return new ModelAndView( "redirect:"+SystemConfigProperties.getString(SystemConfigProperties.ROOT_WEB_URL)+"user-center/verify-email?rst="+result);
	}
	
	
	
	*/
	
}
