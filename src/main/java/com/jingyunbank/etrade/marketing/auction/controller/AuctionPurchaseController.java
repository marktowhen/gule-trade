package com.jingyunbank.etrade.marketing.auction.controller;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
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
import com.jingyunbank.core.Result;
import com.jingyunbank.core.util.UniqueSequence;
import com.jingyunbank.core.web.Login;
import com.jingyunbank.etrade.api.marketing.auction.bo.AuctionGoods;
import com.jingyunbank.etrade.api.marketing.auction.bo.AuctionPriceLog;
import com.jingyunbank.etrade.api.marketing.auction.bo.AuctionUser;
import com.jingyunbank.etrade.api.marketing.auction.service.IAuctionGoodsService;
import com.jingyunbank.etrade.api.marketing.auction.service.IAuctionPriceLog;
import com.jingyunbank.etrade.api.marketing.auction.service.IAuctionUserService;
import com.jingyunbank.etrade.api.marketing.auction.service.context.IAuctionContextService;
import com.jingyunbank.etrade.api.order.presale.bo.Orders;
import com.jingyunbank.etrade.api.user.bo.Address;
import com.jingyunbank.etrade.api.user.service.IAddressService;
import com.jingyunbank.etrade.cart.bean.CartVO;
import com.jingyunbank.etrade.cart.bean.GoodsInCartVO;
import com.jingyunbank.etrade.cart.bean.OrdersInCartVO;
import com.jingyunbank.etrade.cart.controller.CartController;
import com.jingyunbank.etrade.marketing.auction.bean.AuctionGoodsVO;
import com.jingyunbank.etrade.marketing.auction.bean.AuctionPriceLogVO;

@RestController
@RequestMapping("/api/marketing/auction/purchase")
public class AuctionPurchaseController {
	
	@Autowired
	private IAuctionPriceLog auctionPriceLogService;
	@Autowired
	private IAuctionUserService auctionUserService;
	@Autowired
	private IAddressService addressService;
	@Autowired
	private IAuctionContextService auctionContextService;
	@Autowired
	private IAuctionGoodsService auctionGoodsService;
	
		/**
		 * 报名
		 * @param auctionid
		 * @param cart
		 * @param valid
		 * @param session
		 * @return
		 * @throws Exception 
		 */
		@RequestMapping(value="/signUp/{auctionid}", method=RequestMethod.POST)
		public Result<AuctionGoodsVO> signup(@PathVariable String auctionid,
				@Valid @RequestBody CartVO cart,
				BindingResult valid, 
				HttpSession session,HttpServletRequest request) throws Exception{
			String userid = Login.UID(request);
			//userid="Ma9ogkIXSW-y0uSrvfqVIQ";
			Login.UID(session, userid);//存放登录的userid
			//组装参拍人
			AuctionUser auctionUser=new AuctionUser();
			auctionUser.setID(KeyGen.uuid());
			auctionUser.setAuctionGoodsID(auctionid);
			auctionUser.setUID(userid);
			auctionUser.setStatus(AuctionUser.STATUS_NEW);
			auctionUser.setJoinTime(new Date());
			CartVO cartVO = convertCartVO(auctionid, userid,cart);
			Orders orders = new Orders();
			session.setAttribute("AUCTION_ID", auctionid);
			session.setAttribute(CartController.GOODS_IN_CART_TO_CLEARING, new ObjectMapper().writeValueAsString(cartVO));
			BeanUtils.copyProperties(cartVO.getOrders().get(0), orders);
			auctionContextService.signUp(auctionUser, orders);
			
			return Result.ok(new AuctionGoodsVO());
		}
	
		
		/**
		 * 出价
		 * @param auctionGoodsid
		 * @param price
		 * @return
		 * @throws Exception
		 */
		@RequestMapping(value="/bidding", method=RequestMethod.GET)
		public Result<Object> bidding(
				@RequestParam String auctionid,int price,HttpSession session,HttpServletRequest request) throws Exception{
			String userid = Login.UID(request);
			//userid="Ma9ogkIXSW-y0uSrvfqVIQ";
			String auctionUserid="";
			Optional<AuctionUser> auctionUser=auctionUserService.selByUserId(auctionid, userid);
			AuctionPriceLog priceLog=new AuctionPriceLog();
			if(auctionUser.isPresent()){
				AuctionUser auctionU=auctionUser.get();
				auctionUserid=auctionU.getID();
				priceLog.setID(KeyGen.uuid());
				priceLog.setAuctionGoodsID(auctionid);
				priceLog.setUID(userid);
				priceLog.setPrice(new BigDecimal(price));
				priceLog.setAuctionUserID(auctionUserid);
				priceLog.setAddtime(new Date());
			}else{
				return Result.fail("您还未报名,请报名后参与竞拍");
			}
			auctionContextService.delayed(auctionid);
			boolean flag=auctionPriceLogService.save(priceLog);
			if(flag){
				return Result.ok("出价成功");
			}else{
				return Result.fail("出价失败");
			}
			
		}
		
		/**
		 * 支付
		 * @param auctionid
		 * @param cart
		 * @param valid
		 * @param session
		 * @return
		 * @throws Exception 
		 */
		@RequestMapping(value="/payFinal/{auctionid}", method=RequestMethod.POST)
		public Result<AuctionGoodsVO> PayFinal(@PathVariable String auctionid,
				@Valid @RequestBody CartVO cart,
				BindingResult valid, 
				HttpSession session,HttpServletRequest request) throws Exception{
			
			String userid = Login.UID(request);
			//userid="Ma9ogkIXSW-y0uSrvfqVIQ";
			
			AuctionUser auctionUser=new AuctionUser();
			Optional<AuctionUser> auctionUsers=auctionUserService.selByUserId(auctionid, userid);
			if(auctionUsers.isPresent()){
				AuctionUser auctionU=auctionUsers.get();
				auctionUser.setID(auctionU.getID());
				
			}
			auctionUser.setAuctionGoodsID(auctionid);
			
			Login.UID(session, userid);//存放登录的userid
			//组装参拍人
			CartVO cartVO = convertCartVO(auctionid, userid,cart);
			Orders orders = new Orders();
			//session.setAttribute("AUCTION_ID", auctionid);
			session.setAttribute(CartController.GOODS_IN_CART_TO_CLEARING, new ObjectMapper().writeValueAsString(cartVO));
			BeanUtils.copyProperties(cartVO.getOrders().get(0), orders);
			auctionContextService.payFinal(auctionUser, orders);
			
			return Result.ok(new AuctionGoodsVO());
		}
		
	    /**
	     * 查询竞拍出价次数
	     * @param auctionGoodsID
	     * @return
	     * @throws Exception
	     */
		@RequestMapping(value="/count", method=RequestMethod.GET)
		public Result<Integer> count(
				@RequestParam String auctionGoodsID) throws Exception{
			return Result.ok(new Integer(auctionPriceLogService.list(auctionGoodsID).size()));
		}
		
		/**
		 * 查询当前竞拍出价记录
		 * @param auctionGoodsID
		 * @return
		 * @throws Exception
		 */
		@RequestMapping(value="/listPriceLog", method=RequestMethod.GET)
		public Result<List<AuctionPriceLogVO>> list(@RequestParam String auctionGoodsID) throws Exception{
			List<AuctionPriceLog> list=auctionPriceLogService.list(auctionGoodsID);
			List<AuctionPriceLogVO> listvo=new ArrayList<AuctionPriceLogVO>();
			if(list!=null&&list.size()>0){
				for (int i = 0; i < list.size(); i++) {
					listvo.add(getShowVOFromBo(list.get(i)));
				}
			}
			return Result.ok(listvo);
		}
		
		
		
		private AuctionPriceLogVO getShowVOFromBo(AuctionPriceLog showBo){
			AuctionPriceLogVO vo = new AuctionPriceLogVO();
			if(Objects.nonNull(showBo)){
				BeanUtils.copyProperties(showBo, vo);
				
			}
			return vo;
		}
		
		
		/**
		 * 订单生成
		 * @param groupid
		 * @param uid
		 * @param cart1
		 * @return
		 * @throws Exception
		 */
		private CartVO convertCartVO(String auctionid,String uid, CartVO cart1) throws Exception{
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
				order.setExtradata(auctionid);
				
				//纯邮费
				BigDecimal orderpostage = BigDecimal.valueOf(10);
				List<GoodsInCartVO> goods = order.getGoods();
				Optional<AuctionGoods> auctionGoods = auctionGoodsService.single(auctionid);
				AuctionGoods auctionGood=new AuctionGoods();
				if(auctionGoods.isPresent()){
					auctionGood=auctionGoods.get();
				if(order.getType().equals("AUCTION")){
					//报名
					orderprice=auctionGood.getDeposit();
					//满99包邮（因为无法获取到收货地址信息，所有暂且按99包邮算，待选择收货地址后，再刷新邮费）
					orderpostage = BigDecimal.ZERO;//报名时没有邮费
					//orderpostage = (orderprice.compareTo(BigDecimal.valueOf(68)) >= 0 ? BigDecimal.ZERO : orderpostage);
					//无邮费纯价格
					cartpricewithoutpostage = cartpricewithoutpostage.add(orderprice);
					order.setPostage(orderpostage);
					order.setPrice(orderprice.add(orderpostage));
					cartprice = cartprice.add(order.getPrice());
					
				/*	for (GoodsInCartVO gs : goods) {
						BigDecimal gspprice = gs.getPprice();
						BigDecimal gsprice = gs.getPrice();
						int gscount = gs.getCount();
						orderprice = orderprice.add(
								(Objects.nonNull(gspprice)?gspprice:gsprice)
								.multiply(BigDecimal.valueOf(gscount)).setScale(2)
								);
					}*/
					
				}else if(order.getType().equals("AUCTIONFINAL")){
					//支付尾款
					//当前默认售价高于订金
					orderprice=auctionGood.getSoldPrice().subtract(auctionGood.getDeposit());
					//满99包邮（因为无法获取到收货地址信息，所有暂且按99包邮算，待选择收货地址后，再刷新邮费）
					orderpostage = (orderprice.compareTo(BigDecimal.valueOf(68)) >= 0 ? BigDecimal.ZERO : orderpostage);
					order.setPostage(orderpostage);
					//无邮费纯价格
					cartpricewithoutpostage = cartpricewithoutpostage.add(orderprice);
					order.setPrice(orderprice.add(orderpostage));
					cartprice = cartprice.add(order.getPrice());
					
				 }
				
			   }
			}
			
			cart1.setTotalPrice(cartprice);
			cart1.setTotalPriceWithoutPostage(cartpricewithoutpostage);
			return cart1;
		}
		
		
}
