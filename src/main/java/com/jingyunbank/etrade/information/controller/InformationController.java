package com.jingyunbank.etrade.information.controller;

import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jingyunbank.core.KeyGen;
import com.jingyunbank.core.Result;
import com.jingyunbank.etrade.api.information.bo.Information;
import com.jingyunbank.etrade.api.information.service.IInformationService;
import com.jingyunbank.etrade.information.bean.InformationVO;
@Controller
public class InformationController {
	@Autowired
	private IInformationService informationService;
	
	@RequestMapping(value="/api/information/save",method=RequestMethod.PUT)
	@ResponseBody
	public Result saveInformation(InformationVO informationVO,HttpServletRequest request,HttpSession session) throws Exception{
		informationVO.setID(KeyGen.uuid());
		Information information=new Information();
		BeanUtils.copyProperties(informationVO, information);
		if(informationService.save(information)){
			return Result.ok(informationVO);
		}
		return Result.fail("请检查重试");
		
	}
	@RequestMapping(value="/api/information/gets",method=RequestMethod.GET)
	@ResponseBody
	public Result getList(HttpServletRequest request,HttpSession session){
		return Result.ok(informationService.getInformation().stream().map(bo ->{
			InformationVO informationVO=new InformationVO();
			BeanUtils.copyProperties(bo, informationVO);
			return informationVO;
		}).collect(Collectors.toList()));
		
	}
}
