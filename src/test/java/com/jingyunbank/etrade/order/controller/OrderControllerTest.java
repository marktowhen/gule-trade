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
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jingyunbank.core.KeyGen;
import com.jingyunbank.core.web.Login;
import com.jingyunbank.etrade.TestCaseBase;
import com.jingyunbank.etrade.order.presale.bean.PurchaseGoodsVO;
import com.jingyunbank.etrade.order.presale.bean.PurchaseOrderVO;
import com.jingyunbank.etrade.order.presale.bean.PurchaseRequestVO;

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
	public void testSuccess() throws Exception{
		PurchaseRequestVO vo = new PurchaseRequestVO();
		
		vo.setMobile("18688888888");
		vo.setZipcode("678922");
		vo.setAddress("1111111111111111111111");
		vo.setPaytypeCode("ONLINE");
		vo.setPaytypeName("线上支付");
		vo.setReceiver("XXXX");
		vo.setInvoiceTitle("XSADFSADFAS");
		vo.setInvoiceType("个人");
		vo.setUID("XYZXYZ");
		
		List<PurchaseOrderVO> ovos = new ArrayList<PurchaseOrderVO>();
		for (int i = 0; i < 3; i++) {
			PurchaseOrderVO povo = new PurchaseOrderVO();
			povo.setAddtime(new Date());
			povo.setDeliveryTypeCode("D002");
			povo.setDeliveryTypeName("普通快递");
			povo.setID(KeyGen.uuid());
			povo.setMID("XXXXX"+i);
			povo.setMname("YYYYY"+i);
			povo.setNote("NOTE"+i);
			povo.setPostage(new BigDecimal("12.00"));
			List<PurchaseGoodsVO> goods = new ArrayList<PurchaseGoodsVO>();
			for (int j = 0; j < 3; j++) {
				PurchaseGoodsVO g = new PurchaseGoodsVO();
				g.setGID("@@@@#$@#@@SDFASDXCV");
				g.setGname("GXNAME");
				g.setCount(2);
				g.setMID("1111111111111111111111");
				g.setPrice(new BigDecimal("123.3"));
				goods.add(g);
			}
			povo.setGoods(goods);
			ovos.add(povo);
		}

		vo.setOrders(ovos);
		
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(vo);
		System.out.println(json);
		getMockMvc().perform(
					 put("/api/order")
					.contentType(MediaType.APPLICATION_JSON)
					.content(json)
					.sessionAttr(Login.LOGIN_USER_ID, "USER-ID")
					.characterEncoding("UTF-8")
					.accept(MediaType.APPLICATION_JSON)
				)
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(jsonPath("$.code").value("200"))
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
