package com.jingyunbank.etrade.goods.service;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jingyunbank.core.util.CollectionUtils;
import com.jingyunbank.etrade.api.goods.bo.Goods;
import com.jingyunbank.etrade.api.goods.service.IGoodsService;
import com.jingyunbank.etrade.goods.dao.GoodsDao;
import com.jingyunbank.etrade.goods.entity.GoodsDaoVO;

/**
 * 
 * Title: 商品接口
 * 
 * @author duanxf
 * @date 2015年11月4日
 */
@Service("goodsService")
public class GoodsService implements IGoodsService {
	@Resource
	private GoodsDao goodsDao;

	@Override
	public List<Goods> listGoodsByLikeName(String goodsname) throws Exception {
		List<GoodsDaoVO> daolist = goodsDao.selectGoodsByLikeName(goodsname);
		List<Goods> bolist = new ArrayList<Goods>();
		if (daolist != null) {
				bolist = CollectionUtils.copyTo(daolist, Goods.class);
		}
		return bolist;
	}

	@Override
	public List<Goods> listBrands() throws Exception {
		List<Goods> brandslist = new ArrayList<Goods>();
		List<GoodsDaoVO>  Brands = goodsDao.selectBrands();
		if(Brands!=null) {
			brandslist = CollectionUtils.copyTo(Brands, Goods.class);
		}
		return brandslist;
	}

	@Override
	public List<Goods> listTypes() throws Exception {
		List<Goods> typeslist = new ArrayList<Goods>();
		List<GoodsDaoVO>  types = goodsDao.selectTypes();
		if(types!=null) {
			typeslist = CollectionUtils.copyTo(types, Goods.class);
		}
		return typeslist;
	}

}
