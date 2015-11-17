package com.jingyunbank.etrade.goods.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.jingyunbank.core.web.ServletBox;
import com.jingyunbank.etrade.TestCaseBase;

public class MerchantControllerTest extends TestCaseBase {

	/**
	 * 测试 首页商家推荐查询
	 * @throws Exception
	 */
	@Test
	public void test0() throws Exception{
		getMockMvc().perform(
				 put("/api/merchant/recommend/list")
				.sessionAttr("LOGIN_ID", "USER-ID")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(jsonPath("$.code").value("200"))
				.andDo(print()
				);
		//System.out.println(restTemplate.getForEntity("http://localhost:8080/user", String.class).getBody());
		
	}
	/**
	 * 测试商家信息保存
	 * @throws Exception
	 */
	@Test
	 public void test1() throws Exception{
		getMockMvc().perform(
				post("/api/merchant/savemerchant")
				 .sessionAttr(ServletBox.LOGIN_ID, "IoBST6elTCarSyzl6Z277g")
				.param("merchantName", "东阿阿胶"+Math.random()*100)
				.param("merchantEname", "DEEJ"+Math.random()*100)
				.param("merchantCode", "DEEJ"+Math.random()*50)
				.param("merchantAddress", "山东济南")
				.param("merchantScale", "规模100")
				.param("employeeNum", "100")
				.param("tel", "4008008895")
				.param("zipcode", "250000")
				.param("qq", "4932003")
				.param("twoDimensionCode", "二维码地址")
				.param("adminSortNum", "2")
				.param("merchantDesc", "我们卖阿胶！")
				.param("imgPath", "图片地址")
				.param("invoiceFlag", "1")
				.param("invoiceCodes", "A001")
				.param("deliveryCodes", "D001,D002")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
			//.andExpect(jsonPath("$.code").value("500"))
			.andDo(MockMvcResultHandlers.print())
			.andDo(print());
	 }
	 
	/**
	 * 获取发票类型
	 * @throws Exception
	 */
	@Test
	public void test2() throws Exception{
		getMockMvc().perform(
				 put("/api/merchant/invoicetype/list")
				.sessionAttr("LOGIN_ID", "USER-ID")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(jsonPath("$.code").value("200"))
				.andDo(print()
				);
		//System.out.println(restTemplate.getForEntity("http://localhost:8080/user", String.class).getBody());
	}
	
	/**
	 * 测试商家信息修改
	 * @throws Exception
	 */
	@Test
	 public void test3() throws Exception{
		getMockMvc().perform(
				post("/api/merchant/updatemerchant")
				 .sessionAttr(ServletBox.LOGIN_ID, "IoBST6elTCarSyzl6Z277g")
				.param("merchantName", "东阿阿胶"+Math.random()*100)
				.param("merchantEname", "DEEJ"+Math.random()*100)
				.param("merchantCode", "DEEJ"+Math.random()*50)
				.param("merchantAddress", "山东济南liug")
				.param("merchantScale", "规模100")
				.param("employeeNum", "100")
				.param("tel", "4008008895")
				.param("ID", "1")
				.param("zipcode", "250000")
				.param("qq", "4932003")
				.param("twoDimensionCode", "二维码地址")
				.param("adminSortNum", "2")
				.param("merchantDesc", "我们卖阿胶！")
				.param("imgPath", "图片地址")
				.param("invoiceFlag", "1")
				.param("invoiceCodes", "A002,A003")
				.param("deliveryCodes", "D001,D002")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
			//.andExpect(jsonPath("$.code").value("500"))
			.andDo(MockMvcResultHandlers.print())
			.andDo(print());
	 }
	/**
	 * 测试快递类型查询
	 * @throws Exception
	 */
	@Test
	public void test4() throws Exception{
		getMockMvc().perform(
				 put("/api/merchant/deliverytype/list")
				.sessionAttr("LOGIN_ID", "USER-ID")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(jsonPath("$.code").value("200"))
				.andDo(print()
				);
		//System.out.println(restTemplate.getForEntity("http://localhost:8080/user", String.class).getBody());
		
	}
	
	/**
	 * 通过mid查询商家信息
	 * @throws Exception
	 */
	
	@Test
	public void Test5() throws Exception{
		getMockMvc().perform(
				 get("/api/merchant/info/1")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
			.andExpect(jsonPath("$.code").value("200"))
			.andDo(MockMvcResultHandlers.print())
			.andDo(print());
	}
}
