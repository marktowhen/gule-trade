package com.jingyunbank.etrade.order.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.jingyunbank.etrade.TestCaseBase;

public class OrderControllerTest extends TestCaseBase {

	//private RestTemplate restTemplate = new TestRestTemplate();
	@Autowired
	private WebApplicationContext wac;
	private MockMvc mockMvc;
	
	@Before
	public void init(){
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}
	
	@Test
	public void testAddressID() throws Exception{
		mockMvc.perform(
					 put("/orders/submit")
					.param("addressID", "123321123321123321123")
					.param("paytypeID", "1233211233211233211232")
					.param("price", "123.32")
					.param("postage", "12.32")
					.sessionAttr("LOGIN_ID", "XXXX")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
				)
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(jsonPath("$.code").value("500"))
				.andDo(print());
	}
	
	@Test
	public void testPayType() throws Exception{
		mockMvc.perform(
					 put("/orders/submit")
					.param("addressID", "1233211233211233211232")
					.param("paytypeID", "123321123321123321123")
					.param("price", "123.32")
					.param("postage", "12.32")
					.sessionAttr("LOGIN_ID", "USER-ID")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
				)
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(jsonPath("$.code").value("500"))
				.andDo(print());
		
	}
	@Test
	public void testPrice() throws Exception{
		mockMvc.perform(
					 put("/orders/submit")
					.param("addressID", "1233211233211233211232")
					.param("paytypeID", "1233211233211233211232")
					.param("price", "-122")
					.param("postage", "12.32")
					.sessionAttr("LOGIN_ID", "USER-ID")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
				)
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(jsonPath("$.code").value("500"))
				.andDo(print());
		
	}
	@Test
	public void testPostage() throws Exception{
		mockMvc.perform(
					 put("/orders/submit")
					.param("addressID", "1233211233211233211232")
					.param("paytypeID", "1233211233211233211232")
					.param("price", "122")
					.param("postage", "-12.32")
					.sessionAttr("LOGIN_ID", "USER-ID")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
				)
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(jsonPath("$.code").value("500"))
				.andDo(print());
		
	}
	
	@Test
	public void testSuccess() throws Exception{
		mockMvc.perform(
					 put("/orders/submit")
					.param("addressID", "1233211233211233211232")
					.param("paytypeID", "1233211233211233211232")
					.param("price", "122")
					.param("postage", "12.32")
					.sessionAttr("LOGIN_ID", "USER-ID")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
				)
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(jsonPath("$.code").value("200"))
				.andDo(print());
		
	}
	
	@Test
	public void testLoginFirst() throws Exception{
		mockMvc.perform(
					 put("/orders/submit")
					.param("addressID", "1233211233211233211232")
					.param("paytypeID", "1233211233211233211232")
					.param("price", "122")
					.param("postage", "12.32")
					//.sessionAttr("LOGIN_ID", "USER-ID")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
				)
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(jsonPath("$.code").value("500"))
				.andDo(print());
		
	}
	
	@Test
	public void testList() throws Exception{
		mockMvc.perform(
					get("/orders")
					.sessionAttr("login-uid", "123321")
					.accept(MediaType.APPLICATION_JSON)
				)
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(jsonPath("$.code").value("200"))
				.andExpect(jsonPath("$.body[0].postage").value(12.00))
				.andDo(print());
	}
}
