package com.jingyunbank.etrade.goods.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jingyunbank.etrade.TestCaseBase;
import com.jingyunbank.etrade.goods.bean.BrandVO;
import com.jingyunbank.etrade.goods.bean.GoodsOperationVO;

public class GoodsOperationControllerTest extends TestCaseBase {
	@Test
	public void testAddGoods() throws Exception {
		GoodsOperationVO vo = new GoodsOperationVO();
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(vo);
		getMockMvc()
				.perform(put("/api/goodsOperation/save")
						.content(json)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andDo(MockMvcResultHandlers.print()).andReturn();

	}

	@Test
	public void selectById() throws Exception {
		getMockMvc().perform(get("/api/goodsOperation/1").characterEncoding("utf-8"))
					.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(jsonPath("$.code").value("200")).andDo(print());
	}
	@Test
	public void updateVolume() throws Exception {
		getMockMvc().perform(post("/api/goodsOperation/updateVolume").characterEncoding("utf-8")
				.param("gid", "-_ctCfR9ShK5_6tdISLLNA")
				.param("count", "50")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
		.andExpect(jsonPath("$.code").value("200")).andDo(print());
	}
	/**
	 * 修改上下架状态
	 * @throws Exception
	 */
	@Test
	public void updateUp() throws Exception {
		getMockMvc().perform(post("/api/goodsOperation/up/-_ctCfR9ShK5_6tdISLLNA").characterEncoding("utf-8")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
		.andExpect(jsonPath("$.code").value("200")).andDo(print());
	}
	 
	@Test
	public void updateById() throws Exception {
		GoodsOperationVO vo = new GoodsOperationVO();
		vo.setGoodsTitle("aaaa");
//		vo.setProductionDate(new Date());
		
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(vo);
		getMockMvc()
				.perform(put("/api/goodsOperation/update/-_ctCfR9ShK5_6tdISLLNA")
						.content(json)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andDo(MockMvcResultHandlers.print()).andReturn();
	}
	
	@Test
	public void getbrandBymid() throws Exception {
		getMockMvc().perform(get("/api/goodsOperation/brands/1").characterEncoding("utf-8")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
		.andExpect(jsonPath("$.code").value("200")).andDo(print());
	}
	@Test
	public void getmerchant() throws Exception {
		getMockMvc().perform(get("/api/goodsOperation/merchant/list").characterEncoding("utf-8")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
		.andExpect(jsonPath("$.code").value("200")).andDo(print());
	}
	
	
	@Test
	public void saveBrand() throws Exception {
		BrandVO vo = new BrandVO();
		vo.setName("测试品牌添加");
		vo.setMID("1");
		vo.setDesc("测试品牌添加测试品牌添加");
		
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(vo);
		getMockMvc().perform(post("/api/brand/save").characterEncoding("utf-8")
				.content(json)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
		.andExpect(jsonPath("$.code").value("200")).andDo(print());
	}

	
	@Test
	public void getbrand() throws Exception {
		getMockMvc().perform(get("/api/brand/updateveiw/1").characterEncoding("utf-8")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
		.andExpect(jsonPath("$.code").value("200")).andDo(print());
	}
	
	@Test
	public void updateBrand() throws Exception {
		BrandVO vo = new BrandVO();
		vo.setName("测试品牌添加-2");
		vo.setMID("2");
		vo.setDesc("测试品牌添加测试品牌添加-2");
		vo.setAdmin_sort(0);
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(vo);
		getMockMvc().perform(post("/api/brand/update/BZPAEhpaQkiweDTSLTGJ2A").characterEncoding("utf-8")
				.content(json)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
		.andExpect(jsonPath("$.code").value("200")).andDo(print());
	}

}
