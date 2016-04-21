package com.jingyunbank.etrade.marketing.group.service.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.marketing.group.service.context.IGroupEventService;

@Service("groupEventService")
public class GroupEventService implements IGroupEventService {

	

	
	/**
	 * 支付成功 
	 * @param content 订单号
	 * 2016年4月21日 qxs
	 */
	@JmsListener(destination="PAYSUCCESS_CALLBACK", containerFactory="queueJmsListnerContainer")
	public void paysuccess(String content){
//		try {
//			orderContextService.paysuccess(content);
//		} catch (DataRefreshingException | DataSavingException e) {
//			logger.error("处理支付成功是出现异常：" + e.toString());
//		}
	}
	/**
	 * 支付失败
	 * @param content 订单号
	 * 2016年4月21日 qxs
	 */
	@JmsListener(destination="PAYFAILURE_CALLBACK", containerFactory="queueJmsListnerContainer")
	public void payfail(String content){
//		try {
//			orderContextService.payfail(content, "");
//		} catch (DataRefreshingException | DataSavingException e) {
//			logger.error("处理支付失败时出现异常：" + e.toString());
//		}
	}
	
	
	private Logger logger = LoggerFactory.getLogger(GroupEventService.class);
}
