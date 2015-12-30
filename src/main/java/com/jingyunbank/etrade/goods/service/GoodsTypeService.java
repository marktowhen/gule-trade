package com.jingyunbank.etrade.goods.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.goods.bo.GoodsType;
import com.jingyunbank.etrade.api.goods.service.IGoodsTypeService;
import com.jingyunbank.etrade.goods.dao.GoodsTypeDao;
import com.jingyunbank.etrade.goods.entity.GoodsTypeEntity;

@Service("goodsTypeService")
public class GoodsTypeService implements IGoodsTypeService {
	@Resource
	private GoodsTypeDao goodsTypeDao;

	@Override
	public boolean save(GoodsType goodsType) throws Exception {
		GoodsTypeEntity entity = new GoodsTypeEntity();
		BeanUtils.copyProperties(goodsType, entity);
		int i = goodsTypeDao.insertGoodsType(entity);
		if (i > 0) {
			return true;
		}
		return false;
	}

	@Override
	public Optional<GoodsType> singleById(String bid) throws Exception {
		GoodsTypeEntity entity = goodsTypeDao.selectOneGoodsType(bid);
		GoodsType goodsType = null;
		if (Objects.nonNull(entity)) {
			goodsType = new GoodsType();
			BeanUtils.copyProperties(entity, goodsType);
		}
		return Optional.ofNullable(goodsType);
	}

	@Override
	public boolean refreshGoodsType(GoodsType goodsType) throws Exception {
		GoodsTypeEntity entity = new GoodsTypeEntity();
		BeanUtils.copyProperties(goodsType, entity);
		int i = goodsTypeDao.updateGoodsType(entity);
		if (i > 0) {
			return true;
		}
		return false;
	}

	@Override
	public List<GoodsType> listGoodsTypesByName(String name) throws Exception {
		List<GoodsType> goodsTypes = goodsTypeDao.selectGoodsTypes(name).stream().map(dao -> {
			GoodsType bo = new GoodsType();
			BeanUtils.copyProperties(dao, bo);
			return bo;
		}).collect(Collectors.toList());
		return goodsTypes;
	}

	@Override
	public List<GoodsType> listGoodsTypes() throws Exception {
		List<GoodsType> goodsType = goodsTypeDao.selectAllGoodsTypes().stream().map(dao -> {
			GoodsType bo = new GoodsType();
			BeanUtils.copyProperties(dao, bo);
			return bo;
		}).collect(Collectors.toList());
		return goodsType;
	}

	@Override
	public boolean delGoodsType(String tid) throws Exception {
		int i = goodsTypeDao.delGoodsType(tid);
		if (i > 0) {
			return true;
		}
		return false;
	}

}
