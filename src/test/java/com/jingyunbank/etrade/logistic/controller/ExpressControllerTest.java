package com.jingyunbank.etrade.logistic.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.http.MediaType;

import com.jingyunbank.core.web.Login;
import com.jingyunbank.etrade.TestCaseBase;

public class ExpressControllerTest extends TestCaseBase {

	@Test
	public void testList() throws Exception {
		getMockMvc().perform(get("/api/express/list").characterEncoding("utf-8")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(jsonPath("$.code").value("200")).andDo(print());
	}
}
