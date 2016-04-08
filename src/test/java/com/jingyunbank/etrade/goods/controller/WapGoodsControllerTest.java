package com.jingyunbank.etrade.goods.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jingyunbank.etrade.TestCaseBase;
import com.jingyunbank.etrade.wap.goods.bean.GoodsAttrVO;

public class WapGoodsControllerTest extends TestCaseBase {


	
	
	@Test
	public void getAll() throws Exception {
		getMockMvc().perform(get("/api/wap/goods/list").characterEncoding("utf-8")
				
				).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(jsonPath("$.code").value("200")).andDo(print());
	}
	
	
	@Test
	public void getcon() throws Exception {
		getMockMvc().perform(get("/api/wap/goods/condition/g001").characterEncoding("utf-8")
				).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(jsonPath("$.code").value("200")).andDo(print());
	}
	
	
	@Test
	public void getsku() throws Exception {
		getMockMvc().perform(get("/api/wap/goods/single/g001/1,3,5").characterEncoding("utf-8")
				).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(jsonPath("$.code").value("200")).andDo(print());
	}
	
	@Test
	public void getinfo() throws Exception {
		getMockMvc().perform(get("/api/wap/goods/info/g001").characterEncoding("utf-8")
				).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(jsonPath("$.code").value("200")).andDo(print());
	}
	
	@Test
	public void getdetail() throws Exception {
		getMockMvc().perform(get("/api/wap/goods/detail/g001").characterEncoding("utf-8")
				).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(jsonPath("$.code").value("200")).andDo(print());
	}


	@Test
	public void updatestock() throws Exception {
		getMockMvc().perform(get("/api/wap/goods/stock/list").characterEncoding("utf-8")
				.param("skuids", new String[]{"sku001","sku002","sku003"})
				).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(jsonPath("$.code").value("200")).andDo(print());
	}

}
