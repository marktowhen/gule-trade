package com.jingyunbank.etrade.resource.dao;

import java.util.List;

import com.jingyunbank.etrade.resource.entity.FileSystemServerEntity;

public interface FileSystemDao {

	public List<FileSystemServerEntity> selectServers();
	
	public FileSystemServerEntity selectServer(String id);
	
	public FileSystemServerEntity selectFirst();
	
}
