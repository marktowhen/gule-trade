package com.jingyunbank.etrade.marketing.group.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.Result;
import com.jingyunbank.etrade.api.marketing.group.bo.Group;
import com.jingyunbank.etrade.api.marketing.group.bo.GroupGoods;
import com.jingyunbank.etrade.api.marketing.group.bo.GroupOrder;
import com.jingyunbank.etrade.api.marketing.group.bo.GroupUser;
import com.jingyunbank.etrade.api.marketing.group.service.IGroupGoodsService;
import com.jingyunbank.etrade.api.marketing.group.service.IGroupOrderService;
import com.jingyunbank.etrade.api.marketing.group.service.IGroupService;
import com.jingyunbank.etrade.api.marketing.group.service.IGroupUserService;
import com.jingyunbank.etrade.marketing.group.bean.GroupGoodsVO;
import com.jingyunbank.etrade.marketing.group.bean.GroupUserVO;
import com.jingyunbank.etrade.marketing.group.bean.GroupVO;

@RestController
@RequestMapping("/api/marketing/group/user")
public class GroupUserController {

	@Autowired
	private IGroupUserService groupUserService;
	@Autowired 
	private IGroupService groupService;
	@Autowired
	private IGroupOrderService groupOrderService;
	
	@RequestMapping("/list/{groupID}")
	public Result<List<GroupUserVO>> list(@PathVariable String groupID,@RequestParam(required=false) String status){
		return Result.ok(groupUserService.list(groupID, status).stream().map( bo->{
			GroupUserVO vo = new GroupUserVO();
			BeanUtils.copyProperties(bo, vo);
			return vo;
		}).collect(Collectors.toList()));
	}
	
	@RequestMapping("/count/{groupID}")
	public Result<Integer> count(@PathVariable String groupID,@RequestParam(required=false) String status){
		return Result.ok(groupUserService.count(groupID, status));
	}
	/**
	 * 查出用户对应的信息
	 * @param groupid
	 * @param uid
	 * @return
	 */
	@RequestMapping(value="single/{groupid}/{uid}",method=RequestMethod.GET)
	public Result<GroupUserVO> getSingleUser(@PathVariable String groupid,@PathVariable String uid){
		GroupUserVO vo = new GroupUserVO();
		Optional<GroupUser> bo=groupUserService.single(groupid, uid);
		BeanUtils.copyProperties(bo.get(), vo);
		return Result.ok(vo);
		
	}
	/**
	 * 查出用户对应的团的信息
	 * @param uid
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/group/list/{uid}",method=RequestMethod.GET)
	public Result<List<GroupUserVO>> getListGroup(@PathVariable String uid,HttpServletRequest request){
		List<GroupUserVO> volist = new ArrayList<GroupUserVO>();
		groupUserService.getGroup(uid).forEach(bo ->{
			GroupUserVO vo = new GroupUserVO();
			BeanUtils.copyProperties(bo, vo);
			volist.add(vo);
			
		});
		return Result.ok(volist);
		
	}
	/**
	 * 通过id查出对应的团的信息
	 * @param id
	 * @param status
	 * @return
	 */
	@RequestMapping(value="/group/single/{id}",method=RequestMethod.GET)
	public Result<GroupVO> getroupGoods(@PathVariable String id){
		//登陆用户的id     uid=“Ma9ogkIXSW-y0uSrvfqVIQ”
		Optional<Group> bo=groupService.getGroupGoods(id);
		GroupGoodsVO voGoods = new GroupGoodsVO();
		BeanUtils.copyProperties(bo.get().getGoods(), voGoods);
		GroupVO vo = new GroupVO();
		Optional<GroupUser> bouser=groupUserService.single(id, "Ma9ogkIXSW-y0uSrvfqVIQ");
		if(bouser.isPresent()){
			Optional<GroupOrder> boOrder=groupOrderService.singleByGroupUserId(bouser.get().getID());
			vo.setOrderno(String.valueOf(boOrder.get().getOrderno()));
			vo.setOrdertime(boOrder.get().getAddtime());
			double totalPrice=bo.get().getGoods().getGroupPrice().doubleValue()+boOrder.get().getPostage().doubleValue();
			vo.setTotalPrice(totalPrice);
			vo.setPostage(boOrder.get().getPostage());
		}
		
		vo.setGoods(voGoods);
		BeanUtils.copyProperties(bo.get(), vo);
		return Result.ok(vo);
		
	}
	
}
