package com.jingyunbank.etrade.logistic.service.context;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.logistic.bo.Postage;
import com.jingyunbank.etrade.api.logistic.bo.PostageCalculate;
import com.jingyunbank.etrade.api.logistic.bo.PostageDetail;
import com.jingyunbank.etrade.api.logistic.service.context.IPostageCalculateRuleService;

@Service("postageCalculateVolumeService")
public class PostageCalculateRuleVolumeService implements IPostageCalculateRuleService {

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
		if(calculate.getVolume() > postageDetail.getFirstVolume()){
			//续件数量
			double last = calculate.getVolume() - postageDetail.getFirstVolume();
			//续件倍数
			int multi =  (int) (last%postageDetail.getNextVolumn()==0 
							?  last/postageDetail.getNextVolumn() 
							:  (last+last%postageDetail.getNextVolumn())/postageDetail.getNextVolumn());
			return postageDetail.getNextCost().multiply(BigDecimal.valueOf(multi));
		}
		return BigDecimal.ZERO;
	}

}
