package com.jingyunbank.etrade.point.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.jingyunbank.core.web.Login;
import com.jingyunbank.etrade.TestCaseBase;
import com.jingyunbank.etrade.api.vip.point.service.context.IPointContextService;

public class PointControllerTest extends TestCaseBase {

	@Autowired
	private IPointContextService pointContextService; 
	
	/**
	 * 查询
	 * @throws Exception
	 */
	@Test
	public void test0() throws Exception{
		getMockMvc().perform(
				 get("/point/2")
				 .sessionAttr(Login.LOGIN_USER_ID, "1")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
			.andExpect(jsonPath("$.code").value("200"))
			.andDo(MockMvcResultHandlers.print())
			.andDo(print());
	}
	
	/**
	 * 增加
	 * @throws Exception
	 */
	@Test
	public void testAdd() throws Exception{
		pointContextService.addPoint("2", 10, "测试");
	}
	
	/**
	 * 减少
	 * @throws Exception
	 */
	@Test
	public void testMinus() throws Exception{
		pointContextService.minusPoint("2", 10, "测试");
	}
}
