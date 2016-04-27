package com.jingyunbank.etrade.weixinMessage.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.weixinMessage.bo.AccountFans;
import com.jingyunbank.etrade.api.weixinMessage.bo.AccountMenu;
import com.jingyunbank.etrade.api.weixinMessage.bo.Matchrule;
import com.jingyunbank.etrade.api.weixinMessage.bo.MpAccount;
import com.jingyunbank.etrade.api.weixinMessage.bo.MsgBase;
import com.jingyunbank.etrade.api.weixinMessage.bo.MsgNews;
import com.jingyunbank.etrade.api.weixinMessage.bo.MsgRequest;
import com.jingyunbank.etrade.api.weixinMessage.bo.MsgText;
import com.jingyunbank.etrade.api.weixinMessage.service.MyService;
import com.jingyunbank.etrade.weixinMessage.dao.AccountFansDao;
import com.jingyunbank.etrade.weixinMessage.dao.AccountMenuDao;
import com.jingyunbank.etrade.weixinMessage.dao.AccountMenuGroupDao;
import com.jingyunbank.etrade.weixinMessage.dao.MsgBaseDao;
import com.jingyunbank.etrade.weixinMessage.dao.MsgNewsDao;
import com.jingyunbank.etrade.weixinMessage.process.HttpMethod;
import com.jingyunbank.etrade.weixinMessage.process.MsgType;
import com.jingyunbank.etrade.weixinMessage.process.MsgXmlUtil;
import com.jingyunbank.etrade.weixinMessage.process.WxApi;
import com.jingyunbank.etrade.weixinMessage.process.WxApiClient;
import com.jingyunbank.etrade.weixinMessage.process.WxMessageBuilder;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/**
 * 业务消息处理
 * 开发者根据自己的业务自行处理消息的接收与回复；
 */

@Service
public class MyServiceImpl implements MyService{

	@Autowired
	private MsgBaseDao msgBaseDao;
	
	@Autowired
	private MsgNewsDao msgNewsDao;
	
	@Autowired
	private AccountMenuDao menuDao;
	
	@Autowired
	private AccountMenuGroupDao menuGroupDao;
	
	@Autowired
	private AccountFansDao fansDao;

	@Override
	public String processMsg(MsgRequest msgRequest, MpAccount mpAccount) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean syncAccountFansList(MpAccount mpAccount) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public AccountFans syncAccountFans(String openId, MpAccount mpAccount, boolean merge) {
		// TODO Auto-generated method stub
		return null;
	}
	


	
}


