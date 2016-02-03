package com.jingyunbank.etrade.posts.banner.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.jingyunbank.core.Range;
import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.exception.DataRemovingException;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.posts.banner.bo.Banner;
import com.jingyunbank.etrade.api.posts.banner.service.IBannerService;
import com.jingyunbank.etrade.config.CacheConfig;
import com.jingyunbank.etrade.posts.banner.dao.BannerDao;
import com.jingyunbank.etrade.posts.banner.entity.BannerEntity;

@Service("bannerService")
public class BannerService implements IBannerService {
	
	@Autowired
	private BannerDao bannerDao;

	@Override
	@CacheEvict(cacheNames="bannerCache" ,allEntries=true)
	public boolean save(Banner banner) throws DataSavingException {
		BannerEntity entity = new BannerEntity();
		BeanUtils.copyProperties(banner, entity);
		try {
			return bannerDao.insert(entity);
		} catch (Exception e) {
			throw new DataSavingException(e);
		}
	}

	@Override
	@CacheEvict(cacheNames="bannerCache" ,allEntries=true)
	public boolean refresh(Banner banner) throws DataRefreshingException {
		BannerEntity entity = new BannerEntity();
		BeanUtils.copyProperties(banner, entity);
		try {
			return bannerDao.update(entity);
		} catch (Exception e) {
			throw new DataRefreshingException(e);
		}
	}

	@Override
	@CacheEvict(cacheNames="bannerCache" ,allEntries=true)
	public boolean refresh(String id, int order) throws DataRefreshingException {
		Banner banner = new Banner();
		banner.setID(id);
		banner.setOrder(order);
		return refresh(banner);
	}
	
	@Override
	@CacheEvict(cacheNames="bannerCache" ,allEntries=true)
	public boolean remove(String id) throws DataRemovingException {
		try {
			return bannerDao.updateValidStatus(id, false);
		} catch (Exception e) {
			throw new DataRemovingException(e);
		}
	}

	@Override
	@Cacheable(cacheNames = "bannerCache", keyGenerator = CacheConfig.CUSTOM_CACHE_KEY_GENERATOR)
	public List<Banner> list(String type) {
		return bannerDao.select(type).stream()
				.map( entity->{
					Banner bo = new Banner();
					BeanUtils.copyProperties(entity, bo);
					return bo;
				}).collect(Collectors.toList());
	}
	
	@Override
	@Cacheable(cacheNames = "bannerCache", keyGenerator = CacheConfig.CUSTOM_CACHE_KEY_GENERATOR)
	public List<Banner> list(String type, Range range) {
		return bannerDao.selectRange(type, range.getFrom(), range.getTo()-range.getFrom()).stream()
				.map( entity->{
					Banner bo = new Banner();
					BeanUtils.copyProperties(entity, bo);
					return bo;
				}).collect(Collectors.toList());
	}

	@Override
	@Cacheable(cacheNames = "bannerCache", keyGenerator = CacheConfig.CUSTOM_CACHE_KEY_GENERATOR)
	public Banner single(String id) {
		BannerEntity entity = bannerDao.selectSingle(id);
		if(Objects.nonNull(entity)){
			Banner bo = new Banner();
			BeanUtils.copyProperties(entity, bo);
			return bo;
		}
		return null;
	}

	@Override
	@Cacheable(cacheNames = "bannerCache", keyGenerator = CacheConfig.CUSTOM_CACHE_KEY_GENERATOR)
	public int count(String type) {
		return bannerDao.count(type);
	}

	

	

}
