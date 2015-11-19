package com.jingyunbank.etrade.information.dao;

import java.util.List;

import com.jingyunbank.etrade.information.entity.InformationDetailsEntity;




public interface InformationDetailsDao {
	
	public boolean insert(InformationDetailsEntity informationDetailsEntity) throws Exception;
	
	public void delete(String id) throws Exception;
	
	public boolean update(InformationDetailsEntity informationDetailsEntity) throws Exception;
	
	public List<InformationDetailsEntity> selectDetailsBySid(String sid);
	
	public InformationDetailsEntity selectDetailByid(String id);
}
