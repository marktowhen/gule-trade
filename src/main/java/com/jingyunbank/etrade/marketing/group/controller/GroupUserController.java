package com.jingyunbank.etrade.marketing.group.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.Result;
import com.jingyunbank.etrade.api.marketing.group.service.IGroupUserService;
import com.jingyunbank.etrade.marketing.group.bean.GroupUserVO;

@RestController
@RequestMapping("/api/marketing/group/user")
public class GroupUserController {

	@Autowired
	private IGroupUserService groupUserService;
	
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
}
