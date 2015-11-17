package com.jingyunbank.etrade.track.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.jingyunbank.core.web.ServletBox;
import com.jingyunbank.etrade.TestCaseBase;

public class TrackControllerTest extends TestCaseBase{
  
	/**
	 * 我的足迹列表查询 liug  
	 * @throws Exception
	*/
	@Test
	public void test6() throws Exception {
		getMockMvc().perform(post("/api/track/footprint/list").characterEncoding("utf-8"))
					.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(jsonPath("$.code").value("200")).andDo(print());
	}
	/**
	 * 测试添加我的足迹
	 * @throws Exception
	 */
	@Test
	public void test7() throws Exception{
		getMockMvc().perform(
				 post("/api/track/footprint/save")
				 .sessionAttr(ServletBox.LOGIN_ID, "1")
				.param("gid", "3")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
			.andDo(MockMvcResultHandlers.print())
			.andDo(print());
	}
	/**
	 * 测试添加我的收藏（商家）
	 * @throws Exception
	 */
	@Test
	public void test8() throws Exception{
		getMockMvc().perform(
				 post("/api/track/favorites/savemerchant")
				 .sessionAttr(ServletBox.LOGIN_ID, "1")
				.param("mid", "3")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
			.andDo(MockMvcResultHandlers.print())
			.andDo(print());
	}
	/**
	 * 测试添加我的收藏（商品）
	 * @throws Exception
	 */
	@Test
	public void test9() throws Exception{
		getMockMvc().perform(
				 post("/api/track/favorites/savegoods")
				 .sessionAttr(ServletBox.LOGIN_ID, "1")
				.param("gid", "3")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
			.andDo(MockMvcResultHandlers.print())
			.andDo(print());
	}
	/**
	 * 测试查询我的收藏（商家）
	 * @throws Exception
	 */
	@Test
	public void test10() throws Exception{
		getMockMvc().perform(
				 post("/api/track/favorites/listmerchantfavorites")
				 .sessionAttr(ServletBox.LOGIN_ID, "1")
				.param("uid", "3")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
			.andDo(MockMvcResultHandlers.print())
			.andDo(print());
	}
	/**
	 * 测试查询我的收藏（商品）
	 * @throws Exception
	 */
	@Test
	public void test11() throws Exception{
		getMockMvc().perform(
				 post("/api/track/favorites/listgoodsfavorites")
				 .sessionAttr(ServletBox.LOGIN_ID, "1")
				.param("uid", "3")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
			.andDo(MockMvcResultHandlers.print())
			.andDo(print());
	}
	
}
