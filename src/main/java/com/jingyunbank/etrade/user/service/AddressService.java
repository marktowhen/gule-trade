package com.jingyunbank.etrade.user.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.exception.DataUpdatingException;
import com.jingyunbank.etrade.api.user.bo.Address;
import com.jingyunbank.etrade.api.user.service.IAddressService;
import com.jingyunbank.etrade.user.dao.AddressDao;
import com.jingyunbank.etrade.user.entity.AddressEntity;

@Service("addressService")
public class AddressService implements IAddressService{
	@Autowired
	private AddressDao addressDao;

	@Override
	public boolean save(Address address) throws DataSavingException {
		return addressDao.insert(getEntityFromBo(address));
	}

	@Override
	public boolean refresh(Address address) throws DataUpdatingException {
		return addressDao.update(getEntityFromBo(address));
	}

	@Override
	public boolean delete(Address address) throws DataUpdatingException {
		AddressEntity entity = new AddressEntity();
		entity.setID(address.getID());
		entity.setValid(false);
		return addressDao.updateStatus(entity);
	}

	@Override
	public Optional<Address> singleById(String id) {
		AddressEntity entity = new AddressEntity();
		entity.setID(id);
		List<AddressEntity> list = addressDao.selectList(entity);
		if(list!=null && !list.isEmpty()){
			return Optional.of(getBoFromEntity(list.get(0)));
		}
		return Optional.empty();
	}

	

	@Override
	public List<Address> list(String uid) {
		List<Address> result = null;
		AddressEntity entity = new AddressEntity();
		entity.setUID(uid);
		List<AddressEntity> list = addressDao.selectList(entity);
		if(list!=null && !list.isEmpty()){
			result = new ArrayList<Address>();
			for (AddressEntity addressEntity : list) {
				result.add(getBoFromEntity(addressEntity));
			}
		}
		return result;
	}
	
	/**
	 * bo转entity
	 * @param bo
	 * @return entity
	 * 2015年11月5日 qxs
	 */
	private AddressEntity getEntityFromBo(Address  bo){
		AddressEntity  entity = null;
		if(bo!=null){
			entity = new AddressEntity();
			BeanUtils.copyProperties(bo, entity);
		}
		return entity;
	}
	
	/**
	 * entity 转 BO
	 * @param addressEntity
	 * @return
	 * 2015年11月5日 qxs
	 */
	private Address getBoFromEntity(AddressEntity addressEntity) {
		Address vo = null;
		if(addressEntity!=null){
			vo = new Address();
			BeanUtils.copyProperties(addressEntity, vo);
		}
		return vo;
	}

}
