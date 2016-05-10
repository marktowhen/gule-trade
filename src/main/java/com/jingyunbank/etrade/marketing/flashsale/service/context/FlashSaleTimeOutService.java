package com.jingyunbank.etrade.marketing.flashsale.service.context;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.marketing.flashsale.bo.FlashSale;
import com.jingyunbank.etrade.api.marketing.flashsale.bo.FlashSaleOrder;
import com.jingyunbank.etrade.api.marketing.flashsale.bo.FlashSaleUser;
import com.jingyunbank.etrade.api.marketing.flashsale.service.IFlashSaleOrderService;
import com.jingyunbank.etrade.api.marketing.flashsale.service.IFlashSaleService;
import com.jingyunbank.etrade.api.marketing.flashsale.service.IFlashSaleUserService;
import com.jingyunbank.etrade.api.marketing.flashsale.service.context.IFlashSalePurchaseContextService;
import com.jingyunbank.etrade.api.marketing.flashsale.service.context.IFlashSaleTimeOutService;
import com.jingyunbank.etrade.api.order.presale.service.context.IOrderContextService;
import com.jingyunbank.etrade.config.PropsConfig;
import com.jingyunbank.etrade.marketing.group.service.context.GroupRobotService;

@Service
public class FlashSaleTimeOutService implements IFlashSaleTimeOutService{
	@Autowired
	private IFlashSaleUserService flashSaleUserService;
	@Autowired
	private IFlashSalePurchaseContextService flashSalePurchaseContextService;
	private Logger logger = LoggerFactory.getLogger(FlashSaleTimeOutService.class);
	@Override
	@Transactional
	public void payTimeout(){
		List<FlashSaleUser> boList= flashSaleUserService.listByStatus();
		for (FlashSaleUser flashSaleUser : boList) {
			Date outTimer=new Date(flashSaleUser.getOrderTime().getTime()+PropsConfig.getLong(PropsConfig.GROUP_PAY_TIME_OUT)*1000);
			if(outTimer.before(new Date())){
				try {
					flashSalePurchaseContextService.payTimeOut(flashSaleUser);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					logger.error("payTimeout Fail: flashSaleID="+flashSaleUser.getId()+"  reason"+e.getMessage());
				} 
			}
		}
	}
}
