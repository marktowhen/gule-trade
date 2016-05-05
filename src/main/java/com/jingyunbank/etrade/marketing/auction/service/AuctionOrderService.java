package com.jingyunbank.etrade.marketing.auction.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.marketing.auction.bo.AuctionOrder;
import com.jingyunbank.etrade.api.marketing.auction.service.IAuctionOrderService;
import com.jingyunbank.etrade.marketing.auction.dao.AuctionOrderDao;
import com.jingyunbank.etrade.marketing.auction.entity.AuctionOrderEntity;

@Service("auctionOrderService")
public class AuctionOrderService implements IAuctionOrderService {

	@Autowired
	private AuctionOrderDao auctionOrderDao;
	
	@Override
	public boolean save(AuctionOrder auctionOrder) throws DataSavingException {
		try {
			return auctionOrderDao.insert(convert(auctionOrder));
		} catch (Exception e) {
			throw new DataSavingException(e);
		}
	}

	@Override
	public Optional<AuctionOrder> single(String id) {
		return Optional.ofNullable(convert(auctionOrderDao.selectOne(id)));
	}

	@Override
	public Optional<AuctionOrder> singleByOid(String oid) {
		return Optional.ofNullable(convert(auctionOrderDao.selectOne(oid)));
	}

	@Override
	public List<AuctionOrder> list(String auctionGoodsID, String auctionUserID) {
		return auctionOrderDao.selectList(auctionGoodsID, auctionUserID).stream().map(entity->{
			return convert(entity);
		}).collect(Collectors.toList());
	}
	
	private AuctionOrder convert(AuctionOrderEntity entity){
		AuctionOrder bo = null;
		if(entity!=null){
			bo = new AuctionOrder();
			BeanUtils.copyProperties(entity, bo);
		}
		return bo;
	}
	
	private AuctionOrderEntity convert(AuctionOrder bo){
		AuctionOrderEntity entity = null;
		if(bo!=null){
			entity = new AuctionOrderEntity();
			BeanUtils.copyProperties(bo, entity);
		}
		return entity;
	}

}
