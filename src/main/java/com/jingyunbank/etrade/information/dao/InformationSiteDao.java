package com.jingyunbank.etrade.information.dao;

import java.util.List;


import com.jingyunbank.etrade.information.entity.InformationSiteEntity;


public interface InformationSiteDao {
	
	public boolean insert(InformationSiteEntity informationSiteEntity) throws Exception;
	
	public List<InformationSiteEntity> selectSitesBySiteid(String siteid);
	
	public InformationSiteEntity selectSitesByName(String name);
}
