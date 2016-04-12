package com.jingyunbank.etrade.logistic.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jingyunbank.etrade.TestCaseBase;
import com.jingyunbank.etrade.api.logistic.bo.PostageCalculate;
import com.jingyunbank.etrade.api.logistic.service.IPostageService;

public class PostageServiceTest extends TestCaseBase {

	@Autowired
	private IPostageService postageService;
	
	@Test
	public void testSingleCaculate(){
		PostageCalculate p1 = new PostageCalculate();
		p1.setPostageID("p005");
		p1.setNumber(5);
		p1.setCity(1);
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("--------------------------------------");
		System.out.println(""+postageService.calculate(p1));
		System.out.println("--------------------------------------");
		
	}
	
	@Test
	public void testOneShopCaculate(){
		PostageCalculate p1 = new PostageCalculate();
		p1.setPostageID("p005");
		p1.setNumber(5);
		p1.setCity(1);
		
		PostageCalculate p2 = new PostageCalculate();
		p2.setPostageID("p006");
		p2.setNumber(5);
		p2.setCity(1);
		
		List<PostageCalculate> list = new ArrayList<PostageCalculate>();
		list.add(p1);
		list.add(p2);
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("--------------------------------------");
		System.out.println("--------------------------------------");
		System.out.println("--------------------------------------");
		System.out.println(""+postageService.calculateMuti(list, 1));
		System.out.println("--------------------------------------");
		System.out.println("--------------------------------------");
		System.out.println("--------------------------------------");
		
	}
	
	@Test
	public void testMutiShopCaculate(){
		//店铺1
		PostageCalculate p1 = new PostageCalculate();
		p1.setPostageID("p005");
		p1.setNumber(5);
		p1.setCity(1);
		
		PostageCalculate p2 = new PostageCalculate();
		p2.setPostageID("p006");
		p2.setNumber(5);
		p2.setCity(1);
		//店铺2
		PostageCalculate p3 = new PostageCalculate();
		p3.setPostageID("p007");
		p3.setNumber(5);
		p3.setCity(1);
		
		List<PostageCalculate> list = new ArrayList<PostageCalculate>();
		list.add(p1);
		list.add(p2);
		list.add(p3);
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("--------------------------------------");
		System.out.println("--------------------------------------");
		System.out.println("--------------------------------------");
		System.out.println(""+postageService.calculateMuti(list, 1));
		System.out.println("--------------------------------------");
		System.out.println("--------------------------------------");
		System.out.println("--------------------------------------");
		
	}
	
	public static void main(String[] args) {
		System.out.println(		BigDecimal.ZERO.add(new BigDecimal(100)));
	}
}
