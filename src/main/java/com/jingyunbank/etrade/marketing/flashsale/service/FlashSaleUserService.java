package com.jingyunbank.etrade.marketing.flashsale.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.flow.bo.FlowStatus;
import com.jingyunbank.etrade.api.flow.service.IFlowStatusService;
import com.jingyunbank.etrade.api.marketing.flashsale.bo.FlashSaleUser;
import com.jingyunbank.etrade.api.marketing.flashsale.service.IFlashSaleUserService;
import com.jingyunbank.etrade.api.marketing.group.bo.GroupUser;
import com.jingyunbank.etrade.marketing.flashsale.dao.FlashSaleUserDao;
import com.jingyunbank.etrade.marketing.flashsale.entity.FlashSaleUserEntity;
@Service
public class FlashSaleUserService implements IFlashSaleUserService{
	@Autowired 
	private FlashSaleUserDao flashSaleUserDao;
	@Autowired
	private IFlowStatusService flowStatusService;
	@Override
	public boolean save(FlashSaleUser flashSaleUser) throws DataSavingException {
		
		try {
			FlashSaleUserEntity entity = new FlashSaleUserEntity();
			BeanUtils.copyProperties(flashSaleUser, entity);
			int num = flashSaleUserDao.add(entity);
			if(num>0){return true;}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new DataSavingException(e);
		}
		
		return false;
	}
	@Override
	public Optional<FlashSaleUser> single(String id) {
		FlashSaleUser bo = new FlashSaleUser();
		FlashSaleUserEntity entity=flashSaleUserDao.selectFlashSaleUserByid(id);
		BeanUtils.copyProperties(entity, bo);
		return Optional.of(bo);
	}
	@Override
	public boolean refreshStatus(String id, String orderStatus) throws DataRefreshingException {
		try {
			return 	flashSaleUserDao.updateStatus(id, orderStatus);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new DataRefreshingException(e);
		}
	}
	@Override
	public boolean refreshStatus(String ID, String currentStatus, String flowStatusFlag)
			throws DataRefreshingException {
		Optional<FlowStatus> flowStatus = flowStatusService.single(GroupUser.FLOW_TYPE, currentStatus, flowStatusFlag);
		if(flowStatus.isPresent()){
			try {
				return flashSaleUserDao.updateStatus(ID, flowStatus.get().getNextStatus());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				throw new DataRefreshingException(e);
			}
		}
		return false;
	}
	@Override
	public List<FlashSaleUser> listByStatus() {
		List<FlashSaleUserEntity> entityList=flashSaleUserDao.selectFlashByStatus();
		List<FlashSaleUser> boList = new ArrayList<FlashSaleUser>();
		entityList.forEach(entity ->{
			FlashSaleUser bo = new FlashSaleUser();
			BeanUtils.copyProperties(entity, bo);
			boList.add(bo);
		});
		return boList;
	}


}
