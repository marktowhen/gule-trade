package com.jingyunbank.etrade.order.aspect;

import org.aspectj.lang.annotation.Pointcut;

//@Component
public class Pointcuts {
	
	@Pointcut("execution(* com.jingyunbank.etrade.api.order.presale.service.context.IOrderContextService.paysuccess(..))")
    public void paysuccess(){}
	
}
