package com.jingyunbank.etrade.userinfo.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.SimpleDateFormat;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.jingyunbank.etrade.TestCaseBase;

/**
 * @author Administrator 
 * @date 2015年11月6日
	@todo TODO
 */

public class UserInfoControllerTest extends TestCaseBase{
	@Autowired
	private WebApplicationContext wac;
	private MockMvc mockMvc;
	
	@Before
	public void init(){
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}
	
	@Test
	 public void Test0() throws Exception{
		/*SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyy.mm.dd");*/
		 mockMvc.perform(
				 put("/userInfo/add")
				.param("uid", "34")
				.param("country", "1")
				.param("province", "1")
				.param("city", "1")
				.param("address", "山东济南")
				.param("education", "1")
				.param("job", "1")
				.param("income", "1")
				.param("avatar", "rt")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
			.andExpect(jsonPath("$.code").value("500"))
			.andDo(MockMvcResultHandlers.print())
			.andDo(print());
	 }
	
	
		@Test
		public void Test1() throws Exception{
			mockMvc.perform(
				 post("/userInfo/update")
				.param("uid", "1")
				.param("country", "2")
				.param("province", "2")
				.param("city", "2")
				.param("address", "山东济南")
				.param("education", "2")
				.param("job", "2")
				.param("income", "2")
				.param("avatar", "rt")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
			.andExpect(jsonPath("$.code").value("200"))
			.andDo(MockMvcResultHandlers.print())
			.andDo(print());
	}
	
}
