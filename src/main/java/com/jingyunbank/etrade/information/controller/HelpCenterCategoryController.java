package com.jingyunbank.etrade.information.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.KeyGen;
import com.jingyunbank.core.Result;
import com.jingyunbank.etrade.api.information.bo.HelpCenterCategory;
import com.jingyunbank.etrade.api.information.service.IHelpCenterCategoryService;
import com.jingyunbank.etrade.area.bean.CityVO;
import com.jingyunbank.etrade.information.bean.HelpCenterCategoryVO;

@RestController
@RequestMapping("/api/help/center")
public class HelpCenterCategoryController {
	
	@Autowired
	private IHelpCenterCategoryService helpCenterCategoryService;
	
	
	public Result<String> save(HttpServletRequest request, @Valid HelpCenterCategoryVO vo , BindingResult valid) throws Exception{
		if(valid.hasErrors()){
			List<ObjectError> errors = valid.getAllErrors();
			return Result.fail(errors.stream()
						.map(oe -> Arrays.asList(oe.getDefaultMessage()).toString())
						.collect(Collectors.joining(" ; ")));
		}
		HelpCenterCategory bo = getBoFromVO(vo);
		bo.setID(KeyGen.uuid());
		helpCenterCategoryService.save(bo);
		return Result.ok();
	}
	
	
	
	private HelpCenterCategoryVO getVOFromBo(HelpCenterCategory bo){
		if(bo!=null){
			HelpCenterCategoryVO vo = new HelpCenterCategoryVO();
			BeanUtils.copyProperties(bo, vo);
			return vo;
		}
		return null;
	}
	
	private HelpCenterCategory getBoFromVO(HelpCenterCategoryVO vo){
		if(vo!=null){
			HelpCenterCategory bo = new HelpCenterCategory();
			BeanUtils.copyProperties(vo, bo);
			return bo;
		}
		return null;
	}
	

}
