package com.jingyunbank.etrade.user.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.core.Range;
import com.jingyunbank.core.KeyGen;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.user.bo.Address;
import com.jingyunbank.etrade.api.user.service.IAddressService;
import com.jingyunbank.etrade.user.dao.AddressDao;
import com.jingyunbank.etrade.user.entity.AddressEntity;

@Service("addressService")
public class AddressService implements IAddressService{
	@Autowired
	private AddressDao addressDao;

	@Override
	public boolean save(Address address) throws DataSavingException, DataRefreshingException {
		boolean result = false;
		AddressEntity addressEntity=new AddressEntity();
		BeanUtils.copyProperties(address, addressEntity);
		addressEntity.setID(KeyGen.uuid());
		try {
			result = addressDao.insert(addressEntity);
		} catch (Exception e) {
			throw new DataSavingException(e);
		}
		//如果新增的为默认 则把其他的设为非默认
		if(addressEntity.isDefaulted()){
			refreshDefualt(addressEntity.getID(), addressEntity.getUID());
		}
		return result;
	}

	@Override
	public boolean refresh(Address address) throws DataRefreshingException {
		boolean result = false;
		try {
			result = addressDao.update(getEntityFromBo(address));
			if(address.isDefaulted()){
				refreshDefualt(address.getID(), address.getUID());
			}
		} catch (Exception e) {
			throw new DataRefreshingException(e);
		}
		return result;
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
		entity.setValid(true);
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
	 * 设置默认收货地址
	 * @param id
	 * @param uid
	 * 2015年11月9日 qxs
	 * @throws DataRefreshingException 
	 */
	public void refreshDefualt(String id, String uid) throws DataRefreshingException{
		AddressEntity entity = new AddressEntity();
		entity.setUID(uid);
		try {
			//将用户所有的地址改为非默认
			entity.setDefaulted(false);
			addressDao.updateDefault(entity);
			//将指定地址设为默认
			entity.setDefaulted(true);
			entity.setID(id);
			addressDao.updateDefault(entity);
			
		} catch (Exception e) {
			throw new DataRefreshingException(e);
		}
		
	}


	@Override
	public Optional<Address> getDefaultAddress(String uid) {
		AddressEntity entity = new AddressEntity();
		entity.setUID(uid);
		entity.setDefaulted(true);
		entity.setValid(true);
		List<AddressEntity> list = addressDao.selectList(entity);
		if(list!=null && !list.isEmpty()){
			return Optional.of(getBoFromEntity(list.get(0)));
		}
		return Optional.empty();
	}

	@Override
	public boolean remove(String id, String uid) throws DataRefreshingException {
		String IDArray[] = {id};
		return remove(IDArray, uid);
	}
	
	@Override
	public boolean remove(String[] ids, String uid)
			throws DataRefreshingException {
		AddressEntity entity = new AddressEntity();
		entity.setIDArray(ids);
		entity.setValid(false);
		entity.setUID(uid);
		try {
			return addressDao.updateStatus(entity);
		} catch (Exception e) {
			throw new DataRefreshingException(e);
		}
	}

	@Override
	public List<Address> listUserAdd(String uid, Range range) {
		AddressEntity entity = new AddressEntity();
		entity.setUID(uid);
		entity.setValid(true);
		return  addressDao.selectListRang(entity, range.getFrom(), range.getTo()-range.getFrom())
				.stream().map( entityResult -> {
					return getBoFromEntity(entityResult);
				}).collect(Collectors.toList());
	}

	@Override
	public int getAmount(String uid) {
		AddressEntity entity = new AddressEntity();
		entity.setUID(uid);
		entity.setValid(true);
		return addressDao.selectAmount(entity);
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
