package com.jingyunbank.etrade.logistic.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.exception.DataRemovingException;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.logistic.bo.Postage;
import com.jingyunbank.etrade.api.logistic.service.IPostageDetailService;
import com.jingyunbank.etrade.api.logistic.service.IPostageService;
import com.jingyunbank.etrade.api.statics.area.service.IProvinceService;
import com.jingyunbank.etrade.logistic.dao.PostageDao;
import com.jingyunbank.etrade.logistic.entity.PostageEntity;

@Service("postageService")
public class PostageService implements IPostageService {

	@Autowired
	private IProvinceService provinceService;
	@Autowired
	private PostageDao postageDao;
	@Autowired
	private IPostageDetailService postageDetailService;
	
	private static final BigDecimal FULL_NON_POSTAGE_PRICE = BigDecimal.valueOf(199);
	private static final BigDecimal PART_NON_POSTAGE_PRICE = BigDecimal.valueOf(68);
	private static final BigDecimal DEFAULT_POSTAGE = BigDecimal.valueOf(10);
	
	@Override
	public BigDecimal calculate(BigDecimal orderprice, int provenceid) {
		
		if(Objects.isNull(orderprice) || orderprice.compareTo(BigDecimal.ZERO) == 0 ){
			return DEFAULT_POSTAGE;
		}
		//大约等于199包邮，无论地区
		if(orderprice.compareTo(FULL_NON_POSTAGE_PRICE) >= 0){
			return BigDecimal.ZERO;
		}
		//大于99，不是偏远地区包邮，否则10元
		if(orderprice.compareTo(PART_NON_POSTAGE_PRICE) >= 0){
			boolean isfaraway = provinceService.isFaraway(provenceid);
			return isfaraway? DEFAULT_POSTAGE : BigDecimal.ZERO;
		}
		return DEFAULT_POSTAGE;
		
	}

	@Override
	public List<Postage> calculate(List<Postage>  postages) {
		for (Postage postage : postages) {
			postage.setPostage(calculate(postage.getPrice(), postage.getProvince()));
		}
		return postages;
	}

	@Override
	public boolean save(Postage postage) throws DataSavingException {
		PostageEntity entity = new PostageEntity();
		BeanUtils.copyProperties(entity, postage);
		try {
			return postageDao.insert(entity);
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean refresh(Postage postage) throws DataRefreshingException {
		PostageEntity entity = new PostageEntity();
		BeanUtils.copyProperties(entity, postage);
		try {
			return postageDao.update(entity);
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	@Transactional
	public boolean remove(String ID) throws DataRemovingException {
		try {
			postageDao.updateStatus(ID, false);
			postageDetailService.removeByPostageID(ID);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public Postage single(String ID) {
		PostageEntity entity = postageDao.selectOne(ID);
		if(entity!=null){
			Postage bo = new Postage();
			BeanUtils.copyProperties(entity, bo);
			return bo;
		}
		return null;
	}

	@Override
	public List<Postage> list(String MID) {
		return postageDao.selectByMID(MID).stream().map( entity -> {
			Postage bo = new Postage();
			BeanUtils.copyProperties(entity, bo);
			return bo;
		}).collect(Collectors.toList());
	}

}
