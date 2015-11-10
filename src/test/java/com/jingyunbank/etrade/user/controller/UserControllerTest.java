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

import com.jingyunbank.core.web.ServletBox;
import com.jingyunbank.etrade.TestCaseBase;

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
				 put("/api/user/register")
				.param("username", "1xiaoyui")
				.param("mobile", "12788888890")
				.param("email", "55590188@qq.com")
				.param("password", "123490090")
				.param("tradepwd", "1234787887")
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
	 * 测试修改手机号
	 * @throws Exception
	 */
	@Test
	public void testUpdatePhone() throws Exception{
		getMockMvc().perform(
				post("/api/user/update/phone")
				.sessionAttr(ServletBox.LOGIN_ID, "1")
				.param("mobile", "18766169803")
				
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(jsonPath("$.code").value("500"))
				.andDo(MockMvcResultHandlers.print())  
				.andReturn();
	}
	
	/**
	 * 测试修改密码
	 * @throws Exception
	 */
	@Test
	public void testUpdatePassword() throws Exception{
		getMockMvc().perform(
				post("/api/user/update/password")
				.sessionAttr(ServletBox.LOGIN_ID, "1")
				.param("password", "77777777777")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(jsonPath("$.code").value("200"))
				.andDo(MockMvcResultHandlers.print())  
				.andReturn();
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
				.sessionAttr(ServletBox.LOGIN_ID, "1")
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
				.sessionAttr(ServletBox.LOGIN_ID, "1")
				.sessionAttr("UNCHECK_MOBILE", "15853166853")
				.sessionAttr(ServletBox.SMS_MESSAGE, "894")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andDo(MockMvcResultHandlers.print())  
				.andReturn();
			
	}
	
	/**
	 * 测试发邮件
	 * @throws Exception
	 */
	@Test
	public void testSendEmail() throws Exception{
		getMockMvc().perform(
				get("/api/user/email")
				.sessionAttr(ServletBox.LOGIN_ID, "1")
				.param("email", "627956245@qq.com")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andDo(MockMvcResultHandlers.print())  
				.andReturn();
			
	}
	
	/**
	 * 测试验证邮件
	 * @throws Exception
	 */
	@Test
	public void testCheckEmail() throws Exception{
		getMockMvc().perform(
				get("/api/user/ckemail")
				.sessionAttr(ServletBox.LOGIN_ID, "1")
				.param("d", "1")
				.param("u", "NjI3OTU2MjQ1QHFxLmNvbX4xNDQ3MTMzNDUwOTkz")
				.param("m", "EAC57D28C8D9C3D52C0EF1E66727AE8B")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andDo(MockMvcResultHandlers.print())  
				.andExpect(jsonPath("$.code").value("500"))
				.andReturn();
			
	}
	
}
