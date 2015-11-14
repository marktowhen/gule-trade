package com.jingyunbank.etrade.merchant.dao;

import java.util.List;
import java.util.Map;

import com.jingyunbank.etrade.merchant.entity.MerchantEntity;
import com.jingyunbank.etrade.user.entity.UserEntity;

/**
 * @author liug
 *
 */
public interface MerchantDao{
	/**
	 * 商家查询
	 * @param params
	 * @return
	 */
	public List<MerchantEntity> selectMerchants(Map<String, Integer> params) ;
	/**
	 * 商家保存
	 * @param merchantEntity
	 * @return
	 * @throws Exception
	 */
	public boolean insertMerchant(MerchantEntity merchantEntity) throws Exception;

}
