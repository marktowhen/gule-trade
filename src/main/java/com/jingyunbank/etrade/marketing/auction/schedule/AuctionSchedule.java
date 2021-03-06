package com.jingyunbank.etrade.marketing.auction.schedule;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.jingyunbank.core.Range;
import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.marketing.auction.bo.AuctionGoods;
import com.jingyunbank.etrade.api.marketing.auction.bo.AuctionPriceLog;
import com.jingyunbank.etrade.api.marketing.auction.service.IAuctionGoodsService;
import com.jingyunbank.etrade.api.marketing.auction.service.IAuctionPriceLog;
import com.jingyunbank.etrade.api.marketing.auction.service.context.IAuctionContextService;

@Component
public class AuctionSchedule {

	@Autowired
	IAuctionGoodsService auctionGoodsService;
	@Autowired
	IAuctionContextService auctionContextService;
	@Autowired
	IAuctionPriceLog auctionPriceLogService;

	    /**
	    * 一分钟内到期的竞拍
	    */
	    @Scheduled(fixedRate = 600*1000)
	     public void expire() {
	    	auctionContextService.expire();
	     }
	    
	    
	    @Scheduled(fixedRate = 600*1000)
	    public void refreshStatus() throws DataRefreshingException {
	    	List<AuctionGoods> auctionGoodsList=auctionGoodsService.list(new Range(0,2));
	    	if(null!=auctionGoodsList&&auctionGoodsList.size()>0){
	    		for (int i = 0; i < auctionGoodsList.size(); i++) {
	    			if(auctionGoodsList.get(i).getStartTime().getTime()>new Date().getTime()){
	    				auctionGoodsService.refreshStatus(auctionGoodsList.get(i).getID(), "NEW");
	    			}else if(auctionGoodsList.get(i).getStartTime().getTime()<=new Date().getTime()&&auctionGoodsList.get(i).getEndTime().getTime()>new Date().getTime()){
	    				auctionGoodsService.refreshStatus(auctionGoodsList.get(i).getID(), "AUCTIONING");
	    			}else if(auctionGoodsList.get(i).getEndTime().getTime()<=new Date().getTime()){
	    				List<AuctionPriceLog> priceLog=auctionPriceLogService.list(auctionGoodsList.get(i).getID());
	    				if(null!=priceLog&&priceLog.size()>0){
	                      if(!auctionGoodsList.get(i).getStatus().equals("OVER")){
	                    	  auctionGoodsService.refreshSoldPrice(auctionGoodsList.get(i).getID(),priceLog.get(0).getPrice(), priceLog.get(0).getUID());
	                    	  auctionGoodsService.refreshStatus(auctionGoodsList.get(i).getID(), "TOPAY");
	                      }
	    				}else{
	    					auctionGoodsService.refreshStatus(auctionGoodsList.get(i).getID(), "OVER");
	    				}
	    				
	    			}
	    			
	    		}
	    	}
	    }
	    
		//开始竞拍
		
		//竞拍结束
		
		//退还押金
		
		//尾款支付超时
	
}
