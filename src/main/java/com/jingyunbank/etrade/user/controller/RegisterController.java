package com.jingyunbank.etrade.user.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.KeyGen;
import com.jingyunbank.core.Result;
import com.jingyunbank.core.web.ServletBox;
import com.jingyunbank.etrade.api.cart.bo.Cart;
import com.jingyunbank.etrade.api.cart.service.ICartService;
import com.jingyunbank.etrade.api.user.bo.UserInfo;
import com.jingyunbank.etrade.api.user.bo.Users;
import com.jingyunbank.etrade.api.user.service.IEmployeeService;
import com.jingyunbank.etrade.api.user.service.IUserService;
import com.jingyunbank.etrade.message.controller.SMSController;
import com.jingyunbank.etrade.user.bean.UserVO;

@RestController
public class RegisterController {
 	@Autowired
	private IUserService userService;
 	@Autowired
 	private ICartService cartService;
 	@Autowired
 	private IEmployeeService employeeService;
 	
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
	@RequestMapping(value="/api/register",method=RequestMethod.PUT)
	public Result<String> registerCheckCode(@Valid @RequestBody UserVO userVO,BindingResult valid,HttpServletRequest request, HttpSession session,HttpServletResponse response) throws Exception{
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
		Result<String> checkResult = null;
		if(StringUtils.isEmpty(userVO.getMobile())&&StringUtils.isEmpty(userVO.getEmail())){
			return Result.fail("邮箱和手机号至少有一个不为空");
		}
		//验证手机号是否存在
		if(!StringUtils.isEmpty(userVO.getMobile())){
			if(userService.exists(userVO.getMobile())){
				return Result.fail("该手机号已存在。");
			}
			checkResult = SMSController.checkCode(userVO.getCode(), request, ServletBox.SMS_CODE_KEY_IN_SESSION);
		}
		//验证邮箱是否存在
		if(!StringUtils.isEmpty(userVO.getEmail())){
			if(userService.exists(userVO.getEmail())){
				return Result.fail("该邮箱已存在");
			}
			checkResult = SMSController.checkCode(userVO.getCode(), request, ServletBox.EMAIL_CODE_KEY_IN_SESSION);
		}
		//保存用户信息和个人资料信息
		if(checkResult.isOk()){
			Users users=new Users();
			BeanUtils.copyProperties(userVO, users);
			users.setID(KeyGen.uuid());//generate uid here to make view visible
			
			UserInfo userInfo=new UserInfo();
			userInfo.setRegip(ServletBox.ip(request));
			userService.save(users, userInfo);
			//成功后操作
			
			Optional<Cart> candidatecart = cartService.singleCart(users.getID());
			String cartID = null;
			if(candidatecart.isPresent()){
				cartID = candidatecart.get().getID();
			}
			boolean isemployee = employeeService.isEmployee(users.getMobile());
			//用户信息
			LoginController.loginSuccess(users.getID(), users.getUsername(), cartID, isemployee, session, response);
			return	Result.ok("注册信息成功");
		}
		
		return	checkResult;
	}
	
	
}
