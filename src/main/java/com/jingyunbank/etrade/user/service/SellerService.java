package com.jingyunbank.etrade.user.service;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.user.bo.Seller;
import com.jingyunbank.etrade.api.user.service.ISellerService;
import com.jingyunbank.etrade.user.dao.SellerDao;
import com.jingyunbank.etrade.user.entity.SellerEntity;

@Service("sellerService")
public class SellerService implements ISellerService {

	@Autowired
	private SellerDao sellerDao;
	
	@Override
	public Optional<Seller> singleByID(String id) {
		SellerEntity sellerEntity = sellerDao.selectByKey(id);
		if(sellerEntity!=null){
			Seller bo = new Seller();
			BeanUtils.copyProperties(sellerEntity, bo);
			return Optional.of(bo);
		}
		return Optional.empty();
	}

	@Override
	public Optional<Seller> singleByMname(String mname) {
		SellerEntity sellerEntity = sellerDao.selectByKey(mname);
		if(sellerEntity!=null){
			Seller bo = new Seller();
			BeanUtils.copyProperties(sellerEntity, bo);
			return Optional.of(bo);
		}
		return Optional.empty();
	}
	
	@Override
	public boolean refreshPassword(String id, String password)
			throws DataRefreshingException {
		try {
			return sellerDao.updatePassword(id, password);
		} catch (Exception e) {
			throw new DataRefreshingException(e);
		}
	}

}
