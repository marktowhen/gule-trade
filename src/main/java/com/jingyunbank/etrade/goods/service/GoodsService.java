package com.jingyunbank.etrade.goods.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.jingyunbank.core.KeyGen;
import com.jingyunbank.core.Range;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.goods.bo.FootprintGoods;
import com.jingyunbank.etrade.api.goods.bo.GoodsMerchant;
import com.jingyunbank.etrade.api.goods.bo.GoodsShow;
import com.jingyunbank.etrade.api.goods.bo.Hot24Goods;
import com.jingyunbank.etrade.api.goods.bo.HotGoods;
import com.jingyunbank.etrade.api.goods.bo.ShowGoods;
import com.jingyunbank.etrade.api.goods.service.IGoodsService;
import com.jingyunbank.etrade.goods.dao.GoodsDao;
import com.jingyunbank.etrade.goods.entity.FootprintEntity;
import com.jingyunbank.etrade.goods.entity.FootprintGoodsEntity;
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

	@Override
	public List<ShowGoods> listGoodsByLikeName(String goodsname, Range range) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("goodsname", goodsname);
		map.put("from", range.getFrom());
		map.put("size", range.getTo());
		List<ShowGoods> bolist = goodsDao.selectGoodsByLikeName(map).stream().map(dao -> {
			ShowGoods bo = new ShowGoods();
			BeanUtils.copyProperties(dao, bo);
			return bo;
		}).collect(Collectors.toList());

		return bolist;
	}

	@Override
	public List<ShowGoods> listBrands() throws Exception {
		List<ShowGoods> brandslist = goodsDao.selectBrands().stream().map(dao -> {
			ShowGoods bo = new ShowGoods();
			BeanUtils.copyProperties(dao, bo);
			return bo;
		}).collect(Collectors.toList());
		return brandslist;
	}

	@Override
	public List<ShowGoods> listTypes() throws Exception {
		List<ShowGoods> typeslist = goodsDao.selectTypes().stream().map(dao -> {
			ShowGoods bo = new ShowGoods();
			BeanUtils.copyProperties(dao, bo);
			return bo;
		}).collect(Collectors.toList());
		return typeslist;
	}

	@Override
	public List<HotGoods> listHotGoods() throws Exception {
		List<HotGoods> rltlist = new ArrayList<HotGoods>();
		List<HotGoodsEntity> goodslist = goodsDao.selectHotGoods();
		if (goodslist != null) {
			rltlist = goodslist.stream().map(eo -> {
		    	HotGoods bo = new HotGoods();
				try {
					BeanUtils.copyProperties(eo, bo);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return bo;
			}).collect(Collectors.toList());
		}
		return rltlist;
	}

	@Override
	public List<ShowGoods> listGoodsByWhere(GoodsShow goodsshow, Range range) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("from", (int) range.getFrom());
		map.put("size", (int) range.getTo());
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

		List<ShowGoods> showGoodsList = goodsDao.selectGoodsByWhere(map).stream().map(dao -> {
			ShowGoods bo = new ShowGoods();
			BeanUtils.copyProperties(dao, bo);
			return bo;
		}).collect(Collectors.toList());
		return showGoodsList;
	}

	@Override
	public List<ShowGoods> listRecommend() throws Exception {
		List<ShowGoods> recommendlist = goodsDao.selectRecommend().stream().map(dao -> {
			ShowGoods bo = new ShowGoods();
			BeanUtils.copyProperties(dao, bo);
			return bo;
		}).collect(Collectors.toList());
		return recommendlist;
	}

	@Override
	public List<GoodsMerchant> listMerchantByWhere(GoodsShow show, Range range) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("from", (int) range.getFrom());
		map.put("size", (int) range.getTo());
		map.put("brandArr", show.getBrands());
		map.put("typeArr", show.getTypes());
		map.put("beginprice", show.getBeginPrice());
		map.put("endprice", show.getEndPrice());

		List<GoodsMerchant> list = goodsDao.selectMerchantByWhere(map).stream().map(dao -> {
			GoodsMerchant bo = new GoodsMerchant();
			BeanUtils.copyProperties(dao, bo);
			return bo;
		}).collect(Collectors.toList());
		return list;
	}

	@Override
	public List<ShowGoods> listMerchantByWhereGoods(GoodsShow show) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("brandArr", show.getBrands());
		map.put("typeArr", show.getTypes());
		map.put("beginprice", show.getBeginPrice());
		map.put("endprice", show.getEndPrice());
		map.put("mid", show.getMID());
		List<ShowGoods> list = goodsDao.selectMerchantByWhereGoods(map).stream().map(dao -> {
			ShowGoods bo = new ShowGoods();
			BeanUtils.copyProperties(dao, bo);
			return bo;
		}).collect(Collectors.toList());
		return list;
	}
	
	@Override
	public List<Hot24Goods> listHot24Goods() throws Exception {
		List<Hot24Goods> rltlist = new ArrayList<Hot24Goods>();
		List<Hot24GoodsEntity> goodslist = goodsDao.selectHot24Goods();
		if (goodslist != null) {
			rltlist = goodslist.stream().map(eo -> {
				Hot24Goods bo = new Hot24Goods();
				try {
					BeanUtils.copyProperties(eo, bo);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return bo;
			}).collect(Collectors.toList());
		}
		return rltlist;
	}

	@Override
	public List<ShowGoods> listGoodsExpand() throws Exception {
		List<ShowGoods> list = goodsDao.selectGoodsExpand().stream().map(dao ->{
			ShowGoods bo = new ShowGoods();
			BeanUtils.copyProperties(dao, bo);
			return bo;
		}).collect(Collectors.toList());
		return list;
	}
	
	@Override
	public List<FootprintGoods> listFootprintGoods() throws Exception {
		List<FootprintGoods> rltlist = new ArrayList<FootprintGoods>();
		List<FootprintGoodsEntity> goodslist = goodsDao.selectFootprintGoods();
		if (goodslist != null) {
			rltlist = goodslist.stream().map(eo -> {
				FootprintGoods bo = new FootprintGoods();
				try {
					BeanUtils.copyProperties(eo, bo);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return bo;
			}).collect(Collectors.toList());
		}
		return rltlist;
	}

	@Override
	public boolean saveFootprint(String uid,String gid) throws DataSavingException {
		FootprintEntity fe = new FootprintEntity();
		fe.setID(KeyGen.uuid());
		fe.setUID(uid);
		fe.setGID(gid);
		fe.setVisitTime(new Date());
		int result = 0;
		try {
			result = goodsDao.insertFootprint(fe);
		} catch (Exception e) {
			throw new DataSavingException(e);
		}
		if(result > 0){
			return true;
		}
		return false;
	}
	@Override
	public List<ShowGoods> listGoodsByGoodsResult(GoodsShow bo, Range range) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("brandArr", bo.getBrands());
		map.put("typeArr", bo.getTypes());
		map.put("beginprice", bo.getBeginPrice());
		map.put("endprice", bo.getEndPrice());
		map.put("goodsname", bo.getGoodsName());
		map.put("order", bo.getOrder());
		map.put("from", (int) range.getFrom());
		map.put("size", (int) range.getTo());
		List<ShowGoods> list = goodsDao.selectGoodsByGoodsResult(map).stream().map(dao -> {
			ShowGoods goods = new ShowGoods();
			BeanUtils.copyProperties(dao, goods);
			return goods;
		}).collect(Collectors.toList());
		return list;
	}
}
