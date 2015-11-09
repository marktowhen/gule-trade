package com.jingyunbank.etrade.order.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jingyunbank.etrade.TestCaseBase;
import com.jingyunbank.etrade.order.bean.OrderGoodsVO;
import com.jingyunbank.etrade.order.bean.OrderSubmitVO;

public class OrderControllerTest extends TestCaseBase {

	@Test
	public void testDeleteLoginFirst() throws Exception{
		getMockMvc().perform(
				delete("/api/orders/-XbGNv0RToW8LG96BNLpiw")
				//.sessionAttr("login-uid", "123321")
				.accept(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
			.andExpect(jsonPath("$.code").value("500"))
			.andDo(print());
	}
	
	@Test
	public void testDelete() throws Exception{
		getMockMvc().perform(
				delete("/api/orders/-XbGNv0RToW8LG96BNLpiw")
				.sessionAttr("LOGIN_ID", "123321")
				.accept(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
			.andExpect(jsonPath("$.code").value("200"))
			.andDo(print());
	}
	
	@Test
	public void testAddressID() throws Exception{
		getMockMvc().perform(
					 put("/api/orders")
					.param("addressID", "123321123321123321123")
					.param("paytypeID", "1233211233211233211232")
					.param("price", "123.32")
					.param("postage", "12.32")
					.sessionAttr("LOGIN_ID", "XXXX")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
				)
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(jsonPath("$.code").value("500"))
				.andDo(print());
	}
	
	@Test
	public void testPayType() throws Exception{
		getMockMvc().perform(
					 put("/api/orders")
					.param("addressID", "1233211233211233211232")
					.param("paytypeID", "123321123321123321123")
					.param("price", "123.32")
					.param("postage", "12.32")
					.sessionAttr("LOGIN_ID", "USER-ID")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
				)
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(jsonPath("$.code").value("500"))
				.andDo(print());
		
	}
	@Test
	public void testPrice() throws Exception{
		getMockMvc().perform(
					 put("/api/orders")
					.param("addressID", "1233211233211233211232")
					.param("paytypeID", "1233211233211233211232")
					.param("price", "-122")
					.param("postage", "12.32")
					.sessionAttr("LOGIN_ID", "USER-ID")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
				)
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(jsonPath("$.code").value("500"))
				.andDo(print());
		
	}
	@Test
	public void testPostage() throws Exception{
		getMockMvc().perform(
					 put("/api/orders")
					.param("addressID", "1233211233211233211232")
					.param("paytypeID", "1233211233211233211232")
					.param("price", "122")
					.param("postage", "-12.32")
					.sessionAttr("LOGIN_ID", "USER-ID")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
				)
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(jsonPath("$.code").value("500"))
				.andDo(print());
		
	}
	
	@Test
	public void testSuccess() throws Exception{
		getMockMvc().perform(
					 put("/api/orders")
					.param("addressID", "1233211233211233211232")
					.param("paytypeID", "1233211233211233211232")
					.param("paytypeName", "支付宝")
					.param("receiver", "张三四")
					.param("MID", "XXXXX")
					.param("price", "122")
					.param("postage", "12.32")
					.sessionAttr("LOGIN_ID", "USER-ID")
					.contentType(MediaType.APPLICATION_JSON)
					.characterEncoding("UTF-8")
					.accept(MediaType.APPLICATION_JSON)
				)
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(jsonPath("$.code").value("200"))
				.andDo(print());
		
	}
	@Test
	public void testSuccess1() throws Exception{
		OrderSubmitVO vo = new OrderSubmitVO();
		List<OrderGoodsVO> goods = new ArrayList<OrderGoodsVO>();
		vo.setAddressID("1111111111111111111111");
		vo.setPaytypeID("1111111111111111111111");
		vo.setPaytypeName("1111");
		vo.setReceiver("XXXX");
		for (int i = 0; i < 3; i++) {
			OrderGoodsVO g = new OrderGoodsVO();
			g.setCount(2);
			g.setID("1111111111111111111111");
			g.setMID("1111111111111111111111");
			g.setPrice(new BigDecimal(123.3d));
			goods.add(g);
		}
		vo.setGoods(goods);
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(vo);
		System.out.println(json);
		getMockMvc().perform(
					 put("/api/order")
					.contentType(MediaType.APPLICATION_JSON)
					.content(json)
					.sessionAttr("LOGIN_ID", "USER-ID")
					.characterEncoding("UTF-8")
					.accept(MediaType.APPLICATION_JSON)
				)
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(jsonPath("$.code").value("200"))
				.andDo(print());
		
	}
	
	@Test
	public void testLoginFirst() throws Exception{
		getMockMvc().perform(
					 put("/api/orders")
					.param("addressID", "1233211233211233211232")
					.param("paytypeID", "1233211233211233211232")
					.param("price", "122")
					.param("postage", "12.32")
					//.sessionAttr("LOGIN_ID", "USER-ID")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
				)
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(jsonPath("$.code").value("500"))
				.andDo(print());
		
	}
	
	@Test
	public void testUserList() throws Exception{
		getMockMvc().perform(
					get("/api/orders/list/123321")
					.sessionAttr("LOGIN_ID", "123321")
					.accept(MediaType.APPLICATION_JSON)
				)
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(jsonPath("$.code").value("200"))
				.andExpect(jsonPath("$.body[0].postage").value(12.00))
				.andDo(print());
	}
	@Test
	public void testUserListLogin() throws Exception{
		getMockMvc().perform(
					get("/api/orders/list/123321")
					//.sessionAttr("login-uid", "123321")
					.accept(MediaType.APPLICATION_JSON)
				)
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(jsonPath("$.code").value("500"))
				.andDo(print());
	}
	@Test
	public void testAllList() throws Exception{
		getMockMvc().perform(
					get("/api/orders/list")
					//.sessionAttr("login-uid", "123321")
					.accept(MediaType.APPLICATION_JSON)
				)
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(jsonPath("$.code").value("200"))
				.andDo(print());
	}
	
}
