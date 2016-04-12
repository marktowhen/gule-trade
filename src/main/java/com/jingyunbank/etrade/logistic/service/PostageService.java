package com.jingyunbank.etrade.logistic.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.exception.DataRemovingException;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.logistic.bo.Postage;
import com.jingyunbank.etrade.api.logistic.bo.PostageCalculate;
import com.jingyunbank.etrade.api.logistic.bo.PostageDetail;
import com.jingyunbank.etrade.api.logistic.service.IPostageDetailService;
import com.jingyunbank.etrade.api.logistic.service.IPostageService;
import com.jingyunbank.etrade.api.statics.area.service.ICityService;
import com.jingyunbank.etrade.api.statics.area.service.IProvinceService;
import com.jingyunbank.etrade.logistic.dao.PostageDao;
import com.jingyunbank.etrade.logistic.entity.PostageEntity;

@Service("postageService")
public class PostageService implements IPostageService {

	@Autowired
	private IProvinceService provinceService;
	@Autowired
	private PostageDao postageDao;
	@Autowired
	private IPostageDetailService postageDetailService;
	@Autowired
	private ICityService cityService;
	
	@Override
	public boolean save(Postage postage) throws DataSavingException {
		PostageEntity entity = new PostageEntity();
		BeanUtils.copyProperties(entity, postage);
		try {
			return postageDao.insert(entity);
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean refresh(Postage postage) throws DataRefreshingException {
		PostageEntity entity = new PostageEntity();
		BeanUtils.copyProperties(entity, postage);
		try {
			return postageDao.update(entity);
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	@Transactional
	public boolean remove(String ID) throws DataRemovingException {
		try {
			postageDao.updateStatus(ID, false);
			postageDetailService.removeByPostageID(ID);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public Postage single(String ID) {
		PostageEntity entity = postageDao.selectOne(ID);
		if(entity!=null){
			Postage bo = new Postage();
			BeanUtils.copyProperties(entity, bo);
			return bo;
		}
		return null;
	}

	@Override
	public List<Postage> list(String MID) {
		return postageDao.selectByMID(MID).stream().map( entity -> {
			Postage bo = new Postage();
			BeanUtils.copyProperties(entity, bo);
			return bo;
		}).collect(Collectors.toList());
	}

	@Override
	public BigDecimal calculate(PostageCalculate postageCalculate) {
		Postage postage = this.single(postageCalculate.getPostageID(), postageCalculate.getCity());
		if(postage!=null){
			return calculateFirstCost(postage.getPostageDetail()).add(calculateNextCost(postageCalculate, postage, postage.getPostageDetail()));
		}
		return null;
		
	}


	@Override
	public Postage single(String ID, int cityID) {
		Postage postage = this.single(ID);
		if(postage!=null){
			postage.setPostageDetail(postageDetailService.single(ID, cityID));
		}
		return postage;
	}

	@Override
	//分别计算不同店铺的运费再累加
	public BigDecimal calculateMuti(List<PostageCalculate> postageCalculateList, int city) {
		
		//查出所需运费模板
		List<Postage> postageList = getPostageByCalculateList(postageCalculateList);
		//根据postageID匹配对应的运费模板
		postageCalculateList = packagPostage(postageCalculateList, postageList);
		//将运费模板按店铺分组
		Map<String, List<PostageCalculate>> mergeByMID = mergeByMID(postageCalculateList);
		
		BigDecimal result = BigDecimal.ZERO;
		for (String MID : mergeByMID.keySet()) {
			result = result.add(calculateOneMerchat(mergeByMID.get(MID), city));
		}
		
		return result;
	}
	
	/**
	 * 计算单个商铺邮费
	 * @param list
	 * @param city
	 * @return
	 * 2016年4月11日 qxs
	 */
	private BigDecimal calculateOneMerchat(List<PostageCalculate> list, int city) {
		BigDecimal result = BigDecimal.ZERO;
		//将同一运费模板的数据合并
		List<PostageCalculate> mergeRepeat = mergeRepeat(list);
		for(PostageCalculate calculate : mergeRepeat){
			calculate.setCity(city);
			//多个商品 首费 续费综合计算
			//result.add(calculate(calculate));
			PostageDetail postageDetail = postageDetailService.single(calculate.getPostageID(), city);
			calculate.getCalculatRule().setPostageDetail(postageDetail);
		}
		
		//根据首费由大到小排序
		Collections.sort(mergeRepeat, new Comparator<PostageCalculate>() {
			@Override
			public int compare(PostageCalculate o1, PostageCalculate o2) {
				return o2.getCalculatRule().getPostageDetail().getFirstCost().compareTo(o1.getCalculatRule().getPostageDetail().getFirstCost());
			}
		});
		
		for (int i = 0; i < mergeRepeat.size(); i++) {
			//首费只计算一次
			if(i==0){
				result = result.add(calculateFirstCost(mergeRepeat.get(i).getCalculatRule().getPostageDetail()));
			}
			//续费计算
			result = result.add(calculateNextCost(mergeRepeat.get(i), mergeRepeat.get(i).getCalculatRule(), mergeRepeat.get(i).getCalculatRule().getPostageDetail()));
		}
		return result;
	}
	
	//计算续费
	private BigDecimal calculateFirstCost(PostageDetail postageDetail){
		if(postageDetail.isFree()){
			return BigDecimal.ZERO;
		}
		return postageDetail.getFirstCost();
	}
	
	//计算续费
	private BigDecimal calculateNextCost(PostageCalculate calculate ,Postage postage, PostageDetail postageDetail){
		if(postageDetail.isFree()){
			return BigDecimal.ZERO;
		}
		if(Postage.TYPE_NUMBER.equals(postage.getType())){
			if(calculate.getNumber() > postageDetail.getFirstNumber()){
				//续件数量
				int last = calculate.getNumber() - postageDetail.getFirstNumber();
				//续件倍数
				int multi =  last%postageDetail.getNextNumber()==0 
								?  last/postageDetail.getNextNumber() 
								:  (last+last%postageDetail.getNextNumber())/postageDetail.getNextNumber();
				return postageDetail.getNextCost().multiply(BigDecimal.valueOf(multi));
			}
		}else if(Postage.TYPE_WEIGHT.equals(postage.getType())){
			if(calculate.getWeight() > postageDetail.getFirstWeight()){
				//续件数量
				double last = calculate.getWeight() - postageDetail.getFirstWeight();
				//续件倍数
				int multi =  (int) (last%postageDetail.getNextWeight()==0 
								?  last/postageDetail.getNextWeight() 
								:  (last+last%postageDetail.getNextWeight())/postageDetail.getNextWeight());
				return postageDetail.getNextCost().multiply(BigDecimal.valueOf(multi));
			}
		}else{
			if(calculate.getVolume() > postageDetail.getFirstVolume()){
				//续件数量
				double last = calculate.getVolume() - postageDetail.getFirstVolume();
				//续件倍数
				int multi =  (int) (last%postageDetail.getNextVolumn()==0 
								?  last/postageDetail.getNextVolumn() 
								:  (last+last%postageDetail.getNextVolumn())/postageDetail.getNextVolumn());
				return postageDetail.getNextCost().multiply(BigDecimal.valueOf(multi));
			}
		}
		return BigDecimal.ZERO;
	}

	private List<PostageCalculate> packagPostage(
			List<PostageCalculate> postageCalculateList,
			List<Postage> postageList) {
		for (PostageCalculate calculate : postageCalculateList) {
			for (Postage postage : postageList) {
				if(calculate.getPostageID().equals(postage.getID())){
					calculate.setCalculatRule(postage);
					break;
				}
			}
		}
		return postageCalculateList;
	}

	private Map<String, List<PostageCalculate>> mergeByMID(List<PostageCalculate> postageCalculateList){
		Map<String, List<PostageCalculate>>result = new HashMap<String, List<PostageCalculate>>();
		for (PostageCalculate postageCalculate : postageCalculateList) {
			if(result.get(postageCalculate.getCalculatRule().getMID()) != null){
				result.get(postageCalculate.getCalculatRule().getMID()).add(postageCalculate);
			}else{
				List<PostageCalculate> list = new ArrayList<PostageCalculate>();
				list.add(postageCalculate);
				result.put(postageCalculate.getCalculatRule().getMID(), list);
			}
		}
		return result;
	}

	/**
	 * 合并同一运费模板
	 * @param postageCalculateList
	 * @return
	 * 2016年4月11日 qxs
	 */
	private List<PostageCalculate> mergeRepeat(List<PostageCalculate> postageCalculateList) {
		Map<String, PostageCalculate> map = new HashMap<String, PostageCalculate>();
		for (PostageCalculate postageCalculate : postageCalculateList) {
			PostageCalculate merged = map.get(postageCalculate.getPostageID());
			if(merged!=null){
				merged.setNumber(merged.getNumber()+postageCalculate.getNumber());
				merged.setWeight(merged.getWeight()+postageCalculate.getWeight());
				merged.setVolume(merged.getVolume()+postageCalculate.getVolume());
			}else{
				map.put(postageCalculate.getPostageID(), postageCalculate);
			}
		}
		List<PostageCalculate> result = new ArrayList<PostageCalculate>();
		for(String postageID:map.keySet()){
			result.add(map.get(postageID));
		}
		return result;
	}

	private List<Postage> getPostageByCalculateList(List<PostageCalculate> postageCalculateList){
		List<String> postageIDList = new ArrayList<String>();
		for (PostageCalculate postage : postageCalculateList) {
			postageIDList.add(postage.getPostageID());
		}
		return postageDao.selectMuti(postageIDList).stream().map( entity->{
			Postage bo = new Postage();
			BeanUtils.copyProperties(entity, bo);
			return bo;
		}).collect(Collectors.toList());
	}

	public static void main(String[] args) {
		List<PostageDetail> list = new ArrayList<PostageDetail>();
		PostageDetail p1 = new PostageDetail();
		p1.setFirstCost(new BigDecimal(100));
		PostageDetail p2 = new PostageDetail();
		p2.setFirstCost(new BigDecimal(300));
		PostageDetail p3 = new PostageDetail();
		p3.setFirstCost(new BigDecimal(200));
		list.add(p3);
		list.add(p2);
		list.add(p1);
		Collections.sort(list, new Comparator<PostageDetail>() {

			@Override
			public int compare(PostageDetail o1, PostageDetail o2) {
				
				return o2.getFirstCost().compareTo(o1.getFirstCost());
			}
		});
		for (PostageDetail postageDetail : list) {
			
			System.out.println(postageDetail.getFirstCost());
		}
	}

}
