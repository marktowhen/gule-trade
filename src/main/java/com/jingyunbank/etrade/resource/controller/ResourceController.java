package com.jingyunbank.etrade.resource.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jingyunbank.core.Result;
import com.jingyunbank.etrade.api.exception.FileStorageException;
import com.jingyunbank.etrade.api.resource.service.IStoreService;

@RestController
public class ResourceController {

	@Autowired
	private IStoreService storeService;
	
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

	private String process(MultipartFile file) throws IOException, FileStorageException  {
		byte[] contents = file.getBytes();
		String fname = file.getOriginalFilename();
		return storeService.store(fname, contents);
	}
	
}
