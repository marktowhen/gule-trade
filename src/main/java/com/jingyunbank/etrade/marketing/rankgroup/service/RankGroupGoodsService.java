package com.jingyunbank.etrade.marketing.rankgroup.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.core.KeyGen;
import com.jingyunbank.core.Range;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.marketing.rankgroup.bo.RankGroupGoods;
import com.jingyunbank.etrade.api.marketing.rankgroup.bo.RankGroupGoodsPriceSetting;
import com.jingyunbank.etrade.api.marketing.rankgroup.bo.RankGroupGoodsShow;
import com.jingyunbank.etrade.api.marketing.rankgroup.service.IRankGroupGoodsService;
import com.jingyunbank.etrade.marketing.rankgroup.dao.RankGroupGoodsDao;
import com.jingyunbank.etrade.marketing.rankgroup.dao.RankGroupGoodsPriceSettingDao;
import com.jingyunbank.etrade.marketing.rankgroup.entity.RankGroupGoodsEntity;
import com.jingyunbank.etrade.marketing.rankgroup.entity.RankGroupGoodsPriceSettingEntity;


@Service("rankGroupGoodsService")
public class RankGroupGoodsService implements IRankGroupGoodsService{
	
	@Autowired 
    RankGroupGoodsDao rankGroupGoodsDao;
	@Autowired 
	RankGroupGoodsPriceSettingDao rankGroupGoodsPriceSetting;
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
			rankGroupGoodsPriceSetting.insertMany(sentities);
		} catch (Exception e) {
			throw new DataSavingException(e);
		}
		
	}

	@Override
	public List<RankGroupGoodsShow> list(String MID, Range range) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<RankGroupGoods> single(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<RankGroupGoods> singleByGid(String gid) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
