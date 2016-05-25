package com.jingyunbank.etrade.wap.goods.service;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.wap.goods.bo.GoodsSku;
import com.jingyunbank.etrade.api.wap.goods.service.IWapGoodsSkuService;
import com.jingyunbank.etrade.wap.goods.dao.GoodsSkuDao;
import com.jingyunbank.etrade.wap.goods.entity.GoodsSkuEntity;
@Service
public class WapGoodsSkuService implements IWapGoodsSkuService{
	
	@Autowired 
	private GoodsSkuDao goodsSkuDao;
	
	@Override
	public Optional<GoodsSku> single(String id) {
		GoodsSku bo = new GoodsSku();
		GoodsSkuEntity entity=goodsSkuDao.selectGoodsSKuByid(id);
		BeanUtils.copyProperties(entity, bo);
		
		return Optional.of(bo);
	}


}
