package com.jingyunbank.etrade.marketing.group.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jingyunbank.etrade.marketing.group.entity.GroupGoodsPriceSettingEntity;


public interface GroupGoodsPriceSettingDao {

	public void insertMany(@Param("settings") List<GroupGoodsPriceSettingEntity> settings) throws Exception;
	
	public List<GroupGoodsPriceSettingEntity> selectMany(String ggid) ;

}