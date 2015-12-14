package com.jingyunbank.etrade.resource.service;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.core.fs.FileStore;
import com.jingyunbank.etrade.api.exception.FileStorageException;
import com.jingyunbank.etrade.api.resource.bo.FileSystemServer;
import com.jingyunbank.etrade.api.resource.service.IFileSystemService;
import com.jingyunbank.etrade.api.resource.service.IStoreService;

@Service("storeService")
public class StoreService implements IStoreService {
	
	@Autowired
	private IFileSystemService fileSystemService;
	
	@Override
	public String store(String fname, byte[] content) throws FileStorageException {
			fname = FileStore.rename(fname);
			String dpath = FileStore.buildpath();
			///static/upload/2015/11/16/random_sequence_code/random_sequece.name
			FileSystemServer server = fileSystemService.current();
			Path fullpath = Paths.get(server.getRootpath(), 
									dpath,
									fname);
			
			File f = fullpath.toFile();
			File parent = f.getParentFile();
			if(!parent.exists()) parent.mkdirs();
			if(!f.exists())
				try {
					f.createNewFile();
				} catch (IOException e) {
					throw new FileStorageException(e);
				}
			
			String url = new StringBuilder().append(server.getHost()).append(server.getVpath())
								.append(Paths.get(dpath, fname).toString()).toString();
			
			Path p = f.toPath();
			try(FileChannel fc = FileChannel.open(p, StandardOpenOption.WRITE)){
				ByteBuffer buffer = ByteBuffer.wrap(content);
				fc.write(buffer);
			} catch (IOException e) {
				throw new FileStorageException(e);
			}
			return url;
			
	}

}
