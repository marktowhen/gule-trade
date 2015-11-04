package com.jingyunbank.etrade.goods.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.jingyunbank.etrade.goods.entity.MerchantEntity;

/**
 * @author liug
 *
 */
public interface MerchantDao{
	
	public List<MerchantEntity> selectMerchants() ;

}
