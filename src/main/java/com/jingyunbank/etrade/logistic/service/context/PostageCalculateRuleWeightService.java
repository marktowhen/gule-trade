package com.jingyunbank.etrade.logistic.service.context;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.logistic.bo.Postage;
import com.jingyunbank.etrade.api.logistic.bo.PostageCalculate;
import com.jingyunbank.etrade.api.logistic.bo.PostageDetail;
import com.jingyunbank.etrade.api.logistic.service.context.IPostageCalculateRuleService;

@Service("postageCalculateWeightService")
public class PostageCalculateRuleWeightService implements IPostageCalculateRuleService {

	@Override
	public boolean matches(Postage calculate,
			PostageDetail postageDetail) {
		
		return Postage.TYPE_VOLUME.equals(calculate.getType());
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
		if(calculate.getWeight() > postageDetail.getFirstWeight()){
			//续件数量
			double last = calculate.getWeight() - postageDetail.getFirstWeight();
			//续件倍数
			int multi =  (int) (last%postageDetail.getNextWeight()==0 
							?  last/postageDetail.getNextWeight() 
							:  (last+last%postageDetail.getNextWeight())/postageDetail.getNextWeight());
			return postageDetail.getNextCost().multiply(BigDecimal.valueOf(multi));
		}
		return BigDecimal.ZERO;
	}

}
