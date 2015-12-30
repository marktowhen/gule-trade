package com.jingyunbank.etrade.goods.controller;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.jingyunbank.etrade.TestCaseBase;

public class GoodsControllerTest extends TestCaseBase{

	@Test
	public void testAll() throws Exception {
		getMockMvc().perform(get("/api/goods/all/list").characterEncoding("utf-8")
				.param("offset", 0+"")
				.param("size",10+"")
				).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(jsonPath("$.code").value("200")).andDo(print());
	}
	 
	@Test
	public void test1() throws Exception {
		getMockMvc().perform(get("/api/goods/brand/list_three").characterEncoding("utf-8"))
					.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(jsonPath("$.code").value("200")).andDo(print());
	}

	@Test
	public void test2() throws Exception {
		getMockMvc().perform(get("/api/goods/type/list").characterEncoding("utf-8"))
					.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(jsonPath("$.code").value("200")).andDo(print());
	}
	

	@Test
	public void test3() throws Exception {
		getMockMvc().perform(get("/api/goods/queryByWhere/list").characterEncoding("utf-8")
				.param("brands", new String[]{"1","2","3","4"})
				.param("types", new String[]{"1","2","3","4"})
				.param("beginPrice", new BigDecimal(50).toString())
				.param("endPrice", new BigDecimal(350).toString())
				.param("order", 1+""))
					.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(jsonPath("$.code").value("200")).andDo(print());
	}
	@Test
	public void test4() throws Exception {
		getMockMvc().perform(get("/api/goods/recommend/list").characterEncoding("utf-8"))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
		.andExpect(jsonPath("$.code").value("200")).andDo(print());
	}
	
	@Test
	public void test5() throws Exception {
		getMockMvc().perform(get("/api/goods/goodsMerchantByWhere/list").characterEncoding("utf-8")
				.param("brands", new String[]{"1","2"})
				.param("types", new String[]{"1","2"})
				.param("beginPrice", new BigDecimal(50).toString())
				.param("endPrice", new BigDecimal(250).toString())
				)
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
		.andExpect(jsonPath("$.code").value("200")).andDo(print());
	}
	
	@Test
	public void test11() throws Exception {
		getMockMvc().perform(get("/api/goods/stock/list").characterEncoding("utf-8")
				.param("gids", "1").param("gids", "2"))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
		.andExpect(jsonPath("$.code").value("200")).andDo(print());
	}
	@Test
	public void test6() throws Exception {
		getMockMvc().perform(get("/api/goods/merchantGoodsByWhere4/list").characterEncoding("utf-8")
				.param("brands", new String[]{"1","2"})
				.param("types", new String[]{"1","2"})
				.param("beginPrice", new BigDecimal(50).toString())
				.param("endPrice", new BigDecimal(350).toString())
				.param("MID", "1")
				)
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
		.andExpect(jsonPath("$.code").value("200")).andDo(print());
	}
	@Test
	public void testMax() throws Exception {
		getMockMvc().perform(get("/api/goods/merchantgoods/list").characterEncoding("utf-8")
				.param("brands", new String[]{"1","2"})
				.param("types", new String[]{"1","2"})
				.param("beginPrice", new BigDecimal(50).toString())
				.param("endPrice", new BigDecimal(350).toString())
				.param("MID", "1")
				.param("order", 1+"")
				.param("offset", 0+"")
				.param("size",10+"")
				)
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
		.andExpect(jsonPath("$.code").value("200")).andDo(print());
	}
	@Test
	public void test7() throws Exception {
		getMockMvc().perform(get("/api/goods/expand/list").characterEncoding("utf-8"))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
		.andExpect(jsonPath("$.code").value("200")).andDo(print());
	}
	/**
	 * 测试在结果中查询
	 * @throws Exception
	 */
	@Test
	public void test8() throws Exception {
		getMockMvc().perform(get("/api/goods/goodsByResult/list")
				.param("brands", new String[]{"1","2","3","4"})
				.param("types", new String[]{"1","2","3","4"})
				.param("beginPrice", new BigDecimal(50).toString())
				.param("endPrice", new BigDecimal(350).toString())
				.param("goodsName", "阿胶")
				.param("order", 3+"")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
		.andDo(MockMvcResultHandlers.print())  
	            .andReturn();
	}
	@Test
	public void test9() throws Exception {
		getMockMvc().perform(get("/api/goods//1")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
		.andDo(MockMvcResultHandlers.print())  
		.andReturn();
	}
	@Test
	public void test10() throws Exception {
		getMockMvc().perform(get("/api/goods/salesrecords/list/7uIBoJ03TneGthDFteMe6w")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
		.andDo(MockMvcResultHandlers.print())  
		.andReturn();
	}

}
