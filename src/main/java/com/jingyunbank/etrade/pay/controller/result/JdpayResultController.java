package com.jingyunbank.etrade.pay.controller.result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jingyunbank.core.util.MD5;
import com.jingyunbank.etrade.api.pay.service.context.IPayContextService;

@Controller
public class JdpayResultController {
	
	@Autowired
	private IPayContextService payContextService;
	
	@RequestMapping(value="/api/payments/result/jd", method={RequestMethod.GET, RequestMethod.POST})
	public void payresult (HttpServletRequest request, HttpServletResponse response) throws Exception{
		String key = "test";						// 登陆后在上面的导航栏里可能找到“B2C”，在二级导航栏里有“MD5密钥设置”
		// 建议您设置一个16位以上的密钥或更高，密钥最多64位，但设置16位已经足够了
		//****************************************
		
		//获取参数
		String v_oid = request.getParameter("v_oid");		// 订单号
		//String v_pmode = request.getParameter("v_pmode");		// 支付方式中文说明，如"中行长城信用卡"
		String v_pstatus = request.getParameter("v_pstatus");	// 支付结果，20支付完成；30支付失败；
		//String v_pstring = request.getParameter("v_pstring");	// 对支付结果的说明，成功时（v_pstatus=20）为"支付成功"，支付失败时（v_pstatus=30）为"支付失败"
		String v_amount = request.getParameter("v_amount");		// 订单实际支付金额
		String v_moneytype = request.getParameter("v_moneytype");	// 币种
		String v_md5str = request.getParameter("v_md5str");		// MD5校验码
		//String remark1 = request.getParameter("remark1");		// 备注1
		//String remark2 = request.getParameter("remark2");		// 备注2
		
		String text = v_oid+v_pstatus+v_amount+v_moneytype+key; //拼凑加密串
		String v_md5text = MD5.digest(text).toUpperCase();
		if (v_md5str.equals(v_md5text))
		{
			response.getOutputStream().print("ok"); // 告诉服务器验证通过,停止发送
			if ("20".equals(v_pstatus))
			{
				// 支付成功，商户 根据自己业务做相应逻辑处理
				// 此处加入商户系统的逻辑处理（例如判断金额，判断支付状态(20成功,30失败)，更新订单状态等等）......
				payContextService.paysucc(v_oid);
			}else{
				payContextService.payfail(v_oid, "");
			}
		}else{
			//支付失败
			payContextService.payfail(v_oid, "");
		}
	}
}
