package com.jingyunbank.etrade.user.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.jingyunbank.etrade.TestCaseBase;
import com.jingyunbank.etrade.api.user.IUserService;
import com.jingyunbank.etrade.api.user.bo.Users;
import com.jingyunbank.etrade.base.constant.Constant;
import com.jingyunbank.etrade.user.bean.UserVO;

public class AddressControllerTest extends TestCaseBase {
	
	@Autowired
	private IUserService userService;
	/**
	 * 测试新增
	 * @throws Exception
	 */
	@Test
	public void test0() throws Exception{
		getMockMvc().perform(
				 put("/address/add")
				.param("UID", "1")
				.param("name", "q")
				.param("country", "1")
				.param("province", "2")
				.param("city", "1")
				.param("address", "山东济南")
				.param("zipcode", "25600")
				.param("receiver", "aaa")
				.param("mobile", "1876616980")
				.param("telephone", "84936795")
				.param("defaulted", "true")
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
	 * 测试修改
	 * @throws Exception
	 */
	@Test
	public void testUpdate() throws Exception{
		getMockMvc().perform(
				 post("/address/refresh")
				 .param("ID", "67b_hKjITVyO93Kb9lTyXw")
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
		Optional<Users> uOptional = userService.getByUid("1");
		if(uOptional.isPresent()){
			UserVO uservo = new UserVO();
			BeanUtils.copyProperties(uOptional.get(), uservo);
			getMockMvc().perform(
					post("/address/delete")
					.param("IDs", "b,t")
					.sessionAttr(Constant.SESSION_USER, uservo)
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk())
					.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(MockMvcResultHandlers.print())  
					.andReturn();
			
		}else{
			
			System.err.println("用户不存在");
		}
	}
	
	/**
	 * 测试分页
	 * size 每页条数
	 * offset = 当前页码 从1开始
	 * @throws Exception
	 */
	@Test
	public void testListPage() throws Exception{
		Optional<Users> uOptional = userService.getByUid("1");
		if(uOptional.isPresent()){
			UserVO uservo = new UserVO();
			BeanUtils.copyProperties(uOptional.get(), uservo);
			getMockMvc().perform(
					get("/address/queryPage")
					.param("offset", "2")
					.param("size", "2")
					.sessionAttr(Constant.SESSION_USER, uservo)
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk())
					.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
					.andDo(MockMvcResultHandlers.print())  
					.andReturn();
			
		}else{
			
			System.err.println("用户不存在");
		}
	}
	
}
