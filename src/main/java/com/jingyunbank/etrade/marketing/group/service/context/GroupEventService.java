package com.jingyunbank.etrade.marketing.group.service.context;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.marketing.group.service.context.IGroupEventService;
import com.jingyunbank.etrade.api.marketing.group.service.context.IGroupPurchaseContextService;
import com.jingyunbank.etrade.api.order.presale.bo.Orders;
import com.jingyunbank.etrade.api.order.presale.service.IOrderService;

@Service("groupEventService")
public class GroupEventService implements IGroupEventService {

	@Autowired
	private IOrderService orderService;
	@Autowired
	private IGroupPurchaseContextService groupPurchaseContextService;
	
	/**
	 * 支付成功 
	 * @param content 订单号
	 * 2016年4月21日 qxs
	 */
	@JmsListener(destination="PAYSUCCESS_CALLBACK", containerFactory="queueJmsListnerContainer")
	public void paysuccess(String content){
		orderService.listByExtransno(content).forEach( order->{
			//如果是团购
			if(Orders.GROUP_ORDER_TYPE.equals(order.getType())){
				try {
					groupPurchaseContextService.payFinish(order);
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("PAYSUCCESS_CALLBACK:团购处理失败:"+e.getMessage());
				}
			}
		});
	}
	/**
	 * 支付失败
	 * @param content 订单号
	 * 2016年4月21日 qxs
	 */
	@JmsListener(destination="PAYFAILURE_CALLBACK", containerFactory="queueJmsListnerContainer")
	public void payfail(String content){
	orderService.listByExtransno(content).forEach( order->{
			//如果是团购
			if(Orders.GROUP_ORDER_TYPE.equals(order.getType())){
				try {
					groupPurchaseContextService.payFinish(order);
				} catch (Exception e) {
					logger.error("PAYFAILURE_CALLBACK:团购处理失败:"+e.getMessage());
				}
			}
		});
	}
	
	
	private Logger logger = LoggerFactory.getLogger(GroupEventService.class);
}
