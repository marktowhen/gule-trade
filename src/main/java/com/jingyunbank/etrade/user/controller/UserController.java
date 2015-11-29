package com.jingyunbank.etrade.user.controller;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.jingyunbank.core.Result;
import com.jingyunbank.core.util.MD5;
import com.jingyunbank.core.web.AuthBeforeOperation;
import com.jingyunbank.core.web.ServletBox;
import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.message.service.context.ISyncNotifyService;
import com.jingyunbank.etrade.api.user.bo.Users;
import com.jingyunbank.etrade.api.user.service.IUserInfoService;
import com.jingyunbank.etrade.api.user.service.IUserService;
import com.jingyunbank.etrade.message.controller.SMSController;
import com.jingyunbank.etrade.user.bean.UserVO;
@RestController
@RequestMapping("/api/user")
public class UserController {
  	@Autowired
	private IUserService userService;
	@Resource
	private ISyncNotifyService emailService;
	@Resource
	private ISyncNotifyService smsService;
  	
	
	public static final String MOBILE_CODE_CHECK_DATE="MOBILE_CODE_CHECK_DATE";
	public static final String EMAIL_MESSAGE = "EMAIL_MESSAGE";
	
	/**
	 * 通过id查出对应的对象
	 * @param userVO
	 * @param request
	 * @return// get /api/user/current
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/current",method=RequestMethod.GET)
	public Result selectPhone(UserVO userVO,HttpServletRequest request){
		String id = ServletBox.getLoginUID(request);
		Users users=userService.getByUID(id).get();
		BeanUtils.copyProperties(users, userVO);
		return Result.ok(userVO);
	}




	//修改手机号的操作
	/**验证
	 * 1更换手机之后的验证码是否输入正确呢
	 * @param userVO
	 * @param valid
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/phone",method=RequestMethod.PUT)
	public Result checkCodeUpdatePhone(@RequestParam("mobile") String mobile, @RequestParam("code") String code,HttpServletRequest request, HttpSession session) throws Exception{
			/*if(effectiveTime(session)){*/
				String uid = ServletBox.getLoginUID(request);
				Users users=new Users();
				UserVO userVO=new UserVO();
				userVO.setMobile(mobile);
				userVO.setID(uid);
				BeanUtils.copyProperties(userVO, users);
				if(userService.refresh(users)){
					return Result.ok(userVO);
				}
			/*}*/
			
		return Result.fail("手机或验证码不一致,");
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
	//get /api/user?key=139888888888
	///get /api/user?key=email/phone/username
	@RequestMapping(value="/",method=RequestMethod.GET)
	public Result getUserByKey(HttpServletRequest request, HttpSession session,String key) throws Exception{
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
	
	
	
	
	
	
	
	
	//------------------------------验证邮箱end-----------------------------------------------
	
	
	//------------------------------qxs 验证手机  start-----------------------------------------------
	//1、发送邮箱验证码
	

	//3、
	
	
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
	
	private boolean effectiveTime(HttpSession session){
		boolean flag=false;
		Calendar now=Calendar.getInstance();
		Object sessionDate=session.getAttribute(SMSController.MOBILE_CODE_CHECK_DATE);
		if(sessionDate!=null && sessionDate instanceof Date ){
			Calendar checkDate  = Calendar.getInstance();
			checkDate.setTime((Date)sessionDate);
			//+2
			checkDate.add(2, Calendar.MINUTE);
			if(checkDate.before(now)){
				return flag=true;
			}
		}
		return flag;
		
	}

	
	
}
