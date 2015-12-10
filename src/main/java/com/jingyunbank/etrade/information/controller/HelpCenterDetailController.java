package com.jingyunbank.etrade.information.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.etrade.api.information.service.IHelpCenterDetailService;

@RestController
@RequestMapping("/api/help/center/detail")
public class HelpCenterDetailController {

	@Autowired
	private IHelpCenterDetailService helpCenterDetailService;
	
	
}
