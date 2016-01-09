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
import com.jingyunbank.core.web.AuthBeforeOperation;
import com.jingyunbank.core.web.Login;
import com.jingyunbank.etrade.api.order.presale.service.IOrderService;
import com.jingyunbank.etrade.order.presale.bean.Order2ShowVO;
import com.jingyunbank.etrade.order.presale.bean.OrderGoodsVO;

@RestController
public class SellerOrderQueryController {
	
	@Autowired
	private IOrderService orderService;
	
	/**
	 * get /api/orders/seller/0/10?keywords=东阿&status=PAID&fromdate=2015-11-09&mid=xzxcvzx
	 *	
	 * 查询某用户的最新的订单中的从from开始的size条
	 * @param mid
	 * @param session
	 * @return
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/api/orders/seller/{from}/{size}", method=RequestMethod.GET)
	public Result<List<Order2ShowVO>> listMID(
			@PathVariable("from") int from, 
			@PathVariable("size") int size,
			@RequestParam(value="orderno", required=false, defaultValue="") String orderno,
			@RequestParam(value="gname", required=false, defaultValue="") String gname,
			@RequestParam(value="uname", required=false, defaultValue="") String uname,
			@RequestParam(value="mname", required=false, defaultValue="") String mname,
			@RequestParam(value="status", required=false, defaultValue="") String statuscode,
			@RequestParam(value="fromdate", required=false, defaultValue="") String fromdate,
			@RequestParam(value="enddate", required=false, defaultValue="") String enddate,
			HttpSession session){
		String mid = Login.MID(session);
		return Result.ok(orderService.list(null, mid, statuscode, orderno, gname, uname, mname, fromdate, enddate, new Range(from, size+from))
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
	
	/**
	 * get /api/orders/seller/count?keywords=东阿&status=PAID&fromdate=2015-11-09
	 *	
	 * 查询某用户的订单数量
	 * @param uid
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/api/orders/seller/count", method=RequestMethod.GET)
	@AuthBeforeOperation
	public Result<Integer> getAmount( 
			@RequestParam(value="orderno", required=false, defaultValue="") String orderno,
			@RequestParam(value="gname", required=false, defaultValue="") String gname,
			@RequestParam(value="uname", required=false, defaultValue="") String uname,
			@RequestParam(value="mname", required=false, defaultValue="") String mname,
			@RequestParam(value="status", required=false, defaultValue="") String statuscode,
			@RequestParam(value="fromdate", required=false, defaultValue="") String fromdate,
			@RequestParam(value="enddate", required=false, defaultValue="") String enddate,
			HttpSession session){
		String mid = Login.MID(session);
		
		return Result.ok(orderService.count(null, mid, statuscode, orderno, gname, uname, mname, fromdate, enddate));
	}
}
