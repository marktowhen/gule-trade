package com.jingyunbank.etrade.user.controller;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.Result;
import com.jingyunbank.core.web.AuthBeforeOperation;
import com.jingyunbank.core.web.Login;
import com.jingyunbank.etrade.api.user.bo.Seller;
import com.jingyunbank.etrade.api.user.service.ISellerService;
import com.jingyunbank.etrade.user.bean.SellerVO;
@RestController
@RequestMapping("/api/seller")
public class SellerController {

	@Autowired
	private ISellerService sellerService;
	/**
	 * 获得已登录的seller
	 * @param userVO
	 * @param request
	 * @return
	 * 2016年1月9日 qxs
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/current",method=RequestMethod.GET)
	public Result<SellerVO> getCurrentUser(HttpServletRequest request){
		String id = Login.UID(request);
		Optional<Seller> optional = sellerService.singleByID(id);
		if(optional.isPresent()){
			SellerVO vo = new SellerVO();
			BeanUtils.copyProperties(optional.get(), vo);
			return Result.ok(vo);
		}
		return Result.fail("用户不存在或未登录");
	}
	
}
