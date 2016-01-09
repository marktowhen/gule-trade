package com.jingyunbank.etrade.user.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.exception.DataRemovingException;
import com.jingyunbank.etrade.api.user.bo.ManagerRole;
import com.jingyunbank.etrade.api.user.service.IManagerRoleService;
import com.jingyunbank.etrade.user.dao.UserRoleDao;
import com.jingyunbank.etrade.user.entity.ManagerRoleEntity;

@Service("userRoleService")
public class ManagerRoleService implements IManagerRoleService {

	@Autowired
	private UserRoleDao userRoleDao;
	
	@Override
	public List<ManagerRole> list(String uid) {
		return userRoleDao.selectList(uid).stream().map( resultEntity ->{
			ManagerRole c = new ManagerRole();
			BeanUtils.copyProperties(resultEntity, c);
			BeanUtils.copyProperties(resultEntity.getRole(), c.getRole());
			return c;
		}).collect(Collectors.toList());
	}

	@Override
	public ManagerRole single(String id) {
		ManagerRoleEntity entity = userRoleDao.selectOne(id);
		if(entity!=null){
			ManagerRole r = new ManagerRole();
			BeanUtils.copyProperties(entity, r);
			BeanUtils.copyProperties(entity.getRole(), r.getRole());
			return r;
		}
		return null;
	}

	@Override
	public boolean delete(String[] ids) throws DataRemovingException {
		return userRoleDao.updateValid(false, ids);
	}

	@Override
	public boolean isAuthoritative(String uid, String roleCode) {
		ManagerRoleEntity userRoleEntity = userRoleDao.selectByUidAndCode(uid, roleCode);
		if(userRoleEntity!=null){
			return true;
		}
		return false;
	}

}
