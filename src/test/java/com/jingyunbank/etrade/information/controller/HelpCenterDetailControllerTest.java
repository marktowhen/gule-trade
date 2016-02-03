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
import com.jingyunbank.etrade.posts.help.bean.HelpCenterDetailVO;

public class HelpCenterDetailControllerTest extends TestCaseBase {
	
	/**
	 * 新增
	 * @throws Exception
	 * 2015年11月16日 qxs
	 */
	@Test
	public void testSave() throws Exception{
		HelpCenterDetailVO vo = new HelpCenterDetailVO();
		vo.setName("注册");
		vo.setSort(1);
		vo.setContent("呵呵哒");
		vo.setParentID("hb6hQsxbSEyFGfc6jwzNTQ");
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(vo);
		getMockMvc().perform(
				 post("/api/help/center/detail/")
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
		HelpCenterDetailVO vo = new HelpCenterDetailVO();
		vo.setName("注册2");
		vo.setSort(1);
		vo.setContent("呵呵哒");
		vo.setParentID("hb6hQsxbSEyFGfc6jwzNTQ");
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(vo);
		getMockMvc().perform(
				 put("/api/help/center/detail/A8AY8EhmSR-wqnpxaGS7Rg")
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
				 delete("/api/help/center/detail/A8AY8EhmSR-wqnpxaGS7Rg")
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
				 get("/api/help/center/detail/list/hb6hQsxbSEyFGfc6jwzNTQ")
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
	 * 单个查询
	 * @throws Exception
	 * 2015年11月19日 qxs
	 */
	@Test
	public void testSingle() throws Exception{
		getMockMvc().perform(
				 get("/api/help/center/detail/A8AY8EhmSR-wqnpxaGS7Rg")
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
