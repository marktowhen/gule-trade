package com.jingyunbank.etrade.track.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.jingyunbank.core.KeyGen;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.track.bo.CollectGoods;
import com.jingyunbank.etrade.api.track.bo.FootprintGoods;
import com.jingyunbank.etrade.api.track.service.ITrackService;
import com.jingyunbank.etrade.track.dao.TrackDao;
import com.jingyunbank.etrade.track.entity.CollectEntity;
import com.jingyunbank.etrade.track.entity.CollectGoodsVEntity;
import com.jingyunbank.etrade.track.entity.FootprintEntity;
import com.jingyunbank.etrade.track.entity.FootprintGoodsEntity;

/**
 * 
 * Title: 商品接口
 * 
 * @author duanxf
 * @date 2015年11月4日
 */
@Service("trackService")
public class TrackService implements ITrackService {
	@Resource
	private TrackDao trackDao;
	@Override
	public List<FootprintGoods> listFootprintGoods() throws Exception {
		List<FootprintGoods> rltlist = new ArrayList<FootprintGoods>();
		List<FootprintGoodsEntity> goodslist = trackDao.selectFootprintGoods();
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
	public boolean saveCollect(String uid,String fid,String type) throws DataSavingException {
		CollectEntity ce = new CollectEntity();
		ce.setID(KeyGen.uuid());
		ce.setUID(uid);
		ce.setFid(fid);
		ce.setType(type);//1商家2商品
		ce.setCollectTime(new Date());
		int result = 0;
		try {
			result = trackDao.insertCollect(ce);
		} catch (Exception e) {
			throw new DataSavingException(e);
		}
		if(result > 0){
			return true;
		}
		return false;
	}
	
	@Override
	public boolean isCollectExists(String uid,String fid,String type)throws Exception{
		int rlt = 0;
		Map<String,String> map = new HashMap<String,String>();
		map.put("uid", uid);
		map.put("fid", fid);
		map.put("type", type);
		rlt = this.trackDao.isCollectExists(map);
		return rlt > 0 ? true : false;
	}
	
	@Override
	public List<CollectGoods> listMerchantCollect(String uid,String type) throws Exception {
		Map<String,String> map = new HashMap<String,String>();
		map.put("uid", uid);
		map.put("type", type);
		List<CollectGoods> rltlist = new ArrayList<CollectGoods>();
		List<CollectGoodsVEntity> goodslist = trackDao.selectMerchantCollect(map);
		if (goodslist != null) {
			rltlist = goodslist.stream().map(eo -> {
				CollectGoods bo = new CollectGoods();
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
