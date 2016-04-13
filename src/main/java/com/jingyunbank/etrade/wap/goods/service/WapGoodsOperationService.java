package com.jingyunbank.etrade.wap.goods.service;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.wap.goods.bo.GoodsAttrValue;
import com.jingyunbank.etrade.api.wap.goods.bo.GoodsImg;
import com.jingyunbank.etrade.api.wap.goods.bo.GoodsOperation;
import com.jingyunbank.etrade.api.wap.goods.bo.GoodsOperationShow;
import com.jingyunbank.etrade.api.wap.goods.bo.GoodsSku;
import com.jingyunbank.etrade.api.wap.goods.bo.GoodsType;
import com.jingyunbank.etrade.api.wap.goods.service.IWapGoodsOperationService;
import com.jingyunbank.etrade.wap.goods.dao.GoodsAttrValueDao;
import com.jingyunbank.etrade.wap.goods.dao.GoodsImgDao;
import com.jingyunbank.etrade.wap.goods.dao.GoodsSkuDao;
import com.jingyunbank.etrade.wap.goods.dao.WapGoodsOperationDao;
import com.jingyunbank.etrade.wap.goods.entity.GoodsAttrValueEntity;
import com.jingyunbank.etrade.wap.goods.entity.GoodsEntity;
import com.jingyunbank.etrade.wap.goods.entity.GoodsImgEntity;
import com.jingyunbank.etrade.wap.goods.entity.GoodsOperationEntity;
import com.jingyunbank.etrade.wap.goods.entity.GoodsSkuEntity;

@Service("wapGoodsOperationService")
public class WapGoodsOperationService implements IWapGoodsOperationService {

	@Resource
	private GoodsAttrValueDao goodsAttrValueDao;
	@Resource
	private GoodsImgDao goodsImgDao;
	@Resource
	private GoodsSkuDao goodsSkuDao;
	@Resource
	private WapGoodsOperationDao wapGoodsOperationDao;

	@Override
	public boolean saveGoods(GoodsOperation goodsOperation) throws Exception {
		boolean flag = false;
		try {
			// 保存商品
			GoodsEntity goodsEntity = new GoodsEntity();
			BeanUtils.copyProperties(goodsOperation.getGoods(), goodsEntity);
			wapGoodsOperationDao.insertGoods(goodsEntity);
			// 保存图片信息
			for (GoodsImg imgvo : goodsOperation.getImgList()) {
				GoodsImgEntity goodsImgEntity = new GoodsImgEntity();
				BeanUtils.copyProperties(imgvo, goodsImgEntity);
				goodsImgDao.insertGoodsImg(goodsImgEntity);
			}

			// 保存属性信息
			for (GoodsAttrValue attrValue : goodsOperation.getAttrValueList()) {
				GoodsAttrValueEntity attrValueEntity = new GoodsAttrValueEntity();
				BeanUtils.copyProperties(attrValue, attrValueEntity);
				goodsAttrValueDao.insertGoodsAttrValue(attrValueEntity);
			}
			// 保存sku
			for (GoodsSku sku : goodsOperation.getSkuList()) {
				GoodsSkuEntity goodsSkuEntity = new GoodsSkuEntity();
				BeanUtils.copyProperties(sku, goodsSkuEntity);
				goodsSkuDao.insertGoodsSku(goodsSkuEntity);
			}
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	@Override
	public boolean modfiyGoods(GoodsOperation goodsOperation) throws Exception {
		boolean flag = false;
		try {
			// 保存商品
			GoodsEntity goodsEntity = new GoodsEntity();
			BeanUtils.copyProperties(goodsOperation.getGoods(), goodsEntity);
			wapGoodsOperationDao.updateGoods(goodsEntity);
			// 保存图片信息
			goodsImgDao.deleteGoodsImg(goodsEntity.getID());
			for (GoodsImg imgvo : goodsOperation.getImgList()) {
				GoodsImgEntity goodsImgEntity = new GoodsImgEntity();
				BeanUtils.copyProperties(imgvo, goodsImgEntity);
				goodsImgDao.insertGoodsImg(goodsImgEntity);
			}

			// 保存属性信息
			goodsAttrValueDao.deleteGoodsAttrValue(goodsEntity.getID());
			for (GoodsAttrValue attrValue : goodsOperation.getAttrValueList()) {
				GoodsAttrValueEntity attrValueEntity = new GoodsAttrValueEntity();
				BeanUtils.copyProperties(attrValue, attrValueEntity);
				goodsAttrValueDao.insertGoodsAttrValue(attrValueEntity);
			}
			// 保存sku
			goodsSkuDao.deleteGoodsSku(goodsEntity.getID());
			for (GoodsSku sku : goodsOperation.getSkuList()) {
				GoodsSkuEntity goodsSkuEntity = new GoodsSkuEntity();
				BeanUtils.copyProperties(sku, goodsSkuEntity);
				goodsSkuDao.insertGoodsSku(goodsSkuEntity);
			}
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	@Override
	public boolean up(String skuId) throws Exception {
		boolean flag = false;
		try {
			goodsSkuDao.up(skuId);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	@Override
	public boolean down(String skuId) throws Exception {
		boolean flag = false;
		try {
			goodsSkuDao.down(skuId);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;

	}

	@Override
	public Optional<GoodsOperationShow> getGoodsByGid(String gid) throws Exception {
		GoodsOperationEntity entity = goodsSkuDao.selectGoodsByGid(gid);
		GoodsOperationShow operation = null;
		if (Objects.nonNull(entity)) {
			operation = new GoodsOperationShow();
			BeanUtils.copyProperties(entity, operation);
		}
		return Optional.ofNullable(operation);
	}

}
