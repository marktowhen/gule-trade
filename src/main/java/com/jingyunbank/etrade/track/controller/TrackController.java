package com.jingyunbank.etrade.track.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.Result;
import com.jingyunbank.core.web.AuthBeforeOperation;
import com.jingyunbank.core.web.ServletBox;
import com.jingyunbank.etrade.api.track.bo.FavoritesGoods;
import com.jingyunbank.etrade.api.track.bo.FootprintGoods;
import com.jingyunbank.etrade.api.track.service.ITrackService;
import com.jingyunbank.etrade.goods.bean.CommonGoodsVO;
import com.jingyunbank.etrade.track.bean.FavoritesMerchantVO;
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
	@RequestMapping(value = "/footprint/list", method = RequestMethod.GET)
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
	@AuthBeforeOperation
	@RequestMapping(value = "/footprint/save/{gid}", method = RequestMethod.GET)
	public Result saveFootprintGoods(HttpServletRequest request, HttpSession session,@PathVariable String gid) throws Exception {
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
	@RequestMapping(value = "/favorites/savemerchant/{mid}", method = RequestMethod.GET)
	@AuthBeforeOperation
	public Result saveMerchantFavorites(HttpServletRequest request, HttpSession session, @PathVariable String mid) throws Exception {
		boolean flag = false;
		String uid = ServletBox.getLoginUID(request);
		flag = trackService.isFavoritesExists(uid, mid, "1");
		if (flag) {
			return Result.fail("您已经收藏过该商家！");
		}
		flag = trackService.saveFavorites(uid, mid, "1");
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
	@RequestMapping(value = "/favorites/savegoods/{gid}", method = RequestMethod.GET)
	@AuthBeforeOperation
	public Result saveGoodsFavorites(HttpServletRequest request, HttpSession session,@PathVariable String gid) throws Exception {
		boolean flag = false;
		String uid = ServletBox.getLoginUID(request);
		flag = trackService.isFavoritesExists(uid, gid, "2");
		if (flag) {
			return Result.fail("您已经收藏过该商品！");
		}
		flag = trackService.saveFavorites(uid, gid, "2");
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
	@RequestMapping(value = "/favorites/listmerchantfavorites", method = RequestMethod.GET)
	public Result listMerchantFavorites(HttpServletRequest request) throws Exception {
		String uid = ServletBox.getLoginUID(request);
		List<FavoritesMerchantVO> rltlist = new ArrayList<FavoritesMerchantVO>();
		List<FavoritesGoods> goodslist = trackService.listMerchantFavorites(uid, "1");
		List<FavoritesGoods> tmplist = new ArrayList<FavoritesGoods>();// 存放商家分组LIST
		if (goodslist != null && goodslist.size() > 0) {// 将业务对象转换为页面VO对象
			FavoritesMerchantVO cmvo = new FavoritesMerchantVO();
			for (int i = 0; i < goodslist.size(); i++) {
				// 第一条处理
				if (i == 0) {
					tmplist.add(goodslist.get(i));
				} else if (!goodslist.get(i - 1).getMID().equals(goodslist.get(i).getMID())) {// 与上一家不是一家
					cmvo.init(tmplist);
					rltlist.add(cmvo);
					tmplist = new ArrayList<FavoritesGoods>();
					tmplist.add(goodslist.get(i));
					if (i == (goodslist.size() - 1)) {// 最后一条
						cmvo = new FavoritesMerchantVO();
						cmvo.init(tmplist);
						rltlist.add(cmvo);
					} else {
						cmvo = new FavoritesMerchantVO();
					}
				} else if (goodslist.get(i - 1).getMID().equals(goodslist.get(i).getMID())) {// 与上一家是一家
					tmplist.add(goodslist.get(i));
					if (i == (goodslist.size() - 1)) {// 最后一条
						cmvo = new FavoritesMerchantVO();
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
	@RequestMapping(value = "/favorites/listgoodsfavorites", method = RequestMethod.GET)
	public Result listGoodsFavorites(HttpServletRequest request) throws Exception {
			String uid = ServletBox.getLoginUID(request);
			List<CommonGoodsVO> rltlist = new ArrayList<CommonGoodsVO>();
			List<FavoritesGoods> goodslist = trackService.listMerchantFavorites(uid, "2");
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
