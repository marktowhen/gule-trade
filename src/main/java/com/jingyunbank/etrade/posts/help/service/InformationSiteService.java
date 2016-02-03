package com.jingyunbank.etrade.posts.help.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.jingyunbank.core.Range;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.posts.help.bo.InformationSite;
import com.jingyunbank.etrade.api.posts.help.service.IInformationSiteService;
import com.jingyunbank.etrade.config.CacheConfig;
import com.jingyunbank.etrade.posts.help.dao.InformationSiteDao;
import com.jingyunbank.etrade.posts.help.entity.InformationSiteEntity;
@Service
public class InformationSiteService implements IInformationSiteService{
	
	@Autowired
	private InformationSiteDao informationSiteDao;
	//添加多个标题信息
	@Override
	@CacheEvict(cacheNames = "informationSiteCache", allEntries=true)
	public boolean save(InformationSite informationSite) throws DataSavingException {
		boolean flag;
		// TODO Auto-generated method stub
		InformationSiteEntity informationSiteEntity=new InformationSiteEntity();
		BeanUtils.copyProperties(informationSite, informationSiteEntity);
		try {
			if(informationSiteDao.insert(informationSiteEntity)){
				flag=true;
			}else{
				flag=false;
			}
		} catch (Exception e) {
			throw new DataSavingException(e);
		}
		return flag;
	}

	@Override
	@Cacheable(cacheNames = "informationSiteCache", keyGenerator = CacheConfig.CUSTOM_CACHE_KEY_GENERATOR)
	public List<InformationSite> list(String informationID, Range range) {
		
		return informationSiteDao.select(informationID, range.getFrom(),range.getTo()-range.getFrom())
				.stream().map(entity ->{
					InformationSite bo=new InformationSite();
					BeanUtils.copyProperties(entity, bo);
					return bo;
					
				}).collect(Collectors.toList());
	}
	@Override
	@Cacheable(cacheNames = "informationSiteCache", keyGenerator = CacheConfig.CUSTOM_CACHE_KEY_GENERATOR)
	public List<InformationSite> list(String informationID) {
		return informationSiteDao.selectSites(informationID)
				.stream().map(entity ->{
					InformationSite bo=new InformationSite();
					BeanUtils.copyProperties(entity, bo);
					return bo;
					
				}).collect(Collectors.toList());
	}

}
