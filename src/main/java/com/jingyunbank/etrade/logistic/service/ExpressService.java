package com.jingyunbank.etrade.logistic.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.logistic.bo.Express;
import com.jingyunbank.etrade.api.logistic.service.IExpressService;
import com.jingyunbank.etrade.logistic.dao.ExpressDao;

@Service("expressService")
public class ExpressService implements IExpressService {
	@Resource
	private ExpressDao expressDao;

	@Override
	public List<Express> listExpress() throws Exception {
		List<Express> list = expressDao.selectExpress().stream().map(dao -> {
			Express bo = new Express();
			BeanUtils.copyProperties(dao, bo);
			return bo;
		}).collect(Collectors.toList());
		return list;
	}

}
