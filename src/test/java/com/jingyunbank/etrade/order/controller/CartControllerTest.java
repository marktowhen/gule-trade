package com.jingyunbank.etrade.order.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Test;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jingyunbank.core.web.ServletBox;
import com.jingyunbank.etrade.TestCaseBase;
import com.jingyunbank.etrade.order.bean.GoodsInCartVO;


public class CartControllerTest extends TestCaseBase{
	
	@Test
	public void testDelete() throws Exception{
		List<String> s = new ArrayList<>();
		s.add("xxxx");
		s.add("bbbb");
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(s);
		System.out.println(" ================= " + json);
		getMockMvc().perform(
				delete("/api/cart/goods")
				.content(json)
				.contentType(MediaType.APPLICATION_JSON)
				.sessionAttr(ServletBox.LOGIN_ID, "123321")
				.accept(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
			.andExpect(jsonPath("$.code").value("200"))
			.andDo(print());
	}
	
	@Test
	public void testList() throws Exception{
		getMockMvc().perform(
				get("/api/cart/goods/list")
				.sessionAttr(ServletBox.LOGIN_ID, "123321")
				.accept(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
			.andExpect(jsonPath("$.code").value("200"))
			.andDo(print());
	}
	@Test
	public void testPut() throws Exception{
		GoodsInCartVO vo = new GoodsInCartVO();
		vo.setCount(new Random().nextInt(9)+1);
		vo.setGID("1233211233211233211232");
		vo.setGname("东阿阿胶阿胶糕");
		vo.setMID("1233211233211233211232");
		vo.setMname("东阿阿胶长");
		vo.setPrice(new BigDecimal("1200.00"));
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(vo);
		System.out.println(json);
		getMockMvc().perform(
					 put("/api/cart")
					 .content(json)
					.sessionAttr(ServletBox.LOGIN_ID, "123321")
					.sessionAttr(ServletBox.LOGIN_CART_ID, "123321123")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
				)
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(jsonPath("$.code").value("200"))
				.andDo(print());
	}
	@Test
	public void testUpdate0() throws Exception{
		getMockMvc().perform(
					 post("/api/cart/goods/{id}", "rCxskSt0Qp-0zUwiyG7FVw")
					.param("count", "4")
					.sessionAttr(ServletBox.LOGIN_ID, "123321")
					.accept(MediaType.APPLICATION_JSON)
				)
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(jsonPath("$.code").value("200"))
				.andDo(print());
	}
	
}
