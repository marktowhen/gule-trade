package com.jingyunbank.etrade.wap.goods.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.wap.goods.service.IWapGoodsOperationService;
import com.jingyunbank.etrade.wap.goods.dao.GoodsAttrValueDao;
import com.jingyunbank.etrade.wap.goods.dao.GoodsImgDao;
import com.jingyunbank.etrade.wap.goods.dao.GoodsSkuDao;
import com.jingyunbank.etrade.wap.goods.dao.WapGoodsOperationDao;

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
	
	
}
