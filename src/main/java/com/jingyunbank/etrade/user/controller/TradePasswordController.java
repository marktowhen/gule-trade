package com.jingyunbank.etrade.user.controller;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.Result;
import com.jingyunbank.core.web.AuthBeforeOperation;
import com.jingyunbank.core.web.ServletBox;
import com.jingyunbank.etrade.api.user.bo.Users;
import com.jingyunbank.etrade.api.user.service.IUserService;
import com.jingyunbank.etrade.user.bean.UserVO;
@RestController
@RequestMapping("/api/tradepwd")
public class TradePasswordController {

	@Autowired
	private IUserService userService;
	/**
	 * 3修改交易密码
	 * @param userVO
	 * @param session
	 * @return
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/",method=RequestMethod.PUT)
	public Result<UserVO> updateTradePassword(@RequestBody UserVO userVO,HttpSession session,HttpServletRequest request) throws Exception{
		//验证交易密码的有效性
		if(ServletBox.checkIfEmailMobileOK(request.getSession())){
			
			String uid = ServletBox.getLoginUID(request);
			userVO.setID(uid);
			Users users=new Users();
			BeanUtils.copyProperties(userVO, users);
			userService.refresh(users);
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
	@RequestMapping(value="/install/tradepwd",method=RequestMethod.PUT)
	public Result<UserVO> installTradepwd(@RequestBody UserVO userVO,HttpSession session,HttpServletRequest request) throws Exception{
		if(ServletBox.checkIfEmailMobileOK(request.getSession())){
			String uid = ServletBox.getLoginUID(request);
			Optional<Users> optional=userService.single(uid);
			Users users=optional.get();
			/*if(StringUtils.isEmpty(users.getTradepwd())){*/
					userVO.setID(uid);
					BeanUtils.copyProperties(userVO, users);
					userService.refresh(users);
					return Result.ok(userVO);
			/*}*/
		}
		return Result.fail("交易密码已经存在");
	}
	
}
