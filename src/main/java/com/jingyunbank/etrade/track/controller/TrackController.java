package com.jingyunbank.etrade.track.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.KeyGen;
import com.jingyunbank.core.Page;
import com.jingyunbank.core.Range;
import com.jingyunbank.core.Result;
import com.jingyunbank.core.web.AuthBeforeOperation;
import com.jingyunbank.core.web.ServletBox;
import com.jingyunbank.etrade.api.track.bo.AdDetail;
import com.jingyunbank.etrade.api.track.bo.AdModule;
import com.jingyunbank.etrade.api.track.bo.FavoritesGoods;
import com.jingyunbank.etrade.api.track.bo.FootprintGoods;
import com.jingyunbank.etrade.api.track.service.ITrackService;
import com.jingyunbank.etrade.goods.bean.CommonGoodsVO;
import com.jingyunbank.etrade.track.bean.AdDetailVO;
import com.jingyunbank.etrade.track.bean.AdModuleVO;
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
	public Result<FootprintGoodsVO> listFootprintGoods(HttpServletRequest request,@PathVariable String pcount,@PathVariable String pfrom) throws Exception {
			//获取展示条数
			int count = Integer.valueOf(pcount);
			int from = Integer.valueOf(pfrom);
			String uid = ServletBox.getLoginUID(request);
			List<FootprintGoods> goodslist = trackService.listFootprintGoods(from,count,uid);
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
	public Result<?> saveFootprintGoods(HttpServletRequest request, HttpSession session,@PathVariable String gid) throws Exception {
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
	public Result<?> saveMerchantFavorites(HttpServletRequest request, HttpSession session, @PathVariable String mid) throws Exception {
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
	public Result<?> saveGoodsFavorites(HttpServletRequest request, HttpSession session,@PathVariable String gid) throws Exception {
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
	public Result<FavoritesMerchantFacadeVO> listMerchantFavorites(HttpServletRequest request,@PathVariable String pcount,@PathVariable String pfrom) throws Exception {
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
					if (i == (goodslist.size() - 1)) {// 最后一条
						cmvo = new FavoritesMerchantVO();
						cmvo.init(tmplist);
						rltlist.add(cmvo);
					}
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
		int tmpcount = trackService.countMerchantFavorites(uid, "1");
		FavoritesMerchantFacadeVO fvo = new FavoritesMerchantFacadeVO();
		fvo.setCount(tmpcount);
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
	public Result<FavoritesGoodsFacadeVO> listGoodsFavorites(HttpServletRequest request,@PathVariable String pcount,@PathVariable String pfrom) throws Exception {
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
			int tmpcount = trackService.countMerchantFavorites(uid, "2");
			fvo.setCount(tmpcount);
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
	public Result<String> removeFavorites(@PathVariable String id) throws Exception{
		List<String> ids = new ArrayList<String>();
		String tmpstr[] = id.split(",");
		ids = Arrays.asList(tmpstr);
		trackService.removeFavoritesById(ids);
		return Result.ok(id);
	}
	/**
	 * 推荐商家广告的检索
	 * @param request
	 * @param session
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	@RequestMapping(value="/ad/list/{code}", method = RequestMethod.GET)
	public Result<List<AdDetailVO>> listAdDetails(HttpServletRequest request, @PathVariable String code,HttpSession session) throws Exception{
		//转成VO
		List<AdDetail> list = trackService.listAdDetails(code);
		List<AdDetailVO> rlist = new ArrayList<AdDetailVO>();
		AdDetailVO vo = null;
		for(AdDetail bo : list){
			vo = new AdDetailVO();
			BeanUtils.copyProperties(bo,vo);
			rlist.add(vo);
		}
		return Result.ok(rlist);
	}
	/**
	 * 根据ID获取广告模块信息
	 * @param session
	 * @param request
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admodule/info/{id}",method=RequestMethod.GET)
	public Result<?> getAdmoduleInfo(HttpSession session,HttpServletRequest request,@PathVariable String id) throws Exception{
		Optional<AdModule> adModule= trackService.getAdmoduleInfo(id);
		if(adModule.isPresent()){
			AdModule bo = adModule.get();
			AdModuleVO vo = new AdModuleVO();
			BeanUtils.copyProperties(bo,vo);
			return Result.ok(vo);
		}else{
			return Result.fail("查询没有数据！");
		}
	}
	/**
	 * 根据ID获取广告信息
	 * @param session
	 * @param request
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/addetail/info/{id}",method=RequestMethod.GET)
	public Result<?> getAddetailInfo(HttpSession session,HttpServletRequest request,@PathVariable String id) throws Exception{
		Optional<AdDetail> adDetail= trackService.getAddetailInfo(id);
		if(adDetail.isPresent()){
			AdDetail bo = adDetail.get();
			AdDetailVO vo = new AdDetailVO();
			BeanUtils.copyProperties(bo,vo);
			return Result.ok(vo);
		}else{
			return Result.fail("查询没有数据！");
		}
	}
	/**
	 * 保存广告模块信息
	 * @param request
	 * @param session
	 * @param adModuleVO
	 * @param valid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admodule/saveAdmodule", method = RequestMethod.POST)
	public Result<AdModuleVO> saveAdmodule(HttpServletRequest request, HttpSession session,@RequestBody @Valid AdModuleVO adModuleVO,BindingResult valid) throws Exception {
		// 异常信息
		if (valid.hasErrors()) {
			List<ObjectError> errors = valid.getAllErrors();
			return Result.fail(errors.stream().map(oe -> Arrays.asList(oe.getDefaultMessage()).toString())
					.collect(Collectors.joining(" ; ")));
		}
		AdModule adModule = new AdModule();
		BeanUtils.copyProperties(adModuleVO, adModule);
		adModule.setID(KeyGen.uuid());
		if(trackService.saveAdmodule(adModule)){
			return Result.ok(adModuleVO);
		}
		return Result.ok(adModuleVO);
	}
	/**
	 * 保存广告信息
	 * @param request
	 * @param session
	 * @param adDetailVO
	 * @param valid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/addetail/saveAddetail", method = RequestMethod.POST)
	public Result<AdDetailVO> saveAddetail(HttpServletRequest request, HttpSession session,@RequestBody @Valid AdDetailVO adDetailVO,BindingResult valid) throws Exception {
		// 异常信息
		if (valid.hasErrors()) {
			List<ObjectError> errors = valid.getAllErrors();
			return Result.fail(errors.stream().map(oe -> Arrays.asList(oe.getDefaultMessage()).toString())
					.collect(Collectors.joining(" ; ")));
		}
		AdDetail adDetail = new AdDetail();
		BeanUtils.copyProperties(adDetailVO, adDetail);
		adDetail.setID(KeyGen.uuid());
		if(trackService.saveAddetail(adDetail)){
			return Result.ok(adDetailVO);
		}
		return Result.ok(adDetailVO);
	}
	/**
	 * 更新广告模块信息
	 * @param request
	 * @param session
	 * @param adModuleVO
	 * @param valid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admodule/updateAdmodule", method = RequestMethod.POST)
	public Result<AdModuleVO> updateAdmodule(HttpServletRequest request, HttpSession session,@RequestBody @Valid AdModuleVO adModuleVO,BindingResult valid) throws Exception{
		// 异常信息
		if (valid.hasErrors()) {
			List<ObjectError> errors = valid.getAllErrors();
			return Result.fail(errors.stream().map(oe -> Arrays.asList(oe.getDefaultMessage()).toString())
					.collect(Collectors.joining(" ; ")));
		}
		AdModule adModule = new AdModule();
		BeanUtils.copyProperties(adModuleVO, adModule);
		if(this.trackService.updateAdmodule(adModule)){
			return Result.ok(adModuleVO);
		}
		return Result.ok(adModuleVO);
	}
	/**
	 * 更新广告信息
	 * @param request
	 * @param session
	 * @param addetailVO
	 * @param valid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/addetail/updateAddetail", method = RequestMethod.POST)
	public Result<AdDetailVO> updateAddetail(HttpServletRequest request, HttpSession session,@RequestBody @Valid AdDetailVO addetailVO,BindingResult valid) throws Exception{
		// 异常信息
		if (valid.hasErrors()) {
			List<ObjectError> errors = valid.getAllErrors();
			return Result.fail(errors.stream().map(oe -> Arrays.asList(oe.getDefaultMessage()).toString())
					.collect(Collectors.joining(" ; ")));
		}
		AdDetail addetail = new AdDetail();
		BeanUtils.copyProperties(addetailVO, addetail);
		if(this.trackService.updateAddetail(addetail)){
			return Result.ok(addetailVO);
		}
		return Result.ok(addetailVO);
	}
	/**
	 * 查询广告模块列表
	 * @param request
	 * @param adModuleVO
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admodule/list", method = RequestMethod.GET)
	public Result<List<AdModuleVO>> queryAdmoduleList(HttpServletRequest request, AdModuleVO adModuleVO, Page page)
			throws Exception {
		Range range = new Range();
		range.setFrom(page.getOffset());
		range.setTo(page.getSize());
		AdModule adModule = new AdModule();
		BeanUtils.copyProperties(adModuleVO, adModule);
		
		List<AdModuleVO> adModulelist = trackService.listModulesByCondition(adModule, range).stream().map(bo -> {
			AdModuleVO vo = new AdModuleVO();
			BeanUtils.copyProperties(bo, vo);
			return vo;
		}).collect(Collectors.toList());
		
		return Result.ok(adModulelist);
	}
	
	@RequestMapping(value = "/addetail/list", method = RequestMethod.GET)
	public Result<List<AdDetailVO>> queryAddetailList(HttpServletRequest request, AdDetailVO adDetailVO, Page page)
			throws Exception {
		Range range = new Range();
		range.setFrom(page.getOffset());
		range.setTo(page.getSize());
		AdDetail adDetail = new AdDetail();
		BeanUtils.copyProperties(adDetailVO, adDetail);
		
		List<AdDetailVO> adDetaillist = trackService.listAddetailsByCondition(adDetail, range).stream().map(bo -> {
			AdDetailVO vo = new AdDetailVO();
			BeanUtils.copyProperties(bo, vo);
			return vo;
		}).collect(Collectors.toList());
		
		return Result.ok(adDetaillist);
	}
	/**
	 * 删除广告
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/addetail/remove/{id}", method=RequestMethod.DELETE)
	public Result<String> removeAddetail(@PathVariable String id) throws Exception{
		List<String> ids = new ArrayList<String>();
		String tmpstr[] = id.split(",");
		ids = Arrays.asList(tmpstr);
		trackService.removeAddetail(ids);
		return Result.ok(id);
	}
	/**
	 * 删除广告模块
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admodule/remove/{id}", method=RequestMethod.DELETE)
	public Result<String> removeAdmodule(@PathVariable String id) throws Exception{
		List<String> ids = new ArrayList<String>();
		String tmpstr[] = id.split(",");
		ids = Arrays.asList(tmpstr);
		int rlt = trackService.queryAddetailsCount(ids);
		if(rlt <= 0){
			trackService.removeAdmodule(ids);
			return Result.ok(id);
		}else{
			return Result.fail("请先删除该模块下的广告信息！");
		}
	}
	
	/**
	 * 列举为您推荐的商品
	 * @param pcount
	 * @param pfrom
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/recommend/list/{pcount}/{pfrom}", method = RequestMethod.GET)
	public Result<List<CommonGoodsVO>> listRecommendGoods(HttpServletRequest request,@PathVariable String pcount,@PathVariable String pfrom) throws Exception {
			String uid = ServletBox.getLoginUID(request);
			//获取展示条数
			int count = Integer.valueOf(pcount);
			int from = Integer.valueOf(pfrom);
			List<CommonGoodsVO> goodslist = trackService.listRecommendGoods(uid,from,count).stream().map(bo -> {
				CommonGoodsVO vo = new CommonGoodsVO();
				BeanUtils.copyProperties(bo, vo);
				return vo;
			}).collect(Collectors.toList());
			return Result.ok(goodslist);
	}
	/**
	 * 买过该商品的用户还买了
	 * @param request
	 * @param pcount
	 * @param pfrom
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/othergoods/list/{pcount}/{pfrom}", method = RequestMethod.GET)
	public Result<List<CommonGoodsVO>> listOtherGoods(HttpServletRequest request,@PathVariable String gid,@PathVariable String pcount,@PathVariable String pfrom) throws Exception {
			String uid = ServletBox.getLoginUID(request);
			//获取展示条数
			int count = Integer.valueOf(pcount);
			int from = Integer.valueOf(pfrom);
			List<CommonGoodsVO> goodslist = trackService.listOtherGoods(gid,uid,from,count).stream().map(bo -> {
				CommonGoodsVO vo = new CommonGoodsVO();
				BeanUtils.copyProperties(bo, vo);
				return vo;
			}).collect(Collectors.toList());
			return Result.ok(goodslist);
	}
}
