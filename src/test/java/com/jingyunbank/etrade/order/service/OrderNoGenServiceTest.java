package com.jingyunbank.etrade.order.service;

import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.jingyunbank.core.KeyGen;
import com.jingyunbank.etrade.TestCaseBase;
import com.jingyunbank.etrade.api.order.service.IOrderNoGenService;


public class OrderNoGenServiceTest extends TestCaseBase{

	@Autowired
	private IOrderNoGenService orderNoGenService;
	
	@Test
	public void test0(){
		long ss = System.currentTimeMillis();
		for (int i = 0; i < 1000000; i++) {
			String s = orderNoGenService.setMID(KeyGen.uuid()).setUID(KeyGen.uuid()).nexts();
			assertTrue(s.length() > 10);
		}
		System.out.println(System.currentTimeMillis() - ss);
	}
}
