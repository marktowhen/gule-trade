package com.jingyunbank.etrade.user.controller;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.Optional;

import net.minidev.json.JSONObject;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.jingyunbank.etrade.TestCaseBase;
import com.jingyunbank.etrade.api.user.IUserService;
import com.jingyunbank.etrade.api.user.bo.Users;
import com.jingyunbank.etrade.base.constant.Constant;
import com.jingyunbank.etrade.user.bean.UserVO;

public class AddressControllerTest extends TestCaseBase {
	@Autowired
	private IUserService userService;
	//private RestTemplate restTemplate = new TestRestTemplate();
	@Autowired
	private WebApplicationContext wac;
	private MockMvc mockMvc;
	
	@Before
	public void init(){
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}
	/**
	 * 测试新增
	 * @throws Exception
	 */
	@Test
	public void test0() throws Exception{
		assertNotNull(userService);
		assertNotNull(mockMvc);
		JSONObject json = new JSONObject();
		json.put("loginfo", "loginfo");
		json.put("password", "password");
		mockMvc
			//请求信息
		//put("/orders/new").content("{'addressID':'123321123'}").contentType(MediaType.APPLICATION_JSON)
		//?loginfo=loginfo&password=password
			.perform(post("/address/add")
					.param("name", "利的老师靠反垄断思考")
					.content(json.toJSONString())
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
			.andDo(MockMvcResultHandlers.print())  
		            .andReturn();
		//System.out.println(restTemplate.getForEntity("http://localhost:8080/user", String.class).getBody());
		
	}
	
	
	/**
	 * 测试修改
	 * @throws Exception
	 */
	@Test
	public void testUpdate() throws Exception{
		assertNotNull(userService);
		assertNotNull(mockMvc);
		mockMvc.perform(
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
		assertNotNull(userService);
		assertNotNull(mockMvc);
		Optional<Users> uOptional = userService.getByUid("1");
		if(uOptional.isPresent()){
			UserVO uservo = new UserVO();
			BeanUtils.copyProperties(uOptional.get(), uservo);
			mockMvc.perform(
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
		assertNotNull(userService);
		assertNotNull(mockMvc);
		Optional<Users> uOptional = userService.getByUid("1");
		if(uOptional.isPresent()){
			UserVO uservo = new UserVO();
			BeanUtils.copyProperties(uOptional.get(), uservo);
			mockMvc.perform(
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
