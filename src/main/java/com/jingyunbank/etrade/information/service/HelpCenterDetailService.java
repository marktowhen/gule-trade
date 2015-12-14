package com.jingyunbank.etrade.information.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.exception.DataRemovingException;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.information.bo.HelpCenterDetail;
import com.jingyunbank.etrade.api.information.service.IHelpCenterDetailService;
import com.jingyunbank.etrade.information.dao.HelpCenterDetailDao;
import com.jingyunbank.etrade.information.entity.HelpCenterDetailEntity;

@Service("helpCenterDetailService")
public class HelpCenterDetailService implements IHelpCenterDetailService {
	
	@Autowired
	private HelpCenterDetailDao helpCenterDetailDao;

	@Override
	public Optional<HelpCenterDetail> single(String id) {
		HelpCenterDetailEntity entity = helpCenterDetailDao.selectSingle(id);
		if(entity!=null){
			return Optional.of(getBoFromEntity(entity));
		}
		return Optional.empty();
	}

	@Override
	public List<HelpCenterDetail> listAllValid(String categoryID) {
		return helpCenterDetailDao.selectValidList(categoryID).stream()
			.map( entity->{
						return getBoFromEntity(entity);
					}).collect(Collectors.toList());
	}

	@Override
	public boolean save(HelpCenterDetail helpCenterDetail)
			throws DataSavingException {
		try {
			return helpCenterDetailDao.insert(getEntityFromBo(helpCenterDetail));
		} catch (Exception e) {
			throw new DataSavingException(e);
		}
	}

	@Override
	public boolean refresh(HelpCenterDetail helpCenterDetail)
			throws DataRefreshingException {
		try {
			return helpCenterDetailDao.update(getEntityFromBo(helpCenterDetail));
		} catch (Exception e) {
			throw new DataRefreshingException(e);
		}
	}

	@Override
	public boolean remove(String id) throws DataRemovingException {
		try {
			return helpCenterDetailDao.updateValid(id, false);
		} catch (Exception e) {
			throw new DataRemovingException(e);
		}
	}
	
	private HelpCenterDetail getBoFromEntity(HelpCenterDetailEntity entity){
		if(entity!=null){
			HelpCenterDetail bo = new HelpCenterDetail();
			BeanUtils.copyProperties(entity, bo);
			return bo;
		}
		return null;
	}
	
	private HelpCenterDetailEntity getEntityFromBo(HelpCenterDetail bo){
		if(bo!=null){
			HelpCenterDetailEntity entity = new HelpCenterDetailEntity();
			BeanUtils.copyProperties(bo, entity);
			return entity;
		}
		return null;
	}

}
