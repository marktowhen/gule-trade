package com.jingyunbank.etrade.pay.service;

import java.util.ArrayList;
import java.util.List;



import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import com.jingyunbank.etrade.api.pay.bo.PayPlatform;
import com.jingyunbank.etrade.api.pay.service.IPayPlatformService;
import com.jingyunbank.etrade.pay.dao.PayPlatformDao;

@Service("payPlatformService")
public class PayPlatformService implements IPayPlatformService {

	@Autowired
	private PayPlatformDao payPlatformDao;
	
	@Override
	public List<PayPlatform> list() {
		List<PayPlatform> pfs = new ArrayList<PayPlatform>();
		payPlatformDao.selectAll().forEach(x->{
			PayPlatform ppf = new PayPlatform();
			BeanUtils.copyProperties(x, ppf);
			pfs.add(ppf);
		});
		return pfs;
	}

}
