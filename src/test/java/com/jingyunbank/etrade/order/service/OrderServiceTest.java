package com.jingyunbank.etrade.order.service;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jingyunbank.etrade.TestCaseBase;
import com.jingyunbank.etrade.api.order.service.IOrderService;


public class OrderServiceTest extends TestCaseBase
{
	@Autowired
	private IOrderService orderService;
	
	@Test
	public void test0(){
		assertNotNull(orderService);
	}
}
