package com.jingyunbank.etrade.marketing.rankgroup.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jingyunbank.etrade.marketing.rankgroup.entity.RankGroupGoodsPriceSettingEntity;

public interface RankGroupGoodsPriceSettingDao {
	
	/**
	 * 添加团购商品价格区间
	 * @param settings
	 * @throws Exception
	 */
    public void insertMany(@Param("settings") List<RankGroupGoodsPriceSettingEntity> settings) throws Exception;
	
    
    /**
     * 查询团购商品 价格区间
     * @param ggid
     * @return
     */
	public List<RankGroupGoodsPriceSettingEntity> selectMany(String ggid) ;

	public void delete(String ggid) throws Exception;

}
