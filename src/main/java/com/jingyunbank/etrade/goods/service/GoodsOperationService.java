package com.jingyunbank.etrade.goods.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.core.KeyGen;
import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.goods.bo.BaseGoodsOperation;
import com.jingyunbank.etrade.api.goods.bo.GoodsOperation;
import com.jingyunbank.etrade.api.goods.bo.SalesRecord;
import com.jingyunbank.etrade.api.goods.service.IGoodsOperationService;
import com.jingyunbank.etrade.api.goods.service.ISalesRecordsService;
import com.jingyunbank.etrade.goods.dao.GoodsOperationDao;
import com.jingyunbank.etrade.goods.entity.GoodsDetailEntity;
import com.jingyunbank.etrade.goods.entity.GoodsEntity;
import com.jingyunbank.etrade.goods.entity.GoodsImgEntity;
import com.jingyunbank.etrade.goods.entity.GoodsOperationEntity;

@Service("goodsOperationService")
public class GoodsOperationService implements IGoodsOperationService {
	@Resource
	private GoodsOperationDao goodsOperationDao;
	@Autowired
	private ISalesRecordsService salesRecordsService;

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
	public Optional<BaseGoodsOperation> singleById(String gid) throws Exception {
		GoodsOperationEntity goods = goodsOperationDao.selectOne(gid);
		BaseGoodsOperation goodsOperation = null;
		if (Objects.nonNull(goods)) {
			goodsOperation = new BaseGoodsOperation();
			BeanUtils.copyProperties(goods, goodsOperation);
		}
		return Optional.ofNullable(goodsOperation);
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
	public void refreshGoodsVolume(String uid, String uname, String gid, int count)  throws DataSavingException, DataRefreshingException{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("gid", gid);
		map.put("count", count);
		try {
			goodsOperationDao.updateGoodsVolume(map);
		} catch (Exception e) {
			throw new DataRefreshingException(e);
		}
		SalesRecord record = new SalesRecord();
		record.setCount(count);
		record.setGID(gid);
		record.setID(KeyGen.uuid());
		record.setSalesDate(new Date());
		record.setUID(uid);
		record.setUname(uname);
		salesRecordsService.save(record);
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

	@Override
	public List<BaseGoodsOperation> listBrandsByMid(String mid) throws Exception {
		List<BaseGoodsOperation> brandslist = goodsOperationDao.selectBrandsByMid(mid).stream().map(dao -> {
			BaseGoodsOperation bo = new BaseGoodsOperation();
			BeanUtils.copyProperties(dao, bo);
			return bo;
		}).collect(Collectors.toList());
		return brandslist;
	}

	@Override
	public List<BaseGoodsOperation> listMerchant() throws Exception {
		List<BaseGoodsOperation> merchantlist = goodsOperationDao.selectMerchant().stream().map(dao -> {
			BaseGoodsOperation bo = new BaseGoodsOperation();
			BeanUtils.copyProperties(dao, bo);
			return bo;
		}).collect(Collectors.toList());
		return merchantlist;
	}

	@Override
	public boolean refreshCount(String gid, String count) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("gid", gid);
		map.put("count", count);
		int result = goodsOperationDao.updateCount(map);
		if (result > 0) {
			return true;
		}
		return false;

	}


}
