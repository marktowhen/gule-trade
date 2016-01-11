package com.jingyunbank.etrade.user.controller;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.Result;
import com.jingyunbank.core.web.AuthBeforeOperation;
import com.jingyunbank.core.web.Login;
import com.jingyunbank.core.web.ServletBox;
import com.jingyunbank.etrade.api.user.bo.Manager;
import com.jingyunbank.etrade.api.user.bo.Seller;
import com.jingyunbank.etrade.api.user.bo.Users;
import com.jingyunbank.etrade.api.user.service.IManagerService;
import com.jingyunbank.etrade.api.user.service.ISellerService;
import com.jingyunbank.etrade.api.user.service.IUserService;
import com.jingyunbank.etrade.user.bean.PasswordVO;
import com.jingyunbank.etrade.user.bean.UserVO;

@RestController
@RequestMapping("/api/pwd")
public class PasswordController {
	@Autowired
	private IUserService userService;
	@Autowired
	private IManagerService managerService;
	@Autowired
	private ISellerService sellerService;
	

	/**
	 * 2修改登录密码
	 * @param userVO
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/password",method=RequestMethod.PUT)
	public Result<UserVO> updatePassword(@RequestBody UserVO userVO,HttpSession session,HttpServletRequest request) throws Exception{
		if(ServletBox.checkIfEmailMobileOK(request.getSession())){
			String uid = Login.UID(request);
			userVO.setID(uid);
			Users users=new Users();
			BeanUtils.copyProperties(userVO, users);
			userService.refresh(users);
			return Result.ok(userVO);
			
		}
		return Result.fail("修改登录密码失败");
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
	@RequestMapping(value="/forgetpwd/checkcode",method=RequestMethod.PUT)
	public Result<String> forgetpwdCheck(HttpServletRequest request, HttpSession session,@RequestParam("key") String key, @RequestParam("password") String password) throws Exception{
		if(ServletBox.checkIfEmailMobileOK(request.getSession())){
			Optional<Users> usersOptionals = userService.singleByKey(key);
			Users users=usersOptionals.get();
			users.setPassword(password);
			userService.refresh(users);
			return Result.ok("修改成功");
		
		}
		return Result.fail("修改未成功");
		
	}
	
	/**
	 * 修改管理员登录密码
	 * @param userVO
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/manager",method=RequestMethod.PUT)
	public Result<String> refreshManagerPassword(@RequestBody PasswordVO pwdVO ,HttpServletRequest request) throws Exception{
		String uid = Login.UID(request);
		Optional<Manager> optional = managerService.singleByID(uid);
		if(optional.isPresent()){
			if(optional.get().getPassword().equals(pwdVO.getOldPwd())){
				managerService.refreshPassword(uid, pwdVO.getNewPwd());
				return Result.ok();
			}
			return Result.fail("原密码错误");
		}
		return Result.fail("修改登录密码失败");
	}
	
	/**
	 * 修改卖家登录密码
	 * @param userVO
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/seller",method=RequestMethod.PUT)
	public Result<String> refreshSellerPassword(@RequestBody PasswordVO pwdVO , HttpServletRequest request) throws Exception{
		String uid = Login.UID(request);
		Optional<Seller> optional = sellerService.singleByID(uid);
		if(optional.isPresent()){
			if(optional.get().getPassword().equals(pwdVO.getOldPwd())){
				sellerService.refreshPassword(uid, pwdVO.getNewPwd());
				return Result.ok();
			}
			return Result.fail("原密码错误");
		}
		return Result.fail("修改登录密码失败");
	}
	
	
}
