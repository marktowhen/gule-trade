package com.jingyunbank.etrade.order.service;

import java.util.ArrayList;
import java.util.List;



import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import org.springframework.transaction.annotation.Transactional;

import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.order.bo.OrderGoods;
import com.jingyunbank.etrade.api.order.service.IOrderGoodsService;
import com.jingyunbank.etrade.order.dao.OrderGoodsDao;
import com.jingyunbank.etrade.order.entity.OrderGoodsEntity;

@Service("orderGoodsService")
public class OrderGoodsService implements IOrderGoodsService {

	@Autowired
	private OrderGoodsDao orderGoodsDao;
	
	@Override
	@Transactional
	public void save(List<OrderGoods> goods) throws DataSavingException {
		try {
			List<OrderGoodsEntity> ges = new ArrayList<OrderGoodsEntity>();
			goods.forEach(g -> {
				OrderGoodsEntity oge = new OrderGoodsEntity();
				BeanUtils.copyProperties(g, oge);
				ges.add(oge);
			});
			orderGoodsDao.insertMany(ges);
		} catch (Exception e) {
			throw new DataSavingException(e);
		}
	}

}
