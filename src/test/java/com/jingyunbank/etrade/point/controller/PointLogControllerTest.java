package com.jingyunbank.etrade.point.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.jingyunbank.core.web.ServletBox;
import com.jingyunbank.etrade.TestCaseBase;

public class PointLogControllerTest extends TestCaseBase {

	
	/**
	 * 查询
	 * @throws Exception
	 */
	@Test
	public void test0() throws Exception{
		getMockMvc().perform(
				 get("/point/log/2")
				 .sessionAttr(ServletBox.LOGIN_ID, "1")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
			.andExpect(jsonPath("$.code").value("200"))
			.andDo(MockMvcResultHandlers.print())
			.andDo(print());
	}
	
	
	/**
	 * 查询
	 * @throws Exception
	 */
	@Test
	public void testList() throws Exception{
		getMockMvc().perform(
				 get("/point/log/list/2")
				 .param("offset", "0")
				 .param("size", "10")
				 
				 .sessionAttr(ServletBox.LOGIN_ID, "1")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
			.andExpect(jsonPath("$.code").value("200"))
			.andDo(MockMvcResultHandlers.print())
			.andDo(print());
	}
}
