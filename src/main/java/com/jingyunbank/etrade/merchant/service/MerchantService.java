package com.jingyunbank.etrade.merchant.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.merchant.bo.Merchant;
import com.jingyunbank.etrade.api.merchant.service.IMerchantService;
import com.jingyunbank.etrade.goods.service.ServiceTemplate;
import com.jingyunbank.etrade.merchant.dao.MerchantDao;
import com.jingyunbank.etrade.merchant.entity.MerchantEntity;
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

	@Override
	public boolean saveMerchant(Merchant merchant) throws DataSavingException {
		boolean flag=false;
		MerchantEntity me = MerchantEntity.getInstance();
		BeanUtils.copyProperties(merchant, me);
		try {
			if(merchantDao.insertMerchant(me)){
				flag=true;
			}else{
				flag=false;
			}
		} catch (Exception e) {
			throw new DataSavingException(e);
		}
		return flag;
		
	}
}
