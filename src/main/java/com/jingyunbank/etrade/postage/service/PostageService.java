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
	public BigDecimal calculate(BigDecimal orderprice, int provenceid, BigDecimal weight) {
		if(Objects.nonNull(orderprice) && orderprice.compareTo(BigDecimal.valueOf(99)) > 0){
			return BigDecimal.ZERO;
		}
		return BigDecimal.ZERO;
	}

	@Override
	public void calculate(List<Orders> orders) {
		
	}

}
