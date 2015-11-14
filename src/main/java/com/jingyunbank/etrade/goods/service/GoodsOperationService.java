package com.jingyunbank.etrade.goods.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.goods.bo.Goods;
import com.jingyunbank.etrade.api.goods.bo.GoodsOperation;
import com.jingyunbank.etrade.api.goods.service.IGoodsOperationService;
import com.jingyunbank.etrade.goods.dao.GoodsOperationDao;
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
			r1 = goodsOperationDao.insertGoods(entity);
			// 保存商品的详细信息
			GoodsDetailEntity goodsDetailEntity = new GoodsDetailEntity();
			r2 = goodsOperationDao.insertGoodsDetail(goodsDetailEntity);
			// 保存商品的图片信息
			GoodsImgEntity imgEntity = new GoodsImgEntity();
			r3 = goodsOperationDao.insertGoodsImg(imgEntity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (r1 > 0 && r2 > 0 && r3 > 0) {
			return true;
		}
		return false;
	}

}
