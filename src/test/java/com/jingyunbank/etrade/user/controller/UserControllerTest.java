package com.jingyunbank.etrade.user.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import net.minidev.json.JSONObject;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.jingyunbank.etrade.TestCaseBase;
import com.jingyunbank.etrade.base.constant.Constant;

public class UserControllerTest extends TestCaseBase {
	/**
	 * 测试登录
	 * @throws Exception
	 */
	@Test
	public void test0() throws Exception{
		JSONObject json = new JSONObject();
		json.put("loginfo", "loginfo");
		json.put("password", "password");
		getMockMvc()
			.perform(post("/user/login").param("loginfo", "qxstest").param("password", "123456")
					.content(json.toJSONString())
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
			.andDo(MockMvcResultHandlers.print())  
		            .andReturn();
		//System.out.println(restTemplate.getForEntity("http://localhost:8080/user", String.class).getBody());
		
	}
	//测试注册
	@Test
	public void test1() throws Exception{
		getMockMvc().perform(
				 put("/user/register")
				.param("username", "xiaoyux")
				.param("mobile", "15788877899")
				.param("email", "555901@qq.com")
				.param("password", "123456789")
				.param("tradepwd", "12345678")
				.param("nickname", "xiaoxue")
				.param("locked", "true")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
			.andExpect(jsonPath("$.code").value("500"))
			.andDo(MockMvcResultHandlers.print())
			.andDo(print());
		//System.out.println(restTemplate.getForEntity("http://localhost:8080/user", String.class).getBody());
		
	}
	
	/**
	 * 测试发短信
	 * @throws Exception
	 */
	@Test
	public void testMessage() throws Exception{
		getMockMvc().perform(
				get("/api/user/message")
				.param("mobile", "15853166853")
				.sessionAttr(Constant.LOGIN_ID, "1")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andDo(MockMvcResultHandlers.print())  
				.andReturn();
			
	}
	
	
	/**
	 * 测试验证短信
	 * @throws Exception
	 */
	@Test
	public void testCheckMobile() throws Exception{
		getMockMvc().perform(
				post("/api/user/message")
				.param("mobile", "15853166853")
				.param("code", "894")
				.sessionAttr(Constant.LOGIN_ID, "1")
				.sessionAttr("UNCHECK_MOBILE", "15853166853")
				.sessionAttr(Constant.SMS_MESSAGE, "894")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andDo(MockMvcResultHandlers.print())  
				.andReturn();
			
	}
	
}
