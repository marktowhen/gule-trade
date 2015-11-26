package com.jingyunbank.etrade.resource.controller;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jingyunbank.core.Result;
import com.jingyunbank.core.fs.FileStore;
import com.jingyunbank.etrade.api.resource.bo.FileSystemServer;
import com.jingyunbank.etrade.api.resource.service.IFileSystemService;

@RestController
public class ResourceController {

	@Autowired
	private IFileSystemService fileSystemService;
	
	@RequestMapping(value="/api/resource/upload/multiple", method=RequestMethod.POST,
			consumes="multipart/form-data")
	public Result uploadMultiple(@RequestParam(value="file", required=true) MultipartFile[] files) throws Exception{
		List<String> urls = new ArrayList<String>();
		for (MultipartFile file : files){
			urls.add(process(file));
		}
		return Result.ok(urls);
	}
	
	@RequestMapping(value="/api/resource/upload/single", method=RequestMethod.POST,
			consumes="multipart/form-data")
	public Result uploadSingle(@RequestParam(value="file", required=true) MultipartFile file) throws Exception{
		String url = process(file);
		return Result.ok(url);
	}

	private String process(MultipartFile file) throws IOException {
		byte[] contents = file.getBytes();
		
		String fname = file.getOriginalFilename();
		fname = FileStore.rename(fname);
		String dpath = FileStore.buildpath();
		///static/upload/2015/11/16/random_sequence_code/random_sequece.name
		FileSystemServer server = fileSystemService.random();
		Path pp = Paths.get(server.getRootpath(), 
								dpath,
								fname);
		
		File f = pp.toFile();
		File parent = f.getParentFile();
		if(!parent.exists()) parent.mkdirs();
		if(!f.exists()) f.createNewFile();
		
		String url = new StringBuilder().append(server.getHost())
							.append(Paths.get(dpath, fname).toString()).toString();
		
		Path p = f.toPath();
		FileChannel fc = FileChannel.open(p, StandardOpenOption.WRITE);
		ByteBuffer buffer = ByteBuffer.wrap(contents);
		fc.write(buffer);
		fc.close();
		return url;
	}
	
}
