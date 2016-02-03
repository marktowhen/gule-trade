package com.jingyunbank.etrade.posts.links.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;


import com.jingyunbank.etrade.api.posts.links.bo.Link;
import com.jingyunbank.etrade.api.posts.links.service.ILinkService;
import com.jingyunbank.etrade.config.CacheConfig;
import com.jingyunbank.etrade.posts.links.dao.LinkDao;
import com.jingyunbank.etrade.posts.links.entity.LinkEntity;

@Service("linkService")
public class LinkService implements ILinkService {
	@Resource
	private LinkDao linkDao;

	@Override
	@CacheEvict(value="linkCache",allEntries=true)
	public boolean save(Link bo) throws Exception {
		boolean result = false;
		LinkEntity entity = new LinkEntity();
		BeanUtils.copyProperties(bo, entity);
		result = linkDao.insert(entity);
		return result;
	}

	@Override
	@CacheEvict(value="linkCache",allEntries=true)
	public boolean refresh(Link bo) throws Exception {
		boolean result = false;
		LinkEntity entity = new LinkEntity();
		BeanUtils.copyProperties(bo, entity);
		result = linkDao.update(entity);
		return result;
	}

	@Override
	@CacheEvict(value="linkCache",allEntries=true)
	public boolean remove(String id) throws Exception {
		boolean result = false;
		result = linkDao.delete(id);
		return result;
	}

	@Override
	@Cacheable(cacheNames = "linkCache", keyGenerator = CacheConfig.CUSTOM_CACHE_KEY_GENERATOR)
	public List<Link> listLinks() throws Exception {
		List<Link> list = linkDao.select().stream().map(dao -> {
			Link bo = new Link();
			BeanUtils.copyProperties(dao, bo);
			return bo;
		}).collect(Collectors.toList());
		return list;
	}

	@Override
	public Optional<Link> singByID(String id) throws Exception {
		LinkEntity entity = linkDao.selectOne(id);
		Link link = null;
		if (Objects.nonNull(entity)) {
			link = new Link();
			BeanUtils.copyProperties(entity, link);
		}
		return Optional.ofNullable(link);
	}
	
	

}
