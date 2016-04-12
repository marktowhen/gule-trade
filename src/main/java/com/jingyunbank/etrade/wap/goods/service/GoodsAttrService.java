package com.jingyunbank.etrade.wap.goods.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.wap.goods.bo.GoodsAttr;
import com.jingyunbank.etrade.api.wap.goods.bo.GoodsType;
import com.jingyunbank.etrade.api.wap.goods.service.IGoodsAttrService;
import com.jingyunbank.etrade.wap.goods.dao.GoodsAttrDao;
import com.jingyunbank.etrade.wap.goods.entity.GoodsAttrEntity;

@Service("goodsAttrService")
public class GoodsAttrService implements IGoodsAttrService {
	@Resource
	private GoodsAttrDao goodsAttrDao;

	@Override
	public void save(GoodsAttr goodsAttr) throws Exception {
		// TODO Auto-generated method stub
		GoodsAttrEntity attrEntity = new GoodsAttrEntity();
		BeanUtils.copyProperties(goodsAttr, attrEntity);
		goodsAttrDao.insertGoodsAttr(attrEntity);
	}

	@Override
	public Optional<GoodsAttr> singleById(String aid) throws Exception {
		GoodsAttrEntity attrEntity = goodsAttrDao.selectOne(aid);
		GoodsAttr attr = null;
		if (Objects.nonNull(attrEntity)) {
			attr = new GoodsAttr();
			BeanUtils.copyProperties(attrEntity, attr);
		}
		return Optional.ofNullable(attr);
	}

	@Override
	public void refreshGoodsAttr(GoodsAttr goodsAttr) throws Exception {
		GoodsAttrEntity attrEntity = new GoodsAttrEntity();
		BeanUtils.copyProperties(goodsAttr, attrEntity);
		goodsAttrDao.update(attrEntity);
	}

	@Override
	public void romoveGoodsAttr(String aid) throws Exception {
		goodsAttrDao.delete(aid);
	}

	@Override
	public List<GoodsAttr> listGoodsAttr() throws Exception {
		List<GoodsAttr> goodsAttr = goodsAttrDao.select().stream().map(dao -> {
			GoodsAttr bo = new GoodsAttr();
			BeanUtils.copyProperties(dao, bo);
			return bo;
		}).collect(Collectors.toList());
		return goodsAttr;
	}

}
