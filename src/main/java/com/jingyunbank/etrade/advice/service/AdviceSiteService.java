package com.jingyunbank.etrade.advice.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.advice.dao.AdviceSiteDao;
import com.jingyunbank.etrade.advice.entity.AdviceSiteEntity;
import com.jingyunbank.etrade.api.advice.bo.AdviceSite;
import com.jingyunbank.etrade.api.advice.service.IAdviceSiteService;
import com.jingyunbank.etrade.api.exception.DataSavingException;
@Service
public class AdviceSiteService implements IAdviceSiteService{
	
	@Autowired
	private AdviceSiteDao adviceSiteDao;
	//添加多个标题信息
	@Override
	public boolean save(AdviceSite adviceSite) throws DataSavingException {
		boolean flag;
		// TODO Auto-generated method stub
		AdviceSiteEntity adviceSiteEntity=new AdviceSiteEntity();
		BeanUtils.copyProperties(adviceSite, adviceSiteEntity);
		try {
			if(adviceSiteDao.insert(adviceSiteEntity)){
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
	//通过siteid查出所对应的标题
	@Override
	public List<AdviceSite> getSitesBySiteid(String siteid) {
		// TODO Auto-generated method stub
		/*List<AdviceSiteEntity> adviceSiteEntitys=adviceSiteDao.selectSitesBySiteid(siteid);*/
		return adviceSiteDao.selectSitesBySiteid(siteid).stream().map(entity -> {
			AdviceSite adviceSite=new AdviceSite();
			BeanUtils.copyProperties(entity, adviceSite);
			return adviceSite;
		}).collect(Collectors.toList());
	}

}
