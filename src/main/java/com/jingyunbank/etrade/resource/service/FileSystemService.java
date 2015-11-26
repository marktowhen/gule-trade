package com.jingyunbank.etrade.resource.service;

import java.util.Objects;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.core.util.EnvirVariable;
import com.jingyunbank.etrade.api.resource.bo.FileSystemServer;
import com.jingyunbank.etrade.api.resource.service.IFileSystemService;
import com.jingyunbank.etrade.resource.dao.FileSystemDao;
import com.jingyunbank.etrade.resource.entity.FileSystemServerEntity;

@Service("fileSystemService")
public class FileSystemService implements IFileSystemService {

	@Autowired
	private FileSystemDao fileSystemDao;
	
	@Override
	public FileSystemServer current() {
		String serverid = System.getenv(EnvirVariable.SERVER_ID_IN_CLUSTER_ENV_KEY);
		FileSystemServerEntity entity = fileSystemDao.selectServer(Objects.isNull(serverid)?"":serverid);
		if(Objects.isNull(entity)){
			entity = fileSystemDao.selectFirst();
		}
		FileSystemServer server = new FileSystemServer();
		BeanUtils.copyProperties(entity, server);
		return server;
	}

}
