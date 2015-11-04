package com.jingyunbank.etrade.good.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.jingyunbank.core.Range;
import com.jingyunbank.etrade.api.good.bo.Merchant;
import com.jingyunbank.etrade.api.good.service.IGoodService;
/**
 * 
 * @author liug
 *
 */
@Service("goodsService")
public class GoodsService implements IGoodService{

	@Override
	public List<Merchant> list(Range range) {
		return null;
	}

}
