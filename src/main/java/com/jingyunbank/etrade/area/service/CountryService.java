package com.jingyunbank.etrade.area.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.core.Range;
import com.jingyunbank.etrade.api.area.bo.Country;
import com.jingyunbank.etrade.api.area.service.ICountryService;
import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.exception.DataRemovingException;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.area.dao.CountryDao;
import com.jingyunbank.etrade.area.entity.CountryEntity;

@Service("countryService")
public class CountryService implements ICountryService {
	
	@Autowired
	private CountryDao countryDao;

	@Override
	public boolean save(Country bo) throws DataSavingException {
		CountryEntity entity = new CountryEntity();
		BeanUtils.copyProperties(bo, entity);
		
		try {
			return countryDao.insert(entity);
		} catch (Exception e) {
			throw new DataSavingException(e);
		}
	}

	@Override
	public boolean remove(int id) throws DataRemovingException {
		CountryEntity entity = new CountryEntity();
		entity.setCountryID(id);
		
		try {
			return countryDao.delete(entity);
		} catch (Exception e) {
			throw new DataRemovingException(e);
		}
	}

	@Override
	public boolean refresh(Country bo) throws DataRefreshingException {
		CountryEntity entity = new CountryEntity();
		BeanUtils.copyProperties(bo, entity);
		try {
			return countryDao.update(entity);
		} catch (Exception e) {
			throw new DataRefreshingException(e);
		}
	}

	@Override
	public List<Country> selectList(Country bo, Range range) {
		CountryEntity entity = new CountryEntity();
		BeanUtils.copyProperties(bo, entity);
		if(range!=null){
			entity.setOffset(range.getFrom());
			entity.setSize(range.getTo()-range.getFrom());
		}
		return countryDao.select(entity).stream().map( resultEntity ->{
			Country c = new Country();
			BeanUtils.copyProperties(resultEntity, c);
			return c;
		}).collect(Collectors.toList());
	}

	@Override
	public List<Country> selectList(Country bo) {
		return selectList(bo, null);
	}

	@Override
	public Country selectSingle(int id) {
		Country bo = new Country();
		bo.setCountryID(id);
		List<Country> list = selectList(bo);
		if(list!=null && !list.isEmpty()){
			return list.get(0);
		}
		return null;
	}

}
