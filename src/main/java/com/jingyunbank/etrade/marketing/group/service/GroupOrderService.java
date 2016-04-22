package com.jingyunbank.etrade.marketing.group.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.marketing.group.bo.GroupOrder;
import com.jingyunbank.etrade.api.marketing.group.service.IGroupOrderService;
import com.jingyunbank.etrade.marketing.group.dao.GroupOrderDao;
import com.jingyunbank.etrade.marketing.group.entity.GroupOrderEntity;

@Service("groupOrderService")
public class GroupOrderService implements IGroupOrderService {
	
	@Autowired
	private GroupOrderDao groupOrderDao;

	@Override
	public boolean save(GroupOrder groupOrder) throws DataSavingException {
		GroupOrderEntity entity = new GroupOrderEntity();
		BeanUtils.copyProperties(groupOrder, entity);
		try {
			return groupOrderDao.insert(entity);
		} catch (Exception e) {
			throw new DataSavingException(e);
		}
	}

	@Override
	public Optional<GroupOrder> single(String ID) {
		GroupOrderEntity entity = groupOrderDao.selectOne(ID);
		if(entity!=null){
			GroupOrder bo = new GroupOrder();
			BeanUtils.copyProperties(entity, bo);
			return Optional.of(bo);
		}
		return Optional.empty();
	}

	@Override
	public List<GroupOrder> list(String groupUserID) {
		return groupOrderDao.selectList(groupUserID).stream().map(entity->{
			GroupOrder bo = new GroupOrder();
			BeanUtils.copyProperties(entity, bo);
			return bo;
		}).collect(Collectors.toList());
	}

	@Override
	public Optional<GroupOrder> singleByOID(String OID) {
		GroupOrderEntity entity = groupOrderDao.selectByOID(OID);
		if(entity!=null){
			GroupOrder bo = new GroupOrder();
			BeanUtils.copyProperties(entity, bo);
			return Optional.of(bo);
		}
		return Optional.empty();
	}

}
