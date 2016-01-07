package com.jingyunbank.etrade.vip.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jingyunbank.core.web.Login;
import com.jingyunbank.etrade.TestCaseBase;
import com.jingyunbank.etrade.vip.coupon.bean.DiscountCouponVO;

public class DiscountCouponControllerTest extends TestCaseBase{
	
		/**
		 * 新增
		 * @throws Exception
		 * 2015年11月16日 qxs
		 */
		@Test
		public void testSave() throws Exception{
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			DiscountCouponVO vo = new DiscountCouponVO();
			vo.setValue(new BigDecimal(50));
			vo.setStart(format.parse("2015-01-01 00:00:00"));
			vo.setEnd(format.parse("2025-12-31 23:59:59"));
			vo.setThreshhold(new BigDecimal(100));
			vo.setDiscount(new BigDecimal(0.5));
			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(vo);
			getMockMvc().perform(
					 post("/api/vip/coupon/discountcoupon/")
					 .content(json)
					.sessionAttr(Login.LOGIN_ID, "1")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code").value("200"))
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andDo(MockMvcResultHandlers.print())
				.andDo(print());
		}
		
		
		/**
		 * 新增多个
		 * @throws Exception
		 * 2015年11月16日 qxs
		 */
		@Test
		public void testSaveMuti() throws Exception{
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			DiscountCouponVO vo = new DiscountCouponVO();
			vo.setValue(new BigDecimal(50));
			vo.setStart(format.parse("2016-01-01 00:00:00"));
			vo.setEnd(format.parse("2016-6-30 23:59:59"));
			vo.setThreshhold(new BigDecimal(0));
			vo.setDiscount(new BigDecimal(0.2));
			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(vo);
			getMockMvc().perform(
					 post("/api/vip/coupon/discountcoupon/10000")
					 .content(json)
					.sessionAttr(Login.LOGIN_ID, "1")
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
					 get("/api/vip/coupon/discountcoupon/isvalid")
					.param("code", "20151117112038")
					.sessionAttr(Login.LOGIN_ID, "1")
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
					 delete("/api/vip/coupon/discountcoupon/")
					.param("code", "20151117110618")
					.sessionAttr(Login.LOGIN_ID, "1")
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
					 get("/api/vip/coupon/discountcoupon/list")
					 //.param("validTime", "true")
					 .sessionAttr(Login.LOGIN_ID, "1")
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
					 get("/api/vip/coupon/discountcoupon/amount")
					// .param("validTime", "true")
					 .sessionAttr(Login.LOGIN_ID, "1")
					 .contentType(MediaType.APPLICATION_JSON)
					 .accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code").value("200"))
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andDo(MockMvcResultHandlers.print())
				.andDo(print());
		}


}
