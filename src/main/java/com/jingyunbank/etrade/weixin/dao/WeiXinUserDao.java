package com.jingyunbank.etrade.weixin.dao;

import com.jingyunbank.etrade.weixin.entity.SNSUserInfo;

public interface WeiXinUserDao {
	
	public void addUser(SNSUserInfo userInfo) throws Exception;
}
