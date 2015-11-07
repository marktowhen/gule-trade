package com.jingyunbank.etrade.infrastructure;


public interface IMessageNotify {

	public void sendMessage(String uid, String subject, String body) throws Exception;
	
	//send message to manager(s)
	public void sendMngrMessage(String subject, String body) throws Exception;
	
}
