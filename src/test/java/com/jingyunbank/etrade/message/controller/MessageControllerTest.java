package com.jingyunbank.etrade.message.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.jingyunbank.core.web.Login;
import com.jingyunbank.etrade.TestCaseBase;

public class MessageControllerTest extends TestCaseBase{

	/**
	 * 发站内信
	 * @throws Exception
	 * 2015年11月13日 qxs
	 */
	@Test
	public void testSendMessage() throws Exception{
		getMockMvc().perform(
				post("/api/message/")
				.param("title", "测试")
				.param("content", "真帅")
				.param("receiveUID", "1")
				.sessionAttr(Login.LOGIN_USER_ID, "1")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code").value("200"))
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andDo(MockMvcResultHandlers.print())  
				.andReturn();
			
	}
	/**
	 * 单条详情
	 * @throws Exception
	 * 2015年11月13日 qxs
	 */
	@Test
	public void testGetSingle() throws Exception{
		getMockMvc().perform(
				get("/api/message/Xo8oTaXUQi-u81JKpoIndA")
				.sessionAttr(Login.LOGIN_USER_ID, "1")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code").value("200"))
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andDo(MockMvcResultHandlers.print())  
				.andReturn();
			
	}
	
	/**
	 * 列表
	 * @throws Exception
	 * 2015年11月13日 qxs
	 */
	@Test
	public void testList() throws Exception{
		getMockMvc().perform(
				get("/api/message/list/1")
				.param("needReadStatus", "true")
				.param("hasRead", "true")
				.param("offset", "0")
				.param("size", "5")
				.sessionAttr(Login.LOGIN_USER_ID, "1")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code").value("200"))
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andDo(MockMvcResultHandlers.print())  
				.andReturn();
			
	}
	/**
	 * 消息数量
	 * @throws Exception
	 * 2015年11月13日 qxs
	 */
	@Test
	public void testGetAmount() throws Exception{
		getMockMvc().perform(
				get("/api/message/amount/1")
				.param("hasRead", "false")
				.sessionAttr(Login.LOGIN_USER_ID, "1")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code").value("200"))
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andDo(MockMvcResultHandlers.print())  
				.andReturn();
			
	}
	
	/**
	 * 未读列表
	 * @throws Exception
	 * 2015年11月13日 qxs
	 */
	@Test
	public void testListUnread() throws Exception{
		getMockMvc().perform(
				get("/api/message/list/unread/1")
				.param("offset", "0")
				.param("size", "5")
				.sessionAttr(Login.LOGIN_USER_ID, "1")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code").value("200"))
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andDo(MockMvcResultHandlers.print())  
				.andReturn();
			
	}
	/**
	 * 未读数量
	 * @throws Exception
	 * 2015年11月13日 qxs
	 */
	@Test
	public void testGetAmountUnread() throws Exception{
		getMockMvc().perform(
				get("/api/message/amount/unread/1")
				.sessionAttr(Login.LOGIN_USER_ID, "1")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code").value("200"))
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andDo(MockMvcResultHandlers.print())  
				.andReturn();
			
	}
	
	/**
	 * 删除
	 * @throws Exception
	 * 2015年11月13日 qxs
	 */
	@Test
	public void testdelete() throws Exception{
		getMockMvc().perform(
				delete("/api/message/WBok41umRUqb1jrHzS2C0A,cfk8_BplSSygxukvXXQkpw")
				.sessionAttr(Login.LOGIN_USER_ID, "1")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code").value("200"))
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andDo(MockMvcResultHandlers.print())  
				.andReturn();
			
	}
	
	/**
	 * 设置已读未读
	 * @throws Exception
	 * 2015年11月13日 qxs
	 */
	@Test
	public void testRead() throws Exception{
		getMockMvc().perform(
				put("/api/message/WBok41umRUqb1jrHzS2C0A,cfk8_BplSSygxukvXXQkpw")
				.param("hasRead", "true")
				.sessionAttr(Login.LOGIN_USER_ID, "1")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code").value("200"))
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andDo(MockMvcResultHandlers.print())  
				.andReturn();
			
	}
}
