package com.jingyunbank.etrade.message.controller;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jingyunbank.core.KeyGen;
import com.jingyunbank.etrade.TestCaseBase;
import com.jingyunbank.etrade.api.exception.NoticeDispatchException;
import com.jingyunbank.etrade.api.message.bo.Message;
import com.jingyunbank.etrade.api.message.service.context.IAsyncNotifyService;

public class Mqtestcase extends TestCaseBase{

	@Autowired
	private IAsyncNotifyService asyncNotifyService;
	
	@Test
	public void mqtest() throws NoticeDispatchException{
		Message msg = new Message();
		msg.setID(KeyGen.uuid());
		msg.setContent("msgmsgmsgmsgasd123123");
		msg.setSentUID("xcvasdfaszxcvas1123");
		msg.setReceiveUID("xcva123sdf");
		asyncNotifyService.dispatch(msg);
	}
}
