package com.jingyunbank.etrade.logistic.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.Result;
import com.jingyunbank.etrade.api.logistic.bo.Postage;
import com.jingyunbank.etrade.api.logistic.service.IPostageService;
import com.jingyunbank.etrade.logistic.PostageVO;

@RestController
public class PostageController {

	@Autowired
	private IPostageService postageService;
	
	
	@RequestMapping(value="/api/logistic/postage/calculation", method=RequestMethod.PUT)
	public Result<List<PostageVO>> calculate(@RequestBody @Valid List<PostageVO> postages, BindingResult valid ) throws Exception{
		if(valid.hasErrors()){
			return Result.fail("您提交的数据有误，请核实后重新提交。");
		}
		List<Postage> postagebo = postages.stream().map(vo -> {
			Postage bo = new Postage();
			BeanUtils.copyProperties(vo, bo);
			return bo;
		}).collect(Collectors.toList());
		
		postagebo = postageService.calculate(postagebo);
		
		postages = postagebo.stream().map(bo -> {
			PostageVO vo = new PostageVO();
			BeanUtils.copyProperties(bo, vo);
			return vo;
		}).collect(Collectors.toList());
		
		return Result.ok(postages);
	}
}
