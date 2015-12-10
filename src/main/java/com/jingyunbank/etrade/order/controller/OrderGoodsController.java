package com.jingyunbank.etrade.order.controller;

import java.text.SimpleDateFormat;
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
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.Result;
import com.jingyunbank.core.web.AuthBeforeOperation;
import com.jingyunbank.core.web.ServletBox;
import com.jingyunbank.etrade.api.order.bo.OrderGoods;
import com.jingyunbank.etrade.api.order.bo.OrderStatusDesc;
import com.jingyunbank.etrade.api.order.service.IOrderGoodsService;
import com.jingyunbank.etrade.order.bean.OrderGoodsVO;

@RestController
public class OrderGoodsController {
	
	@Autowired
	private IOrderGoodsService orderGoodsService;
	
	/**
	 * 查询某状态的订单产品
	 * @param session
	 * @param request
	 * @return
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/api/order/goods",method=RequestMethod.GET)
	public Result<List<OrderGoodsVO>> listOrderGoods(HttpSession session,HttpServletRequest request){
		String uid = ServletBox.getLoginUID(request);
		return Result.ok(orderGoodsService.listOrderGoods(uid,OrderStatusDesc.RECEIVED)/*OrderStatusDesc.RECEIVED*/
			.stream().map(bo ->{
			
			OrderGoodsVO  orderGoodsVO = new OrderGoodsVO();
			String addtimeStr= new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(bo.getAddtime());
			orderGoodsVO.setAddtimeStr(addtimeStr);
			
			BeanUtils.copyProperties(bo, orderGoodsVO);
			return orderGoodsVO;
		}).collect(Collectors.toList()));
	}
	
	/**
	 * 查出该用户未评价商品的个数
	 * @param session
	 * @param request
	 * @return
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/api/order/goods/nocomment/count",method=RequestMethod.GET)
	public Result<OrderGoodsVO> getNoCommentCount(HttpSession session,HttpServletRequest request){
		int noCommentCount=0;
		String uid = ServletBox.getLoginUID(request);
		noCommentCount=orderGoodsService.listOrderGoods(uid,OrderStatusDesc.RECEIVED).size();
		OrderGoodsVO  orderGoodsVO = new OrderGoodsVO();
		orderGoodsVO.setNoCommentCount(noCommentCount);
		return Result.ok(orderGoodsVO);
		
	}
	/**
	 * 查出该用户已经评价商品的个数
	 * @param session
	 * @param request
	 * @return
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/api/order/goods/comment/count",method=RequestMethod.GET)
	public Result<OrderGoodsVO> getCommentCount(HttpSession session,HttpServletRequest request){
		String uid = ServletBox.getLoginUID(request);
		int CommentCount=orderGoodsService.listOrderGoods(uid,OrderStatusDesc.COMMENTED).size();
		OrderGoodsVO  orderGoodsVO = new OrderGoodsVO();
		orderGoodsVO.setCommentCount(CommentCount);
		return Result.ok(orderGoodsVO);
		
	}
	
	/**COMMENTED
	 * 通过oid查出订单产品
	 * @param oid
	 * @return
	 */
	//@AuthBeforeOperation
	@RequestMapping(value="/api/orders/{oid}/goods",method=RequestMethod.GET)
	public Result<OrderGoodsVO> getOrderGoods(@PathVariable(value="oid") String oid){
			Optional<OrderGoods> optional	=orderGoodsService.singleOrderGoods(oid);
			OrderGoods	orderGoods =optional.get();
			OrderGoodsVO  orderGoodsVO = new OrderGoodsVO();
			String addtimeStrs= new SimpleDateFormat("yyyy-MM-dd").format(orderGoods.getAddtime());
			orderGoodsVO.setAddtimeStr(addtimeStrs);
			
			BeanUtils.copyProperties(orderGoods, orderGoodsVO);
			
			return Result.ok(orderGoodsVO);
				
	}
}
