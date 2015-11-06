package com.jingyunbank.etrade.goods.controller;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import com.jingyunbank.etrade.TestCaseBase;
import com.jingyunbank.etrade.api.goods.service.IGoodsService;
import com.jingyunbank.etrade.goods.service.GoodsService;

public class GoodsControllerTest extends TestCaseBase{
	private RestTemplate restTemplate = new TestRestTemplate();
	@Autowired
	private WebApplicationContext wac;
	private MockMvc mockMvc;

	@Resource
	private IGoodsService goodsService;

	@Before
	public void init() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}

	/**
	 * 测试商品 名称模糊查询
	 * @throws Exception
	 */
	@Test
	public void test0() throws Exception {
		assertNotNull(goodsService);
		assertNotNull(mockMvc);
		mockMvc.perform(post("/goods/query/阿胶").characterEncoding("utf-8"))
					.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(jsonPath("$.code").value("200")).andDo(print());
	}
	/**
	 * 查询品牌
	 * @throws Exception
	 */
	@Test
	public void test1() throws Exception {
		assertNotNull(goodsService);
		assertNotNull(mockMvc);
		mockMvc.perform(post("/goods/brands").characterEncoding("utf-8"))
					.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(jsonPath("$.code").value("200")).andDo(print());
	}
	/**
	 * 查询商品类别
	 * @throws Exception
	 */
	@Test
	public void test2() throws Exception {
		assertNotNull(goodsService);
		assertNotNull(mockMvc);
		mockMvc.perform(post("/goods/types").characterEncoding("utf-8"))
					.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(jsonPath("$.code").value("200")).andDo(print());
	}
	
	/**
	 * 根据属性查询商品
	 * @throws Exception
	 */
	@Test
	public void test3() throws Exception {
		assertNotNull(goodsService);
		assertNotNull(mockMvc);
		mockMvc.perform(post("/goods/goodsByWhere").characterEncoding("utf-8"))
					.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(jsonPath("$.code").value("200")).andDo(print());
	}
}
