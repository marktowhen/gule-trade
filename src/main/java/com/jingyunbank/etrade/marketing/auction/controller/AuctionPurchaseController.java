package com.jingyunbank.etrade.marketing.auction.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.Result;
import com.jingyunbank.etrade.api.marketing.auction.bo.AuctionPriceLog;
import com.jingyunbank.etrade.api.marketing.auction.service.IAuctionPriceLog;
import com.jingyunbank.etrade.marketing.auction.bean.AuctionPriceLogVO;

@RestController
@RequestMapping("/api/marketing/auction/purchase")
public class AuctionPurchaseController {
	
	@Autowired
	private IAuctionPriceLog auctionPriceLogService;
	
	//加价
	
	
	    /**
	     * 查询竞拍出价次数
	     * @param auctionGoodsID
	     * @return
	     * @throws Exception
	     */
		@RequestMapping(value="/count", method=RequestMethod.GET)
		public Result<Integer> count(
				@RequestParam String auctionGoodsID) throws Exception{
			return Result.ok(new Integer(auctionPriceLogService.list(auctionGoodsID).size()));
		}
		
		/**
		 * 查询当前竞拍出价记录
		 * @param auctionGoodsID
		 * @return
		 * @throws Exception
		 */
		@RequestMapping(value="/list", method=RequestMethod.GET)
		public Result<List<AuctionPriceLogVO>> list(@RequestParam String auctionGoodsID) throws Exception{
			List<AuctionPriceLog> list=auctionPriceLogService.list(auctionGoodsID);
			List<AuctionPriceLogVO> listvo=new ArrayList<AuctionPriceLogVO>();
			if(list!=null&&list.size()>0){
				for (int i = 0; i < list.size(); i++) {
					listvo.add(getShowVOFromBo(list.get(i)));
					
				}
			}
			return Result.ok(listvo);
		}
		
		
		
		private AuctionPriceLogVO getShowVOFromBo(AuctionPriceLog showBo){
			AuctionPriceLogVO vo = new AuctionPriceLogVO();
			if(Objects.nonNull(showBo)){
				BeanUtils.copyProperties(showBo, vo);
				
			}
			return vo;
		}
}
