package com.jingyunbank.etrade.weixin.dao;

import java.util.List;

import com.jingyunbank.etrade.weixin.entity.SNSUserInfo;

public interface WeiXinUserDao {
	//添加用户信息（微信信息进行的拉取）
	public void addUser(SNSUserInfo userInfo) throws Exception;
	//查出用户的信息
	public SNSUserInfo selectUserByOpenid(String openId);
}
