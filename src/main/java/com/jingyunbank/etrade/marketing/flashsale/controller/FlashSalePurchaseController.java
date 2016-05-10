package com.jingyunbank.etrade.marketing.flashsale.controller;

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
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jingyunbank.core.KeyGen;
import com.jingyunbank.core.Result;
import com.jingyunbank.core.util.UniqueSequence;
import com.jingyunbank.core.web.Login;
import com.jingyunbank.etrade.api.marketing.flashsale.bo.FlashSale;
import com.jingyunbank.etrade.api.marketing.flashsale.bo.FlashSaleUser;
import com.jingyunbank.etrade.api.marketing.flashsale.service.IFlashSaleService;
import com.jingyunbank.etrade.api.marketing.flashsale.service.context.IFlashSalePurchaseContextService;
import com.jingyunbank.etrade.api.order.presale.bo.OrderStatusDesc;
import com.jingyunbank.etrade.api.order.presale.bo.Orders;
import com.jingyunbank.etrade.api.user.bo.Address;
import com.jingyunbank.etrade.api.user.bo.Users;
import com.jingyunbank.etrade.api.user.service.IAddressService;
import com.jingyunbank.etrade.api.user.service.IUserService;
import com.jingyunbank.etrade.cart.bean.CartVO;
import com.jingyunbank.etrade.cart.bean.GoodsInCartVO;
import com.jingyunbank.etrade.cart.bean.OrdersInCartVO;
import com.jingyunbank.etrade.cart.controller.CartController;
import com.jingyunbank.etrade.marketing.flashsale.bean.FlashSaleUserVo;
@RestController
public class FlashSalePurchaseController {
	@Autowired 
	private IFlashSaleService flashSaleService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IAddressService addressService;
	@Autowired
	private IFlashSalePurchaseContextService flashSalePurchaseContextSevice;
	//点击立即秒杀按钮时的动作
	//必须在登陆以后才可以进行一下操作
	@RequestMapping(value="/api/start/buy/falsh/{flashid}",method=RequestMethod.POST)
	public Result<FlashSaleUserVo> start(@PathVariable String flashid,@Valid @RequestBody CartVO cart,BindingResult valid, HttpSession session,HttpServletRequest request)throws Exception{
		if(valid.hasErrors()){
			return Result.fail("您提交的订单数据不完整，请核实后重新提交！");
		}
		Optional<FlashSale> flashsale=flashSaleService.single(flashid);
		if(!flashsale.isPresent()){
			return Result.fail("该商品不存在。");
		}
		String uid = Login.UID(session);
		uid = "Ma9ogkIXSW-y0uSrvfqVIQ";
		System.out.println(Login.UID(request));
		Optional<Users> user = userService.single(uid);
		
		//库存决定是否是活动产品
		Result<String> checkstart=flashSalePurchaseContextSevice.checkStart(flashsale.get());
		if(checkstart.isBad()){
			return Result.fail(checkstart.getMessage());
		}
		//买秒杀产品的用户的信息
			FlashSaleUser flashSaleUser = new FlashSaleUser();
			flashSaleUser.setId(KeyGen.uuid());
			flashSaleUser.setFlashId(flashsale.get().getId());
			flashSaleUser.setUid(uid);
			flashSaleUser.setFlashSale(flashsale.get());
			flashSaleUser.setUser(user.get());
			flashSaleUser.setOrderStatus(OrderStatusDesc.NEW_CODE);//新形成的订单，未支付状态
			flashSaleUser.setOrderTime(new Date());//形成新订单的时间
		
			
		//	
		CartVO cartVO = convertCartVO(flashid, uid,cart);
		Orders orders = new Orders();
		BeanUtils.copyProperties(cartVO.getOrders().get(0), orders);
		flashSalePurchaseContextSevice.startSale(user.get(), flashSaleUser, orders);
		session.setAttribute(CartController.GOODS_IN_CART_TO_CLEARING, new ObjectMapper().writeValueAsString(cartVO));
		FlashSaleUserVo vo = new FlashSaleUserVo();
		BeanUtils.copyProperties(flashSaleUser, vo);
		
		return Result.ok(vo);
		
	}
	private CartVO convertCartVO(String flashid,String uid, CartVO cart1) throws Exception{
		Optional<Address> addressc = addressService.getDefaultAddress(uid);
		if(addressc.isPresent()){
			Address addr = addressc.get();
			cart1.setAddress(addr.getProvinceName()+"-"+addr.getCityName()+"-"+addr.getAddress());
			cart1.setAddressid(addr.getID());
			cart1.setCity(addr.getCity());
			cart1.setMobile(addr.getMobile());
			cart1.setReceiver(addr.getReceiver());
		}
		List<OrdersInCartVO> orders = cart1.getOrders();
		BigDecimal cartprice = BigDecimal.ZERO;
		BigDecimal cartpricewithoutpostage = BigDecimal.ZERO;
		for (OrdersInCartVO order : orders) {
			order.setID(KeyGen.uuid());
			order.setOrderno(UniqueSequence.next18());
			//纯订单价格
			BigDecimal orderprice = BigDecimal.ZERO;
			order.setExtradata(flashid);
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
		cart1.setTotalPrice(cartprice);
		cart1.setTotalPriceWithoutPostage(cartpricewithoutpostage);
		return cart1;
	}

}
