package com.jingyunbank.etrade.order.postsale.service.context;

import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.order.postsale.bo.Refund;
import com.jingyunbank.etrade.api.order.postsale.service.context.IRefundContextService;

@Service("refundContextService")
public class RefundContextService implements IRefundContextService{

	@Override
	public void request(Refund refund) {
		
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
