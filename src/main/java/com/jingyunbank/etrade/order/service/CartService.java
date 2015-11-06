package com.jingyunbank.etrade.order.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.core.Range;
import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.exception.DataRemovingException;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.order.bo.Cart;
import com.jingyunbank.etrade.api.order.service.ICartService;
import com.jingyunbank.etrade.order.dao.CartDao;
import com.jingyunbank.etrade.order.entity.CartEntity;

@Service("cartService")
public class CartService implements ICartService {

	@Autowired
	private CartDao cartDao;
	
	@Override
	public void save(Cart cart) throws DataSavingException {
		CartEntity entity = new CartEntity();
		BeanUtils.copyProperties(cart, entity);
		try {
			cartDao.insert(entity);
		} catch (Exception e) {
			throw new DataSavingException();
		}
	}

	@Override
	public void refresh(Cart cart) throws DataRefreshingException{
		CartEntity entity = new CartEntity();
		BeanUtils.copyProperties(cart, entity);
		try {
			cartDao.update(entity);
		} catch (Exception e) {
			throw new DataRefreshingException();
		}
	}

	@Override
	public void remove(Cart cart) throws DataRemovingException{
		try {
			cartDao.delete(cart.getID());
		} catch (Exception e) {
			throw new DataRemovingException();
		}
	}

	@Override
	public List<Cart> list(String uid) {
		return cartDao.selectByUID(uid)
				.stream().map(ec -> {
					Cart cart = new Cart();
					BeanUtils.copyProperties(ec, cart);
					return cart;
				}).collect(Collectors.toList());
	}

	@Override
	public List<Cart> list(String uid, Range range) {
		return cartDao.selectRange(uid, range.getFrom(), range.getTo() - range.getFrom())
				.stream().map(ec -> {
					Cart cart = new Cart();
					BeanUtils.copyProperties(ec, cart);
					return cart;
				}).collect(Collectors.toList());
	}

}
