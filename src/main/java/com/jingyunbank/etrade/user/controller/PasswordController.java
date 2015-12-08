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
import com.jingyunbank.core.web.ServletBox;
import com.jingyunbank.etrade.api.user.bo.Users;
import com.jingyunbank.etrade.api.user.service.IUserService;
import com.jingyunbank.etrade.base.util.EtradeUtil;
import com.jingyunbank.etrade.user.bean.UserVO;

@RestController
@RequestMapping("/api/pwd")
public class PasswordController {
	@Autowired
	private IUserService userService;

	public static final String EMAIL_MESSAGE = "EMAIL_MESSAGE";
	
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
		if(EtradeUtil.effectiveTime(request.getSession())){
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
			if(EtradeUtil.effectiveTime(request.getSession())){
				if(password.length()<7||password.length()>20){
					return Result.fail("登录密码必须是8-20位");
				}
				Optional<Users> usersOptionals = userService.getByKey(key);
				Users users=usersOptionals.get();
				users.setPassword(password);
				userService.refresh(users);
				return Result.ok("修改成功");
			
			}
			return Result.fail("修改未成功");
			
		}
	
	
}
