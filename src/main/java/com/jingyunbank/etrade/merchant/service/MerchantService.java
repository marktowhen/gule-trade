package com.jingyunbank.etrade.merchant.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jingyunbank.core.KeyGen;
import com.jingyunbank.core.Range;
import com.jingyunbank.core.Result;
import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.exception.DataRemovingException;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.goods.bo.GoodsList;
import com.jingyunbank.etrade.api.merchant.bo.DeliveryType;
import com.jingyunbank.etrade.api.merchant.bo.InvoiceType;
import com.jingyunbank.etrade.api.merchant.bo.Merchant;
import com.jingyunbank.etrade.api.merchant.service.IMerchantService;
import com.jingyunbank.etrade.goods.service.ServiceTemplate;
import com.jingyunbank.etrade.merchant.dao.MerchantDao;
import com.jingyunbank.etrade.merchant.entity.DeliveryTypeEntity;
import com.jingyunbank.etrade.merchant.entity.InvoiceTypeEntity;
import com.jingyunbank.etrade.merchant.entity.MerchantDeliveryEntity;
import com.jingyunbank.etrade.merchant.entity.MerchantEntity;
import com.jingyunbank.etrade.merchant.entity.MerchantInvoiceEntity;
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
		this.to = 5;
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
	@Override
	public List<InvoiceType> listInvoiceType() throws IllegalAccessException, InvocationTargetException {
		List<InvoiceType> rlist = new ArrayList<InvoiceType>();
		List<InvoiceTypeEntity> list = merchantDao.selectInvoiceType();
		InvoiceType bo = null;
		for(InvoiceTypeEntity e : list){
			bo = new InvoiceType();
			BeanUtils.copyProperties(e,bo);
			rlist.add(bo);
		}
		return rlist;
	}
	@Override
	public boolean saveMerchantInvoiceType(Merchant merchant) throws Exception {
		boolean flag = false;
		try {
		String mid = merchant.getID();
		String codes = merchant.getInvoiceCodes() == null ?"":merchant.getInvoiceCodes();
		String[] codeArr = codes.split(",");
		for(String s : codeArr){
			MerchantInvoiceEntity me = new MerchantInvoiceEntity();
			me.setID(KeyGen.uuid());
			me.setMID(mid);
			me.setCode(s);
			merchantDao.insertMerchantInvoiceType(me);
		}
		flag = true;
		}catch(Exception e){
			throw new DataSavingException(e);
		}
		return flag;
	}
	
	@Override
	public boolean updateMerchant(Merchant merchant) throws DataRefreshingException {
		boolean flag=false;
		MerchantEntity me = MerchantEntity.getInstance();
		BeanUtils.copyProperties(merchant, me);
		try {
			if(merchantDao.updateMerchant(me)){
				flag=true;
			}else{
				flag=false;
			}
		} catch (Exception e) {
			throw new DataRefreshingException(e);
		}
		return flag;
		
	}
	@Override
	public boolean removeMerchantInvoiceType(Merchant merchant) throws DataRemovingException {
		boolean flag=false;
		try {
			flag = merchantDao.deleteMerchantInvoiceType(merchant.getID());
		} catch (Exception e) {
			throw new DataRemovingException(e);
		}
		return flag;
	}
	
	@Override
	public List<DeliveryType> listDeliveryType() throws IllegalAccessException, InvocationTargetException {
		List<DeliveryType> rlist = new ArrayList<DeliveryType>();
		List<DeliveryTypeEntity> list = merchantDao.selectDeliveryType();
		DeliveryType bo = null;
		for(DeliveryTypeEntity e : list){
			bo = new DeliveryType();
			BeanUtils.copyProperties(e,bo);
			rlist.add(bo);
		}
		return rlist;
	}
	
	@Override
	public boolean saveMerchantDeliveryType(Merchant merchant) throws Exception {
		boolean flag = false;
		try {
		String mid = merchant.getID();
		String codes = merchant.getDeliveryCodes() == null ?"":merchant.getDeliveryCodes();
		String[] codeArr = codes.split(",");
		for(String s : codeArr){
			MerchantDeliveryEntity me = new MerchantDeliveryEntity();
			me.setID(KeyGen.uuid());
			me.setMID(mid);
			me.setCode(s);
			merchantDao.insertMerchantDeliveryType(me);
		}
		flag = true;
		}catch(Exception e){
			throw new DataSavingException(e);
		}
		return flag;
	}
	
	@Override
	public boolean removeMerchantDeliveryType(Merchant merchant) throws DataRemovingException {
		boolean flag=false;
		try {
			flag = merchantDao.deleteMerchantDeliveryType(merchant.getID());
		} catch (Exception e) {
			throw new DataRemovingException(e);
		}
		return flag;
	}
	@Override
	public Optional<Merchant> getMerchant(String mid) {
		MerchantEntity merchantEntity = merchantDao.selectMerchantByMid(mid);
		Merchant merchant=new Merchant();
		BeanUtils.copyProperties(merchantEntity, merchant);
		return Optional.of(merchant);
	}
	@Override
	public List<InvoiceType> listInvoiceType(String mid) throws IllegalAccessException, InvocationTargetException {
		List<InvoiceType> rlist = new ArrayList<InvoiceType>();
		List<InvoiceTypeEntity> list = merchantDao.selectInvoiceTypeByMid(mid);
		InvoiceType bo = null;
		for(InvoiceTypeEntity e : list){
			bo = new InvoiceType();
			BeanUtils.copyProperties(e,bo);
			rlist.add(bo);
		}
		return rlist;
	}
	
	@Override
	public List<DeliveryType> listDeliveryType(String mid) throws IllegalAccessException, InvocationTargetException {
		List<DeliveryType> rlist = new ArrayList<DeliveryType>();
		List<DeliveryTypeEntity> list = merchantDao.selectDeliveryTypeByMid(mid);
		DeliveryType bo = null;
		for(DeliveryTypeEntity e : list){
			bo = new DeliveryType();
			BeanUtils.copyProperties(e,bo);
			rlist.add(bo);
		}
		return rlist;
	}
	
	@Override
	public List<Merchant> listMerchantsByCondition(Merchant merchant, Range range) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("from", (int) range.getFrom());
		map.put("size", (int) range.getTo());
		map.put("merchantName", merchant.getMerchantName());
		List<Merchant> showMerchantList = merchantDao.selectMerchantsByCondition(map).stream().map(eo -> {
			Merchant bo = new Merchant();
			BeanUtils.copyProperties(eo, bo);
			return bo;
		}).collect(Collectors.toList());
		return showMerchantList;
	}
	@Override
	public int countMerchants(Merchant merchant) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("merchantName", merchant.getMerchantName());
		int count = merchantDao.selectMerchantsCount(map);
		return count;
	}
}
