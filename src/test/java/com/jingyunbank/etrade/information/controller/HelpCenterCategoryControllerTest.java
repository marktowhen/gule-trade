package com.jingyunbank.etrade.information.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jingyunbank.core.web.Login;
import com.jingyunbank.etrade.TestCaseBase;
import com.jingyunbank.etrade.posts.help.bean.HelpCenterCategoryVO;

public class HelpCenterCategoryControllerTest extends TestCaseBase {
	
	/**
	 * 新增
	 * @throws Exception
	 * 2015年11月16日 qxs
	 */
	@Test
	public void testSave() throws Exception{
		HelpCenterCategoryVO vo = new HelpCenterCategoryVO();
		vo.setName("配送方式说明");
		vo.setSort(2);
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(vo);
		getMockMvc().perform(
				 post("/api/help/center/")
				 .content(json)
				.sessionAttr(Login.LOGIN_USER_ID, "2")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
					
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code").value("200"))
			.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
			.andDo(MockMvcResultHandlers.print())
			.andDo(print());
	}
	
	/**
	 * 修改
	 * @throws Exception
	 * 2015年11月16日 qxs
	 */
	@Test
	public void testUpdate() throws Exception{
		HelpCenterCategoryVO vo = new HelpCenterCategoryVO();
		vo.setName("购物流程说明2");
		vo.setSort(2);
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(vo);
		getMockMvc().perform(
				 put("/api/help/center/hb6hQsxbSEyFGfc6jwzNTQ")
				 .content(json)
				.sessionAttr(Login.LOGIN_USER_ID, "2")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
					
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code").value("200"))
			.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
			.andDo(MockMvcResultHandlers.print())
			.andDo(print());
	}
	
	/**
	 * 删除
	 * @throws Exception
	 * 2015年11月16日 qxs
	 */
	@Test
	public void testDel() throws Exception{
		getMockMvc().perform(
				 delete("/api/help/center/hb6hQsxbSEyFGfc6jwzNTQ")
				.sessionAttr(Login.LOGIN_USER_ID, "1")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code").value("200"))
			.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
			.andDo(MockMvcResultHandlers.print())
			.andDo(print());
	}
	
	/**
	 * 列表查询
	 * @throws Exception
	 * 2015年11月19日 qxs
	 */
	@Test
	public void testList() throws Exception{
		getMockMvc().perform(
				 get("/api/help/center/list")
				 .sessionAttr(Login.LOGIN_USER_ID, "1")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code").value("200"))
			.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
			.andDo(MockMvcResultHandlers.print())
			.andDo(print());
	}

}
