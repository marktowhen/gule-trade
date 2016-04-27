package com.jingyunbank.etrade.weixinMessage.dao;

import java.util.List;

import com.jingyunbank.etrade.api.weixinMessage.bo.AccountMenu;




public interface AccountMenuDao {

	public AccountMenu getById(String id);

	public List<AccountMenu> listForPage(AccountMenu searchEntity);

	public List<AccountMenu> listParentMenu();
	
	public List<AccountMenu> listWxMenus(String gid);
	
	public void add(AccountMenu entity);

	public void update(AccountMenu entity);

	public void delete(AccountMenu entity);



}