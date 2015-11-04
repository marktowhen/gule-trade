package com.jingyunbank.etrade.order.service.context;

import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.exception.OrderDeliveringException;
import com.jingyunbank.etrade.api.exception.OrderGenerateException;
import com.jingyunbank.etrade.api.exception.OrderPaidException;
import com.jingyunbank.etrade.api.exception.OrderPayException;
import com.jingyunbank.etrade.api.exception.OrderPayFailException;
import com.jingyunbank.etrade.api.exception.OrderUpdateException;
import com.jingyunbank.etrade.api.order.bo.Orders;
import com.jingyunbank.etrade.api.order.bo.Refund;
import com.jingyunbank.etrade.api.order.service.context.IOrderContextService;

@Service("orderContextService")
public class OrderContextService implements IOrderContextService {

	@Override
	public void generate(Orders order) throws OrderGenerateException {

	}

	@Override
	public void update(Orders order) throws OrderUpdateException {

	}

	@Override
	public void pay(Orders order) throws OrderPayException {

	}

	@Override
	public void paid(String orderno) throws OrderPaidException {

	}

	@Override
	public void payfail(String orderno) throws OrderPayFailException {

	}

	@Override
	public void delivering(String orderno) throws OrderDeliveringException {

	}

	@Override
	public void delivered(String orderno) {

	}

	@Override
	public void received(String orderno) {

	}

	@Override
	public void cancel(String orderno, String reason) {

	}

	@Override
	public void remove(String orderno) {

	}

	@Override
	public void refund(Refund refund) {

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
