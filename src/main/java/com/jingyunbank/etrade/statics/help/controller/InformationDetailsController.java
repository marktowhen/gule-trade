package com.jingyunbank.etrade.statics.help.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.KeyGen;
import com.jingyunbank.core.Range;
import com.jingyunbank.core.Result;
import com.jingyunbank.etrade.api.statics.help.bo.InformationDetails;
import com.jingyunbank.etrade.api.statics.help.service.IInformationDetailsService;
import com.jingyunbank.etrade.statics.help.bean.InformationDetailsVO;
@RestController
public class InformationDetailsController {
	@Autowired
	private IInformationDetailsService informationDetailsService;
	/**
	 * 保存相应的多个内容
	 * @param request
	 * @param session
	 * @param adviceDetailsVO
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/api/information/savedetails",method=RequestMethod.PUT)
	public Result<InformationDetailsVO> saveDetails(HttpServletRequest request,HttpSession session,@Valid @RequestBody InformationDetailsVO informationDetailsVO,BindingResult valid) throws Exception{
		if(valid.hasErrors()){
			List<ObjectError> errors = valid.getAllErrors();
			return Result.fail(errors.stream()
						.map(oe -> Arrays.asList(oe.getDefaultMessage()).toString())
						.collect(Collectors.joining(" ; ")));
		}
		informationDetailsVO.setID(KeyGen.uuid());;
		informationDetailsVO.setAddtime(new Date());
		InformationDetails informationDetails=new InformationDetails();
		BeanUtils.copyProperties(informationDetailsVO, informationDetails);
		if(informationDetailsService.save(informationDetails)){
			
			return Result.ok(informationDetailsVO);
		}
		return Result.fail("保存失败");
	}
	/**
	 * 通过id删除对应的信息
	 * @param id
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/api/information/delete/{id}",method=RequestMethod.DELETE)
	@ResponseBody
	public Result<String> deleteDetails(@PathVariable String id,HttpServletRequest request,HttpSession session) throws Exception{
		informationDetailsService.remove(id);
		return Result.ok("删除成功");
		
	}
	/**
	 * 通过id修改其内容信息
	 * @param adviceDetailsVO
	 * @param request
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/api/information/update",method=RequestMethod.POST)
	public Result<InformationDetailsVO> updateDetsils(@RequestBody InformationDetailsVO informationDetailsVO,HttpServletRequest request,HttpSession session) throws Exception{
		InformationDetails informationDetails=new InformationDetails();
		BeanUtils.copyProperties(informationDetailsVO, informationDetails);
		if(informationDetailsService.refresh(informationDetails)){
			return Result.ok(informationDetailsVO);
		}
		return Result.fail("修改失败");
		
	}
	/**
	 * 通过sid查询出对应的多个内容信息
	 * (条数有限制)
	 * @param sid
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/api/information/details",method=RequestMethod.GET)
	@ResponseBody
	public Result<List<InformationDetailsVO>> selectDetailBySid(@RequestParam String sid,@RequestParam int from,@RequestParam int size,HttpServletRequest request,HttpSession session) throws Exception{
		Range range = new Range();
		range.setFrom(from);
		range.setTo(from+size);
		return Result.ok(informationDetailsService.list(sid,range)
			.stream().map(bo -> {
			
			InformationDetailsVO vo=new InformationDetailsVO();
			BeanUtils.copyProperties(bo, vo);
			return vo;
		}).collect(Collectors.toList()));
		
	}
	/**
	 * 所有的都查出来
	 * 通过sid查询出对应的多个内容信息
	 * @param sid
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/api/information/anydetails",method=RequestMethod.GET)
	@ResponseBody
	public Result<List<InformationDetailsVO>> selectDetailsBySid(@RequestParam String sid,HttpServletRequest request,HttpSession session) throws Exception{
	
		return Result.ok(informationDetailsService.list(sid)
			.stream().map(bo -> {
			
			InformationDetailsVO vo=new InformationDetailsVO();
			BeanUtils.copyProperties(bo, vo);
			return vo;
		}).collect(Collectors.toList()));
		
	}
	/**
	 * 通过id查询出对应的一条信息
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/api/information/detail/{id}",method=RequestMethod.GET)
	public Result<InformationDetailsVO> selectDetailByid(@PathVariable String id) throws Exception{
	Optional<InformationDetails> informationDetails=informationDetailsService.single(id);
	InformationDetails adviceDetails=informationDetails.get();
	InformationDetailsVO adviceDetailsVO=new InformationDetailsVO();
	BeanUtils.copyProperties(adviceDetails, adviceDetailsVO);
		return Result.ok(adviceDetailsVO);
	}
	/**
	 * 查出添加资讯的所有信息
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/api/information/alldetail",method=RequestMethod.GET)
	public Result<List<InformationDetailsVO>> selectDetails(@RequestParam int from,@RequestParam int size) throws Exception{
		Range range = new Range();
		range.setFrom(from);
		range.setTo(from+size);
		return Result.ok(informationDetailsService.list(range)
				.stream().map(bo -> {
				
				InformationDetailsVO vo=new InformationDetailsVO();
				BeanUtils.copyProperties(bo, vo);
				return vo;
			}).collect(Collectors.toList()));
	}
	@RequestMapping(value="/api/information/byname/detail",method=RequestMethod.GET)
	public Result<List<InformationDetailsVO>> selectDetailsByName(@RequestParam String name,@RequestParam int from,@RequestParam int size) throws Exception{
		Range range = new Range();
		range.setFrom(from);
		range.setTo(from+size);
		return Result.ok(informationDetailsService.listByName(name,range)
				.stream().map(bo -> {
				
				InformationDetailsVO vo=new InformationDetailsVO();
				BeanUtils.copyProperties(bo, vo);
				return vo;
			}).collect(Collectors.toList()));
	}
	@RequestMapping(value="/api/information/detail/orders/{id}",method=RequestMethod.PUT)
	public Result<Integer> getMaxOrders(@PathVariable String id) throws Exception{
		
		return Result.ok(informationDetailsService.selectmaxOrders(id));
		
	}
}
