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
			BeanUtils.copyProperties(bo, orderGoodsVO);
			return orderGoodsVO;
		}).collect(Collectors.toList()));
	}
	/**
	 * 查询某状态的订单产品
	 * @param session
	 * @param request
	 * @return
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/api/comment/order/goods",method=RequestMethod.GET)
	public Result<List<OrderGoodsVO>> listyiOrderGoods(HttpSession session,HttpServletRequest request){
		String uid = ServletBox.getLoginUID(request);
		return Result.ok(orderGoodsService.listOrderGoods(uid,OrderStatusDesc.COMMENTED)/*OrderStatusDesc.RECEIVED*/
			.stream().map(bo ->{
			
			OrderGoodsVO  orderGoodsVO = new OrderGoodsVO();			
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
	public Result<Integer> getNoCommentCount(HttpSession session,HttpServletRequest request){
		String uid = ServletBox.getLoginUID(request);
		int noCommentCount=orderGoodsService.listOrderGoods(uid,OrderStatusDesc.RECEIVED).size();
		return Result.ok(noCommentCount);
		
	}
	/**
	 * 查出该用户已经评价商品的个数
	 * @param session
	 * @param request
	 * @return
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/api/order/goods/comment/count",method=RequestMethod.GET)
	public Result<Integer> getCommentCount(HttpSession session,HttpServletRequest request){
		String uid = ServletBox.getLoginUID(request);
		int CommentCount=orderGoodsService.listOrderGoods(uid,OrderStatusDesc.COMMENTED).size();
		return Result.ok(CommentCount);
		
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
			
			BeanUtils.copyProperties(orderGoods, orderGoodsVO);
			
			return Result.ok(orderGoodsVO);
				
	}
}
