package com.jingyunbank.etrade.marketing.flashsale.service.context;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import com.jingyunbank.core.KeyGen;
import com.jingyunbank.core.Result;
import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.marketing.flashsale.bo.FlashSale;
import com.jingyunbank.etrade.api.marketing.flashsale.bo.FlashSaleOrder;
import com.jingyunbank.etrade.api.marketing.flashsale.bo.FlashSaleUser;
import com.jingyunbank.etrade.api.marketing.flashsale.service.IFlashSaleOrderService;
import com.jingyunbank.etrade.api.marketing.flashsale.service.IFlashSaleService;
import com.jingyunbank.etrade.api.marketing.flashsale.service.IFlashSaleUserService;
import com.jingyunbank.etrade.api.marketing.flashsale.service.context.IFlashSalePurchaseContextService;
import com.jingyunbank.etrade.api.order.presale.bo.OrderStatusDesc;
import com.jingyunbank.etrade.api.order.presale.bo.Orders;
import com.jingyunbank.etrade.api.order.presale.service.IOrderService;
import com.jingyunbank.etrade.api.order.presale.service.context.IOrderContextService;
import com.jingyunbank.etrade.api.user.bo.Users;
@Service
public class FlashSalePurchaseContextSevice implements IFlashSalePurchaseContextService{
	@Autowired
	private IFlashSaleUserService flashSaleUserService;
	@Autowired
	private IFlashSaleOrderService flashSaleOrderService;
	@Autowired
	private IFlashSaleService flashSaleService;
	@Autowired
	private IOrderContextService orderContextService;
	@Override
	public Result<String> checkStart(FlashSale flashsale) {
		if(flashsale.getStock()<=0){
			return Result.fail("该活动已经结束，请关注其他活动");
		}
		return Result.ok();
	}

	@Override
	public void startSale(Users user, FlashSaleUser flashSaleUser, Orders orders)
			throws DataSavingException, DataRefreshingException {
		
		flashSaleUserService.save(flashSaleUser);
		
		FlashSaleOrder flashSaleOrder = new FlashSaleOrder();
		flashSaleOrder.setId(KeyGen.uuid());
		flashSaleOrder.setFlashId(flashSaleUser.getFlashId());
		flashSaleOrder.setFlashUserId(flashSaleUser.getId());
		flashSaleOrder.setOid(orders.getID());
		flashSaleOrder.setOrderno(orders.getOrderno());
		flashSaleOrder.setType("");
		flashSaleOrderService.save(flashSaleOrder);
	}
	
	
	public void paySuccess(Orders order) throws DataRefreshingException{
		Optional<FlashSaleOrder> flashSaleOrder = flashSaleOrderService.single(order.getID());
		if(flashSaleOrder.isPresent()){
			Optional<FlashSaleUser> flashSaleUser=flashSaleUserService.single(flashSaleOrder.get().getFlashUserId());
			if(flashSaleUser.isPresent()){
				flashSaleUserService.refreshStatus(flashSaleUser.get().getId(), OrderStatusDesc.PAID_CODE);
			}
		}
	}

	@Override
	public void payFail(Orders order) throws DataRefreshingException {
		Optional<FlashSaleOrder> flashSaleOrder = flashSaleOrderService.single(order.getID());
		if(flashSaleOrder.isPresent()){
			Optional<FlashSaleUser> flashSaleUser=flashSaleUserService.single(flashSaleOrder.get().getFlashUserId());
			if(flashSaleUser.isPresent()){
				/*flashSaleUserService.refreshStatus(flashSaleUser.get().getId(), flashSaleUser.get().getOrderStatus(),OrderStatusDesc.PAYFAIL_CODE);*/
				flashSaleUserService.refreshStatus(flashSaleUser.get().getId(), OrderStatusDesc.PAYFAIL_CODE);
			}
		}
		
	}

	@Override
	public void payTimeOut(FlashSaleUser flashSaleUser) throws DataSavingException, DataRefreshingException {
		Optional<FlashSale> bo=flashSaleService.single(flashSaleUser.getFlashId());
		FlashSale flashSale=bo.get();
		flashSale.setStock(flashSale.getStock()+1);//默认他的数量是1的情况下的写法！逾期活动的数量恢复
		flashSaleService.refreshStock(flashSale);//超时修改活动商品的数量
		flashSaleUserService.refreshStatus(flashSaleUser.getId(),FlashSaleUser.TIMEOUT);//修改订单状态
		Optional<FlashSaleOrder> orderbo=flashSaleOrderService.singleByUid(flashSaleUser.getId());
		//为防止用户支付已关闭的订单 更改Orders状态
		orderContextService.cancel(Arrays.asList(orderbo.get().getOid()), "超时关闭");
	}
	
	

}
