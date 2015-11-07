package com.jingyunbank.etrade.goods.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.http.MediaType;

import com.jingyunbank.etrade.TestCaseBase;

public class GoodsControllerTest extends TestCaseBase{
//	/**
//	 * 测试商品 名称模糊查询
//	 * @throws Exception
//	 */
//	@Test
//	public void test0() throws Exception {
//		getMockMvc().perform(post("/goods/query/阿胶").characterEncoding("utf-8"))
//					.andExpect(status().isOk())
//				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
//				.andExpect(jsonPath("$.code").value("200")).andDo(print());
//	}
//	/**
//	 * 查询品牌
//	 * @throws Exception
//	 */
//	@Test
//	public void test1() throws Exception {
//		getMockMvc().perform(post("/goods/brands").characterEncoding("utf-8"))
//					.andExpect(status().isOk())
//				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
//				.andExpect(jsonPath("$.code").value("200")).andDo(print());
//	}
//	/**
//	 * 查询商品类别
//	 * @throws Exception
//	 */
//	@Test
//	public void test2() throws Exception {
//		getMockMvc().perform(post("/goods/types").characterEncoding("utf-8"))
//					.andExpect(status().isOk())
//				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
//				.andExpect(jsonPath("$.code").value("200")).andDo(print());
//	}
//	
//	/**
//	 * 根据属性查询商品
//	 * @throws Exception
//	 */
//	@Test
//	public void test3() throws Exception {
//		getMockMvc().perform(post("/goods/goodsByWhere").characterEncoding("utf-8"))
//					.andExpect(status().isOk())
//				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
//				.andExpect(jsonPath("$.code").value("200")).andDo(print());
//	}
//	
	/**
	 * 查询首页热门推荐商品 liug  待修改
	 * @throws Exception
	 */
	@Test
	public void test4() throws Exception {
		getMockMvc().perform(post("/goods/listHotGoods").characterEncoding("utf-8"))
					.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(jsonPath("$.code").value("200")).andDo(print());
	}
}
