package com.jingyunbank.etrade.logistic.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.logistic.bo.Postage;
import com.jingyunbank.etrade.api.logistic.service.IPostageService;
import com.jingyunbank.etrade.api.statics.area.service.IProvinceService;

@Service("postageService")
public class PostageService implements IPostageService {

	@Autowired
	private IProvinceService provinceService;
	
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

}
