package com.jingyunbank.etrade.resource.service;

import java.util.Random;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.resource.bo.FileSystemServer;
import com.jingyunbank.etrade.api.resource.service.IFileSystemService;
import com.jingyunbank.etrade.resource.dao.FileSystemDao;
import com.jingyunbank.etrade.resource.entity.FileSystemServerEntity;

@Service("fileSystemService")
public class FileSystemService implements IFileSystemService {

	@Autowired
	private FileSystemDao fileSystemDao;
	
	@Override
	public FileSystemServer random() {
		int rank = new Random().nextInt(1000);
		FileSystemServerEntity entity = fileSystemDao.selectServer(rank);
		if(entity == null){
			entity = fileSystemDao.selectFirst();
		}
		FileSystemServer server = new FileSystemServer();
		BeanUtils.copyProperties(entity, server);
		return server;
	}

}
