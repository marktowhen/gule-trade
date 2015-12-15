package com.jingyunbank.etrade.resource.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jingyunbank.core.Result;
import com.jingyunbank.etrade.api.exception.FileStorageException;
import com.jingyunbank.etrade.api.resource.service.IStoreService;
import com.jingyunbank.etrade.resource.entity.UeditorImg;

@RestController
public class ResourceController {

	@Autowired
	private IStoreService storeService;
	
	@RequestMapping(value="/api/resource/upload/multiple", method=RequestMethod.POST,
			consumes="multipart/form-data")
	public Result<List<String>> uploadMultiple(@RequestParam(value="file", required=true) MultipartFile[] files) throws Exception{
		List<String> urls = new ArrayList<String>();
		for (MultipartFile file : files){
			urls.add(process(file));
		}
		return Result.ok(urls);
	}
	
	@RequestMapping(value="/api/resource/upload/single", method=RequestMethod.POST,
			consumes="multipart/form-data")
	public Result<String> uploadSingle(@RequestParam(value="file", required=true) MultipartFile file) throws Exception{
		String url = process(file);
		return Result.ok(url);
	}
	
	
	/*ueditor*/
	@RequestMapping(value="/api/resource/ueditor/upload", method=RequestMethod.POST)
	public  void ueditorUpload(HttpServletRequest request,HttpServletResponse response) throws Exception{
		response.addHeader("Access-Control-Allow-Origin", "*"); 
		response.setContentType("text/html;charset=UTF-8");
		MultipartHttpServletRequest multipartRequest=(MultipartHttpServletRequest) request;
		MultipartFile file =multipartRequest.getFile("upfile"); 
		
		byte[] contents = file.getBytes();
		String fname = file.getOriginalFilename();
		String url = storeService.store(fname, contents);
		
		//System.out.println(url);
		/*
		 * 上传返回格式
		 * "{'original':'1.jpg','state':'SUCCESS','title':'1.jpg','url':'D:/img/1.jpg'}";
		 * 
		 * */
		//上传回显 待测试~
		////reStr.setTitle("j_0084.gif");
		UeditorImg reStr = new UeditorImg();
		reStr.setState("SUCCESS");
		reStr.setOriginal(fname);
		reStr.setName(fname);
		reStr.setUrl(url);
		reStr.setType(".jpg");
		reStr.setSize(String.valueOf(contents.length));
			
		/*转成json*/
		ObjectMapper objectMapper = new ObjectMapper();
		String result = objectMapper.writeValueAsString(reStr);
		PrintWriter writer = response.getWriter();
		writer.write(result);
        writer.flush();
        writer.close();
	}
	
	/*ueditor init*/
	@RequestMapping("/api/resource/dispatch")
    public void config(HttpServletRequest request,  HttpServletResponse response, String action) throws Exception {
		response.setContentType("application/json");      
		response.setHeader("Content-Type" , "text/html");
		response.addHeader("Access-Control-Allow-Origin", "*");  
		//config.json 文件位置~
		InputStream inputstream =ResourceController.class.getClassLoader().getResourceAsStream("ueditor-config.json");
		//System.err.println(rootPath);
		String callbackName = request.getParameter("callback");
		StringBuilder builder = new StringBuilder();
		try {
			InputStreamReader reader = new InputStreamReader(inputstream, "UTF-8" );
			BufferedReader bfReader = new BufferedReader( reader );
			String tmpContent = null;
			while ( ( tmpContent = bfReader.readLine() ) != null ) {
				builder.append( tmpContent );
			}
			bfReader.close();
		} catch ( UnsupportedEncodingException e ) {
			// 忽略
		}
		String configContent = this.filter( builder.toString() );
		configContent = configContent.replaceAll(" ", "");
		//System.err.println(configContent);
		String exec = StringUtils.hasText(callbackName)? 
						callbackName+"("+configContent+")"//jsonp approach
						:configContent;
		PrintWriter writer = response.getWriter();
		writer.write(exec);
        writer.flush();
        writer.close();
		        
}
	// 过滤输入字符串, 剔除多行注释以及替换掉反斜杠
	private String filter ( String input ) {
		return input.replaceAll( "/\\*[\\s\\S]*?\\*/", "" );
	}
	
	private String process(MultipartFile file) throws IOException, FileStorageException  {
		byte[] contents = file.getBytes();
		String fname = file.getOriginalFilename();
		return storeService.store(fname, contents);
	}
	
	
	
}
