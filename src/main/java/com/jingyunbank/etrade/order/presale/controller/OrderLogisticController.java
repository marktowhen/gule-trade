package com.jingyunbank.etrade.order.presale.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.KeyGen;
import com.jingyunbank.core.Result;
import com.jingyunbank.core.util.MD5;
import com.jingyunbank.core.web.AuthBeforeOperation;
import com.jingyunbank.core.web.Login;
import com.jingyunbank.etrade.api.order.presale.bo.OrderLogistic;
import com.jingyunbank.etrade.api.order.presale.service.IOrderLogisticService;
import com.jingyunbank.etrade.api.order.presale.service.context.IOrderContextService;
import com.jingyunbank.etrade.api.user.bo.Users;
import com.jingyunbank.etrade.api.user.service.IUserService;
import com.jingyunbank.etrade.order.presale.bean.OrderLogisticVO;

@RestController
public class OrderLogisticController {
	
	@Autowired
	private IOrderContextService orderContextService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IOrderLogisticService orderLogisticService;
	
	/**
	 * 接受用户成功支付后的订单信息
	 * @param oids
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/api/orders/acception", method=RequestMethod.PUT)
	public Result<String> accept(@NotNull @Size(min=1) @RequestBody List<String> oids, BindingResult valid) throws Exception{
		if(valid.hasErrors()){
			return Result.fail("您提交的订单信息有误！");
		}
		if(!orderContextService.accept(oids)){
			return Result.fail("您提交的订单信息有误，请检查后重新尝试！");
		}
		return Result.ok();
	}
	
	/**
	 * 填写订单的物流信息。
	 * @param logisticvo
	 * @param valid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/api/orders/logistic", method=RequestMethod.PUT)
	public Result<String> dispatch(@Valid @RequestBody OrderLogisticVO logisticvo, BindingResult valid) throws Exception{
		if(valid.hasErrors()){
			return Result.fail("您提交的物流信息有误！");
		}
		OrderLogistic logistic = new OrderLogistic();
		BeanUtils.copyProperties(logisticvo, logistic);
		logistic.setAddtime(new Date());
		logistic.setID(KeyGen.uuid());
		if(!orderContextService.dispatch(logistic)){
			return Result.fail("您提交的订单信息有误，请检查后重新尝试！");
		}
		return Result.ok();
	}
	/**
	 * 用户确认收货
	 * put /api/orders/receipt {"oids":["asdf"], "tradepwd":"abcd1234"}
	 * @param oids
	 * @return
	 * @throws Exception
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/api/orders/receipt", method=RequestMethod.PUT)
	public Result<String> receive(@Valid @RequestBody OIDsWithTradePWDVO oidswithpwd, 
			BindingResult valid, HttpSession session) throws Exception{
		if(valid.hasErrors()){
			return Result.fail("您提交的订单信息有误！");
		}
		
		String tradepwd = oidswithpwd.getTradepwd();
		String tradepwdmd5 = MD5.digest(tradepwd);
		Optional<Users> ou = userService.single(Login.UID(session));
		if(!ou.isPresent() || !tradepwdmd5.equals(ou.get().getTradepwd())){
			return Result.fail("支付密码错误！");
		}
		
		if(!orderContextService.received(Arrays.asList(oidswithpwd.getOid()), "买家主动确认收货")){
			return Result.fail("您提交的订单信息有误，请检查后重新尝试！");
		}
		return Result.ok();
	}
	
	@RequestMapping(value="/api/orders/{oid}/logistic", method=RequestMethod.GET)
	public Result<OrderLogisticVO> logistic(@PathVariable String oid) throws Exception{
		Optional<OrderLogistic> candidateBo = orderLogisticService.single(oid);
		if(!candidateBo.isPresent()){
			return Result.ok();
		}
		OrderLogisticVO vo = new OrderLogisticVO();
		BeanUtils.copyProperties(candidateBo.get(), vo);
		return Result.ok(vo);
	}
	
	private static class OIDsWithTradePWDVO{
		@NotNull @Size(min=1) 
		private String oid;
		@NotNull
		@Size(min=6)
		private String tradepwd;
		
		public String getOid() {
			return oid;
		}
		@SuppressWarnings("unused")
		public void setOid(String oid) {
			this.oid = oid;
		}
		public String getTradepwd() {
			return tradepwd;
		}
		@SuppressWarnings("unused")
		public void setTradepwd(String tradepwd) {
			this.tradepwd = tradepwd;
		}
	}
}
