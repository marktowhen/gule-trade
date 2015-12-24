package com.jingyunbank.etrade.vip.point.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.Result;
import com.jingyunbank.etrade.api.vip.point.service.IPointService;

@RestController
public class PointController {
	
	@Autowired
	private IPointService pointService;
	/**
	 * 获取用户当前积分
	 * @param uid
	 * @return
	 * 2015年12月2日 qxs
	 */
	@RequestMapping(value="/point/{uid}", method=RequestMethod.GET)
	public Result<Integer> getPiont(@PathVariable String uid){
		return Result.ok(pointService.getPoint(uid));
	}

}
