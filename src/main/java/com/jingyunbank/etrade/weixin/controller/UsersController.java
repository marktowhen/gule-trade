package com.jingyunbank.etrade.weixin.controller;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.Result;
import com.jingyunbank.etrade.api.weixin.bo.SNSUserInfoBo;
import com.jingyunbank.etrade.api.weixin.service.IWeiXinUserService;
import com.jingyunbank.etrade.weixin.bean.SNSUserInfoVo;

@RestController
public class UsersController {

	@Autowired
	private IWeiXinUserService weixinUserService;
	
	@RequestMapping(value="api/get/user/name/{id}",method=RequestMethod.GET)
	public Result<SNSUserInfoVo> getSingle(@PathVariable String id){
		Optional<SNSUserInfoBo> bo=weixinUserService.singles(id);
		SNSUserInfoVo vo= new SNSUserInfoVo();
		BeanUtils.copyProperties(bo.get(), vo);
		return Result.ok(vo);
		
	}

}
