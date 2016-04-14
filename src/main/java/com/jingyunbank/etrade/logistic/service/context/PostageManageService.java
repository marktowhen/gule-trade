package com.jingyunbank.etrade.logistic.service.context;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.jingyunbank.core.KeyGen;
import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.exception.DataRemovingException;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.logistic.bo.Postage;
import com.jingyunbank.etrade.api.logistic.bo.PostageDetail;
import com.jingyunbank.etrade.api.logistic.service.IPostageDetailService;
import com.jingyunbank.etrade.api.logistic.service.IPostageService;
import com.jingyunbank.etrade.api.logistic.service.context.IPostageManageService;

@Service("postageManageService")
public class PostageManageService implements IPostageManageService {
	
	@Autowired
	private IPostageService postageService;
	@Autowired
	private IPostageDetailService postageDetailService;

	@Override
	@Transactional
	public boolean save(Postage postage, List<PostageDetail> details)
			throws DataSavingException {
		postageService.save(postage);
		postageDetailService.save(details);
		return true;
	}

	@Override
	@Transactional
	public boolean refresh(Postage postage, List<PostageDetail> details)
			throws DataRemovingException, DataRefreshingException, DataSavingException {
		postageService.refresh(postage);
		postageDetailService.deleteBatch(postage.getID());
		
		details.stream().forEach( detail->{
			if(StringUtils.isEmpty(detail.getID())){
				detail.setID(KeyGen.uuid());
			}
			detail.setPostageID(postage.getID());
		});
		postageDetailService.save(details);
		return true;
	}

	@Override
	@Transactional
	public boolean remove(String postageID) throws DataRemovingException {
		postageService.remove(postageID);
		postageDetailService.removeByPostageID(postageID);
		
		return true;
	}

	@Override
	public List<Postage> listOneShopWithDetail(String MID) {
		List<Postage> list = postageService.list(MID);
		list.stream().forEach( postage ->{
			postage.setPostageDetailList(postageDetailService.list(postage.getID()));
		});
		return list;
	}

}
