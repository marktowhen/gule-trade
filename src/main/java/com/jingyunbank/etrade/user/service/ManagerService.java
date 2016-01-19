package com.jingyunbank.etrade.user.service;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.user.bo.Manager;
import com.jingyunbank.etrade.api.user.service.IManagerService;
import com.jingyunbank.etrade.user.dao.ManagerDao;
import com.jingyunbank.etrade.user.entity.ManagerEntity;

@Service("managerService")
public class ManagerService implements IManagerService {

	@Autowired
	private ManagerDao managerDao;
	
	@Override
	public Optional<Manager> singleByID(String id) {
		ManagerEntity managerEntity = managerDao.selectByKey(id);
		if(managerEntity!=null){
			Manager bo = new Manager();
			BeanUtils.copyProperties(managerEntity, bo);
			return Optional.of(bo);
		}
		return Optional.empty();
	}

	@Override
	public Optional<Manager> singleByMname(String mname) {
		ManagerEntity managerEntity = managerDao.selectByKey(mname);
		if(managerEntity!=null){
			Manager bo = new Manager();
			BeanUtils.copyProperties(managerEntity, bo);
			return Optional.of(bo);
		}
		return Optional.empty();
	}

	@Override
	public boolean refreshPassword(String id, String password)
			throws DataRefreshingException {
		try {
			return managerDao.updatePassword(id, password);
		} catch (Exception e) {
			throw new DataRefreshingException(e);
		}
	}


}
