package com.jingyunbank.etrade.order.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Test;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jingyunbank.core.web.Login;
import com.jingyunbank.etrade.TestCaseBase;
import com.jingyunbank.etrade.cart.bean.CartVO;
import com.jingyunbank.etrade.cart.bean.GoodsInCartVO;
import com.jingyunbank.etrade.cart.bean.OrdersInCartVO;
import com.jingyunbank.etrade.cart.controller.CartController;


public class CartControllerTest extends TestCaseBase{
	
	@Test
	public void testDelete() throws Exception{
		getMockMvc().perform(
				delete("/api/cart/goods")
				.param("gids", "XXXXX").param("gids", "YYYYYY")
				.sessionAttr(Login.LOGIN_ID, "123321")
				.accept(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
			.andExpect(jsonPath("$.code").value("200"))
			.andDo(print());
	}
	
	@Test
	public void testList() throws Exception{
		getMockMvc().perform(
				get("/api/cart/goods/list/123321")
				.sessionAttr(Login.LOGIN_ID, "123321")
				.accept(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
			.andExpect(jsonPath("$.code").value("200"))
			.andDo(print());
	}
	@Test
	public void testPut() throws Exception{
		GoodsInCartVO vo = new GoodsInCartVO();
		vo.setCount(new Random().nextInt(9)+1);
		vo.setGID("1233211233211233211232");
		vo.setGname("东阿阿胶阿胶糕");
		vo.setMID("1233211233211233211232");
		vo.setMname("东阿阿胶长");
		vo.setPrice(new BigDecimal("1200.00"));
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(vo);
		System.out.println(json);
		getMockMvc().perform(
					 put("/api/cart")
					 .content(json)
					.sessionAttr(Login.LOGIN_ID, "123321")
					.sessionAttr(Login.LOGIN_CART_ID, "123321123")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
				)
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(jsonPath("$.code").value("200"))
				.andDo(print());
	}
	@Test
	public void testUpdate0() throws Exception{
		getMockMvc().perform(
					 post("/api/cart/goods/{id}", "rCxskSt0Qp-0zUwiyG7FVw")
					.param("count", "4")
					.sessionAttr(Login.LOGIN_ID, "123321")
					.accept(MediaType.APPLICATION_JSON)
				)
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(jsonPath("$.code").value("200"))
				.andDo(print());
	}
	
	@Test
	public void testClearing() throws Exception{
		List<GoodsInCartVO> vos = new ArrayList<GoodsInCartVO>();
		for (int i = 0; i < 3; i++) {
			GoodsInCartVO vo = new GoodsInCartVO();
			vo.setID("XXXXYYYYZZZ");
			vo.setUID("UUUTTTTYYYY");
			vo.setCartID("EEEERRRRXXX");
			vo.setCount(new Random().nextInt(9)+1);
			vo.setGID("1233211233211233211232");
			vo.setGname("东阿阿胶阿胶糕");
			vo.setMID("1233211233211233211232");
			vo.setMname("东阿阿胶长");
			vo.setPrice(new BigDecimal("1200.00"));
			vos.add(vo);
		}
		
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(vos);
		System.out.println(json);
		getMockMvc().perform(
					 post("/api/cart/clearing")
					 .content(json)
					.sessionAttr(Login.LOGIN_ID, "123321")
					.sessionAttr(Login.LOGIN_CART_ID, "123321123")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
				)
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(jsonPath("$.code").value("200"))
				.andDo(print());
	}
	
	@Test
	public void testListClearing() throws Exception{
		CartVO cart = new CartVO();
		OrdersInCartVO ovo = new OrdersInCartVO();
		ovo.setMID("XXXXYYYYZZZZXXXXYYYYZZ");
		ovo.setMname("ZXCVASDF");
		ovo.setPrice(new BigDecimal("128.22"));
		ovo.setPostage(new BigDecimal("12"));
		cart.getOrders().add(ovo);
		
		List<GoodsInCartVO> vos = new ArrayList<GoodsInCartVO>();
		for (int i = 0; i < 3; i++) {
			GoodsInCartVO vo = new GoodsInCartVO();
			vo.setID("XXXXYYYYZZZ");
			vo.setUID("UUUTTTTYYYY");
			vo.setCartID("EEEERRRRXXX");
			vo.setCount(new Random().nextInt(9)+1);
			vo.setGID("1233211233211233211232");
			vo.setGname("东阿阿胶阿胶糕");
			vo.setMID("1233211233211233211232");
			vo.setMname("东阿阿胶长");
			vo.setPrice(new BigDecimal("1200.00"));
			vos.add(vo);
		}
		ovo.getGoods().addAll(vos);
		
		getMockMvc().perform(
					 get("/api/cart/clearing/list")
					.sessionAttr(CartController.GOODS_IN_CART_TO_CLEARING, cart)
					.sessionAttr(Login.LOGIN_ID, "123321")
					.sessionAttr(Login.LOGIN_CART_ID, "123321123")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
				)
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(jsonPath("$.code").value("200"))
				//.andExpect(jsonPath("$.body[0].id").value("XXXXYYYYZZZ"))
				.andDo(print());
	}
	
}
