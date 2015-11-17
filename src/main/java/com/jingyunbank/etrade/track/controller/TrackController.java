package com.jingyunbank.etrade.track.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.Result;
import com.jingyunbank.core.web.ServletBox;
import com.jingyunbank.etrade.api.track.bo.CollectGoods;
import com.jingyunbank.etrade.api.track.bo.FootprintGoods;
import com.jingyunbank.etrade.api.track.service.ITrackService;
import com.jingyunbank.etrade.goods.bean.CommonGoodsVO;
import com.jingyunbank.etrade.track.bean.CollectMerchantVO;
import com.jingyunbank.etrade.track.bean.FootprintGoodsVO;

/**
 * 推广系统
 * @author liug
 *
 */
@RestController
@RequestMapping("/api/track")
public class TrackController {

	@Resource
	protected ITrackService trackService;
	 
	/**
	 * 我的足迹商品查询
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/footprint/list", method = RequestMethod.POST)
	public Result listFootprintGoods() throws Exception {
		try {
			List<FootprintGoods> goodslist = trackService.listFootprintGoods();
			FootprintGoodsVO footprintGoodsVO = new FootprintGoodsVO();
			footprintGoodsVO.init(goodslist);
			return Result.ok(footprintGoodsVO);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 我的足迹商品保存
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/footprint/save", method = RequestMethod.POST)
	public Result saveFootprintGoods(HttpServletRequest request, HttpSession session, String gid) throws Exception {
		String uid = ServletBox.getLoginUID(request);
		boolean flag = trackService.saveFootprint(uid, gid);
		if (flag) {
			return Result.ok("足迹保存成功！");
		} else {
			return Result.fail("足迹保存失败！");
		}
	}
	 
	/**
	 * 我的收藏商家保存
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/collect/savemerchant", method = RequestMethod.POST)
	public Result saveMerchantCollect(HttpServletRequest request, HttpSession session, String mid) throws Exception {
		boolean flag = false;
		String uid = ServletBox.getLoginUID(request);
		flag = trackService.isCollectExists(uid, mid, "1");
		if (flag) {
			return Result.fail("您已经收藏过该商家！");
		}
		flag = trackService.saveCollect(uid, mid, "1");
		if (flag) {
			return Result.ok("收藏成功！");
		} else {
			return Result.fail("收藏失败！");
		}
	}

	/**
	 * 我的收藏商品保存
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/collect/savegoods", method = RequestMethod.POST)
	public Result saveGoodsCollect(HttpServletRequest request, HttpSession session, String gid) throws Exception {
		boolean flag = false;
		String uid = ServletBox.getLoginUID(request);
		flag = trackService.isCollectExists(uid, gid, "2");
		if (flag) {
			return Result.fail("您已经收藏过该商品！");
		}
		flag = trackService.saveCollect(uid, gid, "2");
		if (flag) {
			return Result.ok("收藏成功！");
		} else {
			return Result.fail("收藏失败！");
		}
	}

	/**
	 * 查询我的收藏（商家）
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/collect/listmerchantcollect", method = RequestMethod.POST)
	public Result listMerchantCollect(HttpServletRequest request) throws Exception {
		String uid = ServletBox.getLoginUID(request);
		List<CollectMerchantVO> rltlist = new ArrayList<CollectMerchantVO>();
		List<CollectGoods> goodslist = trackService.listMerchantCollect(uid, "1");
		List<CollectGoods> tmplist = new ArrayList<CollectGoods>();// 存放商家分组LIST
		if (goodslist != null && goodslist.size() > 0) {// 将业务对象转换为页面VO对象
			CollectMerchantVO cmvo = new CollectMerchantVO();
			for (int i = 0; i < goodslist.size(); i++) {
				// 第一条处理
				if (i == 0) {
					tmplist.add(goodslist.get(i));
				} else if (!goodslist.get(i - 1).getMID().equals(goodslist.get(i).getMID())) {// 与上一家不是一家
					cmvo.init(tmplist);
					rltlist.add(cmvo);
					tmplist = new ArrayList<CollectGoods>();
					tmplist.add(goodslist.get(i));
					if (i == (goodslist.size() - 1)) {// 最后一条
						cmvo = new CollectMerchantVO();
						cmvo.init(tmplist);
						rltlist.add(cmvo);
					} else {
						cmvo = new CollectMerchantVO();
					}
				} else if (goodslist.get(i - 1).getMID().equals(goodslist.get(i).getMID())) {// 与上一家是一家
					tmplist.add(goodslist.get(i));
					if (i == (goodslist.size() - 1)) {// 最后一条
						cmvo = new CollectMerchantVO();
						cmvo.init(tmplist);
						rltlist.add(cmvo);
					}
				}

			}
		}
		return Result.ok(rltlist);
	}

	/**
	 * 查询我的收藏（商品）
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/collect/listgoodscollect", method = RequestMethod.POST)
	public Result listGoodsCollect(HttpServletRequest request) throws Exception {
			String uid = ServletBox.getLoginUID(request);
			List<CommonGoodsVO> rltlist = new ArrayList<CommonGoodsVO>();
			List<CollectGoods> goodslist = trackService.listMerchantCollect(uid, "2");
			if (goodslist != null && goodslist.size() > 0) {// 将业务对象转换为页面VO对象
				rltlist = goodslist.stream().map(bo -> {
					CommonGoodsVO vo = new CommonGoodsVO();
					try {
						BeanUtils.copyProperties(bo, vo);
					} catch (Exception e) {
						e.printStackTrace();
					}
					return vo;
				}).collect(Collectors.toList());
			}
			return Result.ok(rltlist);
	}
}
