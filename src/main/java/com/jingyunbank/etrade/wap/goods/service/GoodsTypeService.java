package com.jingyunbank.etrade.wap.goods.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.wap.goods.bo.GoodsType;
import com.jingyunbank.etrade.api.wap.goods.service.IGoodsTypeService;
import com.jingyunbank.etrade.wap.goods.dao.GoodsTypeDao;
import com.jingyunbank.etrade.wap.goods.entity.GoodsTypeEntity;

@Service("goodsTypeService")
public class GoodsTypeService implements IGoodsTypeService {
	@Resource
	private GoodsTypeDao goodsTypeDao;

	@Override
	public void save(GoodsType goodsType) throws Exception {
		GoodsTypeEntity entity = new GoodsTypeEntity();
		BeanUtils.copyProperties(goodsType, entity);
		goodsTypeDao.insertGoodsType(entity);
	}

	@Override
	public void refreshGoodsType(GoodsType goodsType) throws Exception {
		GoodsTypeEntity entity = new GoodsTypeEntity();
		BeanUtils.copyProperties(goodsType, entity);
		goodsTypeDao.updateGoodsType(entity);
	}

	@Override
	public void delGoodsType(String tid) throws Exception {
		goodsTypeDao.delGoodsType(tid);
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

}
