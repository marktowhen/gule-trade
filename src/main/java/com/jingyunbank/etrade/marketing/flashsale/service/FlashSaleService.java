package com.jingyunbank.etrade.marketing.flashsale.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.core.Range;
import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.marketing.flashsale.bo.FlashSale;
import com.jingyunbank.etrade.api.marketing.flashsale.bo.FlashSaleShow;
import com.jingyunbank.etrade.api.marketing.flashsale.service.IFlashSaleService;
import com.jingyunbank.etrade.api.marketing.group.bo.GroupGoodsShow;
import com.jingyunbank.etrade.api.wap.goods.bo.Goods;
import com.jingyunbank.etrade.api.wap.goods.bo.GoodsSku;
import com.jingyunbank.etrade.marketing.flashsale.dao.FlashSaleDao;
import com.jingyunbank.etrade.marketing.flashsale.entity.FlashSaleEntity;
import com.jingyunbank.etrade.marketing.flashsale.entity.FlashSaleShowEntity;
import com.jingyunbank.etrade.marketing.group.entity.GroupGoodsShowEntity;
@Service
public class FlashSaleService implements IFlashSaleService{
	@Autowired
	private FlashSaleDao flashDao;
	
	/**
	 * bo转entity
	 * @param bo
	 * @return entity
	 * 
	 */
	private FlashSaleEntity getEntityFromBo(FlashSale  flashSale){
		FlashSaleEntity  entity = null;
		if(flashSale!=null){
			entity = new FlashSaleEntity();
			BeanUtils.copyProperties(flashSale, entity);
		}
		return entity;
	}
	/**
	 * bo转entity
	 * @param bo
	 * @return entity
	 * 
	 */
	private FlashSale getBoFromEntity(FlashSaleEntity  entity){
		FlashSale  bo = null;
		if(entity!=null){
			bo = new FlashSale();
			BeanUtils.copyProperties(entity, bo);
		}
		return bo;
	}

	@Override
	public boolean saveFlashSale(FlashSale flashSale) throws DataSavingException{
		
		try {
			//保存秒杀商品
			int num=flashDao.addFlashSale(getEntityFromBo(flashSale));
			if(num>0){return true;}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new DataSavingException(e);
		}
		return false;
	}

	@Override
	public List<FlashSaleShow> getFlashSaleMany(String mid, Range range) {
		List<FlashSaleShowEntity> listEntity=flashDao.selectFlashSaleMany(mid, range.getFrom(), (int)(range.getTo()-range.getFrom()));
		List<FlashSaleShow> bo = new ArrayList<FlashSaleShow>();
		listEntity.forEach(entity ->{
			bo.add(getShowBoFromEntity(entity));
		});
		return bo;
	}
	
	private FlashSaleShow getShowBoFromEntity(FlashSaleShowEntity entity){
		FlashSaleShow bo = new FlashSaleShow();
		BeanUtils.copyProperties(entity, bo);
		if(Objects.nonNull(entity.getGoods())){
			Goods goodsBo = new Goods();
			BeanUtils.copyProperties(entity.getGoods(), goodsBo);
			bo.setGoods(goodsBo);
		}
		if(Objects.nonNull(entity.getSku())){
			GoodsSku sku = new GoodsSku();
			BeanUtils.copyProperties(entity.getSku(), sku);
			bo.setSku(sku);
		}
		
		return bo;
	}

	@Override
	public Optional<FlashSale> single(String id) {
		FlashSaleEntity entity=flashDao.selectFlashSaleById(id);
		return Optional.of(getBoFromEntity(entity));
	}
	@Override
	public boolean refresh(FlashSale flashSale) throws DataRefreshingException {
		try {
			int num=flashDao.update(getEntityFromBo(flashSale));
			if(num>0){
				return true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new DataRefreshingException(e);
		}
		return false;
	}


}
