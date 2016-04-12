package com.jingyunbank.etrade.wap.goods.service;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.wap.goods.bo.GoodsInfo;
import com.jingyunbank.etrade.api.wap.goods.service.IWapGoodsInfoOperationService;
import com.jingyunbank.etrade.wap.goods.dao.WapGoodsInfoOperationDao;
import com.jingyunbank.etrade.wap.goods.entity.GoodsInfoEntity;

@Service("wapGoodsOperationService")
public class WapGoodsInfoOperationService implements IWapGoodsInfoOperationService {
	@Resource
	private WapGoodsInfoOperationDao wapGoodsOperationDao;

	@Override
	public void saveGoodsInfo(GoodsInfo infoBO) throws Exception {
		GoodsInfoEntity entity = new GoodsInfoEntity();
		BeanUtils.copyProperties(infoBO, entity);
		wapGoodsOperationDao.insertGoodsInfo(entity);
	}

	@Override
	public void removeGoodsInfo(String gid) throws Exception {
		wapGoodsOperationDao.deleteGoodsInfo(gid);
	}

	@Override
	public void removeGoodsInfoById(String infoId) throws Exception {
		wapGoodsOperationDao.deleteGoodsInfoById(infoId);
	}

}
