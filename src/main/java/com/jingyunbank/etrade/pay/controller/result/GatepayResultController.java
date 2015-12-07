package com.jingyunbank.etrade.pay.controller.result;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jingyunbank.core.util.MD5;
import com.jingyunbank.etrade.api.pay.bo.PayPipeline;
import com.jingyunbank.etrade.api.pay.service.IPayPipelineService;
import com.jingyunbank.etrade.api.pay.service.context.IPayContextService;

@Controller
public class GatepayResultController {
	@Autowired
	private IPayContextService payContextService;
	@Autowired
	private IPayPipelineService payPipelineService;
	
	private static PayPipeline pipeline = new PayPipeline();
	
	
	@RequestMapping(value="/api/payments/result/gateway/async", method={RequestMethod.POST})
	@ResponseBody
	public Map<String, String> payresult (HttpServletRequest request, HttpServletResponse response) throws Exception{
		response.setCharacterEncoding("UTF-8");
		InputStream payresultstream = request.getInputStream();
		Map<String, String> payresult = resolve(payresultstream);
		Map<String, String> result = new HashMap<String, String>();
        if (Objects.isNull(payresult)
        		|| Objects.isNull(payresult.get("result_pay"))
        		|| !"SUCCESS".equals(payresult.get("result_pay"))
        		|| Objects.isNull(payresult.get("sign"))
        		|| Objects.isNull(payresult.get("no_order")))
        {
        	result.put("ret_code", "9999");
        	result.put("ret_msg", "响应结果为空");
        	return result;
        }
        String extransno = payresult.get("no_order");
        try
        {
        	String sign = payresult.get("sign");
        	String key = pipeline.getSignkey();
            if (!sign.equalsIgnoreCase(MD5.digest(compositeGatewayKeyValuePaires(payresult, key))))
            {
            	payContextService.payfail(extransno);
            	result.put("ret_code", "9999");
            	result.put("ret_msg", "签名校验失败");
            	return result;
            }
        } catch (Exception e)
        {
        	payContextService.payfail(extransno);
        	result.put("ret_code", "9999");
        	result.put("ret_msg", "签名校验失败");
        	return result;
        }

		payContextService.paydone(extransno);
		result.put("ret_code", "0000");
    	result.put("ret_msg", "交易成功");
    	return result;
        
	}

	@SuppressWarnings("unchecked")
	private Map<String, String> resolve(InputStream payresultstream) throws Exception {
		Map<String, String> result = new HashMap<String, String>();
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(payresultstream, "utf-8")))
        {
        	ObjectMapper mapper = new ObjectMapper();
            result = mapper.readValue(reader, Map.class);
            
        }catch (Exception e){
        	throw e;
		}
        
        return result;
	}

	private String compositeGatewayKeyValuePaires(Map<String, String> result, String key) {
		StringBuilder builder = new StringBuilder();
		result.entrySet().stream()
			.filter(x->! "sign".equals(x.getKey()))
			.sorted((x, y)->x.getKey().compareToIgnoreCase(y.getKey()))
			.forEach((x)->{
			builder.append(x.getKey()).append("=").append(x.getValue()).append("&");
		});
		builder.append("key=").append(key);
		return builder.toString();
	}

	@PostConstruct
	public void postprocessor(){
		pipeline = payPipelineService.single(PayPipeline.GATEPAY);
	}

}
