package com.jingyunbank.etrade.information.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.http.MediaType;

import com.jingyunbank.etrade.TestCaseBase;

public class InformationControllerTest extends TestCaseBase{
		
	@Test
	public void testSaveSite() throws Exception{
		getMockMvc().perform(
				put("/api/information/save")
				.param("SiteID", "104")
				.param("name", "继续添加")
				.accept(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
			.andDo(print());
	}
	/**
	 * 通过id查出所有的一级标题
	 * @throws Exception
	 */
	@Test
	public void testSelectList() throws Exception{
		getMockMvc().perform(
				get("/api/information/gets")
				.accept(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
			.andDo(print());
		
	}
}
