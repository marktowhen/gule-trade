package com.jingyunbank.etrade.marketing.group.controller;

import java.util.Date;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.KeyGen;
import com.jingyunbank.core.Result;
import com.jingyunbank.core.web.AuthBeforeOperation;
import com.jingyunbank.core.web.Login;
import com.jingyunbank.etrade.api.marketing.group.bo.Group;
import com.jingyunbank.etrade.api.marketing.group.bo.GroupGoods;
import com.jingyunbank.etrade.api.marketing.group.service.IGroupGoodsService;
import com.jingyunbank.etrade.api.marketing.group.service.IGroupService;
import com.jingyunbank.etrade.api.marketing.group.service.context.IGroupPurchaseContextService;
import com.jingyunbank.etrade.api.user.bo.Users;
import com.jingyunbank.etrade.api.user.service.IUserService;
import com.jingyunbank.etrade.marketing.group.bean.GroupVO;

@RestController
public class GroupPurchaseController {

	@Autowired
	private IGroupPurchaseContextService groupPurchaseContextService;
	@Autowired
	private IGroupGoodsService groupGoodsService;
	@Autowired
	private IGroupService groupService;
	@Autowired
	private IUserService userService;

	// 开团
	@AuthBeforeOperation
	@RequestMapping(value = "/api/group/purchase/start/{groupgoodsid}", method = RequestMethod.POST)
	public Result<GroupVO> start(@PathVariable String groupgoodsid,
			HttpSession session) throws Exception {

		Optional<GroupGoods> goods = groupGoodsService.single(groupgoodsid);
		if (!goods.isPresent()) {
			return Result.fail("团购商品不存在。");
		}
		String uid = Login.UID(session);
		Optional<Users> leader = userService.single(uid);
		if (!leader.isPresent())
			return Result.fail("不合法的操作，请先登录认证身份。");
		Group group = new Group();
		group.setID(KeyGen.uuid());
		group.setGoods(goods.get());
		group.setLeader(leader.get());
		group.setStart(new Date());
		groupPurchaseContextService.start(leader.get(), group);
		GroupVO vo = new GroupVO();
		BeanUtils.copyProperties(group, vo);
		return Result.ok(vo);
	}

	// 参团
	@AuthBeforeOperation
	@RequestMapping(value = "/api/group/purchase/join/{groupid}", method = RequestMethod.POST)
	public Result<String> join(@PathVariable String groupid, HttpSession session)
			throws Exception {

		Optional<Group> group = groupService.single(groupid);
		if (!group.isPresent()) {
			return Result.fail("您申请加入的团不存在。");
		}
		String uid = Login.UID(session);
		Optional<Users> user = userService.single(uid);
		if (!user.isPresent())
			return Result.fail("不合法的操作，请先登录认证身份。");
		groupPurchaseContextService.join(user.get(), group.get());
		return Result.ok();
	}

}
