package com.jingyunbank.etrade.goods.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jingyunbank.etrade.TestCaseBase;
import com.jingyunbank.etrade.wap.goods.bean.GoodsInfoVO;

public class WapGoodsOperationControllerTest extends TestCaseBase {
	@Test
	public void testAddGoodsInfo() throws Exception {
		GoodsInfoVO vo1 = new GoodsInfoVO();
		vo1.setGID("g001");
		vo1.setKey("1111");
		vo1.setValue("2222");

		GoodsInfoVO vo2 = new GoodsInfoVO();
		vo2.setGID("g001");
		vo2.setKey("3333");
		vo2.setValue("4444");

		List<GoodsInfoVO> list = new ArrayList<GoodsInfoVO>();
		list.add(vo1);
		list.add(vo2);

		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(list);
		getMockMvc()
				.perform(post("/api/goods/operation/info/save").content(json).contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andDo(MockMvcResultHandlers.print()).andReturn();

	}
	
	
	@Test
	public void testupdateGoodsInfo() throws Exception {
		GoodsInfoVO vo1 = new GoodsInfoVO();
		vo1.setGID("g001");
		vo1.setKey("55");
		vo1.setValue("66");

		GoodsInfoVO vo2 = new GoodsInfoVO();
		vo2.setGID("g001");
		vo2.setKey("77");
		vo2.setValue("88");

		List<GoodsInfoVO> list = new ArrayList<GoodsInfoVO>();
		list.add(vo1);
		list.add(vo2);

		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(list);
		getMockMvc()
				.perform(post("/api/goods/operation/info/update/g001").content(json).contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andDo(MockMvcResultHandlers.print()).andReturn();

	}
	
	
	@Test
	public void a2() throws Exception {
		getMockMvc()
				.perform(put("/api/goods/operation/info/delete/1").contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andDo(MockMvcResultHandlers.print()).andReturn();

	}


}
