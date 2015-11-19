package com.jingyunbank.etrade.information.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.http.MediaType;

import com.jingyunbank.etrade.TestCaseBase;

public class InformationSiteControllerTest extends TestCaseBase{
	
	@Test
	public void testSaveSite() throws Exception{
		getMockMvc().perform(
				put("/api/information/site")
				.param("SiteID", "103")
				.param("name", "品牌故事")
				.accept(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
			.andDo(print());
	}
	/**
	 * 通过siteid查出所有的子集
	 * @throws Exception
	 */
	@Test
	public void testSelectSites() throws Exception{
		getMockMvc().perform(
				get("/api/information/sites/103")
				.accept(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
			.andDo(print());
		
	}
}
