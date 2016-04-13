package com.jingyunbank.etrade.logistic.controller;

import java.math.BigDecimal;
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
import com.jingyunbank.etrade.api.logistic.bo.PostageCalculate;
import com.jingyunbank.etrade.api.logistic.service.context.IPostageCalculateService;
import com.jingyunbank.etrade.logistic.bean.PostageCalculateVO;

@RestController
public class PostageController {

	@Autowired
	private IPostageCalculateService postageCalculateService;
	
	
	@RequestMapping(value="/api/logistic/postage/calculation", method=RequestMethod.PUT)
	public Result<BigDecimal> calculate(@RequestBody @Valid List<PostageCalculateVO> postages, BindingResult valid ) throws Exception{
		if(valid.hasErrors()){
			return Result.fail("您提交的数据有误，请核实后重新提交。");
		}
		if(postages.isEmpty()){
			return Result.ok(BigDecimal.ZERO);
		}
		List<PostageCalculate> postagebo = postages.stream().map(vo -> {
			PostageCalculate bo = new PostageCalculate();
			BeanUtils.copyProperties(vo, bo);
			return bo;
		}).collect(Collectors.toList());
		return Result.ok(postageCalculateService.calculateMuti(postagebo, postagebo.get(0).getCity()));
	}
}
