package com.jingyunbank.etrade.goods.service;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.goods.bo.BaseGoodsOperation;
import com.jingyunbank.etrade.api.goods.bo.Brand;
import com.jingyunbank.etrade.api.goods.service.IBrandService;
import com.jingyunbank.etrade.goods.dao.BrandDao;
import com.jingyunbank.etrade.goods.entity.GoodsBrandEntity;

@Service("brandService")
public class BrandService implements IBrandService {
	@Resource
	private BrandDao brandDao;

	@Override
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
	public List<Brand> listBrandsByMid(String mid) throws Exception {
		// TODO Auto-generated method stub
		List<Brand> brands = brandDao.selectbrand(mid).stream().map(dao -> {
			Brand bo = new Brand();
			BeanUtils.copyProperties(dao, bo);
			return bo;
		}).collect(Collectors.toList());
		return brands;
	}



}
