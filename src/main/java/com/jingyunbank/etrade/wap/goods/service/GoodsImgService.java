package com.jingyunbank.etrade.wap.goods.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.wap.goods.service.IWapGoodsImgService;
import com.jingyunbank.etrade.wap.goods.dao.GoodsImgDao;

@Service("goodsImgService")
public class GoodsImgService implements IWapGoodsImgService {
	@Resource
	private GoodsImgDao goodsImgDao;

}
