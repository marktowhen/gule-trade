package com.jingyunbank.etrade.information.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.information.bo.Information;
import com.jingyunbank.etrade.api.information.service.IInformationService;
import com.jingyunbank.etrade.information.dao.InformationDao;
import com.jingyunbank.etrade.information.entity.InformationEntity;
@Service
public class InformationService implements IInformationService{
	
	
	@Autowired
	private InformationDao informationDao;
	
	//插入新的资讯标题
	@Override
	public boolean save(Information information) throws DataSavingException {
		boolean flag;
		
		InformationEntity informationEntity=new InformationEntity();
		BeanUtils.copyProperties(information, informationEntity);
		try {
			if(informationDao.insert(informationEntity)){
				flag=true;
			}else{
				flag=false;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new DataSavingException(e);
		}
		// TODO Auto-generated method stub
		return flag;
	}
	//查出所有的资讯大标题
	@Override
	public List<Information> getInformation() {
		// TODO Auto-generated method stub
		
		return informationDao.selectList().stream().map(entity -> {
			Information information=new Information();
			BeanUtils.copyProperties(entity, information);
			return information;
		}).collect(Collectors.toList());
	}

}
