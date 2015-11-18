package com.jingyunbank.etrade.advice.controller;

import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jingyunbank.core.KeyGen;
import com.jingyunbank.core.Result;
import com.jingyunbank.etrade.advice.bean.AdviceDetailsVO;
import com.jingyunbank.etrade.api.advice.bo.AdviceDetails;
import com.jingyunbank.etrade.api.advice.service.IAdviceDetailsService;
@Controller
public class AdviceDetailsController {
	@Autowired
	private IAdviceDetailsService adviceDetailsService;
	/**
	 * 保存相应的多个内容
	 * @param request
	 * @param session
	 * @param adviceDetailsVO
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/api/advice/savedetails",method=RequestMethod.PUT)
	@ResponseBody
	public Result saveDetails(HttpServletRequest request,HttpSession session,AdviceDetailsVO adviceDetailsVO) throws Exception{
		
		
		AdviceDetails adviceDetails=new AdviceDetails();
		adviceDetailsVO.setID(KeyGen.uuid());;
		adviceDetailsVO.setPublish(new Date());
		BeanUtils.copyProperties(adviceDetailsVO, adviceDetails);
		if(adviceDetailsService.save(adviceDetails)){
			return Result.ok(adviceDetailsVO);
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
	@RequestMapping(value="/api/advice/delete/{id}",method=RequestMethod.DELETE)
	@ResponseBody
	public Result deleteDetails(@PathVariable String id,HttpServletRequest request,HttpSession session) throws Exception{
		adviceDetailsService.remove(id);
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
	@RequestMapping(value="/api/advice/update",method=RequestMethod.POST)
	@ResponseBody
	public Result updateDetsils(AdviceDetailsVO adviceDetailsVO,HttpServletRequest request,HttpSession session) throws Exception{
		AdviceDetails adviceDetails=new AdviceDetails();
		BeanUtils.copyProperties(adviceDetailsVO, adviceDetails);
		if(adviceDetailsService.refresh(adviceDetails)){
			return Result.ok(adviceDetailsVO);
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
	@RequestMapping(value="/api/advice/details/{sid}",method=RequestMethod.GET)
	@ResponseBody
	public Result selectDetailBySid(@PathVariable String sid,HttpServletRequest request,HttpSession session) throws Exception{
		return Result.ok(adviceDetailsService.getDeailsBySiteid(sid)
			.stream().map(bo -> {
			AdviceDetailsVO vo=new AdviceDetailsVO();
			BeanUtils.copyProperties(bo, vo);
			return vo;
		}).collect(Collectors.toList()));
		
	}
	/**
	 * 通过id查询出对应的一条信息
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/api/advice/detail/{id}",method=RequestMethod.GET)
	@ResponseBody
	public Result selectDetailByid(@PathVariable String id) throws Exception{
	Optional<AdviceDetails> oadviceDetails=adviceDetailsService.getDetailByid(id);
	AdviceDetails adviceDetails=oadviceDetails.get();
	AdviceDetailsVO adviceDetailsVO=new AdviceDetailsVO();
	BeanUtils.copyProperties(adviceDetails, adviceDetailsVO);
		return Result.ok(adviceDetailsVO);
	}
}
