package com.jingyunbank.etrade.marketing.auction.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.core.Range;
import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.marketing.auction.bo.AuctionGoods;
import com.jingyunbank.etrade.api.marketing.auction.service.IAuctionGoodsService;
import com.jingyunbank.etrade.api.marketing.group.bo.Group;
import com.jingyunbank.etrade.api.wap.goods.bo.Goods;
import com.jingyunbank.etrade.api.wap.goods.bo.GoodsSku;
import com.jingyunbank.etrade.marketing.auction.dao.AuctionGoodsDao;
import com.jingyunbank.etrade.marketing.auction.entity.AuctionGoodsEntity;

@Service("auctionGoodsService")
public class AuctionGoodsService implements IAuctionGoodsService {
	
	@Autowired
	private AuctionGoodsDao auctionGoodsDao;

	@Override
	public boolean save(AuctionGoods goods) throws DataSavingException {
		try {
			return auctionGoodsDao.insert(convert(goods));
		} catch (Exception e) {
			throw new DataSavingException(e);
		}
	}

	@Override
	public boolean refreshStatus(String auctionGoodsID, String status) throws DataRefreshingException {
		try {
			return auctionGoodsDao.updateStatus(auctionGoodsID, status);
		} catch (Exception e) {
			throw new DataRefreshingException(e);
		}
	}

	@Override
	public Optional<AuctionGoods> single(String ID) {
		return Optional.ofNullable(convert(auctionGoodsDao.selectOne(ID)));
	}

	@Override
	public List<AuctionGoods> list(Range range) {
		return auctionGoodsDao.selectList(range.getFrom(), range.getTo()-range.getFrom()).stream().map(entity->{
			return convert(entity);
		}).collect(Collectors.toList());
	}

	@Override
	public boolean refreshSoldPrice(String auctionGoodsID, BigDecimal soldPrice, String soldUser) throws DataRefreshingException {
		try {
			return auctionGoodsDao.updateSoldPrice(auctionGoodsID, soldPrice,soldUser);
		} catch (Exception e) {
			throw new DataRefreshingException(e);
		}
	}
	
	private AuctionGoods convert(AuctionGoodsEntity entity){
		AuctionGoods bo = null;
		if(entity!=null){
			bo = new AuctionGoods();
			BeanUtils.copyProperties(entity, bo);
			if(entity.getGoods()!=null){
				Goods goods = new Goods();
				BeanUtils.copyProperties(entity.getGoods(), goods);
				bo.setGoods(goods);
			}
			
			if(entity.getSku()!=null){
				GoodsSku goods = new GoodsSku();
				BeanUtils.copyProperties(entity.getSku(), goods);
				bo.setSku(goods);
			}
		}
		return bo;
	}
	
	private AuctionGoodsEntity convert(AuctionGoods bo){
		AuctionGoodsEntity entity = null;
		if(bo!=null){
			entity = new AuctionGoodsEntity();
			BeanUtils.copyProperties(bo, entity);
		}
		return entity;
	}

	@Override
	public boolean delay(AuctionGoods auctionGoods) throws DataRefreshingException {
		auctionGoods.setDelayAmount(auctionGoods.getDelayAmount()+1);
		
		auctionGoods.setEndTime(refreshEndTime(auctionGoods));
		return auctionGoodsDao.updateDelayAmount(auctionGoods.getID(), auctionGoods.getEndTime());
	}
	
	/**
	 * 投标后 获取最新的结束时间
	 * @param auctionGoods
	 * @return
	 * 2016年5月5日 qxs
	 */
	private Date refreshEndTime(AuctionGoods auctionGoods){
		if(AuctionGoods.TYPE_DELAY.equals(auctionGoods.getType())){
			Date now = new Date();
			//剩余结束
			long last = auctionGoods.getEndTime().getTime()-now.getTime();
			if(last<auctionGoods.getDelaySecond()*1000){
				return new Date(now.getTime()+auctionGoods.getDelaySecond()*1000);
			}
		}
		return auctionGoods.getEndTime();
	}

	@Override
	public boolean isOver(String auctionGoodsID) {
		Optional<AuctionGoods> single = this.single(auctionGoodsID);
		if(single.isPresent()){
			return single.get().getEndTime().before(new Date());
		}
		return false;
	}

	@Override
	public int count() {
		return auctionGoodsDao.count();
	}

	@Override
	public List<AuctionGoods> listOnDead() {
		return convertEntityToBo(auctionGoodsDao.listOnDead());
	}
	
	
	private List<AuctionGoods> convertEntityToBo(List<AuctionGoodsEntity> entityList){
		if(entityList!=null){
			return entityList.stream().map( entity->{
				AuctionGoods bo = new AuctionGoods();
				BeanUtils.copyProperties(entity, bo);
				return bo;
			}).collect(Collectors.toList());
		}
		return new ArrayList<AuctionGoods>();
		
	}

}
