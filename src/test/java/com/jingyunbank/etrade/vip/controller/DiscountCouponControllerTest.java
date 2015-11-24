package com.jingyunbank.etrade.vip.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.jingyunbank.core.web.ServletBox;
import com.jingyunbank.etrade.TestCaseBase;

public class DiscountCouponControllerTest extends TestCaseBase{
	
		/**
		 * 新增
		 * @throws Exception
		 * 2015年11月16日 qxs
		 */
		@Test
		public void testSave() throws Exception{
			getMockMvc().perform(
					 put("/api/discountcoupon/")
					.param("code", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()))
					.param("value", "1")
					.param("discount", "0.99")
					.param("start", "2015-11-16 01:02:03")
					.param("end", "2016-11-18 13:10:90")
					.param("threshhold", "1.111")
					.sessionAttr(ServletBox.LOGIN_ID, "1")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code").value("200"))
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andDo(MockMvcResultHandlers.print())
				.andDo(print());
		}
		
		/**
		 * 判断卡号是否可以被激活
		 * @throws Exception
		 * 2015年11月16日 qxs
		 */
		@Test
		public void testIsValid() throws Exception{
			getMockMvc().perform(
					 get("/api/discountcoupon/isvalid")
					.param("code", "20151117112038")
					.sessionAttr(ServletBox.LOGIN_ID, "1")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code").value("200"))
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andDo(MockMvcResultHandlers.print())
				.andDo(print());
		}
		
		/**
		 * 删除
		 * @throws Exception
		 * 2015年11月16日 qxs
		 */
		@Test
		public void testDel() throws Exception{
			getMockMvc().perform(
					 delete("/api/discountcoupon/")
					.param("code", "20151117110618")
					.sessionAttr(ServletBox.LOGIN_ID, "1")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code").value("200"))
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andDo(MockMvcResultHandlers.print())
				.andDo(print());
		}
		
		/**
		 * 列表查询
		 * @throws Exception
		 * 2015年11月19日 qxs
		 */
		@Test
		public void testList() throws Exception{
			getMockMvc().perform(
					 get("/api/discountcoupon/list")
					 //.param("validTime", "true")
					 .sessionAttr(ServletBox.LOGIN_ID, "1")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code").value("200"))
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andDo(MockMvcResultHandlers.print())
				.andDo(print());
		}
		
		/**
		 * 数量查询
		 * @throws Exception
		 * 2015年11月19日 qxs
		 */
		@Test
		public void testAmount() throws Exception{
			getMockMvc().perform(
					 get("/api/discountcoupon/amount")
					// .param("validTime", "true")
					 .sessionAttr(ServletBox.LOGIN_ID, "1")
					 .contentType(MediaType.APPLICATION_JSON)
					 .accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code").value("200"))
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andDo(MockMvcResultHandlers.print())
				.andDo(print());
		}


}
