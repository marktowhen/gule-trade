package com.jingyunbank.etrade.marketing.auction.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.marketing.auction.bo.AuctionUser;
import com.jingyunbank.etrade.api.marketing.auction.service.IAuctionUserService;
import com.jingyunbank.etrade.marketing.auction.dao.AuctionUserDao;
import com.jingyunbank.etrade.marketing.auction.entity.AuctionUserEntity;

@Service("auctionUserService")
public class AuctionUserService implements IAuctionUserService {

	@Autowired
	private AuctionUserDao auctionUserDao;
	
	@Override
	public boolean save(AuctionUser aUser) throws DataSavingException {
		try {
			return auctionUserDao.insert(convert(aUser));
		} catch (Exception e) {
			throw new DataSavingException(e);
		}
	}

	@Override
	public boolean refreshStatus(String auctionUserID, String status)
			throws DataRefreshingException {
		try {
			return auctionUserDao.updateStatus(auctionUserID, status);
		} catch (Exception e) {
			throw new DataRefreshingException(e);
		}
	}

	@Override
	public List<AuctionUser> list(String auctionID) {
		return auctionUserDao.selectList(auctionID).stream().map(entity->{
			return convert(entity);
		}).collect(Collectors.toList());
	}

	@Override
	public int count(String auctionID) {
		return auctionUserDao.count(auctionID);
	}

	@Override
	public Optional<AuctionUser> single(String auctionUserID) {
		return Optional.ofNullable(convert(auctionUserDao.selectOne(auctionUserID)));
	}
	
	private AuctionUser convert(AuctionUserEntity entity){
		AuctionUser bo = null;
		if(entity!=null){
			bo = new AuctionUser();
			BeanUtils.copyProperties(entity, bo);
		}
		return bo;
	}
	
	private AuctionUserEntity convert(AuctionUser bo){
		AuctionUserEntity entity = null;
		if(bo!=null){
			entity = new AuctionUserEntity();
			BeanUtils.copyProperties(bo, entity);
		}
		return entity;
	}

}
