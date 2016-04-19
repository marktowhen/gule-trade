package com.jingyunbank.weixin.wxcms.mapper;

import java.util.List;

import com.jingyunbank.weixin.core.page.Pagination;
import com.jingyunbank.weixin.wxcms.domain.AccountMenuGroup;


public interface AccountMenuGroupDao {

	public AccountMenuGroup getById(String id);

	public List<AccountMenuGroup> list(AccountMenuGroup searchEntity);

	public Integer getTotalItemsCount(AccountMenuGroup searchEntity);

	public List<AccountMenuGroup> paginationEntity(AccountMenuGroup searchEntity , Pagination<AccountMenuGroup> pagination);

	public void add(AccountMenuGroup entity);

	public void update(AccountMenuGroup entity);
	
	public void updateMenuGroupDisable();
	
	public void updateMenuGroupEnable(String gid);
	
	public void deleteAllMenu(AccountMenuGroup entity);
	
	public void delete(AccountMenuGroup entity);



}

