package com.jingyunbank.etrade.marketing.group.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
import com.jingyunbank.etrade.api.marketing.group.bo.Group;
import com.jingyunbank.etrade.api.marketing.group.bo.GroupGoods;
import com.jingyunbank.etrade.api.marketing.group.service.IGroupGoodsService;
import com.jingyunbank.etrade.api.marketing.group.service.IGroupService;
import com.jingyunbank.etrade.api.marketing.group.service.context.IGroupPurchaseContextService;
import com.jingyunbank.etrade.api.order.presale.bo.Orders;
import com.jingyunbank.etrade.api.user.bo.Address;
import com.jingyunbank.etrade.api.user.bo.Users;
import com.jingyunbank.etrade.api.user.service.IAddressService;
import com.jingyunbank.etrade.api.user.service.IUserService;
import com.jingyunbank.etrade.cart.bean.CartVO;
import com.jingyunbank.etrade.cart.bean.GoodsInCartVO;
import com.jingyunbank.etrade.cart.bean.OrdersInCartVO;
import com.jingyunbank.etrade.cart.controller.CartController;
import com.jingyunbank.etrade.marketing.group.bean.GroupVO;

@RestController
@RequestMapping("/api/marketing/group")
public class GroupPurchaseController {

	@Autowired
	private IGroupPurchaseContextService groupPurchaseContextService;
	@Autowired
	private IGroupGoodsService groupGoodsService;
	@Autowired
	private IGroupService groupService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IAddressService addressService;

	// 开团 
//	@AuthBeforeOperation
	@RequestMapping(value = "/purchase/start/{groupgoodsid}", method = RequestMethod.POST)
	public Result<GroupVO> start(@PathVariable String groupgoodsid,
			@Valid @RequestBody CartVO cart,
			BindingResult valid, 
			HttpSession session) throws Exception {
		if(valid.hasErrors()){
			return Result.fail("您提交的订单数据不完整，请核实后重新提交！");
		}
		Optional<GroupGoods> goods = groupGoodsService.single(groupgoodsid);
		if (!goods.isPresent()) {
			return Result.fail("团购商品不存在。");
		}
		String uid = Login.UID(session);
		uid = "Ma9ogkIXSW-y0uSrvfqVIQ";
		Optional<Users> leader = userService.single(uid);
		//业务校验 比如库存 团购截止时间等
		Result<String> startMatch = groupPurchaseContextService.startMatch(goods.get());
		if(startMatch.isBad()){
			return Result.fail(startMatch.getMessage());
		}
		//组装group
		Group group = new Group();
		group.setID(KeyGen.uuid());
		group.setGroupGoodsID(goods.get().getID());
		group.setLeaderUID(leader.get().getID());
		group.setStart(new Date());
		group.setStatus(Group.STATUS_NEW);
		group.setLeader(leader.get());
		group.setGoods(goods.get());
		
		//提交
		CartVO cartVO = convertCartVO(group.getID(), leader.get().getID(),cart);
		Orders orders = new Orders();
		BeanUtils.copyProperties(cartVO.getOrders().get(0), orders);
		groupPurchaseContextService.start(leader.get(), group,orders );
		session.setAttribute(CartController.GOODS_IN_CART_TO_CLEARING, new ObjectMapper().writeValueAsString(cartVO));
		GroupVO vo = new GroupVO();
		BeanUtils.copyProperties(group, vo);
		return Result.ok(vo);
	}

	// 参团 
//	@AuthBeforeOperation
	@RequestMapping(value = "/purchase/join/{groupid}", method = RequestMethod.POST)
	public Result<String> join(@PathVariable String groupid,
			@Valid @RequestBody CartVO cart,
			BindingResult valid,  HttpSession session)
			throws Exception {

		Optional<Group> group = groupService.single(groupid);
		if (!group.isPresent()) {
			return Result.fail("您申请加入的团不存在。");
		}
		Optional<GroupGoods> goods = groupGoodsService.single(group.get().getGroupGoodsID());
		if (!goods.isPresent()) {
			return Result.fail("团购商品不存在。");
		}
		group.get().setGoods(goods.get());
		String  uid = Login.UID(session);
		uid = "Ma9ogkIXSW-y0uSrvfqVIQ";
		Optional<Users> user = userService.single(uid);
		//业务校验 比如库存 团购截止时间等
		Result<String> joinMatch = groupPurchaseContextService.joinMatch(group.get());
		if(joinMatch.isBad()){
			return Result.fail(joinMatch.getMessage());
		}
		
		CartVO cartVO = convertCartVO(groupid, user.get().getID(),cart);
		Orders orders = new Orders();
		BeanUtils.copyProperties(cartVO.getOrders().get(0), orders);
		groupPurchaseContextService.join(user.get(), group.get(), orders);
		
		session.setAttribute(CartController.GOODS_IN_CART_TO_CLEARING, new ObjectMapper().writeValueAsString(cartVO));
		return Result.ok();
	}
	
	
	private CartVO convertCartVO(String groupid,String uid, CartVO cart1) throws Exception{
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
			order.setExtradata(groupid);
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
