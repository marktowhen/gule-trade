package com.jingyunbank.etrade.user.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.exception.DataRemovingException;
import com.jingyunbank.etrade.api.user.bo.UserRole;
import com.jingyunbank.etrade.api.user.service.IUserRoleService;
import com.jingyunbank.etrade.user.dao.UserRoleDao;
import com.jingyunbank.etrade.user.entity.UserRoleEntity;

@Service("userRoleService")
public class UserRoleService implements IUserRoleService {

	@Autowired
	private UserRoleDao userRoleDao;
	
	@Override
	public List<UserRole> list(String uid) {
		return userRoleDao.selectList(uid).stream().map( resultEntity ->{
			UserRole c = new UserRole();
			BeanUtils.copyProperties(resultEntity, c);
			BeanUtils.copyProperties(resultEntity.getRole(), c.getRole());
			return c;
		}).collect(Collectors.toList());
	}

	@Override
	public UserRole single(String id) {
		UserRoleEntity entity = userRoleDao.selectOne(id);
		if(entity!=null){
			UserRole r = new UserRole();
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
		UserRoleEntity userRoleEntity = userRoleDao.selectByUidAndCode(uid, roleCode);
		if(userRoleEntity!=null){
			return true;
		}
		return false;
	}

}
