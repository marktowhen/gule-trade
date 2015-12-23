package com.jingyunbank.etrade.order.postsale.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.Result;
import com.jingyunbank.etrade.api.order.postsale.service.IRefundTraceService;
import com.jingyunbank.etrade.order.postsale.bean.RefundTraceVO;

@RestController
public class RefundTraceController {

	@Autowired
	private IRefundTraceService refundTraceService;
	
	/**
	 * get /api/refund/zxcv/traces?status=RACCEPT
	 * @param rid
	 * @param status
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/api/refund/{rid}/traces", method=RequestMethod.GET)
	//@AuthBeforeOperation
	public Result<List<RefundTraceVO>> listTraces(@PathVariable("rid") String rid, 
			@RequestParam(value="status", required=false, defaultValue="") String status,
			HttpSession session){
		
		return Result.ok(refundTraceService.list(rid, status)
				.stream().map(bo->{
					RefundTraceVO vo = new RefundTraceVO();
					BeanUtils.copyProperties(bo, vo);
					return vo;
				}).collect(Collectors.toList()));
	}
}
