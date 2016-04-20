package com.jingyunbank.etrade.wap.goods.service;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.wap.goods.bo.GoodsInfo;
import com.jingyunbank.etrade.api.wap.goods.service.IWapGoodsInfoService;
import com.jingyunbank.etrade.wap.goods.dao.WapGoodsInfoDao;
import com.jingyunbank.etrade.wap.goods.entity.GoodsInfoEntity;

@Service("wapGoodsInfoService")
public class WapGoodsInfoService implements IWapGoodsInfoService {
	@Resource
	private WapGoodsInfoDao wapGoodsInfoDao;

	@Override
	public void saveGoodsInfo(GoodsInfo infoBO) throws Exception {
		GoodsInfoEntity entity = new GoodsInfoEntity();
		BeanUtils.copyProperties(infoBO, entity);
		wapGoodsInfoDao.insertGoodsInfo(entity);
	}

	@Override
	public void removeGoodsInfo(String gid) throws Exception {
		wapGoodsInfoDao.deleteGoodsInfo(gid);
	}

	@Override
	public void removeGoodsInfoById(String infoId) throws Exception {
		wapGoodsInfoDao.deleteGoodsInfoById(infoId);
	}

}
