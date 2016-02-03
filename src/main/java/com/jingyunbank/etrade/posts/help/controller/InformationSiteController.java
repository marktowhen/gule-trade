package com.jingyunbank.etrade.posts.help.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.KeyGen;
import com.jingyunbank.core.Range;
import com.jingyunbank.core.Result;
import com.jingyunbank.etrade.api.posts.help.bo.InformationSite;
import com.jingyunbank.etrade.api.posts.help.service.IInformationSiteService;
import com.jingyunbank.etrade.posts.help.bean.InformationSiteVO;

@RestController
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
	public Result<InformationSiteVO> saveSite(InformationSiteVO informationSiteVO,HttpServletRequest request,HttpSession session) throws Exception{
		informationSiteVO.setID(KeyGen.uuid());;
		InformationSite adviceSite=new InformationSite();
		BeanUtils.copyProperties(informationSiteVO, adviceSite);
		if(informationSiteService.save(adviceSite)){
			return Result.ok(informationSiteVO);
		}
		return Result.fail("保存失败");
		
	}
	@RequestMapping(value="api/information/sites/{siteid}",method=RequestMethod.GET)
	public Result<List<InformationSiteVO>> selectGetSites(@PathVariable String siteid,HttpServletRequest request,HttpSession session){
		
		return Result.ok(informationSiteService.list(siteid).stream().map(bo ->{
			InformationSiteVO vo=new InformationSiteVO();
			BeanUtils.copyProperties(bo, vo);
			return vo;
		}).collect(Collectors.toList()));
		
	}
	/**
	 * 首页的显示（得到name和标题）
	 * @param informationID
	 * @param from
	 * @param size
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/api/information/site/detail",method=RequestMethod.GET)
	public Result<List<InformationSiteVO>> selectGetSite(@RequestParam String informationID,@RequestParam int from,@RequestParam int size,HttpServletRequest request,HttpSession session){
		Range range =new Range();
		range.setFrom(from);
		range.setTo(from+size);
		return Result.ok(informationSiteService.list(informationID, range).stream().map(bo ->{
			InformationSiteVO vo=new InformationSiteVO();
			BeanUtils.copyProperties(bo, vo);
			return vo;
		}).collect(Collectors.toList()));
		
	}
	
}
