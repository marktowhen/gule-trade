package com.jingyunbank.etrade.order.postsale.service.context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.order.postsale.bo.Refund;
import com.jingyunbank.etrade.api.order.postsale.service.IRefundCertificateService;
import com.jingyunbank.etrade.api.order.postsale.service.IRefundService;
import com.jingyunbank.etrade.api.order.postsale.service.context.IRefundContextService;

@Service("refundContextService")
public class RefundContextService implements IRefundContextService{

	@Autowired
	private IRefundService refundService;
	@Autowired
	private IRefundCertificateService refundCertificateService;
	
	@Override
	public void request(Refund refund) throws DataSavingException{
		refundService.save(refund);
		refundCertificateService.save(refund.getCertificates());
	}

	@Override
	public void denyRefund(Refund refund) {
		
	}

	@Override
	public void approveRefund(Refund refund) {
		
	}

	@Override
	public boolean canRefund(String oid) {
		return false;
	}

}
