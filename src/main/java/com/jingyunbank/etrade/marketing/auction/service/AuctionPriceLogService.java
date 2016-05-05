package com.jingyunbank.etrade.marketing.auction.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.marketing.auction.bo.AuctionPriceLog;
import com.jingyunbank.etrade.api.marketing.auction.service.IAuctionPriceLog;
import com.jingyunbank.etrade.marketing.auction.dao.AuctionPriceLogDao;
import com.jingyunbank.etrade.marketing.auction.entity.AuctionPriceLogEntity;

@Service("auctionPriceLogService")
public class AuctionPriceLogService implements IAuctionPriceLog {

	@Autowired
	private AuctionPriceLogDao auctionPriceLogDao;
	
	@Override
	public boolean save(AuctionPriceLog priceLog) throws DataSavingException {
		try {
			return auctionPriceLogDao.insert(convert(priceLog));
		} catch (Exception e) {
			throw new DataSavingException(e);
		}
	}

	@Override
	public Optional<AuctionPriceLog> single(String id) {
		return Optional.ofNullable(convert(auctionPriceLogDao.selectOne(id)));
	}

	@Override
	public List<AuctionPriceLog> list(String auctionGoodsID) {
		
		return auctionPriceLogDao.selectListByAGID(auctionGoodsID).stream().map(entity->{
			return convert(entity);
		}).collect(Collectors.toList());
	}

	@Override
	public int count(String auctionGoodsID) {
		return auctionPriceLogDao.countByAGID(auctionGoodsID);
	}

	@Override
	public List<AuctionPriceLog> list(String auctionGoodsID, String UID) {
		return auctionPriceLogDao.selectListByUID(auctionGoodsID,UID).stream().map(entity->{
			return convert(entity);
		}).collect(Collectors.toList());
	}

	@Override
	public int count(String auctionGoodsID, String UID) {
		return auctionPriceLogDao.countByUID(auctionGoodsID, UID);
	}
	
	private AuctionPriceLog convert(AuctionPriceLogEntity entity){
		AuctionPriceLog bo = null;
		if(entity!=null){
			bo = new AuctionPriceLog();
			BeanUtils.copyProperties(entity, bo);
		}
		return bo;
	}
	
	private AuctionPriceLogEntity convert(AuctionPriceLog bo){
		AuctionPriceLogEntity entity = null;
		if(bo!=null){
			entity = new AuctionPriceLogEntity();
			BeanUtils.copyProperties(bo, entity);
		}
		return entity;
	}

}
