package com.jingyunbank.etrade.goods.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jingyunbank.core.Range;
import com.jingyunbank.core.util.CollectionUtils;
import com.jingyunbank.etrade.api.goods.bo.HotGoods;
import com.jingyunbank.etrade.api.goods.bo.ShowGoods;
import com.jingyunbank.etrade.api.goods.bo.GoodsShow;
import com.jingyunbank.etrade.api.goods.service.IGoodsService;
import com.jingyunbank.etrade.goods.dao.GoodsDao;
import com.jingyunbank.etrade.goods.entity.GoodsDaoEntity;
import com.jingyunbank.etrade.goods.entity.HotGoodsEntity;

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
	public List<ShowGoods> listGoodsByLikeName(String goodsname, Range range) throws Exception {
		Map<String, Object> map = new HashMap<>();
		map.put("goodsname", goodsname);
		map.put("from", range.getFrom());
		map.put("size", range.getTo());
		List<GoodsDaoEntity> daolist = goodsDao.selectGoodsByLikeName(map);
		List<ShowGoods> bolist = new ArrayList<ShowGoods>();
		if (daolist != null) {
			bolist = CollectionUtils.copyTo(daolist, ShowGoods.class);
		}
		return bolist;
	}

	@Override
	public List<ShowGoods> listBrands() throws Exception {
		List<ShowGoods> brandslist = new ArrayList<ShowGoods>();
		List<GoodsDaoEntity> Brands = goodsDao.selectBrands();
		if (Brands != null) {
			brandslist = CollectionUtils.copyTo(Brands, ShowGoods.class);
		}
		return brandslist;
	}

	@Override
	public List<ShowGoods> listTypes() throws Exception {
		List<ShowGoods> typeslist = new ArrayList<ShowGoods>();
		List<GoodsDaoEntity> types = goodsDao.selectTypes();
		if (types != null) {
			typeslist = CollectionUtils.copyTo(types, ShowGoods.class);
		}
		return typeslist;
	}

	@Override
	public List<HotGoods> listHotGoods() throws Exception {
		List<HotGoods> rltlist = new ArrayList<HotGoods>();
		List<HotGoodsEntity> goodslist = goodsDao.selectHotGoods();
		if (goodslist != null) {
			rltlist = CollectionUtils.copyTo(goodslist, HotGoods.class);
		}
		return rltlist;
	}

	@Override
	public List<ShowGoods> listGoodsByWhere(GoodsShow goodsshow, Range range) throws Exception {
		Map<String, Object> map = new HashMap<>();

		map.put("from", range.getFrom());
		map.put("to", range.getTo());
		map.put("brandArr", goodsshow.getBrands());
		map.put("typeArr", goodsshow.getTypes());
		map.put("beginprice", goodsshow.getBeginPrice());
		map.put("endprice", goodsshow.getEndPrice());

		if (goodsshow.getOrder() == 1) {
			map.put("order", "1");
		} else if (goodsshow.getOrder() == 2) {
			map.put("order", "2");
		} else if (goodsshow.getOrder() == 3) {
			map.put("order", "3");
		} else if (goodsshow.getOrder() == 4) {
			map.put("order", "4");
		}

		List<GoodsDaoEntity> goodslist = goodsDao.selectGoodsByWhere(map);
		List<ShowGoods> showGoodsList = new ArrayList<ShowGoods>();
		if (goodslist != null) {
			showGoodsList = CollectionUtils.copyTo(goodslist, ShowGoods.class);
		}
		return showGoodsList;
	}

	@Override
	public List<ShowGoods> listRecommend() throws Exception{
		List<ShowGoods> recommendlist = new ArrayList<ShowGoods>();
		List<GoodsDaoEntity> recommend = goodsDao.selectRecommend();
		if (recommend != null) {
			recommendlist = CollectionUtils.copyTo(recommend, ShowGoods.class);
		}
		return recommendlist;
	}
}
