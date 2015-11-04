package com.jingyunbank.etrade.goods.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.goods.bo.Merchant;
import com.jingyunbank.etrade.api.goods.service.IMerchantService;
import com.jingyunbank.etrade.goods.dao.MerchantDao;
/**
 * 
 * @author liug
 *
 */
@Service("merchantService")
public class MerchantService extends ServiceTemplate implements IMerchantService{
	@Resource
	private MerchantDao merchantDao;
	
	@Override
	public List<Merchant> listMerchants() {
		this.from = 0 ;
		this.to = 5;
		return null;
	}

}
