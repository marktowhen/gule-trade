package com.jingyunbank.etrade.goods.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.goods.entity.GoodsDaoVO;
import com.jingyunbank.etrade.goods.entity.GoodsEntity;

/**
 * 
 * Title: GoodsDao
 * 
 * @author duanxf
 * @date 2015年11月4日
 */

public interface GoodsDao {
	public List<GoodsDaoVO> selectGoodsByLikeName(String goodsname) throws Exception;

	public List<GoodsDaoVO> selectBrands() throws Exception;

	public List<GoodsDaoVO> selectTypes() throws Exception;

	public List<GoodsDaoVO> selectGoodsByWhere(Map<String, Object> map) throws Exception;
}
