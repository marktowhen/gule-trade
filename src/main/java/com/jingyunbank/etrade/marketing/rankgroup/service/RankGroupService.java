package com.jingyunbank.etrade.marketing.rankgroup.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.core.KeyGen;
import com.jingyunbank.core.Result;
import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.marketing.group.bo.Group;
import com.jingyunbank.etrade.api.marketing.group.bo.GroupUser;
import com.jingyunbank.etrade.api.marketing.rankgroup.bo.RankGroup;
import com.jingyunbank.etrade.api.marketing.rankgroup.bo.RankGroupGoods;
import com.jingyunbank.etrade.api.marketing.rankgroup.bo.RankGroupOrder;
import com.jingyunbank.etrade.api.marketing.rankgroup.bo.RankGroupUser;
import com.jingyunbank.etrade.api.marketing.rankgroup.service.IRankGroupService;
import com.jingyunbank.etrade.api.order.presale.bo.OrderStatusDesc;
import com.jingyunbank.etrade.api.order.presale.bo.Orders;
import com.jingyunbank.etrade.api.order.presale.service.IOrderGoodsService;
import com.jingyunbank.etrade.api.order.presale.service.context.IOrderContextService;
import com.jingyunbank.etrade.api.user.bo.Users;
import com.jingyunbank.etrade.marketing.rankgroup.dao.RankGroupDao;
import com.jingyunbank.etrade.marketing.rankgroup.entity.RankGroupEntity;
import com.jingyunbank.etrade.marketing.rankgroup.entity.RankGroupGoodsEntity;
import com.jingyunbank.etrade.marketing.rankgroup.entity.RankGroupUserEntity;

@Service("rankGroupService")
public class RankGroupService implements IRankGroupService{
	
	@Autowired
	RankGroupOrderService rankGroupOrderService;
	@Autowired
	RankGroupGoodsService rankGroupGoodsService;
	@Autowired
	RankGroupUserService rankGroupUserService;
	@Autowired
	private IOrderContextService orderContextService;
	@Autowired
	private IOrderGoodsService orderGoodsService;
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
		go.setType(RankGroupOrder.TYPE_DEPOSIT);//支付订金
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
		return convertEntityToBo( rankGroupDao.selectListOnDeadline());
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
		RankGroupEntity entity = rankGroupDao.selectOne(groupid);
		if(Objects.nonNull(entity)){
			RankGroup bo = new RankGroup();
			BeanUtils.copyProperties(entity, bo);
			return Optional.of(bo);
		}
		return Optional.empty();
	}
    
	@Override
	public Optional<RankGroup> singleByGroupGoodID(String groupGoodID) {
		RankGroupEntity entity = rankGroupDao.singleByGroupGoodID(groupGoodID);
		if(Objects.nonNull(entity)){
			RankGroup bo = new RankGroup();
			BeanUtils.copyProperties(entity, bo);
			return Optional.of(bo);
		}
		return Optional.empty();
	}
	@Override
	public Optional<RankGroup> joinDetail(String groupGoodID) {
		RankGroupEntity rankGroup=rankGroupDao.selectOne(groupGoodID);
		if(Objects.nonNull(rankGroup)){
			RankGroup bo = new RankGroup();
			BeanUtils.copyProperties(rankGroup, bo);
			return Optional.of(bo);
		}
		return Optional.empty();
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

	@Override
	public void payFail(Orders order) throws DataRefreshingException {
		Optional<RankGroupOrder> go = rankGroupOrderService.singleByOID( order.getID());
		if(go.isPresent()){
			Optional<RankGroupUser> gu = rankGroupUserService.single(go.get().getGroupUserID());
			if(gu.isPresent()){
				rankGroupUserService.refreshStatus(gu.get().getID(), gu.get().getStatus(), OrderStatusDesc.PAYFAIL_CODE);
			}
		}
		
	}

	@Override
	public void paySuccess(Orders order) throws DataRefreshingException {
		Optional<RankGroupOrder> go = rankGroupOrderService.singleByOID( order.getID());
		if(go.isPresent()){
			Optional<RankGroupUser> gu = rankGroupUserService.single(go.get().getGroupUserID());
			if(gu.isPresent()){
				rankGroupUserService.refreshStatus(gu.get().getID(), gu.get().getStatus(), OrderStatusDesc.PAID_CODE);
				//团长支付成功  开团
				Optional<RankGroup> group = this.single(gu.get().getGroupID());
				if(group.isPresent()&&group.get().getLeaderUID().equals(gu.get().getUID()) && Group.STATUS_NEW.equals(group.get().getStatus())){
					startSuccess(group.get());
				}
			}
		}
		
	}

	@Override
	public void dismiss(RankGroup group) throws DataRefreshingException, DataSavingException {
	     refreshStatus(group.getID(), RankGroup.STATUS_CLOSED);
		group.getBuyers().stream().forEach( user->{
			rankGroupUserService.notice(user, "组团失败 解散");
		});
		this.refound(group.getBuyers());
		
	}

	@Override
	public void payTimeout(RankGroupUser groupUser) throws DataRefreshingException, DataSavingException {
		//通知用户
				rankGroupUserService.notice(groupUser, "支付超时退出");
				rankGroupUserService.refreshStatus(groupUser.getID(), GroupUser.STATUS_CLOSED);
				
				//收集要关闭的订单id
				Optional<RankGroupOrder> groupOrder = rankGroupOrderService.single(groupUser.getID(), RankGroupOrder.TYPE_DEPOSIT);
				//为防止用户支付已关闭的团购订单 更改Orders状态
				orderContextService.cancel(Arrays.asList(groupOrder.get().getOID()), "超时关闭");
		
	}

	@Override
	public void startSuccess(RankGroup group) throws DataRefreshingException {
		refreshStatus(group.getID(), RankGroup.STATUS_CONVENING);
		RankGroupUser user = new RankGroupUser();
		user.setUID(group.getLeaderUID());
		user.setGroupID(group.getID());
		rankGroupUserService.notice(user, "创建成功");
		
	}

	@Override
	public boolean refreshStatus(String ID, String status) throws DataRefreshingException {
		try {
			return rankGroupDao.updateStatus(ID, status);
		} catch (Exception e) {
			throw new DataRefreshingException(e);
		}
	}

	@Override
	public void refound(List<RankGroupUser> groupUserList) throws DataRefreshingException, DataSavingException {
		//关闭团购订单
				for(RankGroupUser user : groupUserList){
					if(GroupUser.STATUS_PAID.equals(user.getStatus())){
						rankGroupUserService.refreshStatus(user.getID(), RankGroupUser.STATUS_REFUNED);
						Optional<RankGroupOrder> go = rankGroupOrderService.single(user.getID(), RankGroupOrder.TYPE_DEPOSIT);
						if(go.isPresent()){
							String gid = rankGroupGoodsService.singleByGroupID(user.getGroupID()).get().getGID();
							//ogid 应该由skuid获取
							String ogid =  orderGoodsService.singleOrderGoods(go.get().getOID(), gid).get().getID();
							orderContextService.refund(go.get().getOID(),ogid);
						}
						//通知用户
						rankGroupUserService.notice(user, "退款通知");
						//订单状态修改？
					}
				}
		
	}
	
	private List<RankGroup> convertEntityToBo(List<RankGroupEntity> entityList){
		if(entityList!=null){
			return entityList.stream().map( entity->{
				RankGroup bo = new RankGroup();
				BeanUtils.copyProperties(entity, bo, "buyers","goods");
				
				bo.setBuyers(converUserEnToBo(entity.getBuyers()));
				bo.setRankGoods(converGoodsEnToBo(entity.getRankGoods()));
				return bo;
			}).collect(Collectors.toList());
		}
		return new ArrayList<RankGroup>();
	}
	
	
	private List<RankGroupUser> converUserEnToBo(List<RankGroupUserEntity> entityList){
		if(entityList!=null){
			return entityList.stream().map( entity->{
				RankGroupUser bo = new RankGroupUser();
				BeanUtils.copyProperties(entity, bo);
				return bo;
			}).collect(Collectors.toList());
		}
		return new ArrayList<RankGroupUser>();
	}
	
	private RankGroupGoods converGoodsEnToBo(RankGroupGoodsEntity entity){
		RankGroupGoods bo = new RankGroupGoods();
		if(entity!=null){
			BeanUtils.copyProperties(entity, bo);
		}
		return bo;
	}

	
	
	
}
