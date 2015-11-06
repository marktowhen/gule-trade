package com.jingyunbank.etrade.order.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.http.MediaType;

import com.jingyunbank.etrade.TestCaseBase;


public class CartControllerTest extends TestCaseBase{
	
	@Test
	public void testDelete() throws Exception{
		getMockMvc().perform(
				delete("/api/carts/-XbGNv0RToW8LG96BNLpiw")
				.sessionAttr("LOGIN_ID", "123321")
				.accept(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
			.andExpect(jsonPath("$.code").value("200"))
			.andDo(print());
	}
	
	@Test
	public void test0() throws Exception{
		getMockMvc().perform(
				get("/api/carts/-XbGNv0RToW8LG96BNLpiw")
				.param("offset", "0")
				.param("size", "10")
				.sessionAttr("LOGIN_ID", "123321")
				.accept(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
			.andExpect(jsonPath("$.code").value("200"))
			.andDo(print());
	}
	@Test
	public void testPut() throws Exception{
		getMockMvc().perform(
					 put("/api/carts")
					.param("GID", "1233211233211233211232")
					.param("price", "123.32")
					.param("count", "2")
					.sessionAttr("LOGIN_ID", "XXXX")
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
					 post("/api/carts")
					.param("GID", "1233211233211233211232")
					.param("price", "123.32")
					.param("count", "2")
					.sessionAttr("LOGIN_ID", "XXXX")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
				)
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(jsonPath("$.code").value("200"))
				.andDo(print());
	}
	
	@Test
	public void testUpdate1() throws Exception{
		getMockMvc().perform(
					 post("/api/carts")
					.param("ID", "12332112331212331212312")
					.param("GID", "1233211233211233211232")
					.param("price", "123.32")
					.param("count", "2")
					.sessionAttr("LOGIN_ID", "XXXX")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
				)
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(jsonPath("$.code").value("200"))
				.andDo(print());
	}
}
