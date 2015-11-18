package com.jingyunbank.etrade.area.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.core.Range;
import com.jingyunbank.etrade.api.area.bo.Province;
import com.jingyunbank.etrade.api.area.service.IProvinceService;
import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.exception.DataRemovingException;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.area.dao.ProvinceDao;
import com.jingyunbank.etrade.area.entity.ProvinceEntity;

@Service("provinceService")
public class ProvinceService implements IProvinceService {
	
	@Autowired
	private ProvinceDao provinceDao;

	@Override
	public boolean save(Province bo) throws DataSavingException {
		ProvinceEntity entity = new ProvinceEntity();
		BeanUtils.copyProperties(bo, entity);
		
		try {
			return provinceDao.insert(entity);
		} catch (Exception e) {
			throw new DataSavingException(e);
		}
	}

	@Override
	public boolean remove(int id) throws DataRemovingException {
		ProvinceEntity entity = new ProvinceEntity();
		entity.setProvinceID(id);
		
		try {
			return provinceDao.delete(entity);
		} catch (Exception e) {
			throw new DataRemovingException(e);
		}
	}

	@Override
	public boolean refresh(Province bo) throws DataRefreshingException {
		ProvinceEntity entity = new ProvinceEntity();
		BeanUtils.copyProperties(bo, entity);
		try {
			return provinceDao.update(entity);
		} catch (Exception e) {
			throw new DataRefreshingException(e);
		}
	}

	@Override
	public List<Province> selectList(Province bo, Range range) {
		ProvinceEntity entity = new ProvinceEntity();
		BeanUtils.copyProperties(bo, entity);
		if(range!=null){
			entity.setOffset(range.getFrom());
			entity.setSize(range.getTo()-range.getFrom());
		}
		return provinceDao.select(entity).stream().map( resultEntity ->{
			Province c = new Province();
			BeanUtils.copyProperties(resultEntity, c);
			return c;
		}).collect(Collectors.toList());
	}

	@Override
	public List<Province> selectList(Province bo) {
		return selectList(bo, null);
	}

	@Override
	public Province selectSingle(int id) {
		Province bo = new Province();
		bo.setProvinceID(id);
		List<Province> list = selectList(bo);
		if(list!=null && !list.isEmpty()){
			return list.get(0);
		}
		return null;
	}

}
