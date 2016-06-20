package com.jingyunbank.etrade.order.presale.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
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
import com.jingyunbank.etrade.api.order.presale.bo.Orders;
import com.jingyunbank.etrade.api.order.presale.service.IOrderService;
import com.jingyunbank.etrade.order.presale.bean.Order2ShowVO;
import com.jingyunbank.etrade.order.presale.bean.OrderGoodsVO;
import com.jingyunbank.etrade.weixin.util.StringUtilss;

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
	 *
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/api/orders/user/list", method=RequestMethod.GET)
	public Result<List<Order2ShowVO>> listUID(
			@RequestParam(value="from", required=false, defaultValue="") int from, 
			@RequestParam(value="size", required=false, defaultValue="") int size,
			@RequestParam(value="anystatus", required=false, defaultValue="") int anystatus,
			@RequestParam(value="status", required=false, defaultValue="") String statuscode,
			HttpSession session,HttpServletRequest request){
		String loginuid = StringUtilss.getSessionId(request);

		
		return Result.ok(orderService.list(loginuid, null, statuscode,anystatus, new Range(from, size+from))
				.stream().map(bo-> {
					Order2ShowVO vo = new Order2ShowVO();
					BeanUtils.copyProperties(bo, vo,"goods");
					vo.setOrderno(String.valueOf(bo.getOrderno()));
					bo.getGoods().forEach(bg -> {
						OrderGoodsVO gvo = new OrderGoodsVO();
						BeanUtils.copyProperties(bg, gvo);
						vo.getGoods().add(gvo);
					});
					return vo;
				}).collect(Collectors.toList()));
	}
	
	/**
	 * get /api/orders/amount/xxxx?keywords=东阿&status=PAID&fromdate=2015-11-09
	 *	
	 * 查询某用户的订单数量
	 * @param uid
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/api/orders/user/count", method=RequestMethod.GET)
	@AuthBeforeOperation
	public Result<Integer> getAmount(
			@RequestParam(value="keywords", required=false, defaultValue="") String keywords,
			@RequestParam(value="status", required=false, defaultValue="") String statuscode,
			@RequestParam(value="fromdate", required=false, defaultValue="") String fromdate,
			HttpSession session,HttpServletRequest request){
		String loginuid = StringUtilss.getSessionId(request);
		
		return Result.ok(orderService.count(loginuid, null, statuscode, keywords, fromdate, null));
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
				OrderGoodsVO gvo = new OrderGoodsVO();
				BeanUtils.copyProperties(bg, gvo);
				vo.getGoods().add(gvo);
			});
		});
		
		return Result.ok(vo);
	}
}
