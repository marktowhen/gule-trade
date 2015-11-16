package com.jingyunbank.etrade.advice.dao;

import java.util.List;

import com.jingyunbank.etrade.advice.entity.AdviceSiteEntity;


public interface AdviceSiteDao {
	
	public boolean insert(AdviceSiteEntity adviceSiteEntity) throws Exception;
	
	public List<AdviceSiteEntity> selectSitesBySiteid(String siteid);
}
