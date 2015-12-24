package com.jingyunbank.etrade.vip.point.service.context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jingyunbank.core.KeyGen;
import com.jingyunbank.etrade.api.vip.point.bo.Point;
import com.jingyunbank.etrade.api.vip.point.bo.PointLog;
import com.jingyunbank.etrade.api.vip.point.service.IPointLogService;
import com.jingyunbank.etrade.api.vip.point.service.IPointService;
import com.jingyunbank.etrade.api.vip.point.service.context.IPointContextService;

@Service("pointContextService")
public class PointContextService implements IPointContextService{

	@Autowired
	private IPointService pointService;
	
	@Autowired
	private IPointLogService pointLogService;
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public boolean addPoint(String uid, int point, String remark) throws Exception {
		Point userPoint = pointService.get(uid);
		if(userPoint!=null){
			pointService.addPoint(uid, point);
		}else{
			userPoint = new Point();
			userPoint.setPoint(point);
			userPoint.setUID(uid);
			pointService.save(userPoint);
		}
		PointLog log = new PointLog();
		log.setID(KeyGen.uuid());
		log.setUID(uid);
		log.setPoint(point);
		log.setRemark(remark);
		pointLogService.save(log);
		return true;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public boolean minusPoint(String uid, int point, String remark) throws Exception{
		Point userPoint = pointService.get(uid);
		if(userPoint!=null && userPoint.getPoint()>=point){
			pointService.minusPoint(uid, point);
		}else{
			throw new Exception("用户积分不足");
		}
		PointLog log = new PointLog();
		log.setID(KeyGen.uuid());
		log.setUID(uid);
		log.setPoint(-point);
		log.setRemark(remark);
		pointLogService.save(log);
		return true;
	}

}
