package com.jingyunbank.etrade.back.goods.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.jingyunbank.core.Range;
import com.jingyunbank.etrade.back.api.goods.bo.GoodsList;
import com.jingyunbank.etrade.back.api.goods.bo.GoodsSearch;
import com.jingyunbank.etrade.back.api.goods.service.IGoodsBKService;
import com.jingyunbank.etrade.back.goods.dao.GoodsBKDao;

/**
 * 商品后台管理服务类
 * @author liug
 *
 */
@Service("goodsBKService")
public class GoodsBKService implements IGoodsBKService {
	@Resource
	private GoodsBKDao goodsBKDao;

	  
	@Override
	public List<GoodsList> listGoodsByCondition(GoodsSearch goodsSearch, Range range) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("from", (int) range.getFrom());
		map.put("size", (int) range.getTo());
		map.put("name", goodsSearch.getName());
		map.put("state", goodsSearch.getState());
		List<GoodsList> showGoodsList = goodsBKDao.selectGoodsByCondition(map).stream().map(dao -> {
			GoodsList bo = new GoodsList();
			BeanUtils.copyProperties(dao, bo);
			return bo;
		}).collect(Collectors.toList());
		return showGoodsList;
	}
}
