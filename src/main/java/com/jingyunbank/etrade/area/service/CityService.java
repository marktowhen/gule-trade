package com.jingyunbank.etrade.area.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.jingyunbank.core.Range;
import com.jingyunbank.etrade.api.area.bo.City;
import com.jingyunbank.etrade.api.area.service.ICityService;
import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.exception.DataRemovingException;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.area.dao.CityDao;
import com.jingyunbank.etrade.area.entity.CityEntity;
import com.jingyunbank.etrade.config.CacheConfig;

@Service("cityService")
public class CityService implements ICityService {
	
	@Autowired
	private CityDao cityDao;

	@Override
	@CacheEvict(cacheNames="cityCache", allEntries=true)
	public boolean save(City bo) throws DataSavingException {
		CityEntity entity = new CityEntity();
		BeanUtils.copyProperties(bo, entity);
		
		try {
			return cityDao.insert(entity);
		} catch (Exception e) {
			throw new DataSavingException(e);
		}
	}

	@Override
	@CacheEvict(cacheNames="cityCache", allEntries=true)
	public boolean remove(int id) throws DataRemovingException {
		CityEntity entity = new CityEntity();
		entity.setCityID(id);
		
		try {
			return cityDao.delete(entity);
		} catch (Exception e) {
			throw new DataRemovingException(e);
		}
	}

	@Override
	@CacheEvict(cacheNames="cityCache", allEntries=true)
	public boolean refresh(City bo) throws DataRefreshingException {
		CityEntity entity = new CityEntity();
		BeanUtils.copyProperties(bo, entity);
		try {
			return cityDao.update(entity);
		} catch (Exception e) {
			throw new DataRefreshingException(e);
		}
	}

	@Override
	@Cacheable(cacheNames = "cityCache", keyGenerator = CacheConfig.CUSTOM_CACHE_KEY_GENERATOR)
	public List<City> list(City bo, Range range) {
		CityEntity entity = new CityEntity();
		BeanUtils.copyProperties(bo, entity);
		if(range!=null){
			entity.setOffset(range.getFrom());
			entity.setSize(range.getTo()-range.getFrom());
		}
		return cityDao.select(entity).stream().map( resultEntity ->{
			City c = new City();
			BeanUtils.copyProperties(resultEntity, c);
			return c;
		}).collect(Collectors.toList());
	}

	@Override
	@Cacheable(cacheNames = "cityCache", keyGenerator = CacheConfig.CUSTOM_CACHE_KEY_GENERATOR)
	public List<City> list(City bo) {
		return list(bo, null);
	}

	@Override
	@Cacheable(cacheNames = "cityCache", keyGenerator = CacheConfig.CUSTOM_CACHE_KEY_GENERATOR)
	public City single(int id) {
		City bo = new City();
		bo.setCityID(id);
		List<City> list = list(bo);
		if(list!=null && !list.isEmpty()){
			return list.get(0);
		}
		return null;
	}

	@Override
	@Cacheable(cacheNames = "cityCache", keyGenerator = CacheConfig.CUSTOM_CACHE_KEY_GENERATOR)
	public List<City> listByProvince(int provinceID) {
		City c = new City();
		c.setProvinceID(provinceID);
		return this.list(c);
	}


}
