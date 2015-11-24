package com.jingyunbank.etrade.order.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.KeyGen;
import com.jingyunbank.core.Result;
import com.jingyunbank.core.web.AuthBeforeOperation;
import com.jingyunbank.core.web.ServletBox;
import com.jingyunbank.etrade.api.order.bo.GoodsInCart;
import com.jingyunbank.etrade.api.order.service.ICartService;
import com.jingyunbank.etrade.order.bean.CartVO;
import com.jingyunbank.etrade.order.bean.GoodsInCartVO;
import com.jingyunbank.etrade.order.bean.OrdersInCartVO;


@RestController
public class CartController {

	public static final String GOODS_IN_CART_TO_CLEARING = "GOODS_IN_CART_TO_CLEARING";
	
	@Autowired
	private ICartService cartService;
	
	/**
	 *	uri : get /api/cart/goods/list
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/api/cart/goods/list",
					method=RequestMethod.GET,
					produces="application/json;charset=UTF-8")
	public Result list(HttpSession session) throws Exception{
		String uid = ServletBox.getLoginUID(session);
		List<GoodsInCart> goodsincart = cartService.listGoods(uid);
		CartVO cart = convert(goodsincart);
		return Result.ok(cart);
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
			order.getGoods().add(goodsVo);
		});
		return cart;
	}
	
	/**
	 * 将商品放入购物车中<br>
	 * uri: put /api/cart {goods info}
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/api/cart",
				method=RequestMethod.PUT, 
				consumes="application/json;charset=UTF-8",
				produces="application/json;charset=UTF-8")
	public Result put(@Valid @RequestBody GoodsInCartVO goods,
						BindingResult valid,
						HttpSession session) throws Exception{
		if(valid.hasErrors()){
			List<ObjectError> errors = valid.getAllErrors();
			return Result.fail(errors.stream()
						.map(oe -> Arrays.asList(oe.getCodes()).toString())
						.collect(Collectors.joining(" ; ")));
		}
		String cid = ServletBox.getLoginCartID(session);
		String uid = ServletBox.getLoginUID(session);
		goods.setUID(uid);
		goods.setCartID(cid);
		goods.setID(KeyGen.uuid());
		goods.setAddtime(new Date());
		GoodsInCart gcart = new GoodsInCart();
		BeanUtils.copyProperties(goods, gcart);
		cartService.save(gcart);
		return Result.ok(goods);
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
	public Result delete(@RequestParam List<String> gids) throws Exception{
		cartService.remove(gids);
		return Result.ok(gids);
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
	public Result update(@PathVariable("id")String id, 
			@RequestParam(value="count", required=true) int count) throws Exception{
		GoodsInCart gic = new GoodsInCart();
		gic.setID(id);
		gic.setCount(count);
		cartService.refresh(gic);
		return Result.ok();
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
	public Result clearing(@Valid @RequestBody CartVO cart,
					BindingResult valid, HttpSession session) throws Exception{
		session.setAttribute(GOODS_IN_CART_TO_CLEARING, cart);
		return Result.ok();
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
	public Result listClearing(HttpSession session) throws Exception{
		Object obj = session.getAttribute(GOODS_IN_CART_TO_CLEARING);
		CartVO cart = new CartVO();
		if(Objects.nonNull(obj)){
			cart = (CartVO)obj;
		}
		return Result.ok(cart);
	}
}
