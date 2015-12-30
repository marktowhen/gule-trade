package com.jingyunbank.etrade.order.postsale.service.context;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.order.postsale.bo.Refund;
import com.jingyunbank.etrade.api.order.postsale.bo.RefundStatusDesc;
import com.jingyunbank.etrade.api.order.postsale.service.IRefundService;
import com.jingyunbank.etrade.api.order.postsale.service.context.IRefundContextService;
import com.jingyunbank.etrade.api.order.postsale.service.context.IRefundRobotService;

@Service("refundRobotService")
public class RefundRobotService implements IRefundRobotService {
	//同意买家退款退货申请过期时限
	public static final long UNACCEPT_EXPIRE_DURATION_IN_SECOND = 10 * 24 * 60 * 60;//10d
	//完成买家退货申请最后一步时限
	public static final long UNFINISH_EXPIRE_DURATION_IN_SECOND = 10 *  24 * 60 * 60;//10d
	
	@Autowired
	private IRefundContextService refundContextService;
	@Autowired
	private IRefundService refundService;
	
	@Override
	@Transactional
	public void autoAccept() throws DataRefreshingException,
			DataSavingException {
		System.out.println("===================自动同意未及时处理买家退款退货申请任务启动======================");
		Instant now = Instant.now();
		Instant deadline = now.minusSeconds(UNACCEPT_EXPIRE_DURATION_IN_SECOND);
		List<Refund> expired = refundService.listBefore(Date.from(deadline), RefundStatusDesc.REQUEST);
		List<String> rids = expired.stream().map(x->x.getID()).collect(Collectors.toList());
		System.out.println("待处理退单："+rids);
		if(rids.size() > 0) refundContextService.accept(rids, "超时处理任务", "超时自动处理");
		System.out.println("===================自动同意未及时处理买家退款退货申请任务完成======================");
	}

	@Override
	@Transactional
	public void autoDone() throws DataRefreshingException, DataSavingException {
		System.out.println("===================自动完成未及时处理买家退货申请任务启动======================");
		Instant now = Instant.now();
		Instant deadline = now.minusSeconds(UNFINISH_EXPIRE_DURATION_IN_SECOND);
		List<Refund> expired = refundService.listBefore(Date.from(deadline), RefundStatusDesc.RETURN);
		List<String> rids = expired.stream().map(x->x.getID()).collect(Collectors.toList());
		System.out.println("待处理退单："+rids);
		if(rids.size() > 0) refundContextService.done(rids, "超时处理任务");
		System.out.println("===================自动完成未及时处理买家退货申请任务完成======================");
	}

}
