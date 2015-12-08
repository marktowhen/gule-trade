package com.jingyunbank.etrade.order.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jingyunbank.core.Range;
import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.exception.DataRemovingException;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.order.bo.OrderGoods;
import com.jingyunbank.etrade.api.order.bo.OrderStatusDesc;
import com.jingyunbank.etrade.api.order.bo.Orders;
import com.jingyunbank.etrade.api.order.service.IOrderService;
import com.jingyunbank.etrade.order.dao.OrderDao;
import com.jingyunbank.etrade.order.entity.OrderEntity;

@Service("orderService")
public class OrderService implements IOrderService{

	@Autowired
	private OrderDao orderDao;
	
	@Override
	@Transactional
	public void save(Orders order) throws DataSavingException {
		OrderEntity entity = new OrderEntity();
		BeanUtils.copyProperties(order, entity);
		try {
			orderDao.insertOne(entity);
		} catch (Exception e) {
			throw new DataSavingException(e);
		}
	}
	
	@Override
	@Transactional
	public void save(List<Orders> orders) throws DataSavingException {
		List<OrderEntity> entities = new ArrayList<OrderEntity>();
		orders.forEach(order->{
			OrderEntity entity = new OrderEntity();
			BeanUtils.copyProperties(order, entity);
			entities.add(entity);
		});
		try {
			orderDao.insertMany(entities);
		} catch (Exception e) {
			throw new DataSavingException(e);
		}
	}

	@Override
	public Optional<Orders> singleByOrderNo(String orderno) {
		return null;
	}

	@Override
	public List<Orders> list(String uid) {
		return orderDao.selectByUID(uid)
			.stream().map(entity -> {
				Orders bo = new Orders();
				BeanUtils.copyProperties(entity, bo);
				entity.getGoods().forEach(ge -> {
					OrderGoods og = new OrderGoods();
					og.setGID(ge.getGID());
					og.setGname(ge.getGname());
					og.setImgpath(ge.getImgpath());
					bo.getGoods().add(og);
				});
				return bo;
			}).collect(Collectors.toList());
	}

	@Override
	public List<Orders> list(String uid, Range range) {
		return orderDao.selectByUIDWithRange(uid, range.getFrom(), range.getTo()-range.getFrom())
				.stream().map(entity -> {
					Orders bo = new Orders();
					BeanUtils.copyProperties(entity, bo, "goods");
					entity.getGoods().forEach(ge -> {
						OrderGoods og = new OrderGoods();
						og.setGID(ge.getGID());
						og.setGname(ge.getGname());
						og.setImgpath(ge.getImgpath());
						bo.getGoods().add(og);
					});
					return bo;
				}).collect(Collectors.toList());
	}

	@Override
	public List<Orders> list(Date start, Date end) {
		return orderDao.selectBetween(start, end)
				.stream().map(entity -> {
					Orders bo = new Orders();
					BeanUtils.copyProperties(entity, bo);
					entity.getGoods().forEach(ge -> {
						OrderGoods og = new OrderGoods();
						og.setGID(ge.getGID());
						og.setGname(ge.getGname());
						og.setImgpath(ge.getImgpath());
						bo.getGoods().add(og);
					});
					return bo;
				}).collect(Collectors.toList());
	}

	@Override
	public List<Orders> list() {
		return orderDao.select()
				.stream().map(entity -> {
					Orders bo = new Orders();
					BeanUtils.copyProperties(entity, bo);
					entity.getGoods().forEach(ge -> {
						OrderGoods og = new OrderGoods();
						og.setGID(ge.getGID());
						og.setGname(ge.getGname());
						og.setImgpath(ge.getImgpath());
						bo.getGoods().add(og);
					});
					return bo;
				}).collect(Collectors.toList());
	}

	@Override
	public void remove(String id) throws DataRemovingException {
		try {
			orderDao.delete(id);
		} catch (Exception e) {
			throw new DataRemovingException(e);
		}
	}

	@Override
	public List<Orders> listOrder(String uid, OrderStatusDesc status) {
		return null;
	}

	@Override
	public void refreshStatus(List<String> oids, OrderStatusDesc status)
			throws DataRefreshingException {
		try {
			orderDao.updateStatus(oids, status);
		} catch (Exception e) {
			throw new DataRefreshingException(e);
		}
	}

	@Override
	public List<Orders> listByExtransno(String extransno) {
		
		return orderDao.selectByExtranso(extransno)
				.stream().map(entity -> {
					Orders bo = new Orders();
					BeanUtils.copyProperties(entity, bo);
					return bo;
				}).collect(Collectors.toList());
	}

	

}
