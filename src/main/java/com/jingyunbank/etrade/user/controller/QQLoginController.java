package com.jingyunbank.etrade.user.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jingyunbank.core.KeyGen;
import com.jingyunbank.core.Result;
import com.jingyunbank.core.web.ServletBox;
import com.jingyunbank.etrade.api.cart.bo.Cart;
import com.jingyunbank.etrade.api.cart.service.ICartService;
import com.jingyunbank.etrade.api.user.bo.QQLogin;
import com.jingyunbank.etrade.api.user.bo.UserInfo;
import com.jingyunbank.etrade.api.user.bo.Users;
import com.jingyunbank.etrade.api.user.service.IEmployeeService;
import com.jingyunbank.etrade.api.user.service.IQQLoginService;
import com.jingyunbank.etrade.api.user.service.IUserService;
import com.jingyunbank.etrade.message.controller.SMSController;
import com.jingyunbank.etrade.user.bean.ThirdLoginBindVO;
import com.jingyunbank.etrade.user.bean.ThirdLoginRegistVO;
import com.jingyunbank.etrade.user.bean.UserVO;

@RestController
@RequestMapping("/api")
public class QQLoginController {
	
	@Autowired
	private IQQLoginService qqLoginService;
	@Autowired
	private IUserService userService;
	@Autowired
	private ICartService cartService;
	@Autowired
	private IEmployeeService employeeService;

	/**
	 * 根据qq返回的身份认证的code 请求qq接口获取用户的唯一标示
	 *  如果在系统中已绑定直接登录 否则跳到绑定页
	 * @param session
	 * @param response
	 * @param code
	 * @throws Exception
	 * 2016年1月27日 qxs
	 */
	@RequestMapping(value = "/login/qq/code", method = RequestMethod.GET)
	public void changeState(HttpSession session, HttpServletResponse response, @RequestParam("code") String code) throws Exception{
		//根据code获取token
		String token = null;
		//根据token获取openID
		String openID = null;
		HashMap<String, Object> qqUserInfo = null;
		try {
			token = getTokenByCode(code);
			openID = getOpenIDByToken(token);
			qqUserInfo = getUserinfoByToken(token, openID);
		} catch (Exception e) {
			response.sendRedirect(QQLogin.REDIRECT_URL+"#/404");
			return;
		}
		//查询是否有对应用户
		QQLogin single = qqLoginService.single(openID);
		if(single!=null){
			//有的话刷新token 登录
			Optional<Users> usersOptional = userService.single(single.getUID());
			if(usersOptional.isPresent()){
				//3、成功之后
				//用户信息放入session 写入cookie
				Users users = usersOptional.get();
				Optional<Cart> candidatecart = cartService.singleCart(users.getID());
				String cartID = null;
				if(candidatecart.isPresent()){
					cartID = candidatecart.get().getID();
				}
				boolean isemployee = employeeService.isEmployee(users.getMobile());
				//用户信息
				LoginController.loginSuccess(users.getID(), users.getUsername(),cartID, isemployee, session, response);
				//跳到成功页面
				response.sendRedirect(QQLogin.REDIRECT_URL);
			}else{
				response.sendRedirect(QQLogin.REDIRECT_URL);
			}
		}else{
			//没有的话跳到第三方登录绑定页面
			response.sendRedirect(QQLogin.REDIRECT_URL+"#/login/bind?type=qq&key="+openID+"&token="+token+"&nickname="+URLEncoder.encode(qqUserInfo.get("nickname").toString(),"utf-8")+"&picture="+qqUserInfo.get("figureurl_qq_1"));
		}
		
		
		
	}
	
	/**
	 * qq绑定已有账号
	 * @param user
	 * @param valid
	 * @param session
	 * @param response
	 * @return
	 * @throws Exception
	 * 2016年1月27日 qxs
	 */
	@RequestMapping(value="/login/bind/qq",method=RequestMethod.POST,
			consumes="application/json;charset=UTF-8")
	public Result<UserVO> qqBind(@Valid @RequestBody ThirdLoginBindVO user, 
					BindingResult valid, HttpSession session,
					HttpServletResponse response) throws Exception{
		if(valid.hasErrors()){
			return Result.fail("用户名或者密码错误！");
		}
		
		//2、根据用户名/手机号/邮箱查询用户信息
		Optional<Users> usersOptional =  userService.singleByKey(user.getLoginKey());
		//是否存在该用户
		if(usersOptional.isPresent()){
			Users users = usersOptional.get();
			//密码是否正确
			if(!users.getPassword().equals(user.getLoginPassword())){
				return Result.fail("用户名或者密码错误！");
			}
		}else{
			return Result.fail("用户名或者密码错误！");
		}
		//3、成功之后
		//进行绑定
		QQLogin qqLogin = qqLoginService.single(user.getThirdLoginKey());
		if(qqLogin!=null){
			//刷新绑定信息
			qqLogin.setAccessToken(user.getAccessToken());
			qqLogin.setUID(usersOptional.get().getID());
			qqLoginService.refreshByID(qqLogin.getID(),user.getAccessToken(),usersOptional.get().getID());
		}else{
			qqLogin = new QQLogin();
			qqLogin.setAccessToken(user.getAccessToken());
			qqLogin.setID(user.getThirdLoginKey());
			qqLogin.setUID(usersOptional.get().getID());
			qqLoginService.save(qqLogin);
		}
		//用户信息放入session 写入cookie
		Users users = usersOptional.get();
		Optional<Cart> candidatecart = cartService.singleCart(users.getID());
		String cartID = null;
		if(candidatecart.isPresent()){
			cartID = candidatecart.get().getID();
		}
		
		boolean isemployee = employeeService.isEmployee(users.getMobile());
		//用户信息
		LoginController.loginSuccess(users.getID(), users.getUsername(),cartID, isemployee, session, response);
		
		
		UserVO vo = new UserVO();
		BeanUtils.copyProperties(users, vo);
		return Result.ok(vo);
	}
	
	/**
	 * qq注册
	 * @param userVO
	 * @param valid
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * 2016年1月21日 qxs
	 */
	@RequestMapping(value="/register/qq",method=RequestMethod.POST)
	public Result<String> qqRegist(@Valid @RequestBody ThirdLoginRegistVO userVO,BindingResult valid,HttpServletRequest request,HttpServletResponse response) throws Exception{
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
		if(userService.exists(userVO.getMobile())){
			return Result.fail("该手机号已存在。");
		}
		Result<String> checkResult = ServletBox.checkCaptcha(userVO.getCode(), ServletBox.SMS_CODE_KEY_IN_SESSION, request);
		//保存用户信息和个人资料信息
		if(checkResult.isOk()){
			Users users=new Users();
			BeanUtils.copyProperties(userVO, users);
			users.setID(KeyGen.uuid());//generate uid here to make view visible
			
			UserInfo userInfo=new UserInfo();
			userInfo.setRegip(ServletBox.ip(request));
			QQLogin qq = new QQLogin();
			BeanUtils.copyProperties(userVO, qq);
			qq.setID(userVO.getThirdLoginKey());
			qq.setUID(users.getID());
			qq.setAccessToken(userVO.getAccessToken());
			qqLoginService.save(qq, users, userInfo);
			//成功之后
			Optional<Cart> candidatecart = cartService.singleCart(users.getID());
			String cartID = null;
			if(candidatecart.isPresent()){
				cartID = candidatecart.get().getID();
			}
			boolean isemployee = employeeService.isEmployee(users.getMobile());
			//用户信息
			LoginController.loginSuccess(users.getID(), users.getUsername(),cartID, isemployee, request.getSession(), response);
			return	Result.ok("注册信息成功");
		}
		return checkResult;
	}
	
	

	private String getOpenIDByToken(String token) throws Exception {
		String url = "https://graph.qq.com/oauth2.0/me?access_token=:ACCESS_TOKEN";
		url = url.replace(":ACCESS_TOKEN", token);
		HttpGet httpGet = new HttpGet(url);
		HttpClient client = HttpClients.createDefault();
		HttpResponse response = client.execute(httpGet);
		HttpEntity entity = response.getEntity();
		if(entity!=null){
			BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));   
			StringBuilder sb = new StringBuilder();   
	        String line = null;   
	        while ((line = reader.readLine()) != null) {   
                sb.append(line + "/n");   
            }
	        
	        Matcher m = Pattern.compile("\"openid\"\\s*:\\s*\"(\\w+)\"").matcher(sb.toString());

	        if (m.find())
	          return m.group(1);
	        else {
	          throw new Exception("server error!");
	        }
		}
		throw new Exception("net error!");
	}

	private String getTokenByCode(String code) throws Exception {
		String url = "https://graph.qq.com/oauth2.0/token?grant_type=authorization_code&client_id=:APP_ID&client_secret=:APP_KEY&code=:AUTHORIZATION_CODE&state=:state&redirect_uri=:REDIRECT_URI";
		url = url.replace(":APP_ID", QQLogin.APP_ID).replace(":APP_KEY", QQLogin.APP_KEY).replace(":AUTHORIZATION_CODE", code).replace(":state", "").replace(":REDIRECT_URI", QQLogin.REDIRECT_URL);
		HttpGet httpGet = new HttpGet(url);
		HttpClient client = HttpClients.createDefault();
		HttpResponse response = client.execute(httpGet);
		HttpEntity entity = response.getEntity();
		if(entity!=null){
			BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));   
			StringBuilder sb = new StringBuilder();   
	        String line = null;   
	        while ((line = reader.readLine()) != null) {   
                sb.append(line + "/n");   
            }
	        
	        String result = sb.toString();
	        String[] split = result.split("&");
	        for (int i = 0; i < split.length; i++) {
				if(split[i].indexOf("access_token")>-1){
					return split[i].split("=")[1];
				}
			}
		}
		throw new Exception("net error!");
	}
	
	private HashMap<String, Object> getUserinfoByToken(String token, String openID) throws Exception {
		String url = "https://graph.qq.com/user/get_user_info?access_token=:ACCESS_TOKEN&oauth_consumer_key=:APP_ID&openid=:OPEN_ID";
		url = url.replace(":ACCESS_TOKEN", token).replace(":APP_ID", QQLogin.APP_ID).replace(":OPEN_ID", openID);
		HttpGet httpGet = new HttpGet(url);
		HttpClient client = HttpClients.createDefault();
		HttpResponse response = client.execute(httpGet);
		HttpEntity entity = response.getEntity();
		if(entity!=null){
			BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));   
			StringBuilder sb = new StringBuilder();   
	        String line = null;   
	        while ((line = reader.readLine()) != null) {   
                sb.append(line);   
            }
	        ObjectMapper obj = new ObjectMapper();
	        @SuppressWarnings("unchecked")
			HashMap<String, Object> readValue = obj.readValue(sb.toString(), HashMap.class);
	        if("0".equals(readValue.get("ret").toString())){
	        	return readValue;
	        }
	        
		}
		throw new Exception("net error!");
	}
}
