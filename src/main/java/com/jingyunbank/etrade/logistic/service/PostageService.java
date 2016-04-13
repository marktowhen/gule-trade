package com.jingyunbank.etrade.logistic.service;

import java.util.List;
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
import com.jingyunbank.etrade.api.logistic.service.context.IPostageCalculateRuleService;
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
	@Autowired
	private List<IPostageCalculateRuleService> postageCalculateRuleServiceList;
	
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

	

	@Override
	public Postage singleWithDetail(String ID, int cityID) {
		Postage postage = this.single(ID);
		if(postage!=null){
			postage.setPostageDetail(postageDetailService.singleFit(ID, cityID));
		}
		return postage;
	}

	@Override
	public List<Postage> list(List<String> postageIDList) {
		return postageDao.selectMuti(postageIDList).stream().map( entity->{
			Postage bo = new Postage();
			BeanUtils.copyProperties(entity, bo);
			return bo;
		}).collect(Collectors.toList());
	}

	

}
