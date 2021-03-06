package com.jingyunbank.etrade.marketing.rankgroup.service;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.marketing.rankgroup.bo.RankGroupOrder;
import com.jingyunbank.etrade.api.marketing.rankgroup.service.IRankGroupOrderService;
import com.jingyunbank.etrade.marketing.rankgroup.dao.RankGroupOrderDao;
import com.jingyunbank.etrade.marketing.rankgroup.entity.RankGroupOrderEntity;

@Service("rankGroupOrderService")
public class RankGroupOrderService implements IRankGroupOrderService{
   @Autowired
   RankGroupOrderDao rankGroupOrderDao;
	@Override
	public boolean save(RankGroupOrder groupOrder) throws DataSavingException {
		RankGroupOrderEntity entity = new RankGroupOrderEntity();
		BeanUtils.copyProperties(groupOrder, entity);
		try {
			return rankGroupOrderDao.insert(entity);
		} catch (Exception e) {
			throw new DataSavingException(e);
		}
	}
	@Override
	public Optional<RankGroupOrder> singleByOID(String OID) {
		RankGroupOrderEntity entity = rankGroupOrderDao.selectByOID(OID);
		if(entity!=null){
			RankGroupOrder bo = new RankGroupOrder();
			BeanUtils.copyProperties(entity, bo);
			return Optional.of(bo);
		}
		return Optional.empty();
	}
	@Override
	public Optional<RankGroupOrder> single(String groupUserID, String orderType) {
		RankGroupOrderEntity entity = rankGroupOrderDao.selectListByType(groupUserID, orderType);
		if(entity!=null){
			RankGroupOrder bo = new RankGroupOrder();
			BeanUtils.copyProperties(entity, bo);
			return Optional.of(bo);
		}
		return Optional.empty();
	}

}
