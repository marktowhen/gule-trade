package com.jingyunbank.etrade.goods.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.Result;
import com.jingyunbank.etrade.api.goods.bo.Merchant;
import com.jingyunbank.etrade.goods.service.MerchantService;
/**
 * 商家管理控制器
 * @author liug
 *
 */
@RestController
@RequestMapping("/merchant")
public class MerchantController {
	@Resource
	private MerchantService merchantService;
	/**
	 * 推荐商家检索
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping("/recommend")
	public Result recommend(HttpServletRequest request, HttpSession session){
		//转成VO
		List<Merchant> list = merchantService.listMerchants();
		Result r = Result.ok(list);
		return r;
	}
}
