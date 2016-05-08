package com.jingyunbank.etrade.marketing.flashsale.service.context;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.core.KeyGen;
import com.jingyunbank.core.Result;
import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.marketing.flashsale.bo.FlashSale;
import com.jingyunbank.etrade.api.marketing.flashsale.bo.FlashSaleOrder;
import com.jingyunbank.etrade.api.marketing.flashsale.bo.FlashSaleUser;
import com.jingyunbank.etrade.api.marketing.flashsale.service.IFlashSaleOrderService;
import com.jingyunbank.etrade.api.marketing.flashsale.service.IFlashSaleUserService;
import com.jingyunbank.etrade.api.marketing.flashsale.service.context.IFlashSalePurchaseContextService;
import com.jingyunbank.etrade.api.order.presale.bo.OrderStatusDesc;
import com.jingyunbank.etrade.api.order.presale.bo.Orders;
import com.jingyunbank.etrade.api.user.bo.Users;
@Service
public class FlashSalePurchaseContextSevice implements IFlashSalePurchaseContextService{
	@Autowired 
	private IFlashSaleUserService flashSaleUserService;
	@Autowired
	private IFlashSaleOrderService flashSaleOrderService;

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
		flashSaleOrder.setUid(flashSaleUser.getId());
		flashSaleOrder.setOid(orders.getID());
		flashSaleOrder.setOrderno(orders.getOrderno());
		flashSaleOrder.setType("");
		flashSaleOrderService.save(flashSaleOrder);
	}
	
	
	public void paySuccess(Orders order) throws DataRefreshingException{
		Optional<FlashSaleOrder> flashSaleOrder = flashSaleOrderService.single(order.getID());
		if(flashSaleOrder.isPresent()){
			Optional<FlashSaleUser> flashSaleUser=flashSaleUserService.single(flashSaleOrder.get().getUid());
			if(flashSaleUser.isPresent()){
				flashSaleUserService.refreshStatus(flashSaleUser.get().getId(), flashSaleUser.get().getOrderStatus(),OrderStatusDesc.PAID_CODE);
			}
		}
	}

	@Override
	public void payFail(Orders order) throws DataRefreshingException {
		Optional<FlashSaleOrder> flashSaleOrder = flashSaleOrderService.single(order.getID());
		if(flashSaleOrder.isPresent()){
			Optional<FlashSaleUser> flashSaleUser=flashSaleUserService.single(flashSaleOrder.get().getUid());
			if(flashSaleUser.isPresent()){
				flashSaleUserService.refreshStatus(flashSaleUser.get().getId(), flashSaleUser.get().getOrderStatus(),OrderStatusDesc.PAYFAIL_CODE);
			}
		}
		
	}
	
	
	

}
