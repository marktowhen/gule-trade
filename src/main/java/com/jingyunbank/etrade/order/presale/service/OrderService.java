package com.jingyunbank.etrade.order.presale.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jingyunbank.core.Range;
import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.order.presale.bo.OrderGoods;
import com.jingyunbank.etrade.api.order.presale.bo.OrderStatusDesc;
import com.jingyunbank.etrade.api.order.presale.bo.Orders;
import com.jingyunbank.etrade.api.order.presale.service.IOrderService;
import com.jingyunbank.etrade.order.presale.dao.OrderDao;
import com.jingyunbank.etrade.order.presale.entity.OrderEntity;

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
	public Optional<Orders> single(String oid) {
		OrderEntity entity = orderDao.selectOne(oid);
		if(Objects.isNull(entity)){
			return Optional.ofNullable(null);
		}
		Orders bo = new Orders();
		BeanUtils.copyProperties(entity, bo, "goods");
		entity.getGoods().forEach(ge -> {
			OrderGoods og = new OrderGoods();
			BeanUtils.copyProperties(ge, og);
			bo.getGoods().add(og);
		});
		return Optional.of(bo);
	}

	@Override
	public List<Orders> list(List<String> oids) {
		return orderDao.selectByOIDs(oids)
			.stream().map(entity -> {
				Orders bo = new Orders();
				BeanUtils.copyProperties(entity, bo, "goods");
				return bo;
			}).collect(Collectors.toList());
	}

	@Override
	public void refreshStatus(List<String> oids, OrderStatusDesc status)
			throws DataRefreshingException {
		try {
			orderDao.updateStatus(oids, status.getCode(), status.getName());
		} catch (Exception e) {
			throw new DataRefreshingException(e);
		}
	}

	@Override
	public List<Orders> listByExtransno(String extransno) {
		
		return orderDao.selectByExtranso(extransno)
				.stream().map(entity -> {
					Orders bo = new Orders();
					BeanUtils.copyProperties(entity, bo, "goods");
					entity.getGoods().forEach(ge -> {
						OrderGoods og = new OrderGoods();
						BeanUtils.copyProperties(ge, og);
						bo.getGoods().add(og);
					});
					return bo;
				}).collect(Collectors.toList());
	}

	@Override
	public List<Orders> list(String uid, String mid, String statuscode,int anystatus,
			Range range) {
		return orderDao.selectKeyStatus(uid, mid, statuscode,anystatus, range.getFrom(), (int)(range.getTo()-range.getFrom()))
				.stream().map(entity -> {
					Orders bo = new Orders();
					BeanUtils.copyProperties(entity, bo,"goods");
					entity.getGoods().forEach(ge -> {
						OrderGoods og = new OrderGoods();
						BeanUtils.copyProperties(ge, og);
						bo.getGoods().add(og);
					});
					return bo;
				}).collect(Collectors.toList());
	}

	@Override
	public Integer count(
			String uid, 
			String mid, 
			String statuscode, 
			String keywords, 
			String fromdate, 
			String enddate){
		return orderDao.selectKeywordsCount(uid, mid, statuscode, keywords, fromdate, enddate);
	}
	
	@Override
	public Integer count(
			String uid, 
			String mid, 
			String statuscode,
			String orderno,
			String gname,
			String uname,
			String mname,
			String fromdate, 
			String enddate) {
		return orderDao.selectConditionCount(uid, mid, statuscode, orderno, gname, uname, mname, fromdate, enddate);
	}

	@Override
	public List<Orders> listBefore(Date deadline, OrderStatusDesc status) {
		return orderDao.selectBefore(deadline, status.getCode())
				.stream().map(entity -> {
					Orders bo = new Orders();
					BeanUtils.copyProperties(entity, bo, "goods");
					return bo;
				}).collect(Collectors.toList());
	}

	@Override
	public List<Orders> listBetween(Date from, Date to, OrderStatusDesc status) {
		return orderDao.selectBetween(from, to, status.getCode())
				.stream().map(entity -> {
					Orders bo = new Orders();
					BeanUtils.copyProperties(entity, bo, "goods");
					return bo;
				}).collect(Collectors.toList());
	}

	@Override
	public List<Orders> list(
							String uid, 
							String mid, 
							String statuscode, 
							String orderno,
							String gname, 
							String uname, String mname,
							String fromdate, String enddate,
							Range range) {
		return orderDao.selectCondition(uid, mid, statuscode, orderno, gname, uname, mname, fromdate, enddate, range.getFrom(), (int)(range.getTo()-range.getFrom()))
				.stream().map(entity -> {
					Orders bo = new Orders();
					BeanUtils.copyProperties(entity, bo, "goods");
					entity.getGoods().forEach(ge -> {
						OrderGoods og = new OrderGoods();
						BeanUtils.copyProperties(ge, og);
						bo.getGoods().add(og);
					});
					return bo;
				}).collect(Collectors.toList());
	}

	@Override
	public boolean shareCoupon(String couponid) {
		int count = orderDao.countCouponOrder(couponid);
		return count >= 1;
	}

}
