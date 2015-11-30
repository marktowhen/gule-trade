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
import com.jingyunbank.etrade.track.bean.FavoritesGoodsFacadeVO;
import com.jingyunbank.etrade.track.bean.FavoritesGoodsVO;
import com.jingyunbank.etrade.track.bean.FavoritesMerchantFacadeVO;
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
	 * 我的足迹商品查询 传入条数和开始
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/footprint/list/{pcount}/{pfrom}", method = RequestMethod.GET)
	public Result listFootprintGoods(@PathVariable String pcount,@PathVariable String pfrom) throws Exception {
			//获取展示条数
			int count = Integer.valueOf(pcount);
			int from = Integer.valueOf(pfrom);
			List<FootprintGoods> goodslist = trackService.listFootprintGoods(from,count);
			FootprintGoodsVO footprintGoodsVO = new FootprintGoodsVO();
			footprintGoodsVO.init(goodslist);
			return Result.ok(footprintGoodsVO);
	}

	/**
	 * 我的足迹商品保存
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/footprint/save/{gid}", method = RequestMethod.GET)
	public Result saveFootprintGoods(HttpServletRequest request, HttpSession session,@PathVariable String gid) throws Exception {
		String uid = ServletBox.getLoginUID(request);
		if(uid == null || "".equals(uid)){
			return null;
		}
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
	@RequestMapping(value = "/favorites/listmerchantfavorites/{pcount}/{pfrom}", method = RequestMethod.GET)
	public Result listMerchantFavorites(HttpServletRequest request,@PathVariable String pcount,@PathVariable String pfrom) throws Exception {
		String uid = ServletBox.getLoginUID(request);
		//获取展示条数
		int count = Integer.valueOf(pcount);
		int from = Integer.valueOf(pfrom);
		List<FavoritesMerchantVO> rltlist = new ArrayList<FavoritesMerchantVO>();
		List<FavoritesGoods> goodslist = trackService.listMerchantFavorites(uid,"1",from,count);
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
		FavoritesMerchantFacadeVO fvo = new FavoritesMerchantFacadeVO();
		fvo.setCount(rltlist.size());
		fvo.setMerchantlist(rltlist);
		return Result.ok(fvo);
	}

	
	/**
	 * 查询我的收藏（商品）根据传入的值进行展示
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/favorites/listgoodsfavorites/{pcount}/{pfrom}", method = RequestMethod.GET)
	public Result listGoodsFavorites(HttpServletRequest request,@PathVariable String pcount,@PathVariable String pfrom) throws Exception {
			//获取展示条数
			int count = Integer.valueOf(pcount);
			int from = Integer.valueOf(pfrom);
			String uid = ServletBox.getLoginUID(request);
			List<FavoritesGoodsVO> rltlist = new ArrayList<FavoritesGoodsVO>();
			List<FavoritesGoods> goodslist = trackService.listMerchantFavorites(uid, "2" ,from,count);
			if (goodslist != null && goodslist.size() > 0) {// 将业务对象转换为页面VO对象
				rltlist = goodslist.stream().map(bo -> {
					FavoritesGoodsVO vo = new FavoritesGoodsVO();
					try {
						BeanUtils.copyProperties(bo, vo);
					} catch (Exception e) {
						e.printStackTrace();
					}
					return vo;
				}).collect(Collectors.toList());
			}
			FavoritesGoodsFacadeVO fvo = new FavoritesGoodsFacadeVO();
			fvo.setCount(rltlist.size());
			fvo.setGoodslist(rltlist);
			return Result.ok(fvo);
	}
	/**
	 * 删除我的收藏信息
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/favorites/remove/{id}", method=RequestMethod.DELETE)
	public Result removeFavorites(@PathVariable String id) throws Exception{
		trackService.removeFavoritesById(id);
		return Result.ok(id);
	}
}
