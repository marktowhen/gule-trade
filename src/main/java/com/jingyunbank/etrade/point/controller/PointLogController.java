package com.jingyunbank.etrade.point.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.Page;
import com.jingyunbank.core.Range;
import com.jingyunbank.core.Result;
import com.jingyunbank.etrade.api.point.service.IPointLogService;
import com.jingyunbank.etrade.point.bean.PointLogVO;

@RestController
public class PointLogController {

	@Autowired
	private IPointLogService pointLogService;
	
	/**
	 * 查询用户所有积分日志
	 * @param uid
	 * @return
	 * 2015年12月2日 qxs
	 */
	@RequestMapping(value="/point/log/{uid}", method=RequestMethod.GET)
	public Result<List<PointLogVO>> getUserLog(@PathVariable String uid){
		
		return Result.ok(pointLogService.list(uid).stream().map(bo -> {
			PointLogVO vo = new PointLogVO();
			BeanUtils.copyProperties(bo, vo);
			return vo;
		}).collect(Collectors.toList()));
	}
	/**
	 * 查询分页积分日志
	 * @param uid
	 * @param page
	 * @return
	 * 2015年12月2日 qxs
	 */
	@RequestMapping(value="/point/log/list/{uid}", method=RequestMethod.GET)
	public Result<List<PointLogVO>> getUserLogList(@PathVariable String uid, Page page){
		Range range = new Range();
		range.setFrom(page.getOffset());
		range.setTo(page.getOffset()+page.getSize());
		return Result.ok(pointLogService.list(uid, range).stream().map(bo -> {
			PointLogVO vo = new PointLogVO();
			BeanUtils.copyProperties(bo, vo);
			return vo;
		}).collect(Collectors.toList()));
	}
	
	
}
