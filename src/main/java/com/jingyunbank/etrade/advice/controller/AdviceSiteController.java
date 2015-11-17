package com.jingyunbank.etrade.advice.controller;

import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jingyunbank.core.KeyGen;
import com.jingyunbank.core.Result;
import com.jingyunbank.etrade.advice.bean.AdviceSiteVO;
import com.jingyunbank.etrade.api.advice.bo.AdviceSite;
import com.jingyunbank.etrade.api.advice.service.IAdviceSiteService;

@Controller
public class AdviceSiteController {
	@Autowired
	private IAdviceSiteService adviceSiteService;
	
	/**
	 * 插入到advice_site表中的多个标题
	 * @param adviceSiteVO
	 * @param request
	 * @param session
	 * @return
	 * @throws Exception
	 */
	
	@RequestMapping(value="/api/advice/site",method=RequestMethod.PUT)
	@ResponseBody
	public Result saveSite(AdviceSiteVO adviceSiteVO,HttpServletRequest request,HttpSession session) throws Exception{
		adviceSiteVO.setID(KeyGen.uuid());;
		AdviceSite adviceSite=new AdviceSite();
		BeanUtils.copyProperties(adviceSiteVO, adviceSite);
		if(adviceSiteService.save(adviceSite)){
			return Result.ok(adviceSiteVO);
		}
		return Result.fail("保存失败");
		
	}
	/**
	 * 通过siteid查出所有的子集
	 * @param siteid
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/api/advice/sites/{siteid}",method=RequestMethod.GET)
	@ResponseBody
	public Result selectSitesById(@PathVariable String siteid,HttpServletRequest request,HttpSession session){
		return Result.ok(adviceSiteService.getSitesBySiteid(siteid).stream().map(bo ->{
			AdviceSiteVO adviceSiteVO=new AdviceSiteVO();
			BeanUtils.copyProperties(bo, adviceSiteVO);
			return adviceSiteVO;
		}).collect(Collectors.toList()));
		
	}
}
