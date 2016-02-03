package com.jingyunbank.etrade.posts.help.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.KeyGen;
import com.jingyunbank.core.Range;
import com.jingyunbank.core.Result;
import com.jingyunbank.etrade.api.posts.help.bo.HelpCenterCategory;
import com.jingyunbank.etrade.api.posts.help.service.IHelpCenterCategoryService;
import com.jingyunbank.etrade.posts.help.bean.HelpCenterCategoryVO;

@RestController
@RequestMapping("/api/information/help/center/category")
public class HelpCenterCategoryController {
	
	@Autowired
	private IHelpCenterCategoryService helpCenterCategoryService;
	
	/**
	 * 新增
	 * @param request
	 * @param vo
	 * @param valid
	 * @return
	 * @throws Exception
	 * 2015年12月11日 qxs
	 */
	//@AuthBeforeOperation(role={Role.MANAGER_CODE},name={Role.MANAGER_NAME})
	@RequestMapping(value="/" , method=RequestMethod.POST)
	public Result<String> save(HttpServletRequest request, @RequestBody @Valid HelpCenterCategoryVO vo , BindingResult valid) throws Exception{
		if(valid.hasErrors()){
			List<ObjectError> errors = valid.getAllErrors();
			return Result.fail(errors.stream()
						.map(oe -> Arrays.asList(oe.getDefaultMessage()).toString())
						.collect(Collectors.joining(" ; ")));
		}
		HelpCenterCategory bo = getBoFromVO(vo);
		bo.setID(KeyGen.uuid());
		bo.setValid(true);
		helpCenterCategoryService.save(bo);
		return Result.ok();
	}
	
	/**
	 * 修改
	 * @param request
	 * @param id
	 * @param vo
	 * @param valid
	 * @return
	 * @throws Exception
	 * 2015年12月11日 qxs
	 */
	//@AuthBeforeOperation(role={Role.MANAGER_CODE},name={Role.MANAGER_NAME})
	@RequestMapping(value="/{id}" , method=RequestMethod.PUT)
	public Result<String> refresh(HttpServletRequest request,@PathVariable String id,@RequestBody @Valid HelpCenterCategoryVO vo , BindingResult valid) throws Exception{
		if(valid.hasErrors()){
			List<ObjectError> errors = valid.getAllErrors();
			return Result.fail(errors.stream()
						.map(oe -> Arrays.asList(oe.getDefaultMessage()).toString())
						.collect(Collectors.joining(" ; ")));
		}
		vo.setID(id);
		helpCenterCategoryService.refresh(getBoFromVO(vo));
		return Result.ok();
	}
	
	/**
	 * 删除
	 * @param request
	 * @param id
	 * @return
	 * @throws Exception
	 * 2015年12月11日 qxs
	 */
	//@AuthBeforeOperation
	@RequestMapping(value="/{id}" , method=RequestMethod.DELETE)
	public Result<String> remove(HttpServletRequest request,@PathVariable String id) throws Exception{
		helpCenterCategoryService.remove(id);
		return Result.ok();
	}
	
	/**
	 * 查看所有有效类别
	 * @return
	 * 2015年12月11日 qxs
	 */
	@RequestMapping(value="/list", method=RequestMethod.GET)
	public Result<List<HelpCenterCategoryVO>> listAllValid(){
		return Result.ok(helpCenterCategoryService.listAllValid().stream()
					.map( bo -> {
						return getVOFromBo(bo);
					}).collect(Collectors.toList()));
	}
	
	/**
	 * 查看有效类别
	 * @return
	 * 2015年12月11日 qxs
	 */
	@RequestMapping(value="/list/{offset}/{size}", method=RequestMethod.GET)
	public Result<List<HelpCenterCategoryVO>> listAllValidPage(@PathVariable long offset, @PathVariable long size){
		Range range = new Range();
		range.setFrom(offset);
		range.setTo(offset + size);
		return Result.ok(helpCenterCategoryService.listAllValid(range).stream()
					.map( bo -> {
						return getVOFromBo(bo);
					}).collect(Collectors.toList()));
	}
	
	/**
	 * 查看单个
	 * @return
	 * 2015年12月11日 qxs
	 */
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public Result<HelpCenterCategoryVO> single(@PathVariable String id){
		Optional<HelpCenterCategory> optional = helpCenterCategoryService.single(id);
		if(optional.isPresent()){
			return Result.ok(getVOFromBo(optional.get()));
		}
		return Result.fail("数据未找到,请确认链接是否正确");
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
