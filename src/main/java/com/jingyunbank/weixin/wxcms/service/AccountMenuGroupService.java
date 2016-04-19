package com.jingyunbank.weixin.wxcms.service;

import java.util.List;

import com.jingyunbank.weixin.core.page.Pagination;
import com.jingyunbank.weixin.wxcms.domain.AccountMenuGroup;



public interface AccountMenuGroupService {

	public AccountMenuGroup getById(String id);

	public List<AccountMenuGroup> list(AccountMenuGroup searchEntity);

	public Pagination<AccountMenuGroup> paginationEntity(AccountMenuGroup searchEntity ,Pagination<AccountMenuGroup> pagination);

	public void add(AccountMenuGroup entity);

	public void update(AccountMenuGroup entity);

	public void delete(AccountMenuGroup entity);



}

