package com.jingyunbank.etrade.order.postsale.service.context;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jingyunbank.core.KeyGen;
import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.exception.DataRemovingException;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.order.postsale.bo.Refund;
import com.jingyunbank.etrade.api.order.postsale.bo.RefundLogistic;
import com.jingyunbank.etrade.api.order.postsale.bo.RefundStatusDesc;
import com.jingyunbank.etrade.api.order.postsale.bo.RefundTrace;
import com.jingyunbank.etrade.api.order.postsale.service.IRefundCertificateService;
import com.jingyunbank.etrade.api.order.postsale.service.IRefundLogisticService;
import com.jingyunbank.etrade.api.order.postsale.service.IRefundService;
import com.jingyunbank.etrade.api.order.postsale.service.IRefundTraceService;
import com.jingyunbank.etrade.api.order.postsale.service.context.IRefundContextService;
import com.jingyunbank.etrade.api.order.presale.service.context.IOrderContextService;

@Service("refundContextService")
public class RefundContextService implements IRefundContextService{

	@Autowired
	private IRefundService refundService;
	@Autowired
	private IRefundCertificateService refundCertificateService;
	@Autowired
	private IRefundTraceService refundTraceService;
	@Autowired
	private IRefundLogisticService refundLogisticService;
	@Autowired
	private IOrderContextService orderContextService;
	
	@Override
	@Transactional
	public void request(Refund refund, String operator) throws DataSavingException, DataRefreshingException{
		refundService.save(refund);
		refundTraceService.save(createRefundTrace(refund, RefundStatusDesc.REQUEST, operator, "申请退款"));
		refundCertificateService.save(refund.getCertificates());
		orderContextService.refund(refund.getOID(), refund.getOGID());
	}
	
	@Override
	@Transactional
	public void refresh(Refund refund, String operator) throws DataSavingException, DataRefreshingException, DataRemovingException{
		refundService.refresh(refund);
		refundCertificateService.remove(refund.getID());
		refundCertificateService.save(refund.getCertificates());
		orderContextService.refund(refund.getOID(), refund.getOGID());
	}

	@Override
	@Transactional
	public void cancel(String RID, String operator, String note) throws DataRefreshingException, DataSavingException {
		Optional<Refund> candidate = refundService.single(RID);
		if(!candidate.isPresent()){
			return;
		}
		Refund refund = candidate.get();
		refundService.refreshStatus(RID, RefundStatusDesc.CANCEL);
		refundTraceService.save(createRefundTrace(refund, RefundStatusDesc.CANCEL, operator, note));
		orderContextService.cancelRefund(refund.getOID(), refund.getOGID());
	}

	@Override
	public void accept(String RID, String operator, String note) throws DataRefreshingException, DataSavingException {
		Optional<Refund> candidate = refundService.single(RID);
		if(!candidate.isPresent()){
			return;
		}
		Refund refund = candidate.get();
		boolean returnGoods = refund.isReturnGoods();
		if(returnGoods){
			refundService.refreshStatus(RID, RefundStatusDesc.ACCEPT);
			refundTraceService.save(createRefundTrace(refund, RefundStatusDesc.ACCEPT, operator, note));
		}else{
			refundService.refreshStatus(RID, RefundStatusDesc.DONE);
			refundTraceService.save(createRefundTrace(refund, RefundStatusDesc.DONE, operator, note));
		}
	}

	@Override
	public void deny(String RID, String operator, String note) throws DataRefreshingException, DataSavingException {
		Optional<Refund> candidate = refundService.single(RID);
		if(!candidate.isPresent()){
			return;
		}
		Refund refund = candidate.get();
		refundService.refreshStatus(RID, RefundStatusDesc.DENIED);
		refundTraceService.save(createRefundTrace(refund, RefundStatusDesc.DENIED, operator, note));
	}

	@Override
	public void doReturn(RefundLogistic logistic, String operator) throws DataSavingException, DataRefreshingException {
		Optional<Refund> candidate = refundService.single(logistic.getRID());
		if(!candidate.isPresent()){
			return;
		}
		Refund refund = candidate.get();
		refundService.refreshStatus(refund.getID(), RefundStatusDesc.RETURN);
		refundTraceService.save(createRefundTrace(refund, RefundStatusDesc.RETURN, operator, logistic.getReceiver()));
		refundLogisticService.save(logistic);
	}

	@Override
	public void done(String RID, String operator) throws DataRefreshingException, DataSavingException {
		Optional<Refund> candidate = refundService.single(RID);
		if(!candidate.isPresent()){
			return;
		}
		Refund refund = candidate.get();
		refundService.refreshStatus(RID, RefundStatusDesc.DONE);
		refundTraceService.save(createRefundTrace(refund, RefundStatusDesc.DONE, operator, ""));
		orderContextService.refundDone(refund.getOID(), refund.getOGID());
	}
	
	//创建订单新建追踪状态
	private RefundTrace createRefundTrace(Refund refund, RefundStatusDesc status, String operator, String note) {
		RefundTrace trace = new RefundTrace();
		trace.setAddtime(new Date());
		trace.setID(KeyGen.uuid());
		trace.setRID(refund.getID());
		trace.setOperator(operator);
		trace.setStatusCode(status.getCode());
		trace.setStatusName(status.getName());
		trace.setNote(note);
		return trace;
	}
}
