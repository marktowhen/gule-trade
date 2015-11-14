package com.jingyunbank.etrade.resource.controller;

import java.util.List;

import javax.servlet.http.Part;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.Result;

@RestController
public class ResourceController {

	@RequestMapping
	public Result upload(List<Part> files) throws Exception{
		
		return Result.ok();
	}
	
}
