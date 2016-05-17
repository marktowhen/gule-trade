package com.jingyunbank.etrade.marketing.rankgroup.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.Result;
import com.jingyunbank.etrade.api.marketing.rankgroup.service.IRankGroupUserService;
import com.jingyunbank.etrade.marketing.rankgroup.bean.RankGroupUserVO;

@RestController
@RequestMapping("/api/marketing/rankgroup/user")
public class RankGroupUserController {
	
	@Autowired
	private IRankGroupUserService rankGroupUserService;
	
	
	//查询参团人
	@RequestMapping("/list/{groupID}")
	public Result<List<RankGroupUserVO>> list(@PathVariable String groupID,@RequestParam(required=false) String status){
		return Result.ok(rankGroupUserService.list(groupID, status).stream().map( bo->{
			RankGroupUserVO vo = new RankGroupUserVO();
			BeanUtils.copyProperties(bo, vo);
			return vo;
		}).collect(Collectors.toList()));
	}
	//查询参团人数
	@RequestMapping("/count/{groupID}")
	public Result<Integer> count(@PathVariable String groupID,@RequestParam(required=false) String status){
		return Result.ok(rankGroupUserService.count(groupID, status));
	}

}
