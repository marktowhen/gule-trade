package com.jingyunbank.etrade.pay.service;

import java.util.List;
import java.util.stream.Collectors;



import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.pay.bo.PayType;
import com.jingyunbank.etrade.api.pay.service.IPayTypeService;
import com.jingyunbank.etrade.pay.dao.PayTypeDao;

@Service("payTypeService")
public class PayTypeService implements IPayTypeService {

	@Autowired
	private PayTypeDao payTypeDao;
	
	@Override
	public List<PayType> list() {
		return payTypeDao.select().stream().map(entity -> {
			PayType type = new PayType();
			BeanUtils.copyProperties(entity, type);
			return type;
		}).collect(Collectors.toList());
	}

}
