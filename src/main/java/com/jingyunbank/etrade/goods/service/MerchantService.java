package com.jingyunbank.etrade.goods.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.goods.bo.Merchant;
import com.jingyunbank.etrade.api.goods.service.IMerchantService;
import com.jingyunbank.etrade.goods.dao.MerchantDao;
import com.jingyunbank.etrade.goods.entity.MerchantEntity;
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
	public List<Merchant> listMerchants() throws IllegalAccessException, InvocationTargetException {
		this.to = 3;
		Map<String, Integer> params = new HashMap<String,Integer>();
		params.put("from", this.from);
		params.put("to", this.to);
		List<Merchant> rlist = new ArrayList<Merchant>();
		List<MerchantEntity> list = merchantDao.selectMerchants(params);
		Merchant bo = null;
		for(MerchantEntity e : list){
			bo = Merchant.getInstance();
			BeanUtils.copyProperties(e,bo);
			rlist.add(bo);
		}
		return rlist;
	}

}
