package com.jingyunbank.etrade.information.controller;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
import com.jingyunbank.etrade.api.information.bo.InformationDetails;
import com.jingyunbank.etrade.api.information.service.IInformationDetailsService;
import com.jingyunbank.etrade.information.bean.InformationDetailsVO;
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
	public Result<InformationDetailsVO> saveDetails(HttpServletRequest request,HttpSession session,@RequestBody InformationDetailsVO informationDetailsVO) throws Exception{
		
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
	public Result deleteDetails(@PathVariable String id,HttpServletRequest request,HttpSession session) throws Exception{
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
	@ResponseBody
	public Result updateDetsils(InformationDetailsVO informationDetailsVO,HttpServletRequest request,HttpSession session) throws Exception{
		InformationDetails informationDetails=new InformationDetails();
		BeanUtils.copyProperties(informationDetailsVO, informationDetails);
		if(informationDetailsService.refresh(informationDetails)){
			return Result.ok(informationDetailsVO);
		}
		return Result.fail("修改失败");
		
	}
	/**
	 * 通过sid查询出对应的多个内容信息
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
		return Result.ok(informationDetailsService.getDeailsBySiteid(sid,range)
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
	@ResponseBody
	public Result selectDetailByid(@PathVariable String id) throws Exception{
	Optional<InformationDetails> informationDetails=informationDetailsService.getDetailByid(id);
	InformationDetails adviceDetails=informationDetails.get();
	InformationDetailsVO adviceDetailsVO=new InformationDetailsVO();
	BeanUtils.copyProperties(adviceDetails, adviceDetailsVO);
		return Result.ok(adviceDetailsVO);
	}
}
