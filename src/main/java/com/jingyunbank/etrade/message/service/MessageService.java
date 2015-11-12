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
	public boolean remove(String id) throws DataRefreshingException {
		MessageEntity e = new MessageEntity();
		e.setID(id);
		e.setStatus(STATUS_DEL);
		try {
			messageDao.update(e);
		} catch (Exception e1) {
			throw new DataRefreshingException(e1);
		}
		return true;
	}
	
	@Override
	public boolean remove(String[] ids) throws DataRefreshingException {
		for (int i = 0; i < ids.length; i++) {
			remove(ids[i]);
		}
		return true;
	}

	@Override
	public Optional<Message> getSingle(String ID) {
		MessageEntity entity = new MessageEntity();
		entity.setID(ID);
		List<MessageEntity> list = messageDao.selectList(entity);
		if(list==null || list.isEmpty()){
			return Optional.empty();
		}
		Message message = new Message();
		BeanUtils.copyProperties(list.get(0) ,message);
		return Optional.of(message);
	}

	@Override
	public List<Message> list(Message message, Range range) {
		List<Message> listResult = new ArrayList<Message>();
		MessageEntity entity = new MessageEntity();
		BeanUtils.copyProperties(message, entity);
		entity.setStart(range.getFrom());
		entity.setOffset(range.getTo()-range.getFrom());
		List<MessageEntity> entityList = messageDao.selectList(entity);
		entityList.forEach(resultEntity->{
			Message messageBo = new Message();
			BeanUtils.copyProperties(resultEntity, messageBo);
			listResult.add(messageBo);
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
			Message messageBo = new Message();
			BeanUtils.copyProperties(resultEntity, messageBo);
			listResult.add(messageBo);
		});
		return listResult;
	}

	
}
