package com.jingyunbank.etrade.resource.controller;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jingyunbank.core.Result;
import com.jingyunbank.core.fs.FileStore;

@RestController
public class ResourceController {

	public static final String UPLOAD_BASE_PATH = "d:/static/upload"; 
	
	@RequestMapping(value="/api/resource/upload/multiple", method=RequestMethod.POST,
			consumes="multipart/form-data")
	public Result uploadMultiple(@RequestParam(value="file", required=true) MultipartFile[] files) throws Exception{
		List<String> urls = new ArrayList<String>();
		for (MultipartFile file : files){
			byte[] contents = file.getBytes();
			String fname = file.getOriginalFilename();
			fname = FileStore.rename(fname);
			String dpath = FileStore.buildpath();
			///static/upload/2015/11/16/random_sequence_code/random_sequece.name
			Path pp = Paths.get(UPLOAD_BASE_PATH, 
									dpath,
									fname);
			
			File f = pp.toFile();
			File parent = f.getParentFile();
			if(!parent.exists()) parent.mkdirs();
			if(!f.exists()) f.createNewFile();
			urls.add(f.getAbsolutePath());
			Path p = f.toPath();
			FileChannel fc = FileChannel.open(p, StandardOpenOption.WRITE);
			ByteBuffer buffer = ByteBuffer.wrap(contents);
			fc.write(buffer);
			fc.close();
		}
		return Result.ok(urls);
	}
	
	@RequestMapping(value="/api/resource/upload/single", method=RequestMethod.POST,
			consumes="multipart/form-data")
	public Result uploadSingle(@RequestParam(value="file", required=true) MultipartFile file) throws Exception{
		byte[] contents = file.getBytes();
		
		String url = file.getOriginalFilename();
		File f = new File("D:/"+file.getOriginalFilename());
		if(!f.exists()) f.createNewFile();
		Path p = f.toPath();
		FileChannel fc = FileChannel.open(p, StandardOpenOption.WRITE);
		ByteBuffer buffer = ByteBuffer.wrap(contents);
		fc.write(buffer);
		fc.close();
		return Result.ok(url);
	}
	
}
