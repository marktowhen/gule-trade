package com.jingyunbank.etrade.order.postsale.controller;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.Result;
import com.jingyunbank.etrade.api.order.postsale.bo.RefundLogistic;
import com.jingyunbank.etrade.api.order.postsale.service.IRefundLogisticService;
import com.jingyunbank.etrade.order.postsale.bean.RefundLogisticVO;

@RestController
public class RefundLogisticController {
	
	@Autowired
	private IRefundLogisticService refundLogisticService;
	/**
	 * get /api/refund/{rid}/logistic
	 
	 * @return
	 */
	@RequestMapping(value="/api/refund/{rid}/logistic", method=RequestMethod.GET)
	public Result<RefundLogisticVO> logistic(@PathVariable String rid) throws Exception{
		Optional<RefundLogistic> candidateBo = refundLogisticService.single(rid);
		if(!candidateBo.isPresent()){
			return Result.ok();
		}
		RefundLogisticVO vo = new RefundLogisticVO();
		BeanUtils.copyProperties(candidateBo.get(), vo);
		return Result.ok(vo);
	}
	
}
