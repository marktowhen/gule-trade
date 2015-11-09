package com.jingyunbank.etrade.goods.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jingyunbank.core.Range;
import com.jingyunbank.core.util.CollectionUtils;
import com.jingyunbank.etrade.api.goods.bo.Goods;
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
	public List<Goods> listGoodsByLikeName(String goodsname, Range range) throws Exception {
		Map<String, Object> map = new HashMap<>();
		map.put("goodsname", goodsname);
		map.put("from", range.getFrom());
		map.put("to", range.getTo());
		List<GoodsDaoEntity> daolist = goodsDao.selectGoodsByLikeName(map);
		List<Goods> bolist = new ArrayList<Goods>();
		if (daolist != null) {
			bolist = CollectionUtils.copyTo(daolist, Goods.class);
		}
		return bolist;
	}

	@Override
	public List<Goods> listBrands() throws Exception {
		List<Goods> brandslist = new ArrayList<Goods>();
		List<GoodsDaoEntity> Brands = goodsDao.selectBrands();
		if (Brands != null) {
			brandslist = CollectionUtils.copyTo(Brands, Goods.class);
		}
		return brandslist;
	}

	@Override
	public List<Goods> listTypes() throws Exception {
		List<Goods> typeslist = new ArrayList<Goods>();
		List<GoodsDaoEntity> types = goodsDao.selectTypes();
		if (types != null) {
			typeslist = CollectionUtils.copyTo(types, Goods.class);
		}
		return typeslist;
	}

	@Override
	public List<Goods> listHotGoods() throws Exception {
		List<Goods> rltlist = new ArrayList<Goods>();
		List<HotGoodsEntity> goodslist = goodsDao.selectHotGoods();
		if (goodslist != null) {
			rltlist = CollectionUtils.copyTo(goodslist, Goods.class);
		}
		return rltlist;
	}

	@Override
	public List<Goods> listGoodsByWhere(GoodsShow goodsshow, Range range) throws Exception {
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
		List<Goods> showGoodsList = new ArrayList<Goods>();
		if (goodslist != null) {
			showGoodsList = CollectionUtils.copyTo(goodslist, Goods.class);
		}
		return showGoodsList;
	}
}
