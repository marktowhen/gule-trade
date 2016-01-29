package com.jingyunbank.etrade.goods.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.jingyunbank.core.Range;
import com.jingyunbank.etrade.api.goods.bo.GoodsList;
import com.jingyunbank.etrade.api.goods.bo.GoodsMerchant;
import com.jingyunbank.etrade.api.goods.bo.GoodsSearch;
import com.jingyunbank.etrade.api.goods.bo.GoodsShow;
import com.jingyunbank.etrade.api.goods.bo.HoneyGoods;
import com.jingyunbank.etrade.api.goods.bo.Hot24Goods;
import com.jingyunbank.etrade.api.goods.bo.HotGoods;
import com.jingyunbank.etrade.api.goods.bo.ShowGoods;
import com.jingyunbank.etrade.api.goods.service.IGoodsService;
import com.jingyunbank.etrade.back.goods.dao.GoodsBKDao;
import com.jingyunbank.etrade.goods.dao.GoodsDao;
import com.jingyunbank.etrade.goods.entity.GoodsDaoEntity;
import com.jingyunbank.etrade.goods.entity.HoneyGoodsEntity;
import com.jingyunbank.etrade.goods.entity.Hot24GoodsEntity;
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
	@Resource
	private GoodsBKDao goodsBKDao;

	@Override
	@Cacheable(cacheNames = "brandCache", keyGenerator = "CustomKG")
	public List<ShowGoods> listBrands() throws Exception {
		List<ShowGoods> brandslist = goodsDao.selectBrands().stream().map(dao -> {
			ShowGoods bo = new ShowGoods();
			BeanUtils.copyProperties(dao, bo);
			return bo;
		}).collect(Collectors.toList());
		return brandslist;
	}

	@Override
	@Cacheable(cacheNames = "typeCache", keyGenerator = "CustomKG")
	public List<ShowGoods> listTypes() throws Exception {
		List<ShowGoods> typeslist = goodsDao.selectTypes().stream().map(dao -> {
			ShowGoods bo = new ShowGoods();
			BeanUtils.copyProperties(dao, bo);
			return bo;
		}).collect(Collectors.toList());
		return typeslist;
	}

	@Override
	@Cacheable(cacheNames = "goodsCache", keyGenerator = "CustomKG")
	public List<HotGoods> listHotGoods() throws Exception {
		List<HotGoods> rltlist = new ArrayList<HotGoods>();
		List<HotGoodsEntity> goodslist = goodsDao.selectHotGoods();
		if (goodslist != null) {
			rltlist = goodslist.stream().map(eo -> {
				HotGoods bo = new HotGoods();
				BeanUtils.copyProperties(eo, bo);
				return bo;
			}).collect(Collectors.toList());
		}
		return rltlist;
	}

	@Override
	@Cacheable(cacheNames = "goodsCache", keyGenerator = "CustomKG")
	public List<ShowGoods> listGoodsByWhere(String[] brands, String[] types, BigDecimal beginPrice, BigDecimal endPrice,
			int order, Range range) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("from", (int) range.getFrom());
		map.put("size", (int) range.getTo());
		map.put("brandArr", brands);
		map.put("typeArr", types);
		map.put("beginprice", beginPrice);
		map.put("endprice", endPrice);

		if (order == 1) {
			map.put("order", "1");
		} else if (order == 2) {
			map.put("order", "2");
		} else if (order == 3) {
			map.put("order", "3");
		} else if (order == 4) {
			map.put("order", "4");
		}

		List<ShowGoods> showGoodsList = goodsDao.selectGoodsByWhere(map).stream().map(dao -> {
			ShowGoods bo = new ShowGoods();
			BeanUtils.copyProperties(dao, bo);
			return bo;
		}).collect(Collectors.toList());
		return showGoodsList;
	}

	@Override
	@Cacheable(cacheNames = "goodsCache", keyGenerator = "CustomKG")
	public List<ShowGoods> listRecommend(String from, String to) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("from", Integer.parseInt(from));
		map.put("to", Integer.parseInt(to));
		List<ShowGoods> recommendlist = goodsDao.selectRecommend(map).stream().map(dao -> {
			ShowGoods bo = new ShowGoods();
			BeanUtils.copyProperties(dao, bo);
			return bo;
		}).collect(Collectors.toList());
		return recommendlist;
	}

	@Override
	@Cacheable(cacheNames = "merchantCache", keyGenerator = "CustomKG")
	public List<GoodsMerchant> listMerchantByWhere(String[] brands, String[] types, BigDecimal beginPrice,
			BigDecimal endPrice, Range range) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("from", (int) range.getFrom());
		map.put("size", (int) range.getTo());
		map.put("brandArr", brands);
		map.put("typeArr", types);
		map.put("beginprice", beginPrice);
		map.put("endprice", endPrice);

		List<GoodsMerchant> list = goodsDao.selectMerchantByWhere(map).stream().map(dao -> {
			GoodsMerchant bo = new GoodsMerchant();
			BeanUtils.copyProperties(dao, bo);
			return bo;
		}).collect(Collectors.toList());
		return list;
	}

	@Override
	@Cacheable(cacheNames = "goodsCache", keyGenerator = "CustomKG")
	public List<ShowGoods> listMerchantByWhereGoodsMax(String[] brands, String[] types, BigDecimal beginPrice,
			BigDecimal endPrice, String mid, int order, Range range) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("brandArr", brands);
		map.put("typeArr", types);
		map.put("beginprice", beginPrice);
		map.put("endprice", endPrice);
		map.put("mid", mid);

		if (order == 1) {
			map.put("order", "1");
		} else if (order == 2) {
			map.put("order", "2");
		} else if (order == 3) {
			map.put("order", "3");
		} else if (order == 4) {
			map.put("order", "4");
		}

		map.put("from", (int) range.getFrom());
		map.put("size", (int) range.getTo());
		List<ShowGoods> list = goodsDao.selectMerchantByWhereGoodsMax(map).stream().map(dao -> {
			ShowGoods bo = new ShowGoods();
			BeanUtils.copyProperties(dao, bo);
			return bo;
		}).collect(Collectors.toList());
		return list;
	}

	@Override
	@Cacheable(cacheNames = "goodsCache", keyGenerator = "CustomKG")
	public List<Hot24Goods> listHot24Goods() throws Exception {
		List<Hot24Goods> rltlist = new ArrayList<Hot24Goods>();
		List<Hot24GoodsEntity> goodslist = goodsDao.selectHot24Goods();
		if (goodslist != null) {
			rltlist = goodslist.stream().map(eo -> {
				Hot24Goods bo = new Hot24Goods();
				BeanUtils.copyProperties(eo, bo);
				return bo;
			}).collect(Collectors.toList());
		}
		return rltlist;
	}

	@Override
	@Cacheable(cacheNames = "goodsCache", keyGenerator = "CustomKG")
	public List<ShowGoods> listGoodsExpand() throws Exception {
		List<ShowGoods> list = goodsDao.selectGoodsExpand().stream().map(dao -> {
			ShowGoods bo = new ShowGoods();
			BeanUtils.copyProperties(dao, bo);
			return bo;
		}).collect(Collectors.toList());
		return list;
	}

	@Override
	@Cacheable(cacheNames = "goodsCache", keyGenerator = "CustomKG")
	public List<ShowGoods> listGoodsByGoodsResult(String[] brands, String[] types, BigDecimal beginPrice,
			BigDecimal endPrice, String goodsname, int order, Range range) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("brandArr", brands);
		map.put("typeArr", types);
		map.put("beginprice", beginPrice);
		map.put("endprice", endPrice);
		map.put("goodsname", goodsname);
		map.put("order", order);
		map.put("from", (int) range.getFrom());
		map.put("size", (int) range.getTo());
		List<ShowGoods> list = goodsDao.selectGoodsByGoodsResult(map).stream().map(dao -> {
			ShowGoods goods = new ShowGoods();
			BeanUtils.copyProperties(dao, goods);
			return goods;
		}).collect(Collectors.toList());
		return list;
	}

	@Override
	//@Cacheable(cacheNames = "goodsCache", keyGenerator = "CustomKG")
	public Optional<ShowGoods> singleById(String gid) throws Exception {
		GoodsDaoEntity goods = goodsDao.selectOne(gid);
		ShowGoods showGoods = null;
		if (Objects.nonNull(goods)) {
			showGoods = new ShowGoods();
			BeanUtils.copyProperties(goods, showGoods);
		}
		//System.out.println(showGoods);
		return Optional.ofNullable(showGoods);
	}

	@Override
	@Cacheable(cacheNames = "goodsCache", keyGenerator = "CustomKG")
	public List<ShowGoods> listAll(Range range) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("from", (int) range.getFrom());
		map.put("size", (int) range.getTo());
		List<ShowGoods> list = goodsDao.selectAllGoods(map).stream().map(dao -> {
			ShowGoods goods = new ShowGoods();
			BeanUtils.copyProperties(dao, goods);
			return goods;
		}).collect(Collectors.toList());
		return list;
	}

	@Override
	@Cacheable(cacheNames = "goodsCache", keyGenerator = "CustomKG")
	public List<HoneyGoods> listHoneyGoods(String gid) throws Exception {
		List<HoneyGoods> rltlist = new ArrayList<HoneyGoods>();
		List<HoneyGoodsEntity> goodslist = goodsDao.selectHoneyGoods(gid);
		if (goodslist != null) {
			rltlist = goodslist.stream().map(eo -> {
				HoneyGoods bo = new HoneyGoods();
				BeanUtils.copyProperties(eo, bo);
				return bo;
			}).collect(Collectors.toList());
		}
		return rltlist;
	}

	@Override
	@Cacheable(cacheNames = "goodsCache", keyGenerator = "CustomKG")
	public List<GoodsList> listGoodsByCondition(String name, int state, String mid, String bid, Range range)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("from", (int) range.getFrom());
		map.put("size", (int) range.getTo());

		map.put("name", name);
		map.put("state", state);
		map.put("mid", mid);
		map.put("bid", bid);

		List<GoodsList> showGoodsList = goodsBKDao.selectGoodsByCondition(map).stream().map(dao -> {
			GoodsList bo = new GoodsList();
			BeanUtils.copyProperties(dao, bo);
			return bo;
		}).collect(Collectors.toList());
		return showGoodsList;
	}

	@Override
	@Cacheable(cacheNames = "brandCache", keyGenerator = "CustomKG")
	public List<ShowGoods> listBrandsThree(Range range) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("from", (int) range.getFrom());
		map.put("size", (int) range.getTo());
		List<ShowGoods> brandslist = goodsDao.selectBrandsThree(map).stream().map(dao -> {
			ShowGoods bo = new ShowGoods();
			BeanUtils.copyProperties(dao, bo);
			return bo;
		}).collect(Collectors.toList());
		return brandslist;
	}

	@Override
	@Cacheable(cacheNames = "typeCache", keyGenerator = "CustomKG")
	public List<ShowGoods> listTypesThree(Range range) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("from", (int) range.getFrom());
		map.put("size", (int) range.getTo());
		List<ShowGoods> typeslist = goodsDao.selectTypesThree(map).stream().map(dao -> {
			ShowGoods bo = new ShowGoods();
			BeanUtils.copyProperties(dao, bo);
			return bo;
		}).collect(Collectors.toList());
		return typeslist;
	}

	@Override
	@Cacheable(cacheNames = "goodsCache", keyGenerator = "CustomKG")
	public List<ShowGoods> listGoodsStcok(List<String> gids) throws Exception {
		List<ShowGoods> stocklist = goodsDao.selectGoodsStock(gids).stream().map(dao -> {
			ShowGoods bo = new ShowGoods();
			BeanUtils.copyProperties(dao, bo);
			return bo;
		}).collect(Collectors.toList());
		return stocklist;
	}

	@Override
	public Map<String, BigDecimal> emprice(List<String> gids) {
		List<GoodsDaoEntity> entities = goodsDao.selectEmprice(gids);
		Map<String, BigDecimal> result = new HashMap<String, BigDecimal>();
		entities.forEach(x -> result.put(x.getGID(), x.getEmprice()));
		return result;
	}

}
