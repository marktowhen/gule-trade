package com.jingyunbank.etrade.pay.service;

import java.util.ArrayList;
import java.util.List;





import java.util.Objects;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;






import com.jingyunbank.etrade.api.pay.bo.PayPipeline;
import com.jingyunbank.etrade.api.pay.service.IPayPipelineService;
import com.jingyunbank.etrade.pay.dao.PayPipelineDao;
import com.jingyunbank.etrade.pay.entity.PayPipelineEntity;

@Service("payPipelineService")
public class PayPipelineService implements IPayPipelineService {

	@Autowired
	private PayPipelineDao payPipelineDao;
	
	@Override
	public List<PayPipeline> list() {
		List<PayPipeline> pfs = new ArrayList<PayPipeline>();
		payPipelineDao.selectAll().forEach(x->{
			PayPipeline ppf = new PayPipeline();
			BeanUtils.copyProperties(x, ppf);
			pfs.add(ppf);
		});
		return pfs;
	}

	@Override
	public PayPipeline single(String pipecode) {
		PayPipeline bo = new PayPipeline();
		PayPipelineEntity entity = payPipelineDao.selectOne(pipecode);
		if(Objects.nonNull(entity))
			BeanUtils.copyProperties(entity, bo);
		return bo;
	}

}
