package com.jingyunbank.etrade.order.postsale.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.Result;
import com.jingyunbank.etrade.api.order.postsale.service.IRefundCertificateService;

@RestController
public class RefundCertificateController {

	@Autowired
	private IRefundCertificateService refundCertificateService;
	/**
	 * get /api/refund/certificates?rid=zcxv
	 * @param mid
	 * @param from
	 * @param size
	 * @param keywords
	 * @param statuscode
	 * @param fromdate
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/api/refund/certificates", method=RequestMethod.GET)
	public Result<List<String>> listCerts(
			@RequestParam(value="rid", required=true) String rid,
			HttpSession session){
		
		return Result.ok(refundCertificateService.list(rid));
		
	}
}
