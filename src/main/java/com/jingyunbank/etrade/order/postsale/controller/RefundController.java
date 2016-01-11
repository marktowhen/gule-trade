package com.jingyunbank.etrade.order.postsale.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.KeyGen;
import com.jingyunbank.core.Result;
import com.jingyunbank.core.util.UniqueSequence;
import com.jingyunbank.core.web.AuthBeforeOperation;
import com.jingyunbank.core.web.Login;
import com.jingyunbank.etrade.api.order.postsale.bo.Refund;
import com.jingyunbank.etrade.api.order.postsale.bo.RefundCertificate;
import com.jingyunbank.etrade.api.order.postsale.bo.RefundLogistic;
import com.jingyunbank.etrade.api.order.postsale.bo.RefundStatusDesc;
import com.jingyunbank.etrade.api.order.postsale.service.IRefundService;
import com.jingyunbank.etrade.api.order.postsale.service.context.IRefundContextService;
import com.jingyunbank.etrade.order.postsale.bean.RefundLogisticVO;
import com.jingyunbank.etrade.order.postsale.bean.RefundRequestVO;

@RestController
public class RefundController {
	
	@Autowired
	private IRefundContextService refundContextService;
	@Autowired
	private IRefundService refundService;
	
	@AuthBeforeOperation
	@RequestMapping(
			value="/api/refund",
			method=RequestMethod.POST,
			consumes=MediaType.APPLICATION_JSON_UTF8_VALUE,
			produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Result<RefundRequestVO> submit(@Valid @RequestBody RefundRequestVO refundvo,
			BindingResult valid, HttpSession session) throws Exception{
		if(valid.hasErrors()){
			return Result.fail("您提交的数据不完整，请核实后重新提交！");
		}
		if(refundvo.getMoney().compareTo(refundvo.getOmoney())>0){
			return Result.fail("退款金额不得高于实际订单价格。");
		}
		String UID = Login.UID(session);
		String uname = Login.uname(session);
		refundvo.setUID(UID);
		refundvo.setAddtime(new Date());
		refundvo.setStatusCode(RefundStatusDesc.REQUEST_CODE);
		refundvo.setStatusName(RefundStatusDesc.REQUEST.getName());
		Optional<Refund> candidate = refundService.singleByOGID(refundvo.getOGID());
		if(candidate.isPresent()){//该商品重新申请退款
			Refund refund = candidate.get();
			refundvo.setID(refund.getID());
			refundvo.setRefundno(refund.getRefundno());
			BeanUtils.copyProperties(refundvo, refund, "certificates");
			refund.getCertificates().clear();
			refundvo.getCertificates().forEach(cer->{
				RefundCertificate rc = new RefundCertificate();
				rc.setID(KeyGen.uuid());
				rc.setPath(cer);
				rc.setRID(refund.getID());
				refund.getCertificates().add(rc);
			});
			refundContextService.refresh(refund, uname);
		}else{
			refundvo.setID(KeyGen.uuid());
			refundvo.setRefundno(UniqueSequence.next18());
			Refund refund = new Refund();
			BeanUtils.copyProperties(refundvo, refund, "certificates");
			refundvo.getCertificates().forEach(cer->{
				RefundCertificate rc = new RefundCertificate();
				rc.setID(KeyGen.uuid());
				rc.setPath(cer);
				rc.setRID(refund.getID());
				refund.getCertificates().add(rc);
			});
			refundContextService.request(refund, uname);
			
		}
		
		return Result.ok(refundvo);
	}
	
	@AuthBeforeOperation
	@RequestMapping(
			value="/api/refund",
			method=RequestMethod.PUT,
			consumes=MediaType.APPLICATION_JSON_UTF8_VALUE,
			produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Result<RefundRequestVO> update(@Valid @RequestBody RefundRequestVO refundvo,
			BindingResult valid, HttpSession session) throws Exception{
		if(valid.hasErrors() || StringUtils.isEmpty(refundvo.getID())){
			return Result.fail("您提交的数据不完整，请核实后重新提交！");
		}
		String uname = Login.uname(session);
		refundvo.setStatusCode(RefundStatusDesc.REQUEST_CODE);
		refundvo.setStatusName(RefundStatusDesc.REQUEST.getName());
		
		Refund refund = new Refund();
		BeanUtils.copyProperties(refundvo, refund, "certificates");
		refundvo.getCertificates().forEach(cer->{
			RefundCertificate rc = new RefundCertificate();
			rc.setID(KeyGen.uuid());
			rc.setPath(cer);
			rc.setRID(refund.getID());
			refund.getCertificates().add(rc);
		});
		refundContextService.refresh(refund, uname);
		
		return Result.ok(refundvo);
	}
	
	@AuthBeforeOperation
	@RequestMapping(
			value="/api/refund/cancellation",
			method=RequestMethod.PUT,
			consumes=MediaType.APPLICATION_JSON_UTF8_VALUE,
			produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Result<String> cancel(@Valid @RequestBody RIDWithNoteVO cancellation,
			BindingResult valid, HttpSession session) throws Exception{
		if(valid.hasErrors()){
			return Result.fail("您提交的数据不完整，请核实后重新提交！");
		}
		String uname = Login.uname(session);
		refundContextService.cancel(cancellation.getRid(), uname, cancellation.getNote());
		
		return Result.ok();
	}
	
	private static class RIDWithNoteVO{
		@NotBlank
		private String rid;
		@NotNull
		private String note;
		public String getRid() {
			return rid;
		}
		@SuppressWarnings("unused")
		public void setRid(String rid) {
			this.rid = rid;
		}
		public String getNote() {
			return note;
		}
		@SuppressWarnings("unused")
		public void setNote(String note) {
			this.note = note;
		}
	}
	
	@RequestMapping(
			value="/api/refund/acception",
			method=RequestMethod.POST,
			consumes={MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE},
			produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Result<String> accept(@RequestParam(required=true) String rid, @RequestParam(required=true) String note, HttpSession session) throws Exception{
		
		refundContextService.accept(Arrays.asList(rid), "卖家", "卖家同意请求。");
		return Result.ok();
	}
	
	@RequestMapping(
			value="/api/refund/denial",
			method=RequestMethod.POST,
			consumes={MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE},
			produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Result<String> deny(@RequestParam(required=true) String rid, @RequestParam(required=true) String note, HttpSession session) throws Exception{
		
		refundContextService.deny(rid, "卖家", note);
		return Result.ok();
	}
	
	@RequestMapping(
			value="/api/refund/completion",
			method=RequestMethod.POST,
			consumes={MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE},
			produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Result<String> done(@RequestParam(required=true) String rid, HttpSession session) throws Exception{
		
		refundContextService.done(Arrays.asList(rid), "卖家");
		return Result.ok();
	}

	/**
	 * 填写订单的物流信息。
	 * @param logisticvo
	 * @param valid
	 * @return
	 * @throws Exception
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/api/refund/logistic", method=RequestMethod.PUT)
	public Result<String> dispatch(@Valid @RequestBody RefundLogisticVO logisticvo, 
			BindingResult valid, HttpSession session) throws Exception{
		if(valid.hasErrors()){
			return Result.fail("您提交的物流信息有误！");
		}
		String uname = Login.uname(session);
		RefundLogistic logistic = new RefundLogistic();
		BeanUtils.copyProperties(logisticvo, logistic);
		logistic.setAddtime(new Date());
		logistic.setID(KeyGen.uuid());
		refundContextService.doReturn(logistic, uname);
		return Result.ok();
	}
}
