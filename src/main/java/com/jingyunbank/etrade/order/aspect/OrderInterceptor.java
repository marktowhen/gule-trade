package com.jingyunbank.etrade.order.aspect;

import org.aspectj.lang.annotation.AfterReturning;


//@Component
//@Scope("singleton")
//@Aspect
public class OrderInterceptor {
	
	@AfterReturning("com.jingyunbank.etrade.order.aspect.Pointcuts.paysuccess() && args(extransno)")
	public void whenPaysuccess(String extransno){
		//
	}
}
