package com.jingyunbank.etrade.order.presale.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.KeyGen;
import com.jingyunbank.core.Result;
import com.jingyunbank.core.util.UniqueSequence;
import com.jingyunbank.core.web.AuthBeforeOperation;
import com.jingyunbank.core.web.Login;
import com.jingyunbank.etrade.api.goods.service.IGoodsService;
import com.jingyunbank.etrade.api.order.presale.bo.OrderGoods;
import com.jingyunbank.etrade.api.order.presale.bo.OrderStatusDesc;
import com.jingyunbank.etrade.api.order.presale.bo.Orders;
import com.jingyunbank.etrade.api.order.presale.service.IOrderGoodsService;
import com.jingyunbank.etrade.api.order.presale.service.IOrderService;
import com.jingyunbank.etrade.api.order.presale.service.context.IOrderContextService;
import com.jingyunbank.etrade.api.vip.coupon.handler.ICouponStrategyResolver;
import com.jingyunbank.etrade.cart.controller.CartController;
import com.jingyunbank.etrade.order.presale.bean.Order2ShowVO;
import com.jingyunbank.etrade.order.presale.bean.OrderGoodsVO;
import com.jingyunbank.etrade.order.presale.bean.PurchaseGoodsVO;
import com.jingyunbank.etrade.order.presale.bean.PurchaseOrderVO;
import com.jingyunbank.etrade.order.presale.bean.PurchaseRequestVO;

@RestController
public class OrderController {

	@Autowired
	private IOrderContextService orderContextService;
	@Autowired
	private ICouponStrategyResolver couponStrategyResolver;
	@Autowired
	private IGoodsService goodsService;
	
	@Autowired 
	private IOrderService orderService;
	@Autowired
	private IOrderGoodsService orderGoodService;
	
	/**
	 * 订单确认并提交<br>
	 * uri: put /api/order {"addressID":"XXXXX", "":"", "orders":[{}, {}, {}]}
	 * @param purchase
	 * @param valid
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@AuthBeforeOperation
	@RequestMapping(
			value="/api/order",
			method=RequestMethod.POST,
			consumes=MediaType.APPLICATION_JSON_UTF8_VALUE,
			produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Result<PurchaseRequestVO> submit(@Valid @RequestBody PurchaseRequestVO purchase,
			BindingResult valid, HttpSession session,HttpServletRequest request) throws Exception{
		if(valid.hasErrors()){
			return Result.fail("您提交的订单数据不完整，请核实后重新提交！");
		}
		String uid = Login.UID(request);
		String uname = Login.uname(request);
		boolean isemployee = Login.employee(session);
		purchase.setUID(uid);
		purchase.setUname(uname);
		purchase.setEmployee(isemployee);

		List<Orders> orders = populateOrderData(purchase, session);
		List<String> gids = new ArrayList<String>();
		for (Orders order : orders) {
			List<String> igids = order.getGoods().stream().map(gs -> gs.getGID()).collect(Collectors.toList());
			gids.addAll(igids);
		}
//		List<ShowGoods> g = goodsService.listGoodsStcok(gids);
//		List<Integer> gstock = g.stream().map(x->x.getCount()).collect(Collectors.toList());
//		if(gstock.stream().anyMatch(x->x <= 0)){
//			return Result.fail("部分商品暂时无货，请检查后重新提交订单。");
//		}
		
		Result<List<Orders>> sr = orderContextService.save(orders);
		if(sr.isOk()){
			session.removeAttribute(CartController.GOODS_IN_CART_TO_CLEARING);
			return Result.ok(purchase);
		}else{
			return Result.fail(sr.getMessage());
		}
	}

	private List<Orders> populateOrderData(PurchaseRequestVO purchase,
			HttpSession session) throws Exception {
		
		List<PurchaseOrderVO> ordervos = purchase.getOrders();
		List<Orders> orders = new ArrayList<Orders>();
		
		for (PurchaseOrderVO ordervo : ordervos) {
			if(StringUtils.isEmpty(ordervo.getID())){
				ordervo.setID(KeyGen.uuid());
				ordervo.setOrderno(UniqueSequence.next18());
			}
			ordervo.setAddtime(new Date());
			
			Orders order = new Orders();
			BeanUtils.copyProperties(ordervo, order);
			BeanUtils.copyProperties(purchase, order);
			
			order.setStatusCode(OrderStatusDesc.NEW.getCode());
			order.setStatusName(OrderStatusDesc.NEW.getName());
			
			List<PurchaseGoodsVO> goodses = ordervo.getGoods();
			List<OrderGoods> orderGoodses = new ArrayList<OrderGoods>();
			for (PurchaseGoodsVO goods : goodses) {
				OrderGoods orderGoods = new OrderGoods();
				BeanUtils.copyProperties(goods, orderGoods);
				orderGoods.setID(KeyGen.uuid());
				orderGoods.setOID(order.getID());
				orderGoods.setOrderno(order.getOrderno());
				orderGoods.setStatusCode(order.getStatusCode());
				orderGoods.setStatusName(order.getStatusName());
				orderGoods.setAddtime(new Date());
				orderGoods.setUID(order.getUID());
				BigDecimal origingoodspprice = orderGoods.getPprice();//促销价
				BigDecimal origingoodsprice = orderGoods.getPrice();
				BigDecimal count = BigDecimal.valueOf(orderGoods.getCount());
				origingoodsprice = //如果促销价不为空，则使用促销价
						(Objects.nonNull(origingoodspprice) && origingoodspprice.compareTo(BigDecimal.ZERO) > 0)?
						origingoodspprice : origingoodsprice;
				orderGoods.setPayout(origingoodsprice.multiply(count));
				orderGoods.setCouponReduce(BigDecimal.ZERO);
				orderGoodses.add(orderGoods);
			}
			
			order.setPayout(order.getPrice());
			order.setGoods(orderGoodses);
			orders.add(order);
		}
		
		return orders;
	}
	
	@AuthBeforeOperation
	@RequestMapping(value="/api/orders/cancellation/{oid}",method=RequestMethod.PUT)
	public Result<String> cancel(@PathVariable String oid, HttpSession session) throws Exception{
		
		if(!orderContextService.cancels(Arrays.asList(oid))){
			return Result.fail("取消失败");
		}
		return Result.ok();
	}
	@AuthBeforeOperation
	@RequestMapping(value="/api/order/single/{oid}")
	public Result<Order2ShowVO> getSingle(@PathVariable String oid){
		Order2ShowVO vo = new Order2ShowVO();
		Optional<Orders> orders=orderService.single(oid);
		vo.setOrderno(String.valueOf(orders.get().getOrderno()));
		BeanUtils.copyProperties(orders.get(), vo, "goods");
		orders.get().getGoods().forEach(og ->{
			OrderGoodsVO  dvo = new OrderGoodsVO();
			BeanUtils.copyProperties(og, dvo);
			vo.getGoods().add(dvo);
			
		});
		return Result.ok(vo);
		
	}
	@AuthBeforeOperation
	@RequestMapping(value="/api/orders/update/status/{oid}",method=RequestMethod.PUT)
	public Result<String> refreshStatus(@PathVariable String oid)throws Exception{
		orderService.refreshStatus(Arrays.asList(oid), OrderStatusDesc.RECEIVED);
		orderGoodService.refreshStatus(Arrays.asList(oid), OrderStatusDesc.RECEIVED);
		return Result.ok("修改状态成功");
		
	}
	
	@AuthBeforeOperation
	@RequestMapping(value="/api/orders/{id}", method=RequestMethod.DELETE)
	public Result<String> remove(@PathVariable String id) throws Exception{
		
		orderContextService.remove(id);
		
		return Result.ok(id);
	}
	

	private static class OIDWithNoteVO{
		@NotNull
		private String oid;
		@NotNull
		private String note;
		public String getOid() {
			return oid;
		}
		@SuppressWarnings("unused")
		public void setOid(String oid) {
			this.oid = oid;
		}
		public String getNote() {
			return note;
		}
		@SuppressWarnings("unused")
		public void setNote(String note) {
			this.note = note;
		}
	}
	
}
