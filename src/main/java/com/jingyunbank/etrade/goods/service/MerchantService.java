package com.jingyunbank.etrade.goods.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.jingyunbank.core.Range;
import com.jingyunbank.etrade.api.goods.bo.Merchant;
import com.jingyunbank.etrade.api.goods.service.IMerchantService;
/**
 * 
 * @author liug
 *
 */
@Service("goodsService")
public class MerchantService implements IMerchantService{

	@Override
	public List<Merchant> list(Range range) {
		return null;
	}

}
