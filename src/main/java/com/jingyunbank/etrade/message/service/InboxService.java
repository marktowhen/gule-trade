package com.jingyunbank.etrade.message.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.core.KeyGen;
import com.jingyunbank.core.Range;
import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.message.bo.Message;
import com.jingyunbank.etrade.api.message.service.IInboxService;
import com.jingyunbank.etrade.message.dao.MessageDao;
import com.jingyunbank.etrade.message.entity.MessageEntity;

@Service("inboxService")
public class InboxService implements IInboxService {

	@Autowired
	private MessageDao messageDao;

	@Override
	public boolean save(Message message) throws DataSavingException {
		MessageEntity entity = new MessageEntity();
		BeanUtils.copyProperties(message, entity);
		try {
			entity.setHasRead(false);
			messageDao.insert(entity);
		} catch (Exception e) {
			throw new DataSavingException(e);
		}
		return true;
	}

	@Override
	public boolean save(Message message, String[] receiveUids) throws DataSavingException {
		List<MessageEntity> list = new ArrayList<MessageEntity>();
		MessageEntity entity = new MessageEntity();
		BeanUtils.copyProperties(message, entity);
		for (int i = 0; i < receiveUids.length; i++) {
			entity.setReceiveUID(receiveUids[i]);
			entity.setID(KeyGen.uuid());
			list.add(entity);
		}
		try {
			messageDao.insertMulti(list);
		} catch (Exception e) {
			throw new DataSavingException(e);
		}
		return true;
	}
	
	@Override
	public boolean save(List<Message> messageList) throws DataSavingException {
		List<MessageEntity> entityList = new ArrayList<MessageEntity>();
		messageList.forEach(message->{
			MessageEntity entity = new MessageEntity();
			BeanUtils.copyProperties(message, entity);
			entityList.add(entity);
		});
		
		try {
			messageDao.insertMulti(entityList);
		} catch (Exception e) {
			throw new DataSavingException(e);
		}
		return true;
	}

	@Override
	public boolean remove(String id, String receiveUID) throws DataRefreshingException {
		MessageEntity e = new MessageEntity();
		e.setID(id);
		e.setReceiveUID(receiveUID);
		e.setStatus(Message.STATUS_DEL);
		try {
			messageDao.updateStatus(e);
		} catch (Exception e1) {
			throw new DataRefreshingException(e1);
		}
		return true;
	}
	
	@Override
	public boolean remove(String[] ids, String receiveUID) throws DataRefreshingException {
		MessageEntity e = new MessageEntity();
		e.setIDs(ids);
		e.setReceiveUID(receiveUID);
		e.setStatus(Message.STATUS_DEL);
		try {
			messageDao.updateStatus(e);
		} catch (Exception e1) {
			throw new DataRefreshingException(e1);
		}
		return true;
	}

	@Override
	public Optional<Message> single(String ID) {
		MessageEntity entity = messageDao.selectSingle(ID);
		if(entity!=null){
			return Optional.of(copyEntityToBo(entity, new Message()));
		}
		return null;
	}

	@Override
	public List<Message> list(String uid, Range range) {
		MessageEntity entity = new MessageEntity();
		entity.setReceiveUID(uid);
		entity.setStatus(Message.STATUS_SUC);
		return  messageDao.selectList(entity, range.getFrom(), range.getTo()-range.getFrom())
					.stream().map( entityResul->{
						return copyEntityToBo(entityResul, new Message());
					}).collect(Collectors.toList());
	}
	
	/**
	 * 查询数量
	 * @param message
	 * @return
	 * 2015年11月13日 qxs
	 */
	@Override
	public int count(String receiveUID) {
		MessageEntity entity = new MessageEntity();
		entity.setReceiveUID(receiveUID);
		entity.setStatus(Message.STATUS_SUC);
		return messageDao.count(entity);
	}

	@Override
	public List<Message> listUnread(String uid, Range range) {
		MessageEntity entity = new MessageEntity();
		entity.setReceiveUID(uid);
		entity.setNeedReadStatus(true);
		entity.setHasRead(false);
		entity.setStatus(Message.STATUS_SUC);
		return  messageDao.selectList(entity, range.getFrom(), range.getTo()-range.getFrom())
					.stream().map( entityResul->{
						return copyEntityToBo(entityResul, new Message());
					}).collect(Collectors.toList());
	}
	@Override
	public int countUnread(String receiveUID) {
		MessageEntity entity = new MessageEntity();
		entity.setReceiveUID(receiveUID);
		entity.setStatus(Message.STATUS_SUC);
		entity.setNeedReadStatus(true);
		entity.setHasRead(false);
		return messageDao.count(entity);
	}
	
	@Override
	public int countRead(String receiveUID) {
		MessageEntity entity = new MessageEntity();
		entity.setReceiveUID(receiveUID);
		entity.setStatus(Message.STATUS_SUC);
		entity.setNeedReadStatus(true);
		entity.setHasRead(true);
		return messageDao.count(entity);
	}

	@Override
	public List<Message> listRead(String receiveUID, Range range) {
		MessageEntity entity = new MessageEntity();
		entity.setReceiveUID(receiveUID);
		entity.setNeedReadStatus(true);
		entity.setHasRead(true);
		entity.setStatus(Message.STATUS_SUC);
		return  messageDao.selectList(entity, range.getFrom(), range.getTo()-range.getFrom())
					.stream().map( entityResul->{
						return copyEntityToBo(entityResul, new Message());
					}).collect(Collectors.toList());
	}

	@Override
	public void refreshReadStatus(String id, boolean hasRead) throws DataRefreshingException  {
		MessageEntity entity = new MessageEntity();
		entity.setID(id);
		entity.setHasRead(hasRead);
		try {
			messageDao.updateReadStatus(entity);
		} catch (Exception e) {
			throw new DataRefreshingException(e);
		}
	}

	@Override
	public void refreshReadStatus(String[] ids, boolean hasRead) throws DataRefreshingException  {
		MessageEntity entity = new MessageEntity();
		entity.setIDs(ids);
		entity.setHasRead(hasRead);
		try {
			messageDao.updateReadStatus(entity);
		} catch (Exception e) {
			throw new DataRefreshingException(e);
		}
	}
	

	/**
	 * 将entity值copy到vo
	 * @param entity
	 * @param message
	 * @return
	 * 2015年11月13日 qxs
	 */
	private Message copyEntityToBo(MessageEntity entity, Message message){
		if(entity==null || message==null){
			return message;
		}
		BeanUtils.copyProperties(entity, message);
		if(entity.getReceiveUser()!=null && message.getReceiveUser()!=null){
			BeanUtils.copyProperties(entity.getReceiveUser(), message.getReceiveUser());
		}
		if(entity.getSendUser()!=null && message.getSendUser()!=null){
			BeanUtils.copyProperties(entity.getSendUser(), message.getSendUser());
		}
		return message;
	}

	@Override
	public void inform(Message msg) throws Exception {
		this.save(msg);
	}

	
	


}
