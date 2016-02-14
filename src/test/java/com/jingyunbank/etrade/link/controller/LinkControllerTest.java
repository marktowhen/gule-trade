package com.jingyunbank.etrade.link.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jingyunbank.etrade.TestCaseBase;
import com.jingyunbank.etrade.goods.bean.BrandVO;
import com.jingyunbank.etrade.statics.links.bean.LinkVO;

public class LinkControllerTest extends TestCaseBase {

	@Test
	public void testList() throws Exception {
		getMockMvc().perform(get("/api/link/all/list").characterEncoding("utf-8")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(jsonPath("$.code").value("200")).andDo(print());
	}
	
	
	@Test
	public void testOne() throws Exception {
		getMockMvc().perform(get("/api/link/updateview/44gmqCnPT5SAEQiUB7PRHg").characterEncoding("utf-8")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(jsonPath("$.code").value("200")).andDo(print());
	}
	
	
	
	@Test
	public void del() throws Exception {
		getMockMvc().perform(put("/api/link/dQXiQ0ZiSW6cT74ZVuoQnA").characterEncoding("utf-8")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(jsonPath("$.code").value("200")).andDo(print());
	}

	@Test
	public void saveLink() throws Exception {
		LinkVO vo = new LinkVO();
		vo.setName("世界杯");
		vo.setImg("img....");
		vo.setStatus(true);
		vo.setUrl("url...");
		vo.setOrder(2);
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(vo);
		getMockMvc()
				.perform(post("/api/link/save").characterEncoding("utf-8").content(json)
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(jsonPath("$.code").value("200")).andDo(print());
	}
	
	
	@Test
	public void updateLink() throws Exception {
		LinkVO vo = new LinkVO();
		vo.setName("世界杯1234");
		vo.setImg("img1234....");
		vo.setStatus(true);
		vo.setUrl("url1234...");
		vo.setOrder(2);
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(vo);
		getMockMvc()
				.perform(post("/api/link/update/dQXiQ0ZiSW6cT74ZVuoQnA").characterEncoding("utf-8").content(json)
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(jsonPath("$.code").value("200")).andDo(print());
	}

}
