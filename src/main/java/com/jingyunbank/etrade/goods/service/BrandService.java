package com.jingyunbank.etrade.goods.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.goods.bo.Brand;
import com.jingyunbank.etrade.api.goods.service.IBrandService;
import com.jingyunbank.etrade.config.CacheConfig;
import com.jingyunbank.etrade.goods.dao.BrandDao;
import com.jingyunbank.etrade.goods.entity.GoodsBrandEntity;

@Service("brandService")
public class BrandService implements IBrandService {
	@Resource
	private BrandDao brandDao;

	@Override
	@CacheEvict(value="brandCache",allEntries=true)
	public boolean save(Brand brand) throws Exception {
		// TODO Auto-generated method stub
		GoodsBrandEntity entity = new GoodsBrandEntity();
		BeanUtils.copyProperties(brand, entity);
		int i = brandDao.insertBrand(entity);
		if (i > 0) {
			return true;
		}
		return false;
	}

	@Override
	
	public Optional<Brand> singleById(String bid) throws Exception {
		GoodsBrandEntity entity = brandDao.selectOne(bid);
		Brand brand = null;
		if (Objects.nonNull(entity)) {
			brand = new Brand();
			BeanUtils.copyProperties(entity, brand);
		}
		return Optional.ofNullable(brand);
	}

	@Override
	@CacheEvict(value="brandCache",allEntries=true)
	public boolean refreshBrand(Brand brand) throws Exception {
		GoodsBrandEntity entity = new GoodsBrandEntity();
		BeanUtils.copyProperties(brand, entity);
		int i = brandDao.updateBrand(entity);
		if (i > 0) {
			return true;
		}
		return false;
	}

	@Override
	@Cacheable(cacheNames = "brandCache", keyGenerator = CacheConfig.CUSTOM_CACHE_KEY_GENERATOR)
	public List<Brand> listBrandsByMid(String mid) throws Exception {
		// TODO Auto-generated method stub
		List<Brand> brands = brandDao.selectbrand(mid).stream().map(dao -> {
			Brand bo = new Brand();
			BeanUtils.copyProperties(dao, bo);
			return bo;
		}).collect(Collectors.toList());
		return brands;
	}

	@Override
	@Cacheable(cacheNames = "brandCache", keyGenerator = CacheConfig.CUSTOM_CACHE_KEY_GENERATOR)
	public List<Brand> listBrands() throws Exception {
		// TODO Auto-generated method stub
		List<Brand> brands = brandDao.selectAllBrands().stream().map(dao -> {
			Brand bo = new Brand();
			BeanUtils.copyProperties(dao, bo);
			return bo;
		}).collect(Collectors.toList());
		return brands;
	}

	@Override
	@CacheEvict(value="brandCache",allEntries=true)
	public boolean delBrand(String bid) throws Exception {
		int i = brandDao.delBrand(bid);
		if (i > 0) {
			return true;
		}
		return false;
	}



}
