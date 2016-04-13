package com.jingyunbank.etrade.logistic.service.context;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.logistic.bo.Postage;
import com.jingyunbank.etrade.api.logistic.bo.PostageCalculate;
import com.jingyunbank.etrade.api.logistic.bo.PostageDetail;
import com.jingyunbank.etrade.api.logistic.service.context.IPostageCalculateRuleService;

@Service("postageCalculateNumberService")
public class PostageCalculateRuleNumberService implements IPostageCalculateRuleService {

	@Override
	public boolean matches(Postage calculate,
			PostageDetail postageDetail) {
		
		return Postage.TYPE_NUMBER.equals(calculate.getType());
	}

	@Override
	public BigDecimal calculateFirstCost(PostageDetail postageDetail) {
		if(postageDetail.isFree()){
			return BigDecimal.ZERO;
		}
		return postageDetail.getFirstCost();
	}

	@Override
	public BigDecimal calculateNextCost(PostageCalculate calculate,
			PostageDetail postageDetail) {
		if(postageDetail.isFree()){
			return BigDecimal.ZERO;
		}
		if(calculate.getNumber() > postageDetail.getFirstNumber()){
			//续件数量
			int last = calculate.getNumber() - postageDetail.getFirstNumber();
			//续件倍数
			int multi =  last%postageDetail.getNextNumber()==0 
							?  last/postageDetail.getNextNumber() 
							:  (last+last%postageDetail.getNextNumber())/postageDetail.getNextNumber();
			return postageDetail.getNextCost().multiply(BigDecimal.valueOf(multi));
		}
		return BigDecimal.ZERO;
	}

}
