package com.jingyunbank.etrade.userinfo.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import org.junit.Test;

import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.jingyunbank.etrade.TestCaseBase;

/**
 * @author Administrator 
 * @date 2015年11月6日
	@todo TODO
 */

public class UserInfoControllerTest extends TestCaseBase{
	
	
/**
 * 测试添加个人信息
 * @throws Exception
 */
	@Test
	 public void Test0() throws Exception{
		/*SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyy.mm.dd");*/
		getMockMvc().perform(
				 put("/api/userInfo/")
				.param("uid", "396")
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
	
	/**
	 * 修改个人资料信息
	 * @throws Exception
	 */
		@Test
		public void Test1() throws Exception{
			getMockMvc().perform(
				 post("/api/userInfo/update")
				.param("uid", "IoBST6elTCarSyzl6Z277g")
				.param("country", "4")
				.param("province", "4")
				.param("city", "4")
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
		
		
		/**
		 * 通过uid查询
		 * @throws Exception
		 */
		
		@Test
		public void Test2() throws Exception{
			getMockMvc().perform(
					 get("/api/userInfo/selectById/IoBST6elTCarSyzl6Z277g")
					 
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(jsonPath("$.code").value("200"))
				.andDo(MockMvcResultHandlers.print())
				.andDo(print());
		}
	
}