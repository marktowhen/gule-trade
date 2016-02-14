package com.jingyunbank.etrade.statics.help.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jingyunbank.etrade.statics.help.entity.InformationSiteEntity;


public interface InformationSiteDao {
	
	public boolean insert(InformationSiteEntity informationSiteEntity) throws Exception;
	
	public List<InformationSiteEntity> selectSites(String informationID);
	
	public List<InformationSiteEntity> select(@Param(value="informationID") String informationID,@Param(value="from") long from,@Param(value="size") long size);
}
