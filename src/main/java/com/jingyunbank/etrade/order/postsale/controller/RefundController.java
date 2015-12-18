package com.jingyunbank.etrade.order.postsale.controller;

import java.util.Date;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.KeyGen;
import com.jingyunbank.core.Result;
import com.jingyunbank.core.web.AuthBeforeOperation;
import com.jingyunbank.core.web.ServletBox;
import com.jingyunbank.etrade.api.order.postsale.bo.Refund;
import com.jingyunbank.etrade.api.order.postsale.bo.RefundCertificate;
import com.jingyunbank.etrade.api.order.postsale.bo.RefundStatusDesc;
import com.jingyunbank.etrade.api.order.postsale.service.context.IRefundContextService;
import com.jingyunbank.etrade.order.postsale.bean.RefundRequestVO;

@RestController
public class RefundController {
	
	@Autowired
	private IRefundContextService refundContextService;
	
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
		
		String UID = ServletBox.getLoginUID(session);
		refundvo.setID(KeyGen.uuid());
		refundvo.setUID(UID);
		refundvo.setAddtime(new Date());
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
		refundContextService.request(refund);
		
		return Result.ok(refundvo);
	}
}
