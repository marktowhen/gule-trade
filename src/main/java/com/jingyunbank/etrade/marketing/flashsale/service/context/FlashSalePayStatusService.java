package com.jingyunbank.etrade.marketing.flashsale.service.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.marketing.flashsale.service.context.IFlashSalePurchaseContextService;
import com.jingyunbank.etrade.api.order.presale.bo.Orders;
import com.jingyunbank.etrade.api.order.presale.service.IOrderService;
@Service
public class FlashSalePayStatusService{
	@Autowired
	private IOrderService orderService;
	@Autowired
	private IFlashSalePurchaseContextService flashSalePurchaseContextService;
	/**
	 * 支付成功
	 * @param content
	 */
	@JmsListener(destination="PAYSUCCESS_CALLBACK", containerFactory="topicJmsListnerContainer")
	public void paySuccess(String content){
		orderService.listByExtransno(content).forEach( order->{
			//如果是秒杀
			if(Orders.FLASH_SALE_TYPE.equals(order.getType())){
				try {
					flashSalePurchaseContextService.paySuccess(order);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					logger.error("PAYSUCCESS_CALLBACK:秒杀处理失败:"+e.getMessage());
				}
			}
		});
	}
	
	@JmsListener(destination="PAYFAILURE_CALLBACK", containerFactory="topicJmsListnerContainer")
	public void payFail(String content){
		orderService.listByExtransno(content).forEach( order->{
			//如果是秒杀
			if(Orders.FLASH_SALE_TYPE.equals(order.getType())){
				try {
					flashSalePurchaseContextService.payFail(order);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					logger.error("PAYSUCCESS_CALLBACK:秒杀处理失败:"+e.getMessage());
				}
			}
		});
	}
	
	private Logger logger = LoggerFactory.getLogger(FlashSalePayStatusService.class);
	
}
