package com.jingyunbank.etrade.wap.goods.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.jingyunbank.etrade.api.wap.goods.bo.GoodsAttrValue;
import com.jingyunbank.etrade.api.wap.goods.bo.GoodsImg;
import com.jingyunbank.etrade.api.wap.goods.bo.GoodsInfo;
import com.jingyunbank.etrade.api.wap.goods.bo.GoodsOperation;
import com.jingyunbank.etrade.api.wap.goods.bo.GoodsOperationShow;
import com.jingyunbank.etrade.api.wap.goods.bo.GoodsSku;
import com.jingyunbank.etrade.api.wap.goods.service.IWapGoodsOperationService;
import com.jingyunbank.etrade.wap.goods.dao.GoodsAttrValueDao;
import com.jingyunbank.etrade.wap.goods.dao.GoodsImgDao;
import com.jingyunbank.etrade.wap.goods.dao.GoodsSkuDao;
import com.jingyunbank.etrade.wap.goods.dao.WapGoodsDao;
import com.jingyunbank.etrade.wap.goods.dao.WapGoodsInfoDao;
import com.jingyunbank.etrade.wap.goods.dao.WapGoodsOperationDao;
import com.jingyunbank.etrade.wap.goods.entity.GoodsAttrValueEntity;
import com.jingyunbank.etrade.wap.goods.entity.GoodsEntity;
import com.jingyunbank.etrade.wap.goods.entity.GoodsImgEntity;
import com.jingyunbank.etrade.wap.goods.entity.GoodsInfoEntity;
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
	private WapGoodsInfoDao wapGoodsInfoDao;

	@Resource
	private WapGoodsOperationDao wapGoodsOperationDao;
	@Resource
	private WapGoodsDao wapGoodsDao;

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
				String values = goodsSkuEntity.getPropertiesValue();
				String[] v = values.split("@");
				
				//用于给ID进行排序
				List<String> arrs = new ArrayList<String>();
				for (int i = 0; i < v.length; i++) {
					String aid = StringUtils
							.trimAllWhitespace(goodsAttrValueDao.selectAttrIdByGidAndValue(goodsEntity.getID(), v[i]));
					arrs.add(aid);
				}
				Object[] v_values = arrs.toArray();
				Arrays.sort(v_values);
				//排序后赋值给属性
				for (Object s : v_values) {
					if (goodsSkuEntity.getProperties() == null || goodsSkuEntity.getProperties() == "") {
						goodsSkuEntity.setProperties((String) s);
					} else {
						goodsSkuEntity.setProperties(goodsSkuEntity.getProperties() + "," + (String) s);
					}
				}
				
				goodsSkuDao.insertGoodsSku(goodsSkuEntity);
			}
			// 保存info
			for (GoodsInfo info : goodsOperation.getInfoList()) {
				GoodsInfoEntity goodsInfoEntity = new GoodsInfoEntity();
				BeanUtils.copyProperties(info, goodsInfoEntity);
				wapGoodsInfoDao.insertGoodsInfo(goodsInfoEntity);
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

			// 保存info
			wapGoodsInfoDao.deleteGoodsInfo(goodsEntity.getID());
			for (GoodsInfo info : goodsOperation.getInfoList()) {
				GoodsInfoEntity goodsInfoEntity = new GoodsInfoEntity();
				BeanUtils.copyProperties(info, goodsInfoEntity);
				wapGoodsInfoDao.insertGoodsInfo(goodsInfoEntity);
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
		GoodsEntity entity = wapGoodsOperationDao.selectGoodsByGid(gid);
		GoodsOperationShow operation = null;
		if (Objects.nonNull(entity)) {
			operation = new GoodsOperationShow();
			BeanUtils.copyProperties(entity, operation);
		}

		// 查找商品的附加信息 --------
		List<GoodsImg> imgList = goodsImgDao.selectGoodsDetailImgs(gid).stream().map(img -> {
			GoodsImg imgbo = new GoodsImg();
			BeanUtils.copyProperties(img, imgbo);
			return imgbo;
		}).collect(Collectors.toList());

		List<GoodsAttrValue> attrValueList = goodsAttrValueDao.selectGoodsAttrValue(gid).stream().map(av -> {
			GoodsAttrValue attrValue = new GoodsAttrValue();
			BeanUtils.copyProperties(av, attrValue);
			return attrValue;
		}).collect(Collectors.toList());

		List<GoodsSku> skuList = goodsSkuDao.selectGoodsSKuByGid(gid).stream().map(skuEntity -> {
			GoodsSku sku = new GoodsSku();
			BeanUtils.copyProperties(skuEntity, sku);
			return sku;
		}).collect(Collectors.toList());

		List<GoodsInfo> infoList = wapGoodsDao.selectGoodsInfo(gid).stream().map(infoEntity -> {
			GoodsInfo info = new GoodsInfo();
			BeanUtils.copyProperties(infoEntity, info);
			return info;
		}).collect(Collectors.toList());

		operation.setAttrValueList(attrValueList);
		operation.setImgList(imgList);
		operation.setSkuList(skuList);
		operation.setInfoList(infoList);
		return Optional.ofNullable(operation);
	}

	@Override
	public void delGoodsByGid(String gid) throws Exception {
		// TODO Auto-generated method stub
		wapGoodsOperationDao.deleteGoods(gid);
	}

}
