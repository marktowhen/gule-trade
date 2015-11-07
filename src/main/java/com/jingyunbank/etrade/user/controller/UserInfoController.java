package com.jingyunbank.etrade.user.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.util.StringUtils;
import com.jingyunbank.core.Result;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.exception.DataUpdatingException;
import com.jingyunbank.etrade.api.user.bo.UserInfo;
import com.jingyunbank.etrade.user.bean.UserInfoVO;
import com.jingyunbank.etrade.user.service.UserInfoService;

/**
 * @author Administrator 
 * @date 2015年11月6日
	@todo TODO
 */
@RestController
@RequestMapping("/userInfo")
public class UserInfoController {
	@Autowired
	private UserInfoService userInfoService;
	
	@RequestMapping(value="/add",method=RequestMethod.PUT)
	public Result addUserInfo(HttpSession session,UserInfoVO userInfoVO) throws DataSavingException{
		UserInfo userInfo=new UserInfo();
		BeanUtils.copyProperties(userInfoVO, userInfo);
	
		if(userInfoService.save(userInfo)){
			return Result.ok(userInfoVO);
		}
		
		return Result.fail("该uid已存在");
		
		
	}
	@RequestMapping(value="/update",method=RequestMethod.POST)
	public Result updateUserInfo(HttpSession session,HttpServletRequest request,UserInfoVO userInfoVO) throws DataUpdatingException{
		UserInfo userInfo=new UserInfo();
		BeanUtils.copyProperties(userInfoVO, userInfo);
		if(userInfoVO.getUid().isEmpty()){
		return	Result.fail("uid不存在即不存在，没办法修改");
		}
		userInfoService.update(userInfo);
		return Result.ok(userInfoVO);
	}
}
