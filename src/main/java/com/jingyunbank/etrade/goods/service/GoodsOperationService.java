package com.jingyunbank.etrade.goods.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.goods.bo.Goods;
import com.jingyunbank.etrade.api.goods.bo.GoodsOperation;
import com.jingyunbank.etrade.api.goods.bo.ShowGoods;
import com.jingyunbank.etrade.api.goods.service.IGoodsOperationService;
import com.jingyunbank.etrade.goods.dao.GoodsOperationDao;
import com.jingyunbank.etrade.goods.entity.GoodsDaoEntity;
import com.jingyunbank.etrade.goods.entity.GoodsDetailEntity;
import com.jingyunbank.etrade.goods.entity.GoodsEntity;
import com.jingyunbank.etrade.goods.entity.GoodsImgEntity;

@Service("goodsOperationService")
public class GoodsOperationService implements IGoodsOperationService {
	@Resource
	private GoodsOperationDao goodsOperationDao;

	@Override
	public boolean save(GoodsOperation goodsOperation) {
		int r1 = 0, r2 = 0, r3 = 0;
		try {
			// 保存商品的信息
			GoodsEntity entity = new GoodsEntity();
			if (goodsOperation.getGoods() != null)
				BeanUtils.copyProperties(goodsOperation.getGoods(), entity);
			r1 = goodsOperationDao.insertGoods(entity);
			// 保存商品的详细信息
			GoodsDetailEntity goodsDetailEntity = new GoodsDetailEntity();
			if (goodsOperation.getGoodsDetail() != null)
				BeanUtils.copyProperties(goodsOperation.getGoodsDetail(), goodsDetailEntity);
			r2 = goodsOperationDao.insertGoodsDetail(goodsDetailEntity);
			// 保存商品的图片信息
			GoodsImgEntity imgEntity = new GoodsImgEntity();
			if (goodsOperation.getGoodsImg() != null)
				BeanUtils.copyProperties(goodsOperation.getGoodsImg(), imgEntity);
			r3 = goodsOperationDao.insertGoodsImg(imgEntity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (r1 > 0 && r2 > 0 && r3 > 0) {
			return true;
		}
		return false;
	}

	@Override
	public Optional<ShowGoods> singleById(String gid) throws Exception {
		GoodsDaoEntity goods = goodsOperationDao.selectOne(gid);
		ShowGoods showGoods = null;
		if (Objects.nonNull(goods)) {
			showGoods = new ShowGoods();
			BeanUtils.copyProperties(goods, showGoods);
		}
		return Optional.ofNullable(showGoods);
	}

	/**
	 * 修改商品信息
	 */
	@Override
	public boolean refreshGoods(GoodsOperation goodsOperation) throws Exception {
		int r1 = 0, r2 = 0, r3 = 0;
		try {
			// 保存商品的信息
			GoodsEntity entity = new GoodsEntity();
			if (goodsOperation.getGoods() != null)
				BeanUtils.copyProperties(goodsOperation.getGoods(), entity);
			r1 = goodsOperationDao.updateGoods(entity);
			// 保存商品的详细信息
			GoodsDetailEntity goodsDetailEntity = new GoodsDetailEntity();
			if (goodsOperation.getGoodsDetail() != null)
				BeanUtils.copyProperties(goodsOperation.getGoodsDetail(), goodsDetailEntity);
			r2 = goodsOperationDao.updateGoodsDetail(goodsDetailEntity);
			// 保存商品的图片信息
			GoodsImgEntity imgEntity = new GoodsImgEntity();
			if (goodsOperation.getGoodsImg() != null)
				BeanUtils.copyProperties(goodsOperation.getGoodsImg(), imgEntity);
			r3 = goodsOperationDao.updateGoodsImg(imgEntity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (r1 > 0 && r2 > 0 && r3 > 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean refreshGoodsVolume(String gid, int count) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("gid", gid);
		map.put("count", count);
		int result = goodsOperationDao.updateGoodsVolume(map);
		if (result > 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean refreshGoodsUp(String gid) throws Exception {
		int result = goodsOperationDao.updateGoodsUp(gid);
		if (result > 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean refreshGoodsDown(String gid) throws Exception {
		int result = goodsOperationDao.updateGoodsDown(gid);
		if (result > 0) {
			return true;
		}
		return false;
	}

}
