package com.jingyunbank.etrade.pay.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.Result;
import com.jingyunbank.core.util.MD5;
import com.jingyunbank.core.web.AuthBeforeOperation;
import com.jingyunbank.core.web.ServletBox;
import com.jingyunbank.etrade.api.pay.bo.OrderPayment;
import com.jingyunbank.etrade.api.pay.service.IPayService;
import com.jingyunbank.etrade.api.pay.service.context.IPayContextService;
import com.jingyunbank.etrade.api.user.bo.Users;
import com.jingyunbank.etrade.api.user.service.IUserService;
import com.jingyunbank.etrade.pay.bean.OrderPaymentRequestVO;
import com.jingyunbank.etrade.pay.bean.OrderPaymentVO;

@RestController
public class PayController {

	@Autowired
	private IPayService payService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IPayContextService payContextService;
	
	/**
	 * 初始化订单支付接口。<p>
	 * 查询出制定订单的支付状态信息
	 * <p>
	 * uri: get /api/payments?oid=xxx&oid=yyyy&oid=zxx
	 * <p>
	 * <b>调用时机：</b><p>
	 * <ul>
	 * 	<li>1，订单确认页，用户点击确认提交订单后，当订单信息保存完成后，将返回以保存的订单信息，并将请求重定向到该接口进行支付请求的操作。
	 *  <li>2，我的订单页，用户选中未支付的订单，并点击立即支付按钮时。
	 * </ul>
	 * <b>注：</b><p>
	 * 该操作会检查是否创建了交易记录（order_payment），如果没有则新建，如果已存在则查询出来返回.
	 * @param payvo
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/api/payments", method=RequestMethod.GET,
					produces="application/json;charset=UTF-8")
	@AuthBeforeOperation
	public Result<List<OrderPaymentVO>> init(@RequestParam(value="oid", required=true) List<String> oids, HttpSession session) throws Exception{
		
		if(oids.size() == 0){
			return Result.fail("您提交的订单信息有误，请核实后进行支付。");
		}
		
		List<OrderPayment> payments = payService.listPayments(oids)
							.stream().filter(x-> !x.isDone()).collect(Collectors.toList());
		
		if(Objects.isNull(payments) || payments.size() == 0 || payments.size() != oids.size()){
			return Result.fail("订单已经失效或者已完成支付，无法完成本次支付，请重新下单。");
		}
		
		List<OrderPaymentVO> opvs = new ArrayList<OrderPaymentVO>();
		payments.forEach(p -> {
			OrderPaymentVO opv = new OrderPaymentVO();
			BeanUtils.copyProperties(p, opv);
			opvs.add(opv);
		});
		return Result.ok(opvs);
	}
	
	/**
	 * 更新支付信息的平台，交易号信息，并构建用户指定的支付平台的支付信息<p>
	 * @param payvo
	 * @param valid
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/api/payments/info", method=RequestMethod.PUT)
	@AuthBeforeOperation
	public Result<Map<String, String>> build(
				@Valid @RequestBody OrderPaymentRequestVO payvo, 
				BindingResult valid,  
				HttpSession session) throws Exception{
		if(valid.hasErrors()){
			return Result.fail("您提交的订单数据有误，请校验！");
		}
		String pipelineCode = payvo.getPipelineCode();
		String pipelineName = payvo.getPipelineName();
		String bankCode = payvo.getBankCode();
		
		String tradepwd = payvo.getTradepwd();
		String tradepwdmd5 = MD5.digest(tradepwd);
		Optional<Users> ou = userService.getByUID(ServletBox.getLoginUID(session));
		if(!ou.isPresent() || !tradepwdmd5.equals(ou.get().getTradepwd())){
			return Result.fail("支付密码错误！");
		}
		Map<String, String> payinfo = payContextService.refreshAndComposite(
				payvo.getPayments()
				.stream().map(x->{
					OrderPayment op = new OrderPayment();
					BeanUtils.copyProperties(x, op);
					return op;
				})
				.collect(Collectors.toList()), 
				pipelineCode, pipelineName,
				bankCode
		);
		
		return Result.ok(payinfo);
	}
	
}
