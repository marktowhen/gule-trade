package com.jingyunbank.etrade.logistic.service.context;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.logistic.bo.Postage;
import com.jingyunbank.etrade.api.logistic.bo.PostageCalculate;
import com.jingyunbank.etrade.api.logistic.bo.PostageDetail;
import com.jingyunbank.etrade.api.logistic.service.IPostageDetailService;
import com.jingyunbank.etrade.api.logistic.service.IPostageService;
import com.jingyunbank.etrade.api.logistic.service.context.IPostageCalculateRuleService;
import com.jingyunbank.etrade.api.logistic.service.context.IPostageCalculateService;

@Service("postageCalculateService")
public class PostageCalculateService implements IPostageCalculateService {

	@Autowired
	private IPostageService postageService;
	@Autowired
	private IPostageDetailService postageDetailService;
	@Autowired
	private List<IPostageCalculateRuleService> postageCalculateRuleServiceList;
	
	@Override
	public BigDecimal calculate(PostageCalculate postageCalculate) {
		Postage postage = postageService.singleWithDetail(postageCalculate.getPostageID(), postageCalculate.getCity(),postageCalculate.getTransportType());
		if(postage!=null){
			//return calculateFirstCost(postage.getPostageDetail()).add(calculateNextCost(postageCalculate, postage, postage.getPostageDetail()));
			for (IPostageCalculateRuleService postageCaculateService : postageCalculateRuleServiceList) {
				if(postageCaculateService.matches(postage, postage.getPostageDetail())){
					return postageCaculateService.calculateFirstCost(postage.getPostageDetail()).add(postageCaculateService.calculateNextCost(postageCalculate, postage.getPostageDetail()));
				}
			}
		}
		return null;
		
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
		//匹配运费详情
		for(PostageCalculate calculate : mergeRepeat){
			calculate.setCity(city);
			PostageDetail postageDetail = postageDetailService.singleFit(calculate.getPostageID(), city,calculate.getTransportType());
			calculate.getCalculatRule().setPostageDetail(postageDetail);
		}
		
		//根据首费由大到小排序
		Collections.sort(mergeRepeat, new Comparator<PostageCalculate>() {
			@Override
			public int compare(PostageCalculate o1, PostageCalculate o2) {
				return o2.getCalculatRule().getPostageDetail().getFirstCost().compareTo(o1.getCalculatRule().getPostageDetail().getFirstCost());
			}
		});
		
		//计算逻辑
		for (int i = 0; i < mergeRepeat.size(); i++) {
			PostageCalculate postageCalculate = mergeRepeat.get(i);
			for (IPostageCalculateRuleService postageCaculateService : postageCalculateRuleServiceList) {
				//找到匹配的service
				if(postageCaculateService.matches(postageCalculate.getCalculatRule(), postageCalculate.getCalculatRule().getPostageDetail())){
					//首费只计算一次
					if(i==0){
						result = result.add(postageCaculateService.calculateFirstCost(mergeRepeat.get(i).getCalculatRule().getPostageDetail()));
					}
					//续费计算
					result = result.add(postageCaculateService.calculateNextCost(mergeRepeat.get(i), mergeRepeat.get(i).getCalculatRule().getPostageDetail()));
					break;
				}
			}
		}
		return result;
	}
	
	/**
	 * 根据PostageCalculate.postageID匹配对应的运费模板
	 * @param postageCalculateList
	 * @param postageList
	 * @return
	 * 2016年4月13日 qxs
	 */
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

	/**
	 * 合并同一店铺的数据
	 * @param postageCalculateList
	 * @return
	 * 2016年4月13日 qxs
	 */
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
		return postageService.list(postageIDList);
	}

}
