package com.jingyunbank.etrade.merchant.service.context;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.merchant.bo.DeliveryType;
import com.jingyunbank.etrade.api.merchant.bo.InvoiceType;
import com.jingyunbank.etrade.api.merchant.bo.Merchant;
import com.jingyunbank.etrade.api.merchant.service.IMerchantService;
import com.jingyunbank.etrade.api.merchant.service.context.IMerchantContextService;
import com.jingyunbank.etrade.goods.service.ServiceTemplate;
/**
 * 
 * @author liug
 *
 */
@Service("merchantContextService")
public class MerchantContextService extends ServiceTemplate implements IMerchantContextService{
	@Autowired
	private IMerchantService merchantService;

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public boolean save(Merchant merchant) throws DataSavingException {
		boolean rlt = false;
		try {
			merchantService.saveMerchant(merchant);
			merchantService.saveMerchantInvoiceType(merchant);
			merchantService.saveMerchantDeliveryType(merchant);
			rlt = true;
		} catch (Exception e) {
			throw new DataSavingException(e);
		}
		return rlt;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public boolean refresh(Merchant merchant) throws DataRefreshingException {
		boolean rlt = false;
		try {
			merchantService.updateMerchant(merchant);
			merchantService.removeMerchantInvoiceType(merchant);
			merchantService.saveMerchantInvoiceType(merchant);
			merchantService.removeMerchantDeliveryType(merchant);
			merchantService.saveMerchantDeliveryType(merchant);
			rlt = true;
		} catch (Exception e) {
			throw new DataRefreshingException(e);
		}
		return rlt;
	}
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public Optional<Merchant> singleByMID(String mid) throws IllegalAccessException, InvocationTargetException {
		
		Optional<Merchant> merchant = merchantService.getMerchant(mid);
		Merchant bo = merchant.get();
		//根据MID查询发票类型信息
		List<InvoiceType> listinvoice = merchantService.listInvoiceType(mid);
		//根据MID查询邮寄类型信息
		List<DeliveryType> listdelivery = merchantService.listDeliveryType(mid);
		bo.setInvoiceList(listinvoice);
		bo.setDeliverylist(listdelivery);
		return merchant;
	}
	
}
