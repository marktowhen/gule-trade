package com.jingyunbank.etrade.postage.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.order.presale.bo.Orders;
import com.jingyunbank.etrade.api.postage.service.IPostageService;

@Service("postageService")
public class PostageService implements IPostageService {

	@Override
	public BigDecimal calculate(String mid, BigDecimal orderprice,
			BigDecimal weight, BigDecimal defaultp) {
		if(Objects.nonNull(orderprice) && orderprice.compareTo(BigDecimal.valueOf(99)) > 0){
			return BigDecimal.ZERO;
		}
		return defaultp;
	}

	@Override
	public void calculate(List<Orders> orders) {
		
	}

}
