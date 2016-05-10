package com.jingyunbank.etrade.marketing.rankgroup.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.jingyunbank.core.KeyGen;
import com.jingyunbank.core.Range;
import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.marketing.group.bo.GroupGoods;
import com.jingyunbank.etrade.api.marketing.rankgroup.bo.RankGroupGoods;
import com.jingyunbank.etrade.api.marketing.rankgroup.bo.RankGroupGoodsPriceSetting;
import com.jingyunbank.etrade.api.marketing.rankgroup.bo.RankGroupGoodsShow;
import com.jingyunbank.etrade.api.marketing.rankgroup.service.IRankGroupGoodsService;
import com.jingyunbank.etrade.api.wap.goods.bo.Goods;
import com.jingyunbank.etrade.api.wap.goods.bo.GoodsSku;
import com.jingyunbank.etrade.marketing.group.entity.GroupGoodsEntity;
import com.jingyunbank.etrade.marketing.rankgroup.dao.RankGroupGoodsDao;
import com.jingyunbank.etrade.marketing.rankgroup.dao.RankGroupGoodsPriceSettingDao;
import com.jingyunbank.etrade.marketing.rankgroup.entity.RankGroupGoodsEntity;
import com.jingyunbank.etrade.marketing.rankgroup.entity.RankGroupGoodsPriceSettingEntity;
import com.jingyunbank.etrade.marketing.rankgroup.entity.RankGroupGoodsShowEntity;


@Service("rankGroupGoodsService")
public class RankGroupGoodsService implements IRankGroupGoodsService{
	
	@Autowired 
    RankGroupGoodsDao rankGroupGoodsDao;
	@Autowired 
	RankGroupGoodsPriceSettingDao rankGroupGoodsPriceSettingDao;
	@Override
	public void save(RankGroupGoods goods) throws DataSavingException {
		goods.setID(KeyGen.uuid());
		List<RankGroupGoodsPriceSetting> priceSettings = goods.getPriceSettings();
		List<RankGroupGoodsPriceSettingEntity> sentities = new ArrayList<RankGroupGoodsPriceSettingEntity>();
		priceSettings.forEach(bo -> {
			RankGroupGoodsPriceSettingEntity en = new RankGroupGoodsPriceSettingEntity();
			BeanUtils.copyProperties(bo, en);
			en.setID(KeyGen.uuid());
			en.setGGID(goods.getID());
			sentities.add(en);
		});
		RankGroupGoodsEntity entity = new RankGroupGoodsEntity();
		BeanUtils.copyProperties(goods, entity, "priceSettings", "groups");
		
		try {
			rankGroupGoodsDao.insert(entity);
			rankGroupGoodsPriceSettingDao.insertMany(sentities);
		} catch (Exception e) {
			throw new DataSavingException(e);
		}
		
	}

	

	@Override
	public List<RankGroupGoodsShow> list(String MID, String goodsName, Range range) {
		List<RankGroupGoodsShowEntity> entities = rankGroupGoodsDao.selectMany(MID,goodsName, range.getFrom(),(int) (range.getTo() - range.getFrom()));
		List<RankGroupGoodsShow> bos = new ArrayList<RankGroupGoodsShow>();
		entities.forEach(entity -> {
			bos.add(getShowBoFromEntity(entity));
		});
		return bos;
	}



	@Override
	public Optional<RankGroupGoods> single(String id) {
		
		RankGroupGoodsEntity enity = rankGroupGoodsDao.selectOne(id);
		if(Objects.nonNull(enity)){
			RankGroupGoods bo = new RankGroupGoods();
			BeanUtils.copyProperties(enity, bo,  "groups");
			return Optional.of(bo);
		}
		return Optional.empty();
	}

	@Override
	public Optional<RankGroupGoods> singleByGid(String gid) {
		RankGroupGoodsEntity enity = rankGroupGoodsDao.selectOneByGroupID(gid);
		if(Objects.nonNull(enity)){
			RankGroupGoods bo = new RankGroupGoods();
			BeanUtils.copyProperties(enity, bo, "priceSettings", "groups");
			return Optional.of(bo);
		}
		return Optional.empty();
	}

	@Override
	public void refresh(RankGroupGoods goodsbo) throws DataRefreshingException {
		List<RankGroupGoodsPriceSetting> priceSettings = goodsbo.getPriceSettings();
		List<RankGroupGoodsPriceSettingEntity> sentities = new ArrayList<RankGroupGoodsPriceSettingEntity>();
		priceSettings.forEach(bo -> {
			RankGroupGoodsPriceSettingEntity en = new RankGroupGoodsPriceSettingEntity();
			BeanUtils.copyProperties(bo, en);
			if(StringUtils.isEmpty(en.getID())){
				en.setID(KeyGen.uuid());
			}
			en.setGGID(goodsbo.getID());
			sentities.add(en);
		});
		RankGroupGoodsEntity entity = new RankGroupGoodsEntity();
		BeanUtils.copyProperties(goodsbo, entity, "priceSettings", "groups");
		
		try {
			rankGroupGoodsDao.update(entity);
			rankGroupGoodsPriceSettingDao.delete(entity.getID());
			rankGroupGoodsPriceSettingDao.insertMany(sentities);
		} catch (Exception e) {
			throw new DataRefreshingException(e);
		}
		
		
	}

	@Override
	public int count(String mid, String goodsName) {
		
		return rankGroupGoodsDao.count(mid,goodsName);
	}
	
	private RankGroupGoodsShow getShowBoFromEntity(RankGroupGoodsShowEntity entity){
		RankGroupGoodsShow bo = new RankGroupGoodsShow();
		BeanUtils.copyProperties(entity, bo, "priceSettings", "groups");
		if(Objects.nonNull(entity.getGoods())){
			Goods goodsBo = new Goods();
			BeanUtils.copyProperties(entity.getGoods(), goodsBo);
			bo.setGoods(goodsBo);
		}
		if(Objects.nonNull(entity.getSku())){
			GoodsSku sku = new GoodsSku();
			BeanUtils.copyProperties(entity.getSku(), sku);
			bo.setSku(sku);
		}
		
		return bo;
	}



	@Override
	public Optional<RankGroupGoods> singleByGroupID(String groupID) {
		RankGroupGoodsEntity enity = rankGroupGoodsDao.selectOneByGroupID(groupID);
		if(Objects.nonNull(enity)){
			RankGroupGoods bo = new RankGroupGoods();
			BeanUtils.copyProperties(enity, bo, "priceSettings", "groups");
			return Optional.of(bo);
		}
		return Optional.empty();
	}

}
