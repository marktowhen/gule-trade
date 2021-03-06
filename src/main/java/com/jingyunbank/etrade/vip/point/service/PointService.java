package com.jingyunbank.etrade.vip.point.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.vip.point.bo.Point;
import com.jingyunbank.etrade.api.vip.point.service.IPointService;
import com.jingyunbank.etrade.vip.point.dao.PointDao;
import com.jingyunbank.etrade.vip.point.entity.PointEntity;

@Service("pointService")
public class PointService implements IPointService {

	@Autowired
	private PointDao pointDao;
	
	@Override
	public boolean save(Point point) throws DataSavingException {
		
		PointEntity entity = new PointEntity();
		BeanUtils.copyProperties(point, entity);
		try {
			return pointDao.insert(entity);
		} catch (Exception e) {
			throw new DataSavingException(e);
		}
	}

	@Override
	public boolean refresh(String uid, int point) throws DataRefreshingException {
		try {
			return pointDao.update(uid, point);
		} catch (Exception e) {
			throw new DataRefreshingException(e);
		}
	}

	@Override
	public boolean addPoint(String uid, int point) throws DataRefreshingException {
		try {
			return pointDao.calculatePoint(uid, point);
		} catch (Exception e) {
			throw new DataRefreshingException(e);
		}
	}

	@Override
	public boolean minusPoint(String uid, int point) throws DataRefreshingException {
		try {
			return pointDao.calculatePoint(uid, -point);
		} catch (Exception e) {
			throw new DataRefreshingException(e);
		}
	}

	@Override
	public int getPoint(String uid) {
		PointEntity entity = pointDao.select(uid);
		if(entity!=null){
			return entity.getPoint();
		}
		return 0;
	}

	@Override
	public Point get(String uid) {
		PointEntity entity = pointDao.select(uid);
		if(entity != null){
			Point bo = new Point();
			BeanUtils.copyProperties(entity, bo);
			return bo;
		}
		return null;
	}

}
