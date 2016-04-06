package com.jingyunbank.etrade.wap.goods.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.wap.goods.bo.GoodsSku;
import com.jingyunbank.etrade.api.wap.goods.bo.GoodsSkuCondition;
import com.jingyunbank.etrade.api.wap.goods.bo.GoodsType;
import com.jingyunbank.etrade.api.wap.goods.bo.ShowGoods;
import com.jingyunbank.etrade.api.wap.goods.service.IWapGoodsService;
import com.jingyunbank.etrade.wap.goods.bean.GoodsShowVO;
import com.jingyunbank.etrade.wap.goods.dao.WapGoodsDao;
import com.jingyunbank.etrade.wap.goods.entity.GoodsSkuConditionEntity;
import com.jingyunbank.etrade.wap.goods.entity.GoodsSkuEntity;

@Service("wapGoodsService")
public class WapGoodsService implements IWapGoodsService {
	@Resource
	private WapGoodsDao wapGoodsDao;

	@Override
	public List<ShowGoods> listGoods(String mid, String tid, String order) throws Exception {
		List<ShowGoods> list = wapGoodsDao.selectGoods(mid, tid, order).stream().map(bo -> {
			ShowGoods vo = new ShowGoods();
			BeanUtils.copyProperties(bo, vo);
			return vo;
		}).collect(Collectors.toList());
		return list;
	}

	@Override
	public Optional<GoodsSkuCondition> singleGoodsSkuCondition(String gid) throws Exception {
		GoodsSkuConditionEntity entity = wapGoodsDao.selectGoodsSkuConditionByGid(gid);
		GoodsSkuCondition condition = null;
		if (Objects.nonNull(entity)) {
			condition = new GoodsSkuCondition();
			BeanUtils.copyProperties(entity, condition);

		}
		return Optional.ofNullable(condition);
	}

	@Override
	public Optional<GoodsSku> singleGoodsSku(String gid, String condition) throws Exception {
		GoodsSkuEntity entity = wapGoodsDao.selectGoodsSku(gid,condition);
		GoodsSku sku = null;
		if (Objects.nonNull(entity)) {
			sku = new GoodsSku();
			BeanUtils.copyProperties(entity, sku);

		}
		return Optional.ofNullable(sku);
	}

}
