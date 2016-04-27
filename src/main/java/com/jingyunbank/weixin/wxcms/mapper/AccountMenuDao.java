package com.jingyunbank.weixin.wxcms.mapper;

import java.util.List;

import com.jingyunbank.weixin.wxcms.domain.AccountMenu;




public interface AccountMenuDao {

	public AccountMenu getById(String id);

	public List<AccountMenu> listForPage(AccountMenu searchEntity);

	public List<AccountMenu> listParentMenu();
	
	public List<AccountMenu> listWxMenus(String gid);
	
	public void add(AccountMenu entity);

	public void update(AccountMenu entity);

	public void delete(AccountMenu entity);



}