package com.jingyunbank.etrade.goods.dao;

import java.util.List;
import java.util.Map;

import com.jingyunbank.etrade.goods.entity.MerchantEntity;

/**
 * @author liug
 *
 */
public interface MerchantDao{
	
	public List<MerchantEntity> selectMerchants(Map<String, Integer> params) ;

}
