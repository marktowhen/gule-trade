package com.jingyunbank.etrade.goods.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.Result;
import com.jingyunbank.etrade.api.goods.bo.Merchant;
import com.jingyunbank.etrade.goods.bean.MerchantVO;
import com.jingyunbank.etrade.goods.service.MerchantService;
/**
 * 商家管理控制器
 * @author liug
 *
 */
@RestController
@RequestMapping("/api/merchant")
public class MerchantController {
	@Resource
	private MerchantService merchantService;
	/**
	 * 推荐商家检索
	 * @param request
	 * @param session
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	@RequestMapping("/recommend/list")
	public Result recommend(HttpServletRequest request, HttpSession session) throws IllegalAccessException, InvocationTargetException{
		//转成VO
		List<Merchant> list = merchantService.listMerchants();
		List<MerchantVO> rlist = new ArrayList<MerchantVO>();
		MerchantVO vo = null;
		for(Merchant bo : list){
			vo = MerchantVO.getInstance();
			BeanUtils.copyProperties(bo,vo);
			rlist.add(vo);
		}
		Result r = Result.ok(list);
		return r;
	}
}
