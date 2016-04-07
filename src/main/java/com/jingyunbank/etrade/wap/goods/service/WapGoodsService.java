package com.jingyunbank.etrade.wap.goods.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.wap.goods.bo.GoodsDeatil;
import com.jingyunbank.etrade.api.wap.goods.bo.GoodsImg;
import com.jingyunbank.etrade.api.wap.goods.bo.GoodsInfo;
import com.jingyunbank.etrade.api.wap.goods.bo.GoodsSku;
import com.jingyunbank.etrade.api.wap.goods.bo.GoodsSkuCondition;
import com.jingyunbank.etrade.api.wap.goods.bo.ShowGoods;
import com.jingyunbank.etrade.api.wap.goods.service.IWapGoodsService;
import com.jingyunbank.etrade.order.presale.entity.OrderTraceEntity;
import com.jingyunbank.etrade.wap.goods.dao.GoodsImgDao;
import com.jingyunbank.etrade.wap.goods.dao.WapGoodsDao;
import com.jingyunbank.etrade.wap.goods.entity.GoodsDeatilEntity;
import com.jingyunbank.etrade.wap.goods.entity.GoodsImgEntity;
import com.jingyunbank.etrade.wap.goods.entity.GoodsSkuConditionEntity;
import com.jingyunbank.etrade.wap.goods.entity.GoodsSkuEntity;

@Service("wapGoodsService")
public class WapGoodsService implements IWapGoodsService {
	@Resource
	private WapGoodsDao wapGoodsDao;
	@Resource
	private GoodsImgDao goodsImgDao;

	@Override
	public List<ShowGoods> listGoods(String mid, String tid, String order, String name) throws Exception {
		List<ShowGoods> list = wapGoodsDao.selectGoods(mid, tid, order, name).stream().map(bo -> {
			ShowGoods vo = new ShowGoods();
			BeanUtils.copyProperties(bo, vo);
			return vo;
		}).collect(Collectors.toList());
		return list;
	}

	@Override
	public Optional<GoodsSkuCondition> singleGoodsSkuCondition(String gid) throws Exception {
		GoodsSkuConditionEntity entity = wapGoodsDao.selectGoodsSkuConditionByGid(gid);
		GoodsSkuCondition condition = null;
		if (Objects.nonNull(entity)) {
			condition = new GoodsSkuCondition();
			BeanUtils.copyProperties(entity, condition);

		}
		return Optional.ofNullable(condition);
	}

	@Override
	public Optional<GoodsSku> singleGoodsSku(String gid, String condition) throws Exception {
		GoodsSkuEntity entity = wapGoodsDao.selectGoodsSku(gid, condition);
		GoodsSku sku = null;
		if (Objects.nonNull(entity)) {
			sku = new GoodsSku();
			BeanUtils.copyProperties(entity, sku);

		}
		return Optional.ofNullable(sku);
	}

	@Override
	public List<GoodsInfo> listGoodsInfo(String gid) throws Exception {
		List<GoodsInfo> list = wapGoodsDao.selectGoodsInfo(gid).stream().map(bo -> {
			GoodsInfo vo = new GoodsInfo();
			BeanUtils.copyProperties(bo, vo);
			return vo;
		}).collect(Collectors.toList());
		return list;
	}

	@Override
	public Optional<GoodsDeatil> singleGoodsDetail(String gid) throws Exception {
		GoodsDeatilEntity entity = wapGoodsDao.selectGoodsDetail(gid);
		GoodsDeatil detail = null;
		if (Objects.nonNull(entity)) {
			detail = new GoodsDeatil();
			BeanUtils.copyProperties(entity, detail);
		}
		//获取商品的展示图片 
		List<GoodsImgEntity> detailImgs = goodsImgDao.selectGoodsDetailImgs(gid);
		//转换
		List<GoodsImg> imgList = new ArrayList<GoodsImg>();
		detailImgs.forEach(d -> {
			GoodsImg img = new GoodsImg();
			BeanUtils.copyProperties(d, img);
			imgList.add(img);
		});
		detail.setImgList(imgList);
		
		return Optional.ofNullable(detail);
	}

}
