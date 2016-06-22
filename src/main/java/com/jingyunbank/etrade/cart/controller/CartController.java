package com.jingyunbank.etrade.cart.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jingyunbank.core.KeyGen;
import com.jingyunbank.core.Range;
import com.jingyunbank.core.Result;
import com.jingyunbank.core.web.AuthBeforeOperation;
import com.jingyunbank.core.web.Login;
import com.jingyunbank.etrade.api.cart.bo.GoodsInCart;
import com.jingyunbank.etrade.api.cart.service.ICartService;
import com.jingyunbank.etrade.api.logistic.service.IPostageService;
import com.jingyunbank.etrade.api.user.bo.Address;
import com.jingyunbank.etrade.api.user.service.IAddressService;
import com.jingyunbank.etrade.cart.bean.CartVO;
import com.jingyunbank.etrade.cart.bean.GoodsInCartVO;
import com.jingyunbank.etrade.cart.bean.OrdersInCartVO;


@RestController
public class CartController {

	public static final String GOODS_IN_CART_TO_CLEARING = "GOODS_IN_CART_TO_CLEARING";
	
	@Autowired
	private ICartService cartService;
	@Autowired
	private IPostageService postageService;
	
	@Autowired
	private IAddressService addressService;
	
	/**
	 *	uri : get /api/cart/goods/list/:uid
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/api/cart/goods/list",
					method=RequestMethod.GET,
					produces="application/json;charset=UTF-8")
	public Result<CartVO> list(HttpServletRequest request) throws Exception{
		String uid=Login.UID(request);
		List<GoodsInCart> goodsincart = cartService.listGoods(uid);
		CartVO cart = convert(goodsincart);
		return Result.ok(cart);
	}
	
	@AuthBeforeOperation
	@RequestMapping(value="/api/cart/goods/count/{uid}",
					method=RequestMethod.GET,
					produces="application/json;charset=UTF-8")
	public Result<Integer> count(@PathVariable String uid) throws Exception{
		int count = cartService.count(uid);
		return Result.ok(count);
	}

	private CartVO convert(List<GoodsInCart> goodsincart) {
		CartVO cart = new CartVO();
		goodsincart.forEach(goods -> {
			String mid = goods.getMID();
			List<OrdersInCartVO> orders = cart.getOrders();
			Optional<OrdersInCartVO> optionalOrder = orders.stream()
									.filter(order -> {return mid.equals(order.getMID());})
									.findAny();
			OrdersInCartVO order = new OrdersInCartVO();
			order.setMID(goods.getMID());
			order.setMname(goods.getMname());
			if(optionalOrder.isPresent()){
				order = optionalOrder.get();
			}else{
				cart.getOrders().add(order);
			}
			GoodsInCartVO goodsVo = new GoodsInCartVO();
			BeanUtils.copyProperties(goods, goodsVo);
			goodsVo.setCount(goodsVo.getStock() < goodsVo.getCount()?goodsVo.getStock():goodsVo.getCount());
			order.getGoods().add(goodsVo);
		});
		return cart;
	}
	
	/**
	 * 必须授权之后才可以进行添加购物车的操作作
	 * 
	 * 将商品放入购物车中<br>
	 * uri: put /api/cart {goods info}
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/api/cart",
				method=RequestMethod.POST, 
				consumes="application/json;charset=UTF-8",
				produces="application/json;charset=UTF-8")
	public Result<GoodsInCartVO> put(@Valid @RequestBody GoodsInCartVO goods,
						BindingResult valid,
						HttpSession session,HttpServletRequest request) throws Exception{
		if(valid.hasErrors()){
			return Result.fail("您提交的数据不完整，请核实后重新提交！");
		}
		String uname=Login.uname(request);
		String uid=Login.UID(request);
		String cid=Login.cartID(request);
		goods.setUID(uid);
		goods.setUname(uname);
		goods.setCartID(cid);
		goods.setID(KeyGen.uuid());
		goods.setAddtime(new Date());
		GoodsInCart gcart = new GoodsInCart();
		BeanUtils.copyProperties(goods, gcart);
		if(cartService.save(gcart)){
			return Result.ok(goods);
		}
			
		return Result.fail("该商品已添加至购物车。");
	}
	
	/**
	 * 将指定的商品从购物车中移除
	 *<br>
	 * uri: delete /api/cart/goods id=[xxxx,yyyy,zzzz]
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/api/cart/goods", 
					method=RequestMethod.DELETE,
					produces="application/json;charset=UTF-8")
	public Result<List<String>> delete(@RequestParam List<String> gids) throws Exception{
		cartService.remove(gids);
		return Result.ok(gids);
	}
	
	@AuthBeforeOperation
	@RequestMapping(value="/api/cart/goods/{uid}", method=RequestMethod.DELETE,
					produces="application/json;charset=UTF-8")
	public Result<String> delete(@PathVariable("uid") String uid, HttpSession session) throws Exception{
		String loginuid = Login.UID(session);
		if(loginuid.equals(uid)){
			cartService.clear(uid);
		}
		return Result.ok(uid);
	}
	
	/**
	 * 更新购物车中商品的数量
	 * <br>
	 * uri : post /api/cart/goods/xyzefsdgzxc count=2
	 * @param goods
	 * @return
	 * @throws Exception
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/api/cart/goods/{id}", method=RequestMethod.POST)
	public Result<Integer> update(@PathVariable("id")String id, 
			@RequestParam(value="count", required=true) int count) throws Exception{
		GoodsInCart gic = new GoodsInCart();
		gic.setID(id);
		gic.setCount(count);
		cartService.refresh(gic);
		return Result.ok(count);
	}
	
	/**
	 * 购物车去结算<br>
	 * 将购物车中选中的商品进行结算<br>
	 * 
	 * uri: post /api/cart/clearing [{'':''}, {'':''}, {'':''}]
	 * @return
	 * @throws Exception
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/api/cart/clearing", method=RequestMethod.POST)
	public Result<CartVO> clearing(@Valid @RequestBody CartVO cart,
			BindingResult valid, HttpSession session,HttpServletRequest request) throws Exception{
		if(valid.hasErrors()){
			return Result.fail("您提交的订单信息有误，请核实后重新提交。");
		}
//		CartVO cart1 = new CartVO();
//		List<OrdersInCartVO> iorders = new ArrayList<OrdersInCartVO>();
//		OrdersInCartVO ovo = new OrdersInCartVO();
//		ovo.setMID("m1");
//		ovo.setMname("乐谷微商城");
//		ovo.setPostage(new BigDecimal(0));
//		ovo.setType(Orders.BASE_ORDER_TYPE);
//		List<GoodsInCartVO> gvos = new ArrayList<GoodsInCartVO>();
//		GoodsInCartVO gvo = new GoodsInCartVO();
//		gvo.setGID("6J5oe2uzTVuUCP-P3-sJbA");
//		gvo.setSKUID("0y75PXhVQ-K-1Dxksmif5w");
//		gvo.setGname("牛仔裤");
//		gvo.setMID("m1");
//		gvo.setMname("乐谷微商城");
//		gvo.setPrice(new BigDecimal(888));
//		gvo.setCount(2);
//		gvos.add(gvo);
//		
//		GoodsInCartVO gvo1 = new GoodsInCartVO();
//		gvo1.setGID("6J5oe2uzTVuUCP-P3-sJbA");
//		gvo1.setSKUID("2GphUbWJS36NNR3iQ6qkcw");
//		gvo1.setGname("牛仔裤");
//		gvo1.setMID("m1");
//		gvo1.setMname("乐谷微商城");
//		gvo1.setPrice(new BigDecimal(888));
//		gvo1.setCount(2);
//		gvos.add(gvo1);
//		ovo.setGoods(gvos);
//		iorders.add(ovo);
//		cart1.setOrders(iorders);
		
		String uid=Login.UID(request);
		Optional<Address> addressc = addressService.getDefaultAddress(uid);
		if(addressc.isPresent()){
			Address addr = addressc.get();
			cart.setAddress(addr.getProvinceName()+"-"+addr.getCityName()+"-"+addr.getAddress());
			cart.setAddressid(addr.getID());
			cart.setCity(addr.getCity());
			cart.setMobile(addr.getMobile());
			cart.setReceiver(addr.getReceiver());
		}
		List<OrdersInCartVO> orders = cart.getOrders();
		BigDecimal cartprice = BigDecimal.ZERO;
		BigDecimal cartpricewithoutpostage = BigDecimal.ZERO;
		for (OrdersInCartVO order : orders) {
			//纯订单价格
			BigDecimal orderprice = BigDecimal.ZERO;
			//纯邮费
			BigDecimal orderpostage = BigDecimal.valueOf(10);
			List<GoodsInCartVO> goods = order.getGoods();
			for (GoodsInCartVO gs : goods) {
				BigDecimal gspprice = gs.getPprice();
				BigDecimal gsprice = gs.getPrice();
				int gscount = gs.getCount();
				orderprice = orderprice.add(
							(Objects.nonNull(gspprice)?gspprice:gsprice)
									.multiply(BigDecimal.valueOf(gscount)).setScale(2)
						);
			}
			//满99包邮（因为无法获取到收货地址信息，所有暂且按99包邮算，待选择收货地址后，再刷新邮费）
			orderpostage = (orderprice.compareTo(BigDecimal.valueOf(68)) >= 0 ? BigDecimal.ZERO : orderpostage);
            order.setPostage(orderpostage);
            order.setPrice(orderprice.add(orderpostage));
            cartprice = cartprice.add(order.getPrice());
            cartpricewithoutpostage = cartpricewithoutpostage.add(orderprice);
		}
		cart.setTotalPrice(cartprice);
		cart.setTotalPriceWithoutPostage(cartpricewithoutpostage);
		
		ObjectMapper mapper = new ObjectMapper();
		session.setAttribute(GOODS_IN_CART_TO_CLEARING, mapper.writeValueAsString(cart));
		return Result.ok(cart);
	}
	
	/**
	 * 列出所有购物车页面选中的将要被结算的商品列表<br>
	 * url: get /api/cart/clearing/list
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/api/cart/clearing/list", method=RequestMethod.GET)
	public Result<CartVO> listClearing(HttpSession session) throws Exception{
		Object obj = session.getAttribute(GOODS_IN_CART_TO_CLEARING);
		CartVO cart = new CartVO();
		if(Objects.nonNull(obj)){
			ObjectMapper mapper = new ObjectMapper();
			cart = mapper.readValue((String)obj, CartVO.class);
		}
		return Result.ok(cart);
	}
}
