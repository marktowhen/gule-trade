package com.jingyunbank.weixin.wxcms.service;

import java.util.List;

import com.jingyunbank.weixin.wxcms.domain.MsgBase;




public interface MsgBaseService {

	public MsgBase getById(String id);

	public List<MsgBase> listForPage(MsgBase searchEntity);

	public void add(MsgBase entity);

	public void update(MsgBase entity);

	public void delete(MsgBase entity);



}