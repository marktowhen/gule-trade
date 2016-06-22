package com.jingyunbank.etrade.marketing.auction.controller;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.Result;
import com.jingyunbank.core.web.Login;
import com.jingyunbank.etrade.api.marketing.auction.bo.AuctionPriceLog;
import com.jingyunbank.etrade.api.marketing.auction.bo.AuctionUser;
import com.jingyunbank.etrade.api.marketing.auction.service.IAuctionGoodsService;
import com.jingyunbank.etrade.api.marketing.auction.service.IAuctionPriceLog;
import com.jingyunbank.etrade.api.marketing.auction.service.IAuctionUserService;
import com.jingyunbank.etrade.api.marketing.auction.service.context.IAuctionContextService;
import com.jingyunbank.etrade.api.user.service.IAddressService;
import com.jingyunbank.etrade.marketing.auction.bean.AuctionPriceLogVO;
import com.jingyunbank.etrade.marketing.auction.bean.AuctionUserVO;

@RestController
@RequestMapping("/api/marketing/auction/my")
public class AuctionUserController {
	
	@Autowired
	private IAuctionPriceLog auctionPriceLogService;
	@Autowired
	private IAuctionUserService auctionUserService;
	@Autowired
	private IAddressService addressService;
	@Autowired
	private IAuctionContextService auctionContextService;
	@Autowired
	private IAuctionGoodsService auctionGoodsService;
	
	//list
		@RequestMapping(value="/list", method=RequestMethod.GET)
		public Result<List<AuctionUserVO>> list(@RequestParam(required=true) String status,HttpSession session,HttpServletRequest request) throws Exception{
			String userid=Login.UID(request);
			//userid="Ma9ogkIXSW-y0uSrvfqVIQ";
			if(status.equals("NEW")){
				return Result.ok(auctionUserService.selMyAuction(userid,"").stream().map(bo->{
					return getShowVOFromBo(bo);
				}).collect(Collectors.toList()));
				
				
				
			}else{
				return Result.ok(auctionUserService.selMyAuction(userid, status).stream().map(bo->{
					return getShowVOFromBo(bo);
				}).collect(Collectors.toList()));
			}
			//auctionUserService.selMyAuction(userid, status);
			//auctionUserService.selMyAuction(userid, status)
		}
		@RequestMapping(value="/ifSign", method=RequestMethod.GET)
		public Result<Integer> ifSign(@RequestParam(required=true) String auctionid,@RequestParam(required=true) String uid,HttpSession session,HttpServletRequest request) throws Exception{
			String userid=Login.UID(request);
			//userid="Ma9ogkIXSW-y0uSrvfqVIQ";
			if(null==uid||""==uid){
				uid=Login.UID(session);
			}
			 uid=userid;
			//auctionUserService.ifSign(auctionid, uid);
			return Result.ok(auctionUserService.ifSign(auctionid, uid));
		}
		@RequestMapping(value="/depositStatus", method=RequestMethod.GET)
		public Result<AuctionUserVO> depositStatus(@RequestParam(required=true) String auctionid,@RequestParam(required=true) String uid,HttpSession session,HttpServletRequest request) throws Exception{
			String userid=Login.UID(request);
			//userid="Ma9ogkIXSW-y0uSrvfqVIQ";
			if(null==uid||""==uid){
				uid=Login.UID(session);
			}
			uid=userid;
			Optional<AuctionUser> auctionUser=auctionUserService.selByUserId(auctionid, userid);
			if(auctionUser.isPresent()){
				return Result.ok(getShowVOFromBo(auctionUser.get()));
			}
			return Result.ok(new AuctionUserVO());
		}
		
		
		private AuctionUserVO getShowVOFromBo(AuctionUser showBo){
			AuctionUserVO vo = new AuctionUserVO();
			if(Objects.nonNull(showBo)){
				BeanUtils.copyProperties(showBo, vo);
			}
			return vo;
		}
		
}
