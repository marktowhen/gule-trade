package com.jingyunbank.etrade.marketing.group.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jingyunbank.core.Range;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.marketing.group.bo.GroupGoods;
import com.jingyunbank.etrade.api.marketing.group.bo.GroupGoodsPriceSetting;
import com.jingyunbank.etrade.api.marketing.group.service.IGroupGoodsService;
import com.jingyunbank.etrade.marketing.group.dao.GroupGoodsDao;
import com.jingyunbank.etrade.marketing.group.dao.GroupGoodsPriceSettingDao;
import com.jingyunbank.etrade.marketing.group.entity.GroupGoodsEntity;
import com.jingyunbank.etrade.marketing.group.entity.GroupGoodsPriceSettingEntity;

@Service("groupGoodsService")
public class GroupGoodsService implements IGroupGoodsService {

	@Autowired
	private GroupGoodsDao groupGoodsDao;
	@Autowired
	private GroupGoodsPriceSettingDao groupGoodsPriceSettingDao;
	
	@Override
	@Transactional
	public void save(GroupGoods goods) throws DataSavingException {
		List<GroupGoodsPriceSetting> priceSettings = goods.getPriceSettings();
		List<GroupGoodsPriceSettingEntity> sentities = new ArrayList<GroupGoodsPriceSettingEntity>();
		priceSettings.forEach(bo -> {
			GroupGoodsPriceSettingEntity en = new GroupGoodsPriceSettingEntity();
			BeanUtils.copyProperties(bo, en);
			sentities.add(en);
		});
		GroupGoodsEntity entity = new GroupGoodsEntity();
		BeanUtils.copyProperties(goods, entity, "priceSettings", "groups");
		
		try {
			groupGoodsDao.insert(entity);
			groupGoodsPriceSettingDao.insertMany(sentities);
		} catch (Exception e) {
			throw new DataSavingException(e);
		}
	}

	@Override
	public Optional<GroupGoods> single(String ggid) {
		GroupGoodsEntity enity = groupGoodsDao.selectOne(ggid);
		if(Objects.nonNull(enity)){
			GroupGoods bo = new GroupGoods();
			BeanUtils.copyProperties(enity, bo, "priceSettings", "groups");
			return Optional.of(bo);
		}
		return Optional.empty();
	}

	@Override
	public List<GroupGoods> list(Range range) {
		List<GroupGoodsEntity> entities = groupGoodsDao.selectMany(range.getFrom(),(int) (range.getTo() - range.getFrom()));
		List<GroupGoods> bos = new ArrayList<GroupGoods>();
		entities.forEach(entity -> {
			GroupGoods bo = new GroupGoods();
			BeanUtils.copyProperties(entity, bo, "priceSettings", "groups");
			bos.add(bo);
		});
		return bos;
	}

	@Override
	public Optional<GroupGoods> singleByGroupID(String groupID) {
		GroupGoodsEntity enity = groupGoodsDao.selectOneByGroupID(groupID);
		if(Objects.nonNull(enity)){
			GroupGoods bo = new GroupGoods();
			BeanUtils.copyProperties(enity, bo, "priceSettings", "groups");
			return Optional.of(bo);
		}
		return Optional.empty();
	}

}
