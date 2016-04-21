package com.jingyunbank.etrade.marketing.group.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
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

import com.jingyunbank.core.KeyGen;
import com.jingyunbank.core.Result;
import com.jingyunbank.core.util.UniqueSequence;
import com.jingyunbank.core.web.AuthBeforeOperation;
import com.jingyunbank.core.web.Login;
import com.jingyunbank.etrade.api.marketing.group.bo.Group;
import com.jingyunbank.etrade.api.marketing.group.bo.GroupGoods;
import com.jingyunbank.etrade.api.marketing.group.service.IGroupGoodsService;
import com.jingyunbank.etrade.api.marketing.group.service.IGroupService;
import com.jingyunbank.etrade.api.marketing.group.service.context.IGroupPurchaseContextService;
import com.jingyunbank.etrade.api.order.presale.bo.OrderGoods;
import com.jingyunbank.etrade.api.order.presale.bo.OrderStatusDesc;
import com.jingyunbank.etrade.api.order.presale.bo.Orders;
import com.jingyunbank.etrade.api.user.bo.Users;
import com.jingyunbank.etrade.api.user.service.IUserService;
import com.jingyunbank.etrade.marketing.group.bean.GroupVO;
import com.jingyunbank.etrade.order.presale.bean.PurchaseGoodsVO;
import com.jingyunbank.etrade.order.presale.bean.PurchaseOrderVO;
import com.jingyunbank.etrade.order.presale.bean.PurchaseRequestVO;

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

	// 开团 
	@AuthBeforeOperation
	@RequestMapping(value = "/purchase/start/{groupgoodsid}", method = RequestMethod.POST)
	public Result<GroupVO> start(@PathVariable String groupgoodsid,
			@Valid @RequestBody PurchaseRequestVO purchase,
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
		Optional<Users> leader = userService.single(uid);
		if (!leader.isPresent())
			return Result.fail("不合法的操作，请先登录认证身份。");
		//业务校验 比如库存 团购截止时间等
		Result<String> startMatch = groupPurchaseContextService.startMatch(goods.get());
		if(startMatch.isBad()){
			return Result.fail(startMatch.getMessage());
		}
		Group group = new Group();
		group.setID(KeyGen.uuid());
		group.setGoods(goods.get());
		group.setLeader(leader.get());
		group.setStart(new Date());
		
		
		purchase.setUID(uid);
		purchase.setUname(Login.uname(session));
		purchase.setEmployee(Login.employee(session));
		List<Orders> orders = populateOrderData(purchase, session);
		
		groupPurchaseContextService.start(leader.get(), group, orders);
		GroupVO vo = new GroupVO();
		BeanUtils.copyProperties(group, vo);
		return Result.ok(vo);
	}

	// 参团 
	@AuthBeforeOperation
	@RequestMapping(value = "/purchase/join/{groupid}", method = RequestMethod.POST)
	public Result<String> join(@PathVariable String groupid, HttpSession session)
			throws Exception {

		Optional<Group> group = groupService.single(groupid);
		if (!group.isPresent()) {
			return Result.fail("您申请加入的团不存在。");
		}
		String uid = Login.UID(session);
		Optional<Users> user = userService.single(uid);
		if (!user.isPresent())
			return Result.fail("不合法的操作，请先登录认证身份。");
		groupPurchaseContextService.join(user.get(), group.get(), null);
		return Result.ok();
	}
	
	
	private List<Orders> populateOrderData(PurchaseRequestVO purchase,
			HttpSession session) throws Exception {
		
		List<PurchaseOrderVO> ordervos = purchase.getOrders();
		List<Orders> orders = new ArrayList<Orders>();
		
		for (PurchaseOrderVO ordervo : ordervos) {
			ordervo.setID(KeyGen.uuid());
			ordervo.setOrderno(UniqueSequence.next18());
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
	

}
