package com.jingyunbank.etrade.pay.controller.result;

import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jingyunbank.core.util.MD5;
import com.jingyunbank.etrade.api.order.presale.service.context.IOrderContextService;
import com.jingyunbank.etrade.api.pay.bo.PayPipeline;
import com.jingyunbank.etrade.api.pay.service.IPayPipelineService;

@Controller
public class WXPayResultController {
	@Autowired
	private IOrderContextService orderContextService;
	@Autowired
	private IPayPipelineService payPipelineService;
	
	private static PayPipeline pipeline = new PayPipeline();
	
	@RequestMapping(value="/api/payments/result/wxpay/async", method={RequestMethod.POST})
	public void payresult (HttpServletRequest request, HttpServletResponse response) throws Exception{
		response.setCharacterEncoding("UTF-8");
		try(InputStream is = request.getInputStream()){
			JAXBContext jaxbcontext = JAXBContext.newInstance(WXPayResult.class);
			WXPayResult wxresult = (WXPayResult) jaxbcontext.createUnmarshaller().unmarshal(is);
			logger.info("微信支付结果异步通知参数->"+wxresult);
			if(StringUtils.hasText(wxresult.getReturn_code()) 
					&& StringUtils.hasText(wxresult.getResult_code())
					&& wxresult.getResult_code().equals("SUCCESS")
					&& wxresult.getReturn_code().equals("SUCCESS")){
				Map<String, String> mapresult = BeanUtils.describe(wxresult);
				String providedSign = wxresult.getSign();
				String signkey = pipeline.getSignkey();
				String calculatedSign = MD5.digest(compositeWXPayKeyValuePaires(mapresult, signkey)).toUpperCase();
				String extorderno = wxresult.getOut_trade_no();
				
				if(StringUtils.hasText(providedSign)
						&& StringUtils.hasText(calculatedSign)
						&& providedSign.equals(calculatedSign)){
					
					orderContextService.paysuccess(extorderno);
					
					StringBuilder sbuilder = new StringBuilder();
					sbuilder.append("<xml>\n")
					.append("<return_code><![CDATA[SUCCESS]]></return_code>\n")
					.append("<return_msg><![CDATA[OK]]></return_msg>\n")
					.append("</xml>");
					try(PrintWriter writer = response.getWriter()){
						writer.print(sbuilder.toString());
					}
					return;
				}
				orderContextService.payfail(extorderno, "支付结果的签名校验失败："+calculatedSign);
				StringBuilder sbuilder = new StringBuilder();
				sbuilder.append("<xml>\n")
				.append("<return_code><![CDATA[FAIL]]></return_code>\n")
				.append("<return_msg><![CDATA[签名失败]]></return_msg>\n")
				.append("</xml>");
				try(PrintWriter writer = response.getWriter()){
					writer.print(sbuilder.toString());
				}
				return;
			}
		}
		StringBuilder sbuilder = new StringBuilder();
		sbuilder.append("<xml>\n")
		.append("<return_code><![CDATA[FAIL]]></return_code>\n")
		.append("<return_msg><![CDATA[参数格式校验错误]]></return_msg>\n")
		.append("</xml>");
		try(PrintWriter writer = response.getWriter()){
			writer.print(sbuilder.toString());
		}
		return;
	}

	private String compositeWXPayKeyValuePaires(Map<String, String> result, String key) {
		StringBuilder builder = new StringBuilder();
		result.entrySet().stream()
			.filter(x-> !x.getKey().equals("sign") && !x.getKey().equals("class"))
			.sorted((x, y)->x.getKey().compareTo(y.getKey())).forEach((x)->{
				builder.append(x.getKey()).append("=").append(x.getValue()).append("&");
		});
		builder.append("key=").append(key);
		return builder.toString();
	}

	@PostConstruct
	public void postprocessor(){
		pipeline = payPipelineService.single(PayPipeline.WXPAY);
	}
	private Logger logger = LoggerFactory.getLogger(WXPayResultController.class);
}
