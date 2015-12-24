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

import com.jingyunbank.core.Range;
import com.jingyunbank.core.Result;
import com.jingyunbank.etrade.api.order.postsale.service.IRefundService;
import com.jingyunbank.etrade.order.postsale.bean.Refund2ShowVO;

@RestController
public class SellerRefundQueryController {

	@Autowired
	private IRefundService refundService;
	
	/**
	 * get /api/refund/seller/0/10?keywords=东阿&status=PAID&fromdate=2015-11-09&mid
	 *	
	 * 查询某用户的退单的从from开始的size条
	 * @param mid
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/api/refund/seller/{from}/{size}", method=RequestMethod.GET)
	public Result<List<Refund2ShowVO>> listMID(
			@RequestParam(value="mid", required=false, defaultValue="") String mid, 
			@PathVariable("from") int from, 
			@PathVariable("size") int size,
			@RequestParam(value="keywords", required=false, defaultValue="") String keywords,
			@RequestParam(value="status", required=false, defaultValue="") String statuscode,
			@RequestParam(value="fromdate", required=false, defaultValue="1970-01-01") String fromdate,
			HttpSession session){
		
		return Result.ok(refundService.listm(mid, statuscode, fromdate, keywords, new Range(from, size+from))
				.stream().map(bo-> {
					Refund2ShowVO vo = new Refund2ShowVO();
					BeanUtils.copyProperties(bo, vo, "certificates");
					vo.setOrderno(String.valueOf(bo.getOrderno()));
					return vo;
				}).collect(Collectors.toList()));
	}
	
}
