package com.jingyunbank.etrade.message.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.KeyGen;
import com.jingyunbank.core.Page;
import com.jingyunbank.core.Range;
import com.jingyunbank.core.Result;
import com.jingyunbank.core.web.AuthBeforeOperation;
import com.jingyunbank.core.web.ServletBox;
import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.message.bo.Message;
import com.jingyunbank.etrade.api.message.service.IInboxService;
import com.jingyunbank.etrade.base.util.EtradeUtil;
import com.jingyunbank.etrade.message.bean.MessageVO;

@RestController
@RequestMapping("/api/message")
public class MessageController {
	
	@Autowired
	private IInboxService inboxService;
	
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
	@AuthBeforeOperation
	@RequestMapping(value="/",method=RequestMethod.PUT)
	public Result sendSysMessage(HttpServletRequest request,@Valid MessageVO messageVO, BindingResult valid) throws Exception{
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
		messageVO.setStatus(IInboxService.STATUS_SUC);
		messageVO.setType(IInboxService.TYPE_LETTER);
		List<Message> listMsg = new ArrayList<Message>();
		for (int i = 0; i < receiveUids.length; i++) {
			Message message = new Message();
			BeanUtils.copyProperties(messageVO, message);
			message.setID(KeyGen.uuid());
			message.setReceiveUID(receiveUids[i]);
			listMsg.add(message);
		}
		inboxService.save(listMsg);
		return Result.ok();
	}
	/**
	 * 查询消息详情
	 * @param id
	 * @param request
	 * @return
	 * 2015年11月12日 qxs
	 * @throws DataRefreshingException 
	 */
	@RequestMapping(value="/{id}",method=RequestMethod.GET)
	public Result getSingleInfo(@PathVariable String id, HttpServletRequest request)throws Exception{
		Optional<Message> messageOption = inboxService.getSingle(id, ServletBox.getLoginUID(request));
		if(messageOption.isPresent()){
			//如果未读则置为已读
			if(!messageOption.get().isHasRead()){
				messageOption.get().setHasRead(true);
				inboxService.refreshReadStatus(messageOption.get());
			}
			
			return Result.ok(copyBoToVo(messageOption.get(), new MessageVO()));
		}
		return Result.fail("未找到");
	}
	
	/**
	 * 查询用户的消息列表
	 * @param messageVO
	 * @param needReadStatus 是否需要关注是否已读的状态
	 * 		(boolean类型默认为false 当vo中hasRead值为false,无法区分是查询未读信息还是查询所有的)
	 * @param request
	 * @param page
	 * @return
	 * 2015年11月13日 qxs
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/list/{uid}",method=RequestMethod.GET)
	public Result getListUID(MessageVO messageVO,boolean needReadStatus
			, HttpServletRequest request, Page page) throws Exception{
		Message message = new  Message();
		BeanUtils.copyProperties(messageVO, message);
		message.setNeedReadStatus(needReadStatus);
		message.setReceiveUID(ServletBox.getLoginUID(request));
		if(message.getStatus()==0){
			message.setStatus(IInboxService.STATUS_SUC);
		}
		Range range = new Range();
		range.setFrom(page.getOffset());
		range.setTo(page.getOffset()+page.getSize());
		return Result.ok( inboxService.list(message, range)
				.stream().map(bo ->{
					return copyBoToVo(bo, new MessageVO());
				}).collect(Collectors.toList()));
	}
	
	/**
	 * 查询用户的消息列表数量
	 * @param messageVO
	 * @param needReadStatus 是否需要关注是否已读的状态
	 * 		(boolean类型默认为false 当vo中hasRead值为false,无法区分是查询未读信息还是查询所有的)
	 * @param request
	 * @return
	 * 2015年11月13日 qxs
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/amount/{uid}",method=RequestMethod.GET)
	public Result getAmountUID(MessageVO messageVO, boolean needReadStatus
			, HttpServletRequest request) throws Exception{
		Message message = new  Message();
		BeanUtils.copyProperties(messageVO, message);
		if(message.getStatus()==0){
			message.setStatus(IInboxService.STATUS_SUC);
		}
		message.setNeedReadStatus(needReadStatus);
		message.setReceiveUID(ServletBox.getLoginUID(request));
		return Result.ok( inboxService.getAmount(message));
	}
	
	/**
	 * 逻辑删除
	 * @param id 多个id逗号分隔
	 * @param request
	 * @return
	 * 2015年11月13日 qxs
	 * @throws DataRefreshingException 
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/{id}",method=RequestMethod.DELETE)
	public Result remove(@PathVariable String id, HttpServletRequest request) throws Exception{
		
		inboxService.remove(id.split(","), ServletBox.getLoginUID(request));
		return Result.ok();
	}
	
	/**
	 * 修改消息的读取状态
	 * @param id 多个id逗号分隔
	 * @param request
	 * @return
	 * 2015年11月13日 qxs
	 * @throws DataRefreshingException 
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/{id}",method=RequestMethod.POST)
	public Result readMessage(@PathVariable String id, boolean hasRead, HttpServletRequest request) throws Exception{
		Message message = new Message();
		message.setReceiveUID(ServletBox.getLoginUID(request));
		message.setHasRead(hasRead);
		inboxService.refreshReadStatus(id.split(","), message);
		return Result.ok();
	}
	
	/**
	 * 将entity值copy到vo
	 * @param sourceBo
	 * @param targetVO
	 * @return
	 * 2015年11月13日 qxs
	 */
	private MessageVO copyBoToVo(Message sourceBo, MessageVO targetVO){
		if(sourceBo==null || targetVO==null){
			return targetVO;
		}
		BeanUtils.copyProperties(sourceBo, targetVO);
		if(sourceBo.getReceiveUser()!=null && targetVO.getReceiveUser()!=null){
			BeanUtils.copyProperties(sourceBo.getReceiveUser(), targetVO.getReceiveUser());
		}
		if(sourceBo.getSendUser()!=null && targetVO.getSendUser()!=null){
			BeanUtils.copyProperties(sourceBo.getSendUser(), targetVO.getSendUser());
		}
		return targetVO;
	}

}
