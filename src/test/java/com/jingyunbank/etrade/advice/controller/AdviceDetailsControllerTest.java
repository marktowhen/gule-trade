package com.jingyunbank.etrade.advice.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.http.MediaType;

import com.jingyunbank.etrade.TestCaseBase;

public class AdviceDetailsControllerTest extends TestCaseBase{
	/**
	 * 增加多个内容
	 * @throws Exception
	 */
	@Test
	public void testSaveDetails() throws Exception{
		
		getMockMvc().perform(
				put("/api/advice/savedetails")
				.param("sid", "B_zVBGGoRkqdq1Zvs5tLxw")
				.param("name", "人物事故")
				.param("title", "个人专辑")
				.param("content", "受不少新老客户的关爱")
				.param("status", "1")
				.param("orders", "5")
				.accept(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
			.andDo(print());
	}
	/**
	 * 删除某一条记录
	 * @throws Exception
	 */
	@Test
	public void testDeleteDetails() throws Exception{
		getMockMvc().perform(
				post("/api/advice/delete/1Qy7eqxSStKzgp-O8QjiXw")
				.accept(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
			.andDo(print());
	}
	/**
	 * 修改内容信息
	 * @throws Exception
	 */
	@Test
	public void testUpdateDetails() throws Exception{
		getMockMvc().perform(
				post("/api/advice/update")
				.param("id", "nEhuYPL2Rcus0_KG4pIfXA")
				.param("name", "人物传记")
				.accept(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
			.andDo(print());
	}
	/**
	 * 通过sid查出各种内容
	 * @throws Exception
	 */
	@Test
	public void testGetDetailsBySid() throws Exception{
		getMockMvc().perform(
				get("/api/advice/details/B_zVBGGoRkqdq1Zvs5tLxw")
				.accept(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
			.andDo(print());
	}
	/**
	 * 通过id查出对应的内容
	 * @throws Exception
	 */
	@Test
	public void testGetDetailByid() throws Exception{
		getMockMvc().perform(
				get("/api/advice/detail/zEm49RFHSq-QrxQK38J1Gw")
				.accept(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
			.andDo(print());
	}
	
}
