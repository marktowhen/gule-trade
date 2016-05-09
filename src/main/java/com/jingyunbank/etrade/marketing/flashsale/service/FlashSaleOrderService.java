package com.jingyunbank.etrade.marketing.flashsale.service;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.marketing.flashsale.bo.FlashSaleOrder;
import com.jingyunbank.etrade.api.marketing.flashsale.service.IFlashSaleOrderService;
import com.jingyunbank.etrade.marketing.flashsale.dao.FlashSaleOrderDao;
import com.jingyunbank.etrade.marketing.flashsale.entity.FlashSaleOrderEntity;
@Service
public class FlashSaleOrderService implements IFlashSaleOrderService{

	@Autowired
	private FlashSaleOrderDao flashSaleOrderDao;
	@Override
	public boolean save(FlashSaleOrder flashSaleOrder) throws DataSavingException {
		
		try {
			FlashSaleOrderEntity entity = new FlashSaleOrderEntity();
			BeanUtils.copyProperties(flashSaleOrder, entity);
			int num=flashSaleOrderDao.add(entity);
			if(num>0){
				return true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new DataSavingException(e);
		}
		return false;
	}
	@Override
	public Optional<FlashSaleOrder> single(String oid) {
		FlashSaleOrderEntity entity=flashSaleOrderDao.selectFlashOrderByoid(oid);
		FlashSaleOrder bo = new FlashSaleOrder();
		BeanUtils.copyProperties(entity, bo);
		return Optional.of(bo);
	}
	@Override
	public Optional<FlashSaleOrder> singleByUid(String flashUserId) {
		FlashSaleOrderEntity entity=flashSaleOrderDao.selectFlashOrderByuid(flashUserId);
		FlashSaleOrder bo = new FlashSaleOrder();
		BeanUtils.copyProperties(entity, bo);
		return Optional.of(bo);
	}

}
