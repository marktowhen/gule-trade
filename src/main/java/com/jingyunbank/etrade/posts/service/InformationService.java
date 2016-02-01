package com.jingyunbank.etrade.posts.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.posts.bo.Information;
import com.jingyunbank.etrade.api.posts.service.IInformationService;
import com.jingyunbank.etrade.config.CacheConfig;
import com.jingyunbank.etrade.posts.dao.InformationDao;
import com.jingyunbank.etrade.posts.entity.InformationEntity;
@Service
public class InformationService implements IInformationService{
	
	
	@Autowired
	private InformationDao informationDao;
	
	//插入新的资讯标题
	@Override
	@CacheEvict(cacheNames = "informationCache", allEntries=true)
	public boolean save(Information information) throws DataSavingException {
		boolean flag;
		
		InformationEntity informationEntity=new InformationEntity();
		BeanUtils.copyProperties(information, informationEntity);
		try {
			if(informationDao.insert(informationEntity)){
				flag=true;
			}else{
				flag=false;
			}
		} catch (Exception e) {
			throw new DataSavingException(e);
		}
		return flag;
	}
	//查出所有的资讯大标题
	@Override
	@Cacheable(cacheNames = "informationCache", keyGenerator = CacheConfig.CUSTOM_CACHE_KEY_GENERATOR)
	public List<Information> getInformation() {
		
		return informationDao.selectList().stream().map(entity -> {
			Information information=new Information();
			BeanUtils.copyProperties(entity, information);
			return information;
		}).collect(Collectors.toList());
	}

}
