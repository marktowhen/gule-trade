package com.jingyunbank.etrade.order.aspect;

import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
public class Pointcuts {
	
	@Pointcut("execution(* com.jingyunbank.etrade.api.order.presale.service.context.IOrderContextService.save(..))")
	public void save(){}
	
	@Pointcut("execution(* com.jingyunbank.etrade.api.order.presale.service.context.IOrderContextService.paysuccess(..))")
    public void paysuccess(){}
	
	@Pointcut("execution(* com.jingyunbank.etrade.api.order.presale.service.context.IOrderContextService.payfail(..))")
	public void payfail(){}
	
	@Pointcut("execution(* com.jingyunbank.etrade.api.order.presale.service.context.IOrderContextService.accept(..))")
	public void accept(){}
	
	@Pointcut("execution(* com.jingyunbank.etrade.api.order.presale.service.context.IOrderContextService.dispatch(..))")
	public void dispatch(){}
	
	@Pointcut("execution(* com.jingyunbank.etrade.api.order.presale.service.context.IOrderContextService.received(..))")
	public void received(){}
	
	@Pointcut("execution(* com.jingyunbank.etrade.api.order.presale.service.context.IOrderContextService.cancel(..))")
	public void cancel(){}
	
	@Pointcut("execution(* com.jingyunbank.etrade.api.order.presale.service.context.IOrderContextService.refund(..))")
	public void refund(){}
	
	@Pointcut("execution(* com.jingyunbank.etrade.api.order.presale.service.context.IOrderContextService.refundDone(..))")
	public void refundDone(){}
	
	@Pointcut("execution(* com.jingyunbank.etrade.api.order.presale.service.context.IOrderContextService.cancelRefund(..))")
	public void cancelRefund(){}
	
}
