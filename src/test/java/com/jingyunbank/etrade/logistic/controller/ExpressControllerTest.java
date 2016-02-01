package com.jingyunbank.etrade.logistic.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.http.MediaType;

import com.jingyunbank.etrade.TestCaseBase;

public class ExpressControllerTest extends TestCaseBase {

	@Test
	public void testList() throws Exception {
		getMockMvc().perform(get("/api/logistic/express/list").characterEncoding("utf-8")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(jsonPath("$.code").value("200")).andDo(print());
	}
	
	
	@Test
	public void testInfo() throws Exception {
		getMockMvc().perform(get("/api/logistic/express/info/123456/ZTO/719151393719").characterEncoding("utf-8")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(jsonPath("$.code").value("200")).andDo(print());
	}
}
