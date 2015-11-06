package com.jingyunbank.etrade.order.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.http.MediaType;

import com.jingyunbank.etrade.TestCaseBase;


public class CartControllerTest extends TestCaseBase{
	
	@Test
	public void test0() throws Exception{
		getMockMvc().perform(
				get("/carts/-XbGNv0RToW8LG96BNLpiw")
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
	
}
