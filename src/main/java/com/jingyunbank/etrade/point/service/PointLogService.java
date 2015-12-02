package com.jingyunbank.etrade.point.service;

import java.util.List;
import java.util.stream.Collectors;




import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;





import com.jingyunbank.core.Range;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.point.bo.PointLog;
import com.jingyunbank.etrade.api.point.service.IPointLogService;
import com.jingyunbank.etrade.point.dao.PointLogDao;
import com.jingyunbank.etrade.point.entity.PointLogEntity;

@Service("pointLogService")
public class PointLogService implements IPointLogService {
	
	@Autowired
	private PointLogDao pointLogDao;

	@Override
	public List<PointLog> list(String uid) {
		
		return pointLogDao.select(uid).stream().map( entity ->{
			PointLog bo = new PointLog();
			BeanUtils.copyProperties(entity, bo);
			return bo;
		}).collect(Collectors.toList());
	}

	@Override
	public List<PointLog> list(String uid, Range range) {
		return pointLogDao.selectList(uid, range.getFrom(), range.getTo()-range.getFrom()).stream().map( entity ->{
			PointLog bo = new PointLog();
			BeanUtils.copyProperties(entity, bo);
			return bo;
		}).collect(Collectors.toList());
	}

	@Override
	public boolean save(PointLog pointLog) throws DataSavingException {
		PointLogEntity entity = new PointLogEntity();
		BeanUtils.copyProperties(pointLog, entity);
		try {
			return pointLogDao.insert(entity);
		} catch (Exception e) {
			throw new DataSavingException(e);
		}
	}

}
