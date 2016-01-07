package com.jingyunbank.etrade.user.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.jingyunbank.core.web.Login;
import com.jingyunbank.etrade.TestCaseBase;

public class AddressControllerTest extends TestCaseBase {
	
	
	/**
	 * 测试新增
	 * @throws Exception
	 */
	@Test
	public void test0() throws Exception{
		getMockMvc().perform(
				 post("/api/address/")
				 .sessionAttr(Login.LOGIN_ID, "1")
				.param("valid", "true")
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
	 * 测试新增
	 * @throws Exception
	 */
	@Test
	public void testAdd() throws Exception{
		getMockMvc().perform(
				post("/api/address/")
				 .sessionAttr(Login.LOGIN_ID, "1")
				.param("name", "q")
				.param("country", "1")
				.param("province", "2")
				.param("city", "1")
				.param("address", "山东济南")
				.param("zipcode", "25600")
				.param("receiver", "aaa")
				.param("mobile", "15853161111")
				.param("telephone", "84936795")
				.param("defaulted", "true")
				.param("valid", "true")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
			.andExpect(jsonPath("$.code").value("200"))
			.andDo(MockMvcResultHandlers.print())
			.andDo(print());
		//System.out.println(restTemplate.getForEntity("http://localhost:8080/user", String.class).getBody());
		
	}
	
	/**
	 * 测试修改
	 * @throws Exception
	 */
	@Test
	public void testUpdate() throws Exception{
		getMockMvc().perform(
				 post("/api/address/67b_hKjITVyO93Kb9lTyXw")
				 //.param("ID", "67b_hKjITVyO93Kb9lTyXw")
				.param("UID", "1")
				.param("name", "q23")
				.param("country", "1")
				.param("province", "3")
				.param("city", "3")
				.param("address", "山东济南3")
				.param("zipcode", "256003")
				.param("receiver", "3")
				.param("mobile", "18766169801")
				.param("telephone", "84936795")
				.param("defaulted", "true")
				.param("valid", "true")
				 .sessionAttr(Login.LOGIN_ID, "1")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
			.andDo(MockMvcResultHandlers.print())  
		            .andReturn();
		
	}
	
	
	/**
	 * 测试删除
	 * @throws Exception
	 */
	@Test
	public void testDelete() throws Exception{
		getMockMvc().perform(
				delete("/api/address/b,t")
				//.param("ID", "b,t")
				.sessionAttr(Login.LOGIN_ID, "1")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andDo(MockMvcResultHandlers.print())  
				.andReturn();
			
	}
	
	/**
	 * 测试分页
	 * size 每页条数
	 * offset = 偏移量 从0开始
	 * @throws Exception
	 */
	@Test
	public void testListPage() throws Exception{
		getMockMvc().perform(
				get("/api/address/list")
				.param("offset", "0")
				.param("size", "2")
				.param("uid", "1")
				.sessionAttr(Login.LOGIN_ID, "1")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andDo(MockMvcResultHandlers.print())  
				.andReturn();
			
	}
	
	/**
	 * 测试查数量
	 * @throws Exception
	 */
	@Test
	public void testGetAmout() throws Exception{
		getMockMvc().perform(
				get("/api/address/amount")
				.sessionAttr(Login.LOGIN_ID, "1")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andDo(MockMvcResultHandlers.print())  
				.andReturn();
			
	}
	
	/**
	 * 测试查单个
	 * @throws Exception
	 */
	@Test
	public void testGetSingle() throws Exception{
		getMockMvc().perform(
				get("/api/address/-D2c0YzaQFGDMqvpiFo3dw")
				.sessionAttr(Login.LOGIN_ID, "1")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andDo(MockMvcResultHandlers.print())  
				.andReturn();
			
	}
	
	/**
	 * 测试查默认地址
	 * @throws Exception
	 */
	@Test
	public void testGetDefault() throws Exception{
		getMockMvc().perform(
				get("/api/address/default")
				.sessionAttr(Login.LOGIN_ID, "1")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andDo(MockMvcResultHandlers.print())  
				.andReturn();
			
	}
	
}
