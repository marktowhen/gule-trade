package com.jingyunbank.etrade.information.controller;

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
import com.jingyunbank.etrade.api.information.bo.InformationSite;
import com.jingyunbank.etrade.api.information.service.IInformationSiteService;
import com.jingyunbank.etrade.information.bean.InformationSiteVO;

@Controller
public class InformationSiteController {
	@Autowired
	private IInformationSiteService informationSiteService;
	
	/**
	 * 插入到advice_site表中的多个标题
	 * @param adviceSiteVO
	 * @param request
	 * @param session
	 * @return
	 * @throws Exception
	 */
	
	@RequestMapping(value="/api/information/site",method=RequestMethod.PUT)
	@ResponseBody
	public Result saveSite(InformationSiteVO informationSiteVO,HttpServletRequest request,HttpSession session) throws Exception{
		informationSiteVO.setID(KeyGen.uuid());;
		InformationSite adviceSite=new InformationSite();
		BeanUtils.copyProperties(informationSiteVO, adviceSite);
		if(informationSiteService.save(adviceSite)){
			return Result.ok(informationSiteVO);
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
	@RequestMapping(value="/api/information/sites/{siteid}",method=RequestMethod.GET)
	@ResponseBody
	public Result selectSitesById(@PathVariable String siteid,HttpServletRequest request,HttpSession session) throws Exception{
		return Result.ok(informationSiteService.getSitesBySiteid(siteid).stream().map(bo ->{
			InformationSiteVO informationSiteVO=new InformationSiteVO();
			BeanUtils.copyProperties(bo, informationSiteVO);
			return informationSiteVO;
		}).collect(Collectors.toList()));
		
	}
}
