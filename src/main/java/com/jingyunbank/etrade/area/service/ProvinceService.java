package com.jingyunbank.etrade.area.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
	@CacheEvict(cacheNames = "provinceCache" , allEntries=true)
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
	@CacheEvict(cacheNames = "provinceCache" , allEntries=true)
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
	@CacheEvict(cacheNames = "provinceCache" , allEntries=true)
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
	@Cacheable(cacheNames = "provinceCache", keyGenerator = "CustomKG")
	public List<Province> list(Province bo, Range range) {
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
	@Cacheable(cacheNames = "provinceCache", keyGenerator = "CustomKG")
	public List<Province> list(Province bo) {
		return list(bo, null);
	}

	@Override
	@Cacheable(cacheNames = "provinceCache", keyGenerator = "CustomKG")
	public Province single(int id) {
		Province bo = new Province();
		bo.setProvinceID(id);
		List<Province> list = list(bo);
		if(list!=null && !list.isEmpty()){
			return list.get(0);
		}
		return null;
	}

	@Override
	@Cacheable(cacheNames = "provinceCache", keyGenerator = "CustomKG")
	public List<Province> listByCountry(int countryID) {
		Province p = new Province();
		p.setCountryID(countryID);
		return this.list(p);
	}

	@Override
	@Cacheable(cacheNames = "provinceCache", keyGenerator = "CustomKG")
	public boolean isFaraway(int provinceID) {
		Province province = single(provinceID);
		if(province!=null){
			return province.isFaraway();
		}
		return true;
	}

}
