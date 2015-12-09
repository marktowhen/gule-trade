package com.jingyunbank.etrade.order.controller;

import java.util.List;
import java.util.Optional;
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
import com.jingyunbank.core.web.AuthBeforeOperation;
import com.jingyunbank.core.web.ServletBox;
import com.jingyunbank.etrade.api.order.bo.Orders;
import com.jingyunbank.etrade.api.order.service.IOrderService;
import com.jingyunbank.etrade.order.bean.Order2ShowVO;
import com.jingyunbank.etrade.order.bean.Order2ShowVO.OrderGoods2ShowVO;

@RestController
public class OrderQueryController {
	
	@Autowired
	private IOrderService orderService;
	
	/**
	 * get /api/orders/xxxx/0/10?keywords=东阿&status=PAID&fromdate=2015-11-09
	 *	
	 * 查询某用户的最新的订单中的从from开始的size条
	 * @param uid
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/api/orders/{uid}/{from}/{size}", method=RequestMethod.GET)
	@AuthBeforeOperation
	public Result<List<Order2ShowVO>> listUID(
			@PathVariable("uid") String uid, 
			@PathVariable("from") int from, 
			@PathVariable("size") int size,
			@RequestParam(value="keywords", required=false, defaultValue="") String keywords,
			@RequestParam(value="status", required=false, defaultValue="") String statuscode,
			@RequestParam(value="fromdate", required=false, defaultValue="1970-01-01") String fromdate,
			HttpSession session){
		String loginuid = ServletBox.getLoginUID(session);
		if(!loginuid.equalsIgnoreCase(uid))return Result.fail("无权访问！");
		
		return Result.ok(orderService.list(uid, statuscode, fromdate, keywords, new Range(from, size+from))
				.stream().map(bo-> {
					Order2ShowVO vo = new Order2ShowVO();
					BeanUtils.copyProperties(bo, vo, "goods");
					vo.setOrderno(String.valueOf(bo.getOrderno()));
					bo.getGoods().forEach(bg -> {
						OrderGoods2ShowVO gvo = new OrderGoods2ShowVO();
						BeanUtils.copyProperties(bg, gvo);
						vo.getGoods().add(gvo);
					});
					return vo;
				}).collect(Collectors.toList()));
	}
	
	@RequestMapping(value="/api/orders/{oid}", method=RequestMethod.GET)
	@AuthBeforeOperation
	public Result<Order2ShowVO> singleOrder(@PathVariable("oid") String oid){
		Order2ShowVO vo = new Order2ShowVO();
		Optional<Orders> order = orderService.single(oid);
		
		order.ifPresent(bo -> {
			BeanUtils.copyProperties(bo, vo, "goods");
			vo.setOrderno(String.valueOf(bo.getOrderno()));
			bo.getGoods().forEach(bg -> {
				OrderGoods2ShowVO gvo = new OrderGoods2ShowVO();
				BeanUtils.copyProperties(bg, gvo);
				vo.getGoods().add(gvo);
			});
		});
		
		return Result.ok(vo);
	}
}
