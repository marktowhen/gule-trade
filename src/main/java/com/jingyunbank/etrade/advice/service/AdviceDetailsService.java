package com.jingyunbank.etrade.advice.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.advice.dao.AdviceDetailsDao;
import com.jingyunbank.etrade.advice.entity.AdviceDetailsEntity;
import com.jingyunbank.etrade.api.advice.bo.AdviceDetails;
import com.jingyunbank.etrade.api.advice.service.IAdviceDetailsService;

import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.exception.DataRemovingException;
import com.jingyunbank.etrade.api.exception.DataSavingException;
@Service
public class AdviceDetailsService implements IAdviceDetailsService{
	 
	@Autowired
	private AdviceDetailsDao adviceDetailsDao;
	//保存所有的内容信息
	@Override
	public boolean save(AdviceDetails adviceDetail) throws DataSavingException {
		boolean flag;
		AdviceDetailsEntity adviceDetailsEntity=new AdviceDetailsEntity();
		BeanUtils.copyProperties(adviceDetail, adviceDetailsEntity);
		
		try {
			if(adviceDetailsDao.insert(adviceDetailsEntity)){
				flag=true;
			}else{
				flag=false;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new DataSavingException(e);
		}
		return flag;
	}
	//删除对应的内容信息
	@Override
	public void remove(String id) throws DataRemovingException {
		// TODO Auto-generated method stub
		try {
			adviceDetailsDao.delete(id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new DataRemovingException(e);
		}
	}
	//修改对应的内容
	@Override
	public boolean refresh(AdviceDetails adviceDetails) throws DataRefreshingException {
		boolean flag;
		AdviceDetailsEntity adviceDetailsEntity=new AdviceDetailsEntity();
		BeanUtils.copyProperties(adviceDetails, adviceDetailsEntity);
		try {
			if(adviceDetailsDao.update(adviceDetailsEntity)){
				flag=true;
			}else{
				flag=false;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new DataRefreshingException(e);
		}
		return flag;
	}
	//通过siteid查询对应的多条信息内容
	@Override
	public List<AdviceDetails> getDeailsBySiteid(String sid) {
		// TODO Auto-generated method stub
		return adviceDetailsDao.selectDetailsBySid(sid).stream().map(entity -> {
			AdviceDetails adviceDetails=new AdviceDetails();
			BeanUtils.copyProperties(entity, adviceDetails);
			return adviceDetails;
		}).collect(Collectors.toList());
	}
	//通过id查询出对应的一条信息的详细内容
	@Override
	public Optional<AdviceDetails> getDetailByid(String id) {
		// TODO Auto-generated method stub
		AdviceDetailsEntity adviceDetailsEntity=new AdviceDetailsEntity();
		adviceDetailsEntity=adviceDetailsDao.selectDetailByid(id);
		AdviceDetails adviceDetails=new AdviceDetails();
		BeanUtils.copyProperties(adviceDetailsEntity, adviceDetails);
		
		return Optional.of(adviceDetails);
	}
	

}
