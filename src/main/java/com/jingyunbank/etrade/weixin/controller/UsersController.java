package com.jingyunbank.etrade.weixin.controller;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.Result;
import com.jingyunbank.core.web.AuthBeforeOperation;
import com.jingyunbank.core.web.Login;
import com.jingyunbank.etrade.api.weixin.bo.SNSUserInfoBo;
import com.jingyunbank.etrade.api.weixin.service.IWeiXinUserService;
import com.jingyunbank.etrade.weixin.bean.SNSUserInfoVo;
import com.jingyunbank.etrade.weixin.util.StringUtilss;

@RestController
public class UsersController {

	@Autowired
	private IWeiXinUserService weixinUserService;
	/*@AuthBeforeOperation*/
	@RequestMapping(value="api/get/user/name",method=RequestMethod.GET)
	public Result<SNSUserInfoVo> getSingle(HttpServletRequest request,
			HttpServletResponse resp){
		String uid = StringUtilss.getSessionId(request,resp);
		Optional<SNSUserInfoBo> bo=weixinUserService.singles(uid);
		SNSUserInfoVo vo= new SNSUserInfoVo();
		BeanUtils.copyProperties(bo.get(), vo);
		return Result.ok(vo);
		
	}

}
