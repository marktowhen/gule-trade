package com.jingyunbank.etrade.marketing.auction.service.context;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.marketing.auction.bo.AuctionGoods;
import com.jingyunbank.etrade.api.marketing.auction.bo.AuctionUser;
import com.jingyunbank.etrade.api.marketing.auction.service.context.IAuctionContextService;
import com.jingyunbank.etrade.api.user.bo.Users;

@Service("auctionContextService")
public class AuctionContextService implements IAuctionContextService {

	@Override
	public boolean addDeposit(Users user, AuctionGoods auctionGoods) {
		// TODO Auto-generated method stub
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
