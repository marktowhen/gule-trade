package com.jingyunbank.etrade.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.exception.DataRemovingException;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.user.bo.Employee;
import com.jingyunbank.etrade.api.user.service.IEmployeeService;
import com.jingyunbank.etrade.user.dao.EmployeeDao;

@Service("employeeService")
public class EmployeeService implements IEmployeeService {

	@Autowired
	private EmployeeDao employeeDao;
	
	@Override
	public void save(Employee employee) throws DataSavingException {
		
	}

	@Override
	public void refresh(Employee employee) throws DataRefreshingException {

	}

	@Override
	public void remove(String id) throws DataRemovingException {

	}

	@Override
	public boolean isEmployee(String mobile) {
		return employeeDao.count(mobile) > 0;
	}

}
