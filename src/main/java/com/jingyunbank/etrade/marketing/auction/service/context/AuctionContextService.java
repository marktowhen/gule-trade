package com.jingyunbank.etrade.marketing.auction.service.context;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.core.KeyGen;
import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.marketing.auction.bo.AuctionGoods;
import com.jingyunbank.etrade.api.marketing.auction.bo.AuctionOrder;
import com.jingyunbank.etrade.api.marketing.auction.bo.AuctionPriceLog;
import com.jingyunbank.etrade.api.marketing.auction.bo.AuctionUser;
import com.jingyunbank.etrade.api.marketing.auction.service.IAuctionGoodsService;
import com.jingyunbank.etrade.api.marketing.auction.service.IAuctionOrderService;
import com.jingyunbank.etrade.api.marketing.auction.service.IAuctionPriceLog;
import com.jingyunbank.etrade.api.marketing.auction.service.IAuctionUserService;
import com.jingyunbank.etrade.api.marketing.auction.service.context.IAuctionContextService;
import com.jingyunbank.etrade.api.order.presale.bo.Orders;
import com.jingyunbank.etrade.api.user.bo.Users;
import com.jingyunbank.etrade.api.user.service.IUserService;

@Service("auctionContextService")
public class AuctionContextService implements IAuctionContextService {
    @Autowired
	private IAuctionUserService auctionUserService;
    @Autowired
    private IAuctionOrderService auctionOrderService;
    @Autowired
    private IAuctionGoodsService auctionGoodsService;
    @Autowired
    private IAuctionPriceLog auctionPriceLogService;
    @Autowired
    private IUserService userService;
	
	@Override
	public boolean signUp(AuctionUser auctionUser, Orders orders) throws DataSavingException,DataRefreshingException{
		
		if(auctionUser!=null&&orders!=null){
			boolean saveUser=auctionUserService.save(auctionUser);
			
			AuctionOrder auctionOrder=new AuctionOrder();
			auctionOrder.setID(KeyGen.uuid()); 
			auctionOrder.setAuctionUserID(auctionUser.getID());//auction_user 表 ID
			auctionOrder.setAuctionGoodsID(auctionUser.getAuctionGoodsID());//auction_goods表ID
			auctionOrder.setOID(orders.getID());//订单ID
			auctionOrder.setOrderno(orders.getOrderno());//订单单号
			auctionOrder.setType(AuctionOrder.TYPE_DEPOSIT);//订单类型 ---定金
			
			boolean saveOrder=auctionOrderService.save(auctionOrder);
			if(saveUser&&saveOrder){
				return true;
			}
		}
		
		return false;
	}
    
	@Override
	public boolean bidding(AuctionPriceLog priceLog) {
		
		return false;
	}
    
	
	@Override
	public void delayed(String auctionid) throws DataRefreshingException {
		Optional<AuctionGoods> auctionGoods=auctionGoodsService.single(auctionid);
		AuctionGoods auctionGood;
		if(auctionGoods.isPresent()){
			auctionGood=auctionGoods.get();
			//Date startTime=auctionGood.getStartTime();
			Date endTime=auctionGood.getEndTime();
			int delayTime=auctionGood.getDelaySecond();
			if((endTime.getTime()-new Date().getTime())/1000<delayTime){
				auctionGood.setEndTime(new Date((new Date().getTime()+delayTime*1000)));
				auctionGoodsService.delay(auctionGood);
			}else{
				System.out.println("time is  enough");
				/*System.out.println("不用推迟 。。。。。。。。");
				auctionGood.setEndTime(new Date((new Date().getTime()+delayTime*9000)));
				System.out.println(auctionGood.getEndTime()+"time is");*/
				//auctionGoodsService.delay(auctionGood);
				
			}
			
		}
		
	}

	@Override
	public boolean payFinal(AuctionUser auctionUser,Orders orders) throws DataSavingException, DataRefreshingException {
		AuctionOrder auctionOrder=new AuctionOrder();
		auctionOrder.setID(KeyGen.uuid()); 
		auctionOrder.setAuctionUserID(auctionUser.getID());//auction_user 表 ID
		auctionOrder.setAuctionGoodsID(auctionUser.getAuctionGoodsID());//auction_goods表ID
		auctionOrder.setOID(orders.getID());//订单ID
		auctionOrder.setOrderno(orders.getOrderno());//订单单号
		auctionOrder.setType(AuctionOrder.TYPE_DEPOSIT);//订单类型 ---定金
		
		boolean saveOrder=auctionOrderService.save(auctionOrder);
		return false;
	}
     
	
	
	@Override
	public void expire() {
		List<AuctionGoods> list=auctionGoodsService.listOnDead();
		List<AuctionPriceLog> listPriceLog;
		if(list!=null){
			for (int i = 0; i < list.size(); i++) {
				list.get(i).getID();
				listPriceLog=auctionPriceLogService.list(list.get(i).getID());
				if(listPriceLog!=null&&listPriceLog.size()>0){
					//listPriceLog.get(i).getAuctionUserID();
					Users user=userService.selUserByUserId(listPriceLog.get(i).getAuctionUserID());
					if(i==0){
						System.out.println("胜出者发送通知"+user.getOpenId());
					}else{
						System.out.println("出局者发送通知  "+user.getOpenId());
					}
				}
				
			}
		}
		
		
	}

	@Override
	public boolean addDeposit(Users user, AuctionGoods auctionGoods) {
		
		return false;
	}

	@Override
	public boolean submitTender(AuctionUser auctionUser, BigDecimal price) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean refoundDeposit(AuctionUser auctionUser) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean payBalance(Users bargainee, AuctionGoods goods) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean payBalanceTimeout(AuctionGoods goods) {
		// TODO Auto-generated method stub
		return false;
	}

}
