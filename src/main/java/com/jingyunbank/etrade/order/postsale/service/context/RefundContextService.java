package com.jingyunbank.etrade.order.postsale.service.context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jingyunbank.core.KeyGen;
import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.exception.DataRemovingException;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.exception.NoticeDispatchException;
import com.jingyunbank.etrade.api.order.postsale.bo.Refund;
import com.jingyunbank.etrade.api.order.postsale.bo.RefundLogistic;
import com.jingyunbank.etrade.api.order.postsale.bo.RefundStatusDesc;
import com.jingyunbank.etrade.api.order.postsale.bo.RefundTrace;
import com.jingyunbank.etrade.api.order.postsale.service.IRefundCertificateService;
import com.jingyunbank.etrade.api.order.postsale.service.IRefundLogisticService;
import com.jingyunbank.etrade.api.order.postsale.service.IRefundService;
import com.jingyunbank.etrade.api.order.postsale.service.IRefundTraceService;
import com.jingyunbank.etrade.api.order.postsale.service.context.IRefundContextService;
import com.jingyunbank.etrade.api.order.postsale.service.context.IRefundEventService;
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
	@Autowired
	private IRefundEventService refundEventService;
	
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
		refundService.refreshStatus(Arrays.asList(RID), RefundStatusDesc.CANCEL);
		refundTraceService.save(createRefundTrace(refund, RefundStatusDesc.CANCEL, operator, note));
		orderContextService.cancelRefund(refund.getOID(), refund.getOGID());
	}

	@Override
	public boolean accept(List<String> RIDs, String operator, String note) throws DataRefreshingException, DataSavingException {
		List<Refund> refunds = refundService.list(RIDs);
		if(refunds.size() == 0){
			return false;
		}
		if(refunds.stream().anyMatch(refund -> !RefundStatusDesc.REQUEST_CODE.equals(refund.getStatusCode()))){
			return false;
		}
		List<Refund> returns = refunds.stream().filter(refund->refund.isReturnGoods()).collect(Collectors.toList());
		//退货
		if(Objects.nonNull(returns) && returns.size() > 0){
			List<String> rids = returns.stream().map(x->x.getID()).collect(Collectors.toList());
			refundService.refreshStatus(rids, RefundStatusDesc.ACCEPT);
			List<RefundTrace> traces = new ArrayList<RefundTrace>();
			for (Refund refund : returns) {
				traces.add(createRefundTrace(refund, RefundStatusDesc.ACCEPT, operator, note));
			}
			refundTraceService.save(traces);
			try {
				refundEventService.broadcast(returns, IRefundEventService.MQ_REFUND_QUEUE_DONE);
			} catch (NoticeDispatchException e) {
				e.printStackTrace();
			}
		}
		//退款
		refunds = refunds.stream().filter(refund->!refund.isReturnGoods()).collect(Collectors.toList());
		if(Objects.nonNull(refunds) && refunds.size() > 0){
			List<String> rids = refunds.stream().map(x->x.getID()).collect(Collectors.toList());
			refundService.refreshStatus(rids, RefundStatusDesc.DONE);
			List<RefundTrace> traces = new ArrayList<RefundTrace>();
			for (Refund refund : refunds) {
				traces.add(createRefundTrace(refund, RefundStatusDesc.DONE, operator, note));
			}
			refundTraceService.save(traces);
			List<String> ogids = refunds.stream().map(x->x.getOGID()).collect(Collectors.toList());
			orderContextService.refundDone(ogids);
			try {
				refundEventService.broadcast(refunds, IRefundEventService.MQ_REFUND_QUEUE_DONE);
			} catch (NoticeDispatchException e) {
				e.printStackTrace();
			}
		}
		
		return true;
	}

	@Override
	public void deny(String RID, String operator, String note) throws DataRefreshingException, DataSavingException {
		Optional<Refund> candidate = refundService.single(RID);
		if(!candidate.isPresent()){
			return;
		}
		Refund refund = candidate.get();
		refundService.refreshStatus(Arrays.asList(RID), RefundStatusDesc.DENIED);
		refundTraceService.save(createRefundTrace(refund, RefundStatusDesc.DENIED, operator, note));
	}

	@Override
	public void doReturn(RefundLogistic logistic, String operator) throws DataSavingException, DataRefreshingException {
		Optional<Refund> candidate = refundService.single(logistic.getRID());
		if(!candidate.isPresent()){
			return;
		}
		Refund refund = candidate.get();
		refundService.refreshStatus(Arrays.asList(refund.getID()), RefundStatusDesc.RETURN);
		refundTraceService.save(createRefundTrace(refund, RefundStatusDesc.RETURN, operator, logistic.getReceiver()));
		refundLogisticService.save(logistic);
	}

	@Override
	public boolean done(List<String> RIDs, String operator) throws DataRefreshingException, DataSavingException {
		List<Refund> refunds = refundService.list(RIDs);
		if(refunds.size() == 0){
			return false;
		}
		if(refunds.stream().anyMatch(refund -> !RefundStatusDesc.RETURN_CODE.equals(refund.getStatusCode()))){
			return false;
		}
		List<String> rids = refunds.stream().map(x->x.getID()).collect(Collectors.toList());
		refundService.refreshStatus(rids, RefundStatusDesc.DONE);
		List<RefundTrace> traces = new ArrayList<RefundTrace>();
		for (Refund refund : refunds) {
			traces.add(createRefundTrace(refund, RefundStatusDesc.DONE, operator, "卖家同意退款"));
		}
		refundTraceService.save(traces);
		List<String> ogids = refunds.stream().map(x->x.getOGID()).collect(Collectors.toList());
		orderContextService.refundDone(ogids);
		return true;
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
