package com.jingyunbank.etrade.marketing.rankgroup.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.core.KeyGen;
import com.jingyunbank.core.Result;
import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.marketing.group.bo.Group;
import com.jingyunbank.etrade.api.marketing.rankgroup.bo.RankGroup;
import com.jingyunbank.etrade.api.marketing.rankgroup.bo.RankGroupGoods;
import com.jingyunbank.etrade.api.marketing.rankgroup.bo.RankGroupOrder;
import com.jingyunbank.etrade.api.marketing.rankgroup.bo.RankGroupUser;
import com.jingyunbank.etrade.api.marketing.rankgroup.service.IRankGroupService;
import com.jingyunbank.etrade.api.order.presale.bo.Orders;
import com.jingyunbank.etrade.api.user.bo.Users;
import com.jingyunbank.etrade.marketing.rankgroup.dao.RankGroupDao;
import com.jingyunbank.etrade.marketing.rankgroup.entity.RankGroupEntity;

@Service("rankGroupService")
public class RankGroupService implements IRankGroupService{
	
	@Autowired
	RankGroupOrderService rankGroupOrderService;
	@Autowired
	RankGroupGoodsService rankGroupGoodsService;
	@Autowired
	RankGroupUserService rankGroupUserService;
	@Autowired
	RankGroupDao rankGroupDao;
	
	@Override
	public void start(Users leader, RankGroup group, Orders orders)
			throws DataSavingException, DataRefreshingException {
		group.setLeaderUID(leader.getID());
		group.setStart(new Date());
		//save group.
		this.save(group);
		RankGroupUser user = new RankGroupUser();
		user.setID(KeyGen.uuid());
		user.setGroupID(group.getID());
		user.setJointime(new Date());
		user.setUID(leader.getID());
		user.setPaid(group.getRankGoods().getDeposit());
		user.setStatus(RankGroupUser.STATUS_NEW);
		//save group user
		rankGroupUserService.save(user);
		
		RankGroupOrder go = new RankGroupOrder();
		go.setGroupID(group.getID());
		go.setGroupUserID(user.getID());
		go.setID(KeyGen.uuid());
		go.setOID(orders.getID());
		go.setOrderno(orders.getOrderno());
		go.setType(RankGroupOrder.TYPE_DEPOSIT);
		rankGroupOrderService.save(go);
	}

	@Override
	public void join(Users user, RankGroup group, Orders orders) throws DataSavingException, DataRefreshingException {
		RankGroupUser guser = new RankGroupUser();
		guser.setID(KeyGen.uuid());
		guser.setGroupID(group.getID());
		guser.setJointime(new Date());
		guser.setUID(user.getID());
		guser.setPaid(group.getRankGoods().getDeposit());
		guser.setStatus(RankGroupUser.STATUS_NEW);
		//save group user
		rankGroupUserService.save(guser);
		
		RankGroupOrder go = new RankGroupOrder();
		go.setGroupID(group.getID());
		go.setGroupUserID(guser.getID());
		go.setID(KeyGen.uuid());
		go.setOID(orders.getID());
		go.setOrderno(orders.getOrderno());
		go.setType(RankGroupOrder.TYPE_DEPOSIT);
		rankGroupOrderService.save(go);
		
	}

	@Override
	public List<RankGroup> listOnDeadline() {
		return null;
	}

	@Override
	public Result<String> startMatch(RankGroupGoods groupGoods) {
		if(groupGoods.getDeadline().before(new Date())){
			return Result.fail("该团购已结束,请及时参加其他活动");
		}
		
		if(!groupGoods.isShow()){
			return Result.fail("团购商品未上架");
		}
		
		return Result.ok();
	}

	@Override
	public Optional<RankGroup> single(String groupid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Result<String> joinMatch(RankGroup group) {
		if(!Group.STATUS_CONVENING.equals(group.getStatus())){
			return Result.fail("团购非召集中,请选择其他团购");
		}
		//团购人数
		Optional<RankGroupGoods> gg = rankGroupGoodsService.single(group.getGroupGoodsID());
		if(!gg.isPresent()){
			return Result.fail("团购商品不存在,请选择其他团购");
		}
		if(!gg.get().isShow()){
			return Result.fail("团购商品未上架");
		}
		
		if(gg.get().getDeadline().before(new Date())){
			return Result.fail("该团购已结束,请及时参加其他活动");
		}
		return Result.ok();
	}

	@Override
	public boolean save(RankGroup group) throws DataSavingException {
		RankGroupEntity entity = new RankGroupEntity();
		BeanUtils.copyProperties(group, entity);
		try {
			return rankGroupDao.insert(entity);
		} catch (Exception e) {
			throw new DataSavingException(e);
		}
	}

	
	
	@Override
	public boolean full(String groupID) {
		Optional<RankGroup> group = this.single(groupID);
		if(group.isPresent()){
			Optional<RankGroupGoods> goods = rankGroupGoodsService.single(group.get().getGroupGoodsID());
			//List<RankGroupUser> gUser = rankGroupUserService.list(groupID,RankGroupUser.STATUS_PAID);
			if(goods.isPresent()){
				return true;
			}
		}
		
		return false;
	}
	
	
	

}
