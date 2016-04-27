package com.jingyunbank.etrade.weixinMessage.dao;

import java.util.List;

import com.jingyunbank.etrade.api.weixinMessage.bo.MsgBase;
import com.jingyunbank.etrade.api.weixinMessage.bo.MsgNews;
import com.jingyunbank.etrade.api.weixinMessage.bo.MsgText;




public interface MsgBaseDao {

	public MsgBase getById(String id);

	public List<MsgBase> listForPage(MsgBase searchEntity);

	public List<MsgNews> listMsgNewsByBaseId(String[] ids);
	
	public MsgText getMsgTextByBaseId(String id);
	
	public MsgText getMsgTextBySubscribe();
	
	public MsgText getMsgTextByInputCode(String inputcode);
	
	public void add(MsgBase entity);

	public void update(MsgBase entity);
	
	public void updateInputcode(MsgBase entity);

	public void delete(MsgBase entity);

}