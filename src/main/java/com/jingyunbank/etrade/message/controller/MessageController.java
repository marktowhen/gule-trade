package com.jingyunbank.etrade.message.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.websocket.server.PathParam;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.util.BeanUtil;
import com.jingyunbank.core.KeyGen;
import com.jingyunbank.core.Page;
import com.jingyunbank.core.Result;
import com.jingyunbank.core.web.ServletBox;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.message.bo.Message;
import com.jingyunbank.etrade.api.message.service.IMessageService;
import com.jingyunbank.etrade.base.util.EtradeUtil;
import com.jingyunbank.etrade.message.bean.MessageVO;
import com.jingyunbank.etrade.message.service.MessageService;

@RestController
@RequestMapping("/api/message")
public class MessageController {
	
	@Autowired
	private IMessageService messageService;
	
	/**
	 * 发送站内信
	 * 向多人发送receiveUid逗号分隔
	 * @param request
	 * @param messageVO 
	 * @param valid
	 * @return
	 * 2015年11月12日 qxs
	 * @throws DataSavingException 
	 */
	@RequestMapping(value="/",method=RequestMethod.PUT)
	public Result sendSysMessage(HttpServletRequest request,@Valid MessageVO messageVO, BindingResult valid) throws DataSavingException{
		//验证vo信息
		if(valid.hasErrors()){
			List<ObjectError> errors = valid.getAllErrors();
			return Result.fail(errors.stream()
						.map(oe -> Arrays.asList(oe.getDefaultMessage()).toString())
						.collect(Collectors.joining(" ; ")));
		}
		String[] receiveUids = messageVO.getReceiveUID().split(",");
		messageVO.setAddip(EtradeUtil.getIpAddr(request));
		messageVO.setSentUID(ServletBox.getLoginUID(request));
		messageVO.setStatus(MessageService.STATUS_SUC);
		List<Message> listMsg = new ArrayList<Message>();
		for (int i = 0; i < receiveUids.length; i++) {
			Message message = new Message();
			BeanUtils.copyProperties(messageVO, message);
			message.setID(KeyGen.uuid());
			listMsg.add(message);
		}
		messageService.save(listMsg);
		return Result.ok();
	}
	/**
	 * 查询消息详情
	 * @param id
	 * @param request
	 * @return
	 * 2015年11月12日 qxs
	 */
	@RequestMapping(value="/{id}",method=RequestMethod.GET)
	public Result getSingleInfo(@PathParam(value="id") String id, HttpServletRequest request){
		Optional<Message> messageOption = messageService.getSingle(id);
		if(messageOption.isPresent()){
			MessageVO vo = new MessageVO();
			BeanUtils.copyProperties(messageOption.get(), vo);
			return Result.ok(vo);
		}
		return Result.fail("未找到");
	}
	
	

}
