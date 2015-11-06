package com.jingyunbank.etrade.user.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jingyunbank.core.Range;
import com.jingyunbank.core.KeyGen;
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
		AddressEntity addressEntity=new AddressEntity();
		BeanUtils.copyProperties(address, addressEntity);
		addressEntity.setID(KeyGen.uuid());
		return addressDao.insert(addressEntity);
	}

	@Override
	public boolean refresh(Address address) throws DataUpdatingException {
		return addressDao.update(getEntityFromBo(address));
	}

	@Override
	public boolean delete(Address address) throws DataUpdatingException {
		AddressEntity entity = new AddressEntity();
		entity.setIDArray(address.getIDArray());
		entity.setValid(false);
		entity.setUID(address.getUID());
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

	@Override
	public List<Address> listPage(Address address, Range range) {
		AddressEntity entityFromBo = getEntityFromBo(address);
		entityFromBo.setFrom(range.getFrom());
		entityFromBo.setSize(range.getTo()-range.getFrom());
		entityFromBo.setValid(true);
		List<AddressEntity> entityList = addressDao.selectListRang(entityFromBo);
		List<Address> boList = new ArrayList<Address>();
		if(entityList!=null && !entityList.isEmpty()){
			for (AddressEntity entity : entityList) {
				boList.add(getBoFromEntity(entity));
			}
		}
		return boList;
	}

}
