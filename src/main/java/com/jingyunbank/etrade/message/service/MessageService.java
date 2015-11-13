package com.jingyunbank.etrade.message.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.core.KeyGen;
import com.jingyunbank.core.Range;
import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.message.bo.Message;
import com.jingyunbank.etrade.api.message.service.IMessageService;
import com.jingyunbank.etrade.message.dao.MessageDao;
import com.jingyunbank.etrade.message.entity.MessageEntity;

@Service("messageService")
public class MessageService implements IMessageService {

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
		e.setStatus(STATUS_DEL);
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
		e.setStatus(STATUS_DEL);
		try {
			messageDao.updateStatus(e);
		} catch (Exception e1) {
			throw new DataRefreshingException(e1);
		}
		return true;
	}

	@Override
	public Optional<Message> getSingle(String ID, String receiveUID) {
		MessageEntity entity = new MessageEntity();
		entity.setID(ID);
		entity.setReceiveUID(receiveUID);
		List<MessageEntity> list = messageDao.selectList(entity);
		if(list==null || list.isEmpty()){
			return Optional.empty();
		}
		return Optional.of(copyEntityToBo(list.get(0), new Message()));
	}

	@Override
	public List<Message> list(Message message, Range range) {
		List<Message> listResult = new ArrayList<Message>();
		MessageEntity entity = new MessageEntity();
		BeanUtils.copyProperties(message, entity);
		entity.setOffset(range.getFrom());
		entity.setSize(range.getTo()-range.getFrom());
		List<MessageEntity> entityList = messageDao.selectList(entity);
		entityList.forEach(resultEntity->{
			listResult.add(copyEntityToBo(resultEntity, new Message()));
		});
		return listResult;
	}

	@Override
	public List<Message> list(Message message) {
		List<Message> listResult = new ArrayList<Message>();
		MessageEntity entity = new MessageEntity();
		BeanUtils.copyProperties(message, entity);
		List<MessageEntity> entityList = messageDao.selectList(entity);
		entityList.forEach(resultEntity->{
			listResult.add(copyEntityToBo(resultEntity, new Message()));
		});
		return listResult;
	}

	@Override
	public void refreshReadStatus(Message message) throws DataRefreshingException  {
		MessageEntity entity = new MessageEntity();
		BeanUtils.copyProperties(message, entity);
		try {
			messageDao.updateReadStatus(entity);
		} catch (Exception e) {
			throw new DataRefreshingException(e);
		}
	}

	@Override
	public void refreshReadStatus(String[] ids, Message message) throws DataRefreshingException  {
		MessageEntity entity = new MessageEntity();
		BeanUtils.copyProperties(message, entity);
		entity.setIDs(ids);
		try {
			messageDao.updateReadStatus(entity);
		} catch (Exception e) {
			throw new DataRefreshingException(e);
		}
	}
	/**
	 * 查询数量
	 * @param message
	 * @return
	 * 2015年11月13日 qxs
	 */
	@Override
	public int getAmount(Message message) {
		MessageEntity entity = new MessageEntity();
		BeanUtils.copyProperties(message, entity);
		return messageDao.getAmount(entity);
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
	
}
