package com.jingyunbank.etrade.track.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.jingyunbank.core.KeyGen;
import com.jingyunbank.core.Range;
import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.exception.DataRemovingException;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.track.bo.AdDetail;
import com.jingyunbank.etrade.api.track.bo.AdModule;
import com.jingyunbank.etrade.api.track.bo.FavoritesGoods;
import com.jingyunbank.etrade.api.track.bo.FootprintGoods;
import com.jingyunbank.etrade.api.track.bo.RecommendGoods;
import com.jingyunbank.etrade.api.track.service.ITrackService;
import com.jingyunbank.etrade.goods.service.ServiceTemplate;
import com.jingyunbank.etrade.track.dao.TrackDao;
import com.jingyunbank.etrade.track.entity.AdDetailEntity;
import com.jingyunbank.etrade.track.entity.AdModuleEntity;
import com.jingyunbank.etrade.track.entity.FavoritesEntity;
import com.jingyunbank.etrade.track.entity.FavoritesGoodsVEntity;
import com.jingyunbank.etrade.track.entity.FootprintEntity;
import com.jingyunbank.etrade.track.entity.FootprintGoodsEntity;
import com.jingyunbank.etrade.track.entity.RecommendGoodsEntity;

/**
 * 
 * Title: 推广服务
 * 
 * @author liug
 * @date 2015年11月4日
 */
@Service("trackService")
public class TrackService extends ServiceTemplate implements ITrackService {
	@Resource
	private TrackDao trackDao;
	@Override
	public List<FootprintGoods> listFootprintGoods(int from,int to) throws Exception {
		this.from = from;
		this.to = to;
		Map<String, Integer> params = new HashMap<String,Integer>();
		params.put("from", this.from);
		params.put("to", this.to);
		List<FootprintGoods> rltlist = new ArrayList<FootprintGoods>();
		List<FootprintGoodsEntity> goodslist = trackDao.selectFootprintGoods(params);
		if (goodslist != null) {
			rltlist = goodslist.stream().map(eo -> {
				FootprintGoods bo = new FootprintGoods();
				try {
					BeanUtils.copyProperties(eo, bo);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return bo;
			}).collect(Collectors.toList());
		}
		return rltlist;
	}

	@Override
	public boolean saveFootprint(String uid, String gid) throws DataSavingException {
		FootprintEntity fe = new FootprintEntity();
		fe.setID(KeyGen.uuid());
		fe.setUID(uid);
		fe.setGID(gid);
		fe.setVisitTime(new Date());
		int result = 0;
		try {
			result = trackDao.insertFootprint(fe);
		} catch (Exception e) {
			throw new DataSavingException(e);
		}
		if (result > 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean saveFavorites(String uid,String fid,String type) throws DataSavingException {
		FavoritesEntity ce = new FavoritesEntity();
		ce.setID(KeyGen.uuid());
		ce.setUID(uid);
		ce.setFid(fid);
		ce.setType(type);//1商家2商品
		ce.setCollectTime(new Date());
		int result = 0;
		try {
			result = trackDao.insertFavorites(ce);
		} catch (Exception e) {
			throw new DataSavingException(e);
		}
		if(result > 0){
			return true;
		}
		return false;
	}
	
	@Override
	public boolean isFavoritesExists(String uid,String fid,String type)throws Exception{
		int rlt = 0;
		Map<String,String> map = new HashMap<String,String>();
		map.put("uid", uid);
		map.put("fid", fid);
		map.put("type", type);
		rlt = this.trackDao.isFavoritesExists(map);
		return rlt > 0 ? true : false;
	}
	
	@Override
	public List<FavoritesGoods> listMerchantFavorites(String uid,String type,int from,int to) throws Exception {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("uid", uid);
		map.put("type", type);
		this.from = from;
		this.to = to;
		map.put("from", this.from);
		map.put("to", this.to);
		List<FavoritesGoods> rltlist = new ArrayList<FavoritesGoods>();
		List<FavoritesGoodsVEntity> goodslist = trackDao.selectMerchantFavorites(map);
		if (goodslist != null) {
			rltlist = goodslist.stream().map(eo -> {
				FavoritesGoods bo = new FavoritesGoods();
				try {
					BeanUtils.copyProperties(eo, bo);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return bo;
			}).collect(Collectors.toList());
		}
		return rltlist;
	}
	
	@Override
	public int countMerchantFavorites(String uid,String type) throws Exception {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("uid", uid);
		map.put("type", type);
		int count = trackDao.selectMerchantFavoritesCount(map);
		return count;
	}

	@Override
	public boolean removeFavoritesById(List<String> id) throws DataRemovingException {
		boolean flag=false;
		try {
			flag = trackDao.deleteFavoritesById(id);
		} catch (Exception e) {
			throw new DataRemovingException(e);
		}
		return flag;
	}
	
	@Override
	public List<AdDetail> listAdDetails(String code) throws IllegalAccessException, InvocationTargetException {
		Map<String, String> params = new HashMap<String,String>();
		params.put("code", code);
		List<AdDetail> rlist = new ArrayList<AdDetail>();
		List<AdDetailEntity> list = trackDao.selectAdDetails(params);
		AdDetail bo = null;
		for(AdDetailEntity e : list){
			bo = new AdDetail();
			BeanUtils.copyProperties(e,bo);
			rlist.add(bo);
		}
		return rlist;
	}
	
	@Override
	public Optional<AdModule> getAdmoduleInfo(String id) {
		AdModuleEntity adModuleEntity = trackDao.selectAdmoduleById(id);
		AdModule adModule=new AdModule();
		BeanUtils.copyProperties(adModuleEntity, adModule);
		return Optional.of(adModule);
	}
	
	@Override
	public Optional<AdDetail> getAddetailInfo(String id) {
		AdDetailEntity adDetailEntity = trackDao.selectAddetailById(id);
		AdDetail adDetail=new AdDetail();
		BeanUtils.copyProperties(adDetailEntity, adDetail);
		return Optional.of(adDetail);
	}
	
	@Override
	public boolean saveAdmodule(AdModule adModule) throws DataSavingException {
		boolean flag=false;
		AdModuleEntity me = new AdModuleEntity();
		BeanUtils.copyProperties(adModule, me);
		try {
			if(trackDao.insertAdModule(me)){
				flag=true;
			}else{
				flag=false;
			}
		} catch (Exception e) {
			throw new DataSavingException(e);
		}
		return flag;
		
	}
	
	@Override
	public boolean saveAddetail(AdDetail adDetail) throws DataSavingException {
		boolean flag=false;
		AdDetailEntity me = new AdDetailEntity();
		BeanUtils.copyProperties(adDetail, me);
		try {
			if(trackDao.insertAdDetail(me)){
				flag=true;
			}else{
				flag=false;
			}
		} catch (Exception e) {
			throw new DataSavingException(e);
		}
		return flag;
	}
	
	@Override
	public boolean updateAdmodule(AdModule adModule) throws DataRefreshingException {
		boolean flag=false;
		AdModuleEntity me = new AdModuleEntity();
		BeanUtils.copyProperties(adModule, me);
		try {
			if(trackDao.updateAdmodule(me)){
				flag=true;
			}else{
				flag=false;
			}
		} catch (Exception e) {
			throw new DataRefreshingException(e);
		}
		return flag;
	}
	
	@Override
	public boolean updateAddetail(AdDetail adDetail) throws DataRefreshingException {
		boolean flag=false;
		AdDetailEntity me = new AdDetailEntity();
		BeanUtils.copyProperties(adDetail, me);
		try {
			if(trackDao.updateAddetail(me)){
				flag=true;
			}else{
				flag=false;
			}
		} catch (Exception e) {
			throw new DataRefreshingException(e);
		}
		return flag;
		
	}
	
	@Override
	public List<AdModule> listModulesByCondition(AdModule adModule, Range range) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("from", (int) range.getFrom());
		map.put("size", (int) range.getTo());
		map.put("name", adModule.getName());
		List<AdModule> showAdModuleList = trackDao.selectModulesByCondition(map).stream().map(eo -> {
			AdModule bo = new AdModule();
			BeanUtils.copyProperties(eo, bo);
			return bo;
		}).collect(Collectors.toList());
		return showAdModuleList;
	}
	
	@Override
	public List<AdDetail> listAddetailsByCondition(AdDetail adDetail, Range range) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("from", (int) range.getFrom());
		map.put("size", (int) range.getTo());
		map.put("name", adDetail.getName());
		map.put("adModuleId", adDetail.getAdModuleId());
		List<AdDetail> showAdDetailList = trackDao.selectAddetailsByCondition(map).stream().map(eo -> {
			AdDetail bo = new AdDetail();
			BeanUtils.copyProperties(eo, bo);
			return bo;
		}).collect(Collectors.toList());
		return showAdDetailList;
	}
	
	@Override
	public boolean removeAddetail(List<String> id) throws DataRemovingException {
		boolean flag=false;
		try {
			flag = trackDao.deleteAddetail(id);
		} catch (Exception e) {
			throw new DataRemovingException(e);
		}
		return flag;
	}
	@Override
	public boolean removeAdmodule(List<String> id) throws DataRemovingException {
		boolean flag=false;
		try {
			flag = trackDao.deleteAdmodule(id);
		} catch (Exception e) {
			throw new DataRemovingException(e);
		}
		return flag;
	}
	@Override
	public int queryAddetailsCount(List<String> id) throws Exception {
		int rlt=0;
			rlt = trackDao.selectAddetailsCount(id);
		return rlt;
	}
	
	public List<RecommendGoods> listRecommendGoods(String uid,int from,int to) throws Exception {
		this.from = from;
		this.to = to;
		Map<String, Object> params = new HashMap<String,Object>();
		params.put("uid", uid);
		params.put("from", this.from);
		params.put("to", this.to);
		List<RecommendGoods> rltlist = new ArrayList<RecommendGoods>();
		String bidstr = "";
		String tidstr = "";
		if(uid == null || "".equals(uid)){
			
		}else{
			//查询品牌字符串
		    bidstr = trackDao.selectRecommendBidstr(params).get("bidstr");
			//查询类别字符串
		    tidstr = trackDao.selectRecommendTidstr(params).get("tidstr");
		}
		List<String> bids = new ArrayList<String>();
		String tmpbidstr[] = bidstr.split(",");
		bids = Arrays.asList(tmpbidstr);
		List<String> tids = new ArrayList<String>();
		String tmptidstr[] = tidstr.split(",");
		tids = Arrays.asList(tmptidstr);
		//查询相关产品
		List<RecommendGoodsEntity> goodslist = trackDao.selectRecommendGoods(bids,tids,from,to,uid);
		if (goodslist != null) {
			rltlist = goodslist.stream().map(eo -> {
				RecommendGoods bo = new RecommendGoods();
				try {
					BeanUtils.copyProperties(eo, bo);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return bo;
			}).collect(Collectors.toList());
		}
		return rltlist;
	}
	
	
}

