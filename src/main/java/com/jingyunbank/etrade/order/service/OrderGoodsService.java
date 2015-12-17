package com.jingyunbank.etrade.order.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.order.presale.bo.OrderGoods;
import com.jingyunbank.etrade.api.order.presale.bo.OrderStatusDesc;
import com.jingyunbank.etrade.api.order.presale.service.IOrderGoodsService;
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
	
	/**
	 * 产品某条件下的订单信息
	 */
	@Override
	public List<OrderGoods> listOrderGoods(String uid,OrderStatusDesc status) { /*OrderStatusDesc status*/
		return orderGoodsDao.selectByUID(uid,status)
				.stream().map(entity -> {
					OrderGoods orderGoods = new OrderGoods();
					BeanUtils.copyProperties(entity, orderGoods);
					return orderGoods;
				}).collect(Collectors.toList());
	}
	/**
	 * 通过gid查出单个的对象
	 * @param gid
	 * @return
	 */
	@Override
	public Optional<OrderGoods> singleOrderGoods(String id) {
		OrderGoods orderGoods = new OrderGoods(); 
		OrderGoodsEntity orderGoodsEntity =	orderGoodsDao.selectByID(id);
		BeanUtils.copyProperties(orderGoodsEntity, orderGoods);
		return Optional.of(orderGoods);
	}

	@Override
	public void refreshStatus(List<String> oids, OrderStatusDesc status) throws DataRefreshingException{
		try {
			orderGoodsDao.updateStatus(oids, status);
		} catch (Exception e) {
			throw new DataRefreshingException(e);
		}
	}
	@Override
	public void refreshGoodStatus(String id, OrderStatusDesc status) throws DataRefreshingException{
		try {
			orderGoodsDao.updateGoodStatus(id, status);
		} catch (Exception e) {
			throw new DataRefreshingException(e);
		}
	}

	@Override
	public int getByOID(String oid, OrderStatusDesc status) {
		
		
		return orderGoodsDao.getByOID(oid, status);
	}

	
}
