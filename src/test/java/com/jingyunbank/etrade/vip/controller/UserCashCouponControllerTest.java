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
import com.jingyunbank.etrade.api.vip.service.IUserCashCouponService;

public class UserCashCouponControllerTest extends TestCaseBase{
	
	@Autowired
	private IUserCashCouponService userCashCouponService;
	
		/**
		 * 新增
		 * @throws Exception
		 * 2015年11月16日 qxs
		 */
		@Test
		public void testSave() throws Exception{
			getMockMvc().perform(
					 put("/api/vip/coupon/cashcoupon/user/")
					.param("code", "5922969784792231886")
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
					 get("/api/vip/coupon/cashcoupon/user/1")
					.param("cashCoupon.threshholdLow", "1")
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
					 post("/api/vip/coupon/cashcoupon/user/")
					.param("oid", "1")
					.param("couponId", "ly87INApQGaCDzKS4evskA")
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
					 get("/api/vip/coupon/cashcoupon/user/amount")
					.param("cashCoupon.threshholdLow", "1")
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
			userCashCouponService.lock("Ys3WfVB-RUK-Yff_-hqacQ", "2");
		}
		
		@Test
		public void testUnlock() throws Exception{
			userCashCouponService.unlock("Ys3WfVB-RUK-Yff_-hqacQ", "2");
		}
		
		
		@Test
		public void testSingle() throws Exception{
			userCashCouponService.single("Ys3WfVB-RUK-Yff_-hqacQ", "2");
		}
		
		/**
		 * 查询可用的
		 * @throws Exception
		 * 2015年11月17日 qxs
		 */
		@Test
		public void testUseableList() throws Exception{
			getMockMvc().perform(
					 get("/api/vip/coupon/cashcoupon/user/useable/2")
					//.param("orderPrice", "100")
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
