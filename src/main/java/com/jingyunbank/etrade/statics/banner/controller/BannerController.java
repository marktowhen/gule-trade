package com.jingyunbank.etrade.statics.banner.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.KeyGen;
import com.jingyunbank.core.Range;
import com.jingyunbank.core.Result;
import com.jingyunbank.etrade.api.statics.banner.bo.Banner;
import com.jingyunbank.etrade.api.statics.banner.service.IBannerService;
import com.jingyunbank.etrade.statics.banner.bean.BannerVO;

@RestController
@RequestMapping("/api/banner")
public class BannerController {
	
	@Autowired
	private IBannerService bannerService;
	
	@RequestMapping(value="/", method=RequestMethod.POST)
	public Result<String> save(@RequestBody @Valid BannerVO vo,BindingResult valid) throws Exception{
		if(valid.hasErrors()){
			List<ObjectError> errors = valid.getAllErrors();
			return Result.fail(errors.stream()
						.map(oe -> Arrays.asList(oe.getDefaultMessage()).toString())
						.collect(Collectors.joining(" ; ")));
		}
		Banner banner = new Banner();
		BeanUtils.copyProperties(vo, banner);
		banner.setID(KeyGen.uuid());
		banner.setValid(true);
		bannerService.save(banner);
		return Result.ok();
	}
	
	@RequestMapping(value="/{ID}", method=RequestMethod.PUT)
	public Result<String> refresh(@RequestBody @Valid BannerVO vo,@PathVariable String ID,BindingResult valid) throws Exception{
		if(valid.hasErrors()){
			List<ObjectError> errors = valid.getAllErrors();
			return Result.fail(errors.stream()
						.map(oe -> Arrays.asList(oe.getDefaultMessage()).toString())
						.collect(Collectors.joining(" ; ")));
		}
		
		Banner banner = new Banner();
		BeanUtils.copyProperties(vo, banner);
		banner.setValid(true);
		banner.setID(ID);
		bannerService.refresh(banner);
		return Result.ok();
	}
	
	@RequestMapping(value="/{ID}", method=RequestMethod.DELETE)
	public Result<String> remove(@PathVariable String ID) throws Exception{
		bannerService.remove(ID);
		return Result.ok();
	}
	
	@RequestMapping(value="/list/{from}/{size}", method=RequestMethod.GET)
	public Result<List<BannerVO>> list(@RequestParam(required=false) String type,@PathVariable long from,@PathVariable long size) throws Exception{
		Range range = new Range();
		range.setFrom(from);
		range.setTo(size+from);
		return Result.ok(bannerService.list(type, range).stream().map( bo ->{
			BannerVO vo = new BannerVO();
			BeanUtils.copyProperties(bo, vo);
			return vo;
		}).collect(Collectors.toList()));
	}
	
	@RequestMapping(value="/list", method=RequestMethod.GET)
	public Result<List<BannerVO>> list(@RequestParam String type) throws Exception{
		return Result.ok(bannerService.list(type).stream().map( bo ->{
			BannerVO vo = new BannerVO();
			BeanUtils.copyProperties(bo, vo);
			return vo;
		}).collect(Collectors.toList()));
	}
	
	@RequestMapping(value="/count", method=RequestMethod.GET)
	public Result<Integer> count(@RequestParam(required=false) String type) throws Exception{
		
		return Result.ok(bannerService.count(type));
	}
	
	@RequestMapping(value="/{ID}", method=RequestMethod.GET)
	public Result<BannerVO> single(@PathVariable String ID) throws Exception{
		Banner bo = bannerService.single(ID);
		if(Objects.nonNull(bo)){
			BannerVO vo = new BannerVO();
			BeanUtils.copyProperties(bo, vo);
			return Result.ok(vo);
		}
		return Result.fail("未找到");
	}
	
	

}
