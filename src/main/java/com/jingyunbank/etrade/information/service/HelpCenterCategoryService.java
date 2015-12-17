package com.jingyunbank.etrade.information.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.core.Range;
import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.exception.DataRemovingException;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.information.bo.HelpCenterCategory;
import com.jingyunbank.etrade.api.information.service.IHelpCenterCategoryService;
import com.jingyunbank.etrade.information.dao.HelpCenterCategoryDao;
import com.jingyunbank.etrade.information.entity.HelpCenterCategoryEntity;

@Service("helpCenterCategoryService")
public class HelpCenterCategoryService implements IHelpCenterCategoryService {
	
	@Autowired
	private HelpCenterCategoryDao helpCenterCategoryDao;

	@Override
	public Optional<HelpCenterCategory> single(String id) {
		HelpCenterCategoryEntity single = helpCenterCategoryDao.selectSingle(id);
		if(single!=null){
			return Optional.of(getBoFromEntity(single));
		}
		return Optional.empty();
	}

	@Override
	public List<HelpCenterCategory> listAllValid(Range range) {
		return helpCenterCategoryDao.selectValidListPage(range.getFrom(), range.getTo()-range.getFrom()).stream()
			.map( entity ->{return getBoFromEntity(entity);}).collect(Collectors.toList());
	}
	
	@Override
	public List<HelpCenterCategory> listAllValid() {
		return helpCenterCategoryDao.selectValidList().stream()
			.map( entity ->{return getBoFromEntity(entity);}).collect(Collectors.toList());
	}

	@Override
	public boolean save(HelpCenterCategory helpCenterCategory)
			throws DataSavingException {
		try {
			return helpCenterCategoryDao.insert(getEntityFromBo(helpCenterCategory));
		} catch (Exception e) {
			throw new DataSavingException(e);
		}
	}

	@Override
	public boolean refresh(HelpCenterCategory helpCenterCategory)
			throws DataRefreshingException {
		try {
			return helpCenterCategoryDao.update(getEntityFromBo(helpCenterCategory));
		} catch (Exception e) {
			throw new DataRefreshingException(e);
		}
	}

	@Override
	public boolean remove(String id) throws DataRemovingException {
		try {
			return helpCenterCategoryDao.updateValid(id, false);
		} catch (Exception e) {
			throw new DataRemovingException(e);
		}
	}
	
	private HelpCenterCategory getBoFromEntity(HelpCenterCategoryEntity entity){
		if(entity!=null){
			HelpCenterCategory bo = new HelpCenterCategory();
			BeanUtils.copyProperties(entity, bo);
			return bo;
		}
		return null;
	}
	
	private HelpCenterCategoryEntity getEntityFromBo(HelpCenterCategory bo){
		if(bo!=null){
			HelpCenterCategoryEntity entity = new HelpCenterCategoryEntity();
			BeanUtils.copyProperties(bo, entity);
			return entity;
		}
		return null;
		
	}

}
