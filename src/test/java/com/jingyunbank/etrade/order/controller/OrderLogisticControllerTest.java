package com.jingyunbank.etrade.order.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.http.MediaType;

import com.jingyunbank.core.web.Login;
import com.jingyunbank.etrade.TestCaseBase;

public class OrderLogisticControllerTest extends TestCaseBase{
	
	@Test
	public void testSuccess() throws Exception{
		getMockMvc().perform(
					 put("/api/orders/receipt")
					.contentType(MediaType.APPLICATION_JSON)
					.content("{\"oids\":[\"asdf\"], \"tradepwd\":\"abcd1234\"}")
					.sessionAttr(Login.LOGIN_ID, "Ma9ogkIXSW-y0uSrvfqVIQ")
					.characterEncoding("UTF-8")
					.accept(MediaType.APPLICATION_JSON)
				)
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(jsonPath("$.code").value("500"))
				.andDo(print());
		
	}
}
