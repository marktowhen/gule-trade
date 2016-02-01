package com.jingyunbank.etrade.posts.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.jingyunbank.core.Range;
import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.exception.DataRemovingException;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.posts.bo.InformationDetails;
import com.jingyunbank.etrade.api.posts.service.IInformationDetailsService;
import com.jingyunbank.etrade.posts.dao.InformationDetailsDao;
import com.jingyunbank.etrade.posts.entity.InformationDetailsEntity;
@Service
public class InformationDetailsService implements IInformationDetailsService{
	 
	@Autowired
	private InformationDetailsDao informationDetailsDao;
	//保存所有的内容信息
	@Override
	@CacheEvict(cacheNames = "informationDetailsCache", allEntries=true)
	public boolean save(InformationDetails informationDetail) throws DataSavingException {
		boolean flag;
		InformationDetailsEntity informationDetailsEntity=new InformationDetailsEntity();
		BeanUtils.copyProperties(informationDetail, informationDetailsEntity);
		
		try {
			if(informationDetailsDao.insert(informationDetailsEntity)){
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
	@CacheEvict(cacheNames = "informationDetailsCache", allEntries=true)
	public void remove(String id) throws DataRemovingException {
		// TODO Auto-generated method stub
		try {
			informationDetailsDao.delete(id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new DataRemovingException(e);
		}
	}
	//修改对应的内容
	@Override
	@CacheEvict(cacheNames = "informationDetailsCache", allEntries=true)
	public boolean refresh(InformationDetails informationDetails) throws DataRefreshingException {
		boolean flag;
		InformationDetailsEntity informationDetailsEntity=new InformationDetailsEntity();
		BeanUtils.copyProperties(informationDetails, informationDetailsEntity);
		try {
			if(informationDetailsDao.update(informationDetailsEntity)){
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
	@Cacheable(cacheNames = "informationDetailsCache", keyGenerator = "CustomKG")
	public List<InformationDetails> list(String sid,Range range) {
		// TODO Auto-generated method stub
		
		return informationDetailsDao.selectDetailsBySid(sid,range.getFrom(),range.getTo()-range.getFrom()).stream().map(entity -> {
			InformationDetails informationDetails=new InformationDetails();
			BeanUtils.copyProperties(entity, informationDetails);
			return informationDetails;
		}).collect(Collectors.toList());
	}
	//通过id查询出对应的一条信息的详细内容
	@Override
	@Cacheable(cacheNames = "informationDetailsCache", keyGenerator = "CustomKG")
	public Optional<InformationDetails> single(String id) {
		// TODO Auto-generated method stub
		InformationDetailsEntity informationDetailsEntity=new InformationDetailsEntity();
		informationDetailsEntity=informationDetailsDao.selectDetailByid(id);
		InformationDetails informationDetails=new InformationDetails();
		BeanUtils.copyProperties(informationDetailsEntity, informationDetails);
		
		return Optional.of(informationDetails);
	}
	/**
	 * 查出所有的详情信息
	 */
	@Override
	@Cacheable(cacheNames = "informationDetailsCache", keyGenerator = "CustomKG")
	public List<InformationDetails> list(String sid) {
		return informationDetailsDao.selectDetailBySid(sid).stream().map(entity -> {
			InformationDetails informationDetails=new InformationDetails();
			BeanUtils.copyProperties(entity, informationDetails);
			return informationDetails;
		}).collect(Collectors.toList());
	}
	/**
	 * 查出添加资讯的所有信息
	 */
	@Override
	@Cacheable(cacheNames = "informationDetailsCache", keyGenerator = "CustomKG")
	public List<InformationDetails> list(Range range) {
		
		return informationDetailsDao.selectDetail(range.getFrom(),range.getTo()-range.getFrom()).stream().map(entity ->{
			InformationDetails bo=new InformationDetails();
			BeanUtils.copyProperties(entity, bo);
			return bo;
		}).collect(Collectors.toList());
	}
	@Override
	@Cacheable(cacheNames = "informationDetailsCache", keyGenerator = "CustomKG")
	public List<InformationDetails> listByName(String name, Range range) {
		
		return informationDetailsDao.selectByName(name,range.getFrom(),range.getTo()-range.getFrom()).stream().map(entity ->{
			InformationDetails bo=new InformationDetails();
			BeanUtils.copyProperties(entity, bo);
			return bo;
		}).collect(Collectors.toList());
	}
	@Override
	@Cacheable(cacheNames = "informationDetailsCache", keyGenerator = "CustomKG")
	public int selectmaxOrders(String id) throws Exception {
		int max=informationDetailsDao.selectmaxOrders(); 
		return informationDetailsDao.updateMaxOrders(id, max+1);
	}
	

}
