package com.jingyunbank.etrade.vip.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.jingyunbank.core.web.ServletBox;
import com.jingyunbank.etrade.TestCaseBase;
import com.jingyunbank.etrade.api.vip.service.IUserDiscountCouponService;

public class UserDiscountCouponControllerTest extends TestCaseBase{
	
	@Autowired
	private IUserDiscountCouponService userDiscountCouponService;
	
		/**
		 * 新增
		 * @throws Exception
		 * 2015年11月16日 qxs
		 */
		@Test
		public void testSave() throws Exception{
			getMockMvc().perform(
					 put("/api/vip/coupon/discountcoupon/user/")
					.param("code", "20151117161936")
					.sessionAttr(ServletBox.LOGIN_ID, "1")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code").value("500"))
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andDo(MockMvcResultHandlers.print())
				.andDo(print());
		}
		/**
		 * 查询
		 * @throws Exception
		 * 2015年11月17日 qxs
		 */
		@Test
		public void testGet() throws Exception{
			getMockMvc().perform(
					 get("/api/vip/coupon/discountcoupon/user/1")
					.param("discountCouponVO.threshholdLow", "2")
					.sessionAttr(ServletBox.LOGIN_ID, "1")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code").value("200"))
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andDo(MockMvcResultHandlers.print())
				.andDo(print());
		}
		
		@Test
		public void testConsume() throws Exception{
			getMockMvc().perform(
					 post("/api/vip/coupon/discountcoupon/user/")
					.param("oid", "1")
					.param("couponId", "WYIgbRGhRqOP30iGE2Yrmg")
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
		 * 查询数量
		 * @throws Exception
		 * 2015年11月17日 qxs
		 */
		@Test
		public void testGetAmount() throws Exception{
			getMockMvc().perform(
					 get("/api/vip/coupon/discountcoupon/user/amount")
					//.param("cashCoupon.threshholdLow", "1")
					.sessionAttr(ServletBox.LOGIN_ID, "1")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code").value("200"))
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andDo(MockMvcResultHandlers.print())
				.andDo(print());
		}
		
		@Test
		public void testLock() throws Exception{
			userDiscountCouponService.lock("A2KMNs64RHiNZjSEi7rY3w", "2");
		}
		
		@Test
		public void testUnlock() throws Exception{
			userDiscountCouponService.unlock("A2KMNs64RHiNZjSEi7rY3w", "2");
		}
		
		
		@Test
		public void testSingle() throws Exception{
			userDiscountCouponService.single("A2KMNs64RHiNZjSEi7rY3w", "2");
		}
		
		/**
		 * 查询可用的
		 * @throws Exception
		 * 2015年11月17日 qxs
		 */
		@Test
		public void testUseableList() throws Exception{
			getMockMvc().perform(
					 get("/api/vip/coupon/discountcoupon/user/useable/2")
					.param("orderPrice", "60")
					.sessionAttr(ServletBox.LOGIN_ID, "2")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code").value("200"))
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andDo(MockMvcResultHandlers.print())
				.andDo(print());
		}
		
		
		
		
		

}
