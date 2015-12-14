package com.jingyunbank.etrade.information.controller;

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
import com.jingyunbank.core.Result;
import com.jingyunbank.etrade.api.information.bo.HelpCenterDetail;
import com.jingyunbank.etrade.api.information.service.IHelpCenterDetailService;
import com.jingyunbank.etrade.information.bean.HelpCenterDetailVO;

@RestController
@RequestMapping("/api/help/center/detail")
public class HelpCenterDetailController {

	@Autowired
	private IHelpCenterDetailService helpCenterDetailService;
	
	/**
	 * 新增
	 * @param request
	 * @param vo
	 * @param valid
	 * @return
	 * @throws Exception
	 * 2015年12月11日 qxs
	 */
	//@AuthBeforeOperation
	@RequestMapping(value="/" , method=RequestMethod.POST)
	public Result<String> save(HttpServletRequest request,@RequestBody @Valid HelpCenterDetailVO vo , BindingResult valid) throws Exception{
		if(valid.hasErrors()){
			List<ObjectError> errors = valid.getAllErrors();
			return Result.fail(errors.stream()
						.map(oe -> Arrays.asList(oe.getDefaultMessage()).toString())
						.collect(Collectors.joining(" ; ")));
		}
		HelpCenterDetail bo = getBoFromVO(vo);
		bo.setValid(true);
		bo.setID(KeyGen.uuid());
		helpCenterDetailService.save(bo);
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
	//@AuthBeforeOperation
	@RequestMapping(value="/{id}" , method=RequestMethod.PUT)
	public Result<String> refresh(HttpServletRequest request,@PathVariable String id,@RequestBody @Valid HelpCenterDetailVO vo , BindingResult valid) throws Exception{
		if(valid.hasErrors()){
			List<ObjectError> errors = valid.getAllErrors();
			return Result.fail(errors.stream()
						.map(oe -> Arrays.asList(oe.getDefaultMessage()).toString())
						.collect(Collectors.joining(" ; ")));
		}
		vo.setID(id);
		helpCenterDetailService.refresh(getBoFromVO(vo));
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
		helpCenterDetailService.remove(id);
		return Result.ok();
	}
	
	/**
	 * 根据类别查看
	 * @return
	 * 2015年12月11日 qxs
	 */
	@RequestMapping(value="/list/{parentID}", method=RequestMethod.GET)
	public Result<List<HelpCenterDetailVO>> listAllValid(@PathVariable String parentID){
		return Result.ok(helpCenterDetailService.listAllValid(parentID).stream()
					.map( bo -> {
						return getVOFromBo(bo);
					}).collect(Collectors.toList()));
	}
	
	/**
	 * 查看所有有效类别
	 * @return
	 * 2015年12月11日 qxs
	 */
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public Result<HelpCenterDetailVO> single(@PathVariable String id){
		Optional<HelpCenterDetail> optional = helpCenterDetailService.single(id);
		if(optional.isPresent()){
			return Result.ok(getVOFromBo(optional.get()));
		}
		return Result.fail("未找到");
	}
	
	
	
	
	private HelpCenterDetailVO getVOFromBo(HelpCenterDetail bo){
		if(bo!=null){
			HelpCenterDetailVO vo = new HelpCenterDetailVO();
			BeanUtils.copyProperties(bo, vo);
			return vo;
		}
		return null;
	}
	
	private HelpCenterDetail getBoFromVO(HelpCenterDetailVO vo){
		if(vo!=null){
			HelpCenterDetail bo = new HelpCenterDetail();
			BeanUtils.copyProperties(vo, bo);
			return bo;
		}
		return null;
	}
	
}
