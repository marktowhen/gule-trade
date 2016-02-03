package com.jingyunbank.etrade.posts.help.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jingyunbank.core.Range;
import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.exception.DataRemovingException;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.posts.help.bo.HelpCenterCategory;
import com.jingyunbank.etrade.api.posts.help.service.IHelpCenterCategoryService;
import com.jingyunbank.etrade.api.posts.help.service.IHelpCenterDetailService;
import com.jingyunbank.etrade.config.CacheConfig;
import com.jingyunbank.etrade.posts.help.dao.HelpCenterCategoryDao;
import com.jingyunbank.etrade.posts.help.entity.HelpCenterCategoryEntity;


@Service("helpCenterCategoryService")
public class HelpCenterCategoryService implements IHelpCenterCategoryService {
	
	@Autowired
	private HelpCenterCategoryDao helpCenterCategoryDao;
	
	@Autowired
	private IHelpCenterDetailService helpCenterDetailService;

	@Override
	public Optional<HelpCenterCategory> single(String id) {
		HelpCenterCategoryEntity single = helpCenterCategoryDao.selectSingle(id);
		if(single!=null){
			return Optional.of(getBoFromEntity(single));
		}
		return Optional.empty();
	}

	@Override
	@Cacheable(cacheNames = "helpCenterCache", keyGenerator = CacheConfig.CUSTOM_CACHE_KEY_GENERATOR)
	public List<HelpCenterCategory> listAllValid(Range range) {
		
		List<HelpCenterCategoryEntity> listPage = helpCenterCategoryDao.selectValidListPage(range.getFrom(), range.getTo()-range.getFrom());
		if(listPage!=null && !listPage.isEmpty()){
			String [] ids = new String[listPage.size()];
			for (int i = 0; i < ids.length; i++) {
				ids[i] = listPage.get(i).getID();
			}
			return helpCenterCategoryDao.selectListByCondition(ids).stream()
					.map( entity ->{return getBoFromEntity(entity);}).collect(Collectors.toList());
		}
		return new ArrayList<HelpCenterCategory>();
		
	}
	
	@Override
	@Cacheable(cacheNames = "helpCenterCache", keyGenerator = CacheConfig.CUSTOM_CACHE_KEY_GENERATOR)
	public List<HelpCenterCategory> listAllValid() {
		return helpCenterCategoryDao.selectValidList().stream()
			.map( entity ->{return getBoFromEntity(entity);}).collect(Collectors.toList());
	}

	@Override
	@CacheEvict(cacheNames = "helpCenterCache", allEntries=true)
	public boolean save(HelpCenterCategory helpCenterCategory)
			throws DataSavingException {
		try {
			return helpCenterCategoryDao.insert(getEntityFromBo(helpCenterCategory));
		} catch (Exception e) {
			throw new DataSavingException(e);
		}
	}

	@Override
	@CacheEvict(cacheNames = "helpCenterCache", allEntries=true)
	public boolean refresh(HelpCenterCategory helpCenterCategory)
			throws DataRefreshingException {
		try {
			return helpCenterCategoryDao.update(getEntityFromBo(helpCenterCategory));
		} catch (Exception e) {
			throw new DataRefreshingException(e);
		}
	}

	@Override
	@Transactional
	@CacheEvict(cacheNames = "helpCenterCache", allEntries=true)
	public boolean remove(String id) throws DataRemovingException {
		try {
			helpCenterCategoryDao.updateValid(id, false);
			helpCenterDetailService.removeByCategory(id);
			return true;
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
