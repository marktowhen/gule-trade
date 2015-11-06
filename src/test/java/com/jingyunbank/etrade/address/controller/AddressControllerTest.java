package com.jingyunbank.etrade.address.controller;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import net.minidev.json.JSONObject;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.jingyunbank.core.KeyGen;
import com.jingyunbank.etrade.TestCaseBase;
import com.jingyunbank.etrade.api.user.IUserService;
import com.jingyunbank.etrade.api.user.service.IAddressService;


public class AddressControllerTest extends TestCaseBase {
	/*@Autowired
	private IUserService userService;*/
	@Autowired IAddressService addressService;
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
		mockMvc.perform(
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
	
	
}
