package com.jingyunbank.etrade.advice.dao;

import java.util.List;

import com.jingyunbank.etrade.advice.entity.AdviceDetailsEntity;


public interface AdviceDetailsDao {
	
	public boolean insert(AdviceDetailsEntity adviceDetailsEntity) throws Exception;
	
	public void delete(String id) throws Exception;
	
	public boolean update(AdviceDetailsEntity adviceDetailsEntity) throws Exception;
	
	public List<AdviceDetailsEntity> selectDetailsBySid(String sid);
	
	public AdviceDetailsEntity selectDetailByid(String id);
}
