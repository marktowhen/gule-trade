package com.jingyunbank.etrade.goods.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

public class GoodsControllerTest extends TestCaseBase{
/*
//	 * 测试商品 名称模糊查询
//	 * @throws Exception
	@Test
	public void test0() throws Exception {
		getMockMvc().perform(get("/api/goods/阿胶").characterEncoding("utf-8"))
					.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(jsonPath("$.code").value("200")).andDo(print());
	}

	 
	@Test
	public void test1() throws Exception {
		getMockMvc().perform(post("/api/goods/listBrands").characterEncoding("utf-8"))
					.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(jsonPath("$.code").value("200")).andDo(print());
	}
	*//**
	 * 查询商品类别
	 * @throws Exception
	 *//*
	@Test
	public void test2() throws Exception {
		getMockMvc().perform(post("/api/goods/listTypes").characterEncoding("utf-8"))
					.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(jsonPath("$.code").value("200")).andDo(print());
	}
//	
	*//**
	 * 根据属性查询商品
	 * @throws Exception
	 *//*
	@Test
	public void test3() throws Exception {
		getMockMvc().perform(get("/api/goods/listByWhere").characterEncoding("utf-8"))
					.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(jsonPath("$.code").value("200")).andDo(print());
	}
	*/
	/**
	 * 查询首页热门推荐商品 liug  
	 * @throws Exception
	*/
	@Test
	public void test4() throws Exception {
		getMockMvc().perform(post("/api/goods/hotgoods/list").characterEncoding("utf-8"))
					.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(jsonPath("$.code").value("200")).andDo(print());
	}
	/**
	 * 查询24小时热卖推荐商品 liug  
	 * @throws Exception
	*/
	@Test
	public void test5() throws Exception {
		getMockMvc().perform(post("/api/goods/hot24goods/list").characterEncoding("utf-8"))
					.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(jsonPath("$.code").value("200")).andDo(print());
	}
	/**
	 * 我的足迹列表查询 liug  
	 * @throws Exception
	*/
	@Test
	public void test6() throws Exception {
		getMockMvc().perform(post("/api/goods/footprint/list").characterEncoding("utf-8"))
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
				 post("/api/goods/footprint/save")
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
				 post("/api/goods/collect/savemerchant")
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
				 post("/api/goods/collect/savegoods")
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
				 post("/api/goods/collect/listmerchantcollect")
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
				 post("/api/goods/collect/listgoodscollect")
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
