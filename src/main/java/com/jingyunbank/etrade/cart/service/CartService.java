package com.jingyunbank.etrade.cart.service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jingyunbank.core.Range;
import com.jingyunbank.etrade.api.cart.bo.Cart;
import com.jingyunbank.etrade.api.cart.bo.GoodsInCart;
import com.jingyunbank.etrade.api.cart.service.ICartService;
import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.exception.DataRemovingException;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.cart.dao.CartDao;
import com.jingyunbank.etrade.cart.entity.CartEntity;
import com.jingyunbank.etrade.cart.entity.GoodsInCartEntity;

@Service("cartService")
public class CartService implements ICartService {

	@Autowired
	private CartDao cartDao;
	
	@Override
	@Transactional
	public void save(Cart cart) throws DataSavingException {
		CartEntity entity = new CartEntity();
		BeanUtils.copyProperties(cart, entity);
		try {
			cartDao.insertCart(entity);
		} catch (Exception e) {
			throw new DataSavingException(e);
		}
	}

	@Override
	public void refresh(GoodsInCart goods) throws DataRefreshingException{
		GoodsInCartEntity entity = new GoodsInCartEntity();
		BeanUtils.copyProperties(goods, entity);
		try {
			cartDao.update(entity);
		} catch (Exception e) {
			throw new DataRefreshingException(e);
		}
	}

	@Override
	public void remove(String id) throws DataRemovingException{
		try {
			cartDao.deleteOne(id);
		} catch (Exception e) {
			throw new DataRemovingException(e);
		}
	}

	@Override
	public boolean save(GoodsInCart goods) throws DataSavingException {
		GoodsInCartEntity gicentity = cartDao.selectOneGoods(goods.getUID(), goods.getGID(),goods.getSKUID());
		if(Objects.nonNull(gicentity)) return false;
		
		GoodsInCartEntity entity = new GoodsInCartEntity();
		BeanUtils.copyProperties(goods, entity);
		try {
			cartDao.insertOneGoods(entity);
		} catch (Exception e) {
			throw new DataSavingException(e);
		};
		return true;
	}

//	@Override
//	public void save(List<GoodsInCart> goods) throws DataSavingException {
//		List<GoodsInCartEntity> entities = new ArrayList<GoodsInCartEntity>();
//		for (GoodsInCart gs : goods) {
//			GoodsInCartEntity gice = new GoodsInCartEntity();
//			BeanUtils.copyProperties(gs, gice);
//			entities.add(gice);
//		}
//		try {
//			cartDao.insertManyGoods(entities);
//		} catch (Exception e) {
//			throw new DataSavingException(e);
//		};
//	}

	@Override
	public void remove(List<String> gidsInCart) throws DataRemovingException {
		try {
			cartDao.deleteMany(gidsInCart);
		} catch (Exception e) {
			throw new DataRemovingException(e);
		}
	}

	@Override
	public List<GoodsInCart> listGoods(String uid) {
		List<GoodsInCartEntity> entities = cartDao.selectAllByUID(uid);
		
		return entities.stream().map(entity -> {
			GoodsInCart goodsInCart = new GoodsInCart();
			BeanUtils.copyProperties(entity, goodsInCart);
			return goodsInCart;
		}).collect(Collectors.toList());
	}

	@Override
	public List<GoodsInCart> listGoodsIntCart(String cartID) {
		List<GoodsInCartEntity> entities = cartDao.selectAllByCartID(cartID);
		
		return entities.stream().map(entity -> {
			GoodsInCart goodsInCart = new GoodsInCart();
			BeanUtils.copyProperties(entity, goodsInCart);
			return goodsInCart;
		}).collect(Collectors.toList());
	}

	@Override
	public Optional<Cart> singleCart(String uid) {
		CartEntity cart = cartDao.selectOne(uid);
		Cart cbo = null;
		if(Objects.nonNull(cart)){
			cbo = new Cart();
			BeanUtils.copyProperties(cart, cbo);
		}
		return Optional.ofNullable(cbo);
	}

	@Override
	public void clear(String uid) throws DataRemovingException {
		try {
			cartDao.deleteByUID(uid);
		} catch (Exception e) {
			throw new DataRemovingException(e);
		}
	}

	@Override
	public void remove(String gid, String uid) throws DataRemovingException {
		try {
			cartDao.deleteUserGoods(uid, Arrays.asList(new String[]{gid}));
		} catch (Exception e) {
			throw new DataRemovingException(e);
		}
	}

	@Override
	public void remove(List<String> gids, String uid)
			throws DataRemovingException {
		try {
			cartDao.deleteUserGoods(uid, gids);
		} catch (Exception e) {
			throw new DataRemovingException(e);
		}
	}

	@Override
	public int count(String uid) {
		return cartDao.count(uid);
	}

}
