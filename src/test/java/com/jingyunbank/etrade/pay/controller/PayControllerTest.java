package com.jingyunbank.etrade.pay.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jingyunbank.core.web.Login;
import com.jingyunbank.etrade.TestCaseBase;
import com.jingyunbank.etrade.api.order.presale.bo.Orders;
import com.jingyunbank.etrade.api.order.presale.service.IOrderService;
import com.jingyunbank.etrade.pay.bean.OrderPaymentRequestVO;
import com.jingyunbank.etrade.pay.bean.PayOrderVO;

public class PayControllerTest extends TestCaseBase{

	@Autowired
	private IOrderService orderService;
	
	@Test
	public void testSubmit() throws Exception{
		List<Orders> orders = orderService.list(Arrays.asList("USER-ID")).stream().filter(x->x.getAddtime().toInstant().plusSeconds(Orders.VALID_TIME_IN_SECOND).isAfter(Instant.now())).collect(Collectors.toList());
		List<PayOrderVO> povo = new ArrayList<PayOrderVO>();
		orders.forEach(bo -> {
			PayOrderVO vo = new PayOrderVO();
			BeanUtils.copyProperties(bo, vo);
			vo.setOID(bo.getID());
			povo.add(vo);
		});
		
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(povo);
		System.out.println(json);
		getMockMvc().perform(
					 put("/api/payments/detail")
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
	public void testBuild() throws Exception{
		List<String> oids = new ArrayList<>();
		oids.add("xvcdfscw1r");
		OrderPaymentRequestVO vo = new OrderPaymentRequestVO();
		vo.setOrderids(oids);
		vo.setPipelineCode("LLPAY");
		
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(vo);
		System.out.println(json);
		getMockMvc().perform(
					 post("/api/payments/prepay")
					.content(json)
					.contentType(MediaType.APPLICATION_JSON)
					.sessionAttr(Login.LOGIN_USER_ID, "USER-ID")
					.characterEncoding("UTF-8")
					.accept(MediaType.APPLICATION_JSON)
				)
				.andExpect(status().isOk())
//				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
//				.andExpect(jsonPath("$.code").value("200"))
				.andDo(print());
		
	}
}
