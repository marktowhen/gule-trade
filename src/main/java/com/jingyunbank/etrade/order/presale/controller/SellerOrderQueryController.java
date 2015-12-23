package com.jingyunbank.etrade.order.presale.controller;

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
import com.jingyunbank.etrade.api.order.presale.service.IOrderService;
import com.jingyunbank.etrade.order.presale.bean.Order2ShowVO;
import com.jingyunbank.etrade.order.presale.bean.OrderGoodsVO;

@RestController
public class SellerOrderQueryController {
	
	@Autowired
	private IOrderService orderService;
	
	/**
	 * get /api/orders/seller/xxxx/0/10?keywords=东阿&status=PAID&fromdate=2015-11-09
	 *	
	 * 查询某用户的最新的订单中的从from开始的size条
	 * @param mid
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/api/orders/seller/{mid}/{from}/{size}", method=RequestMethod.GET)
	public Result<List<Order2ShowVO>> listMID(
			@PathVariable("mid") String mid, 
			@PathVariable("from") int from, 
			@PathVariable("size") int size,
			@RequestParam(value="keywords", required=false, defaultValue="") String keywords,
			@RequestParam(value="status", required=false, defaultValue="") String statuscode,
			@RequestParam(value="fromdate", required=false, defaultValue="1970-01-01") String fromdate,
			HttpSession session){
		
		return Result.ok(orderService.listm(mid, statuscode, fromdate, keywords, new Range(from, size+from))
				.stream().map(bo-> {
					Order2ShowVO vo = new Order2ShowVO();
					BeanUtils.copyProperties(bo, vo, "goods");
					vo.setOrderno(String.valueOf(bo.getOrderno()));
					bo.getGoods().forEach(bg -> {
						OrderGoodsVO gvo = new OrderGoodsVO();
						BeanUtils.copyProperties(bg, gvo);
						vo.getGoods().add(gvo);
					});
					return vo;
				}).collect(Collectors.toList()));
	}
}
