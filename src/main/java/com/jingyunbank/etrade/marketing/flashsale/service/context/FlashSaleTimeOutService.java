package com.jingyunbank.etrade.marketing.flashsale.service.context;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
import com.jingyunbank.etrade.api.marketing.flashsale.service.context.IFlashSaleTimeOutService;
import com.jingyunbank.etrade.api.order.presale.service.context.IOrderContextService;
import com.jingyunbank.etrade.config.PropsConfig;

@Service
public class FlashSaleTimeOutService implements IFlashSaleTimeOutService{
	@Autowired
	private IFlashSaleUserService flashSaleUserService;
	@Autowired
	private IFlashSaleOrderService flashSaleOrderService;
	@Autowired
	private IFlashSaleService flashSaleService;
	@Autowired
	private IOrderContextService orderContextService;
	
	@Override
	@Transactional
	public void payTimeout()throws DataRefreshingException, DataSavingException{
		List<FlashSaleUser> boList= flashSaleUserService.listByStatus();
		for (FlashSaleUser flashSaleUser : boList) {
			Date outTimer=new Date(flashSaleUser.getOrderTime().getTime()+PropsConfig.getLong(PropsConfig.GROUP_PAY_TIME_OUT)*1000);
			if(outTimer.before(new Date())){
				try {
					Optional<FlashSale> bo=flashSaleService.single(flashSaleUser.getFlashId());
					FlashSale flashSale=bo.get();
					flashSale.setStock(flashSale.getStock()+1);//默认他的数量是1的情况下的写法！逾期活动的数量恢复
					flashSaleService.refreshStock(flashSale);//超时修改活动商品的数量
					flashSaleUserService.refreshStatus(flashSaleUser.getId(),FlashSaleUser.TIMEOUT);//修改订单状态
					Optional<FlashSaleOrder> orderbo=flashSaleOrderService.singleByUid(flashSaleUser.getId());
					//为防止用户支付已关闭的订单 更改Orders状态
					orderContextService.cancel(Arrays.asList(orderbo.get().getOid()), "超时关闭");
				} catch (DataRefreshingException e) {
					// TODO Auto-generated catch block
					throw new DataRefreshingException(e);
				}
			}
		}
		
	}

	
}
