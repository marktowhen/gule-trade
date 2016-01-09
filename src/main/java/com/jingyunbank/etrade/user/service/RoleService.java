package com.jingyunbank.etrade.user.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.user.bo.Role;
import com.jingyunbank.etrade.api.user.service.IRoleService;
import com.jingyunbank.etrade.user.dao.RoleDao;
import com.jingyunbank.etrade.user.entity.RoleEntity;

@Service("roleService")
public class RoleService implements IRoleService {

	@Autowired
	private RoleDao roleDao;
	
	@Override
	public List<Role> list() {
		return roleDao.selectList().stream().map( resultEntity ->{
			Role c = new Role();
			BeanUtils.copyProperties(resultEntity, c);
			return c;
		}).collect(Collectors.toList());
	}

	@Override
	public Role single(String idOrCode) {
		RoleEntity roleEntity = roleDao.selectOne(idOrCode);
		if(roleEntity!=null){
			Role c = new Role();
			BeanUtils.copyProperties(roleEntity, c);
			return c;
		}
		return null;
	}

}
