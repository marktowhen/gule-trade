package com.jingyunbank.etrade.user.dao;

import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.user.entity.EmployeeEntity;

public interface EmployeeDao {

	public void insert(EmployeeEntity employee) throws DataSavingException;
	
	public void update(EmployeeEntity employee) throws Exception;
	
	public void delete(String id) throws Exception;
	
	public int count(String mobile);
	
}
