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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.KeyGen;
import com.jingyunbank.core.Page;
import com.jingyunbank.core.Range;
import com.jingyunbank.core.Result;
import com.jingyunbank.core.web.AuthBeforeOperation;
import com.jingyunbank.core.web.Login;
import com.jingyunbank.core.web.ServletBox;
import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.message.bo.Message;
import com.jingyunbank.etrade.api.message.service.IInboxService;
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
	@RequestMapping(value="/",method=RequestMethod.POST)
	public Result<String> sendSysMessage(HttpServletRequest request,@Valid MessageVO messageVO, BindingResult valid) throws Exception{
		//验证vo信息
		if(valid.hasErrors()){
			List<ObjectError> errors = valid.getAllErrors();
			return Result.fail(errors.stream()
						.map(oe -> Arrays.asList(oe.getDefaultMessage()).toString())
						.collect(Collectors.joining(" ; ")));
		}
		String[] receiveUids = messageVO.getReceiveUID().split(",");
		messageVO.setAddip(ServletBox.ip(request));
		messageVO.setSentUID(Login.UID(request));
		messageVO.setStatus(Message.STATUS_SUC);
		messageVO.setType(Message.TYPE_LETTER);
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
	public Result<MessageVO> single(@PathVariable String id, HttpServletRequest request)throws Exception{
		Optional<Message> messageOption = inboxService.single(id);
		if(messageOption.isPresent()){
			//如果未读则置为已读
			if(!messageOption.get().isHasRead()){
				messageOption.get().setHasRead(true);
				inboxService.refreshReadStatus(messageOption.get().getID(), true);
			}
			
			return Result.ok(copyBoToVo(messageOption.get(), new MessageVO()));
		}
		return Result.fail("该消息不存在,请确认链接是否正确");
	}
	
	
	/**
	 * 查询用户的消息列表
	 * @param uid
	 * @param page
	 * @return
	 * @throws Exception
	 * 2015年11月20日 qxs
	 */
	/*@AuthBeforeOperation*/
	@RequestMapping(value="/list/{uid}/{from}/{size}",method=RequestMethod.GET)
	public Result<List<MessageVO>> getList(@PathVariable String uid ,@PathVariable int from, @PathVariable int size) throws Exception{
		Range range = new Range();
		range.setFrom(from);
		range.setTo(from+size);
		return Result.ok( inboxService.list(uid, range)
				.stream().map(bo ->{
					return copyBoToVo(bo, new MessageVO());
				}).collect(Collectors.toList()));
	}
	
	
	/**
	 * 查询用户的消息列表数量
	 * @param uid
	 * @param request
	 * @return
	 * @throws Exception
	 * 2015年11月20日 qxs
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/amount/{uid}",method=RequestMethod.GET)
	public Result<Integer> count(@PathVariable String uid , HttpServletRequest request) throws Exception{
		return Result.ok( inboxService.count(uid));
	}
	
	/**
	 * 查询用户未读的消息列表
	 * @param uid
	 * @param request
	 * @param page
	 * @return
	 * @throws Exception
	 * 2015年11月20日 qxs
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/unread/list/{uid}",method=RequestMethod.GET)
	public Result<List<MessageVO>> getUnreadList(@PathVariable String uid , HttpServletRequest request, Page page) throws Exception{
		Range range = new Range();
		range.setFrom(page.getOffset());
		range.setTo(page.getOffset()+page.getSize());
		return Result.ok( inboxService.listUnread(uid, range)
				.stream().map(bo ->{
					return copyBoToVo(bo, new MessageVO());
				}).collect(Collectors.toList()));
	}
	
	
	/**
	 * 查询用户未读的消息列表数量
	 * @param uid
	 * @param request
	 * @return
	 * @throws Exception
	 * 2015年11月20日 qxs
	 */
	/*@AuthBeforeOperation*/
	@RequestMapping(value="/unread/amount/{uid}",method=RequestMethod.GET)
	public Result<Integer> countUnread(@PathVariable String uid , HttpServletRequest request) throws Exception{
		return Result.ok( inboxService.countUnread(uid));
	}
	
	/**
	 * 查询用户已读的消息列表
	 * @param uid
	 * @param request
	 * @param page
	 * @return
	 * @throws Exception
	 * 2015年11月20日 qxs
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/read/list/{uid}",method=RequestMethod.GET)
	public Result<List<MessageVO>> getReadList(@PathVariable String uid , HttpServletRequest request, Page page) throws Exception{
		Range range = new Range();
		range.setFrom(page.getOffset());
		range.setTo(page.getOffset()+page.getSize());
		return Result.ok( inboxService.listRead(uid, range)
				.stream().map(bo ->{
					return copyBoToVo(bo, new MessageVO());
				}).collect(Collectors.toList()));
	}
	
	
	/**
	 * 查询用户已读的消息列表数量
	 * @param uid
	 * @param request
	 * @return
	 * @throws Exception
	 * 2015年11月20日 qxs
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/read/amount/{uid}",method=RequestMethod.GET)
	public Result<Integer> countRead(@PathVariable String uid , HttpServletRequest request) throws Exception{
		return Result.ok( inboxService.countRead(uid));
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
	public Result<String> remove(@PathVariable String id, HttpServletRequest request) throws Exception{
		
		inboxService.remove(id.split(","), Login.UID(request));
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
	@RequestMapping(value="/{id}",method=RequestMethod.PUT)
	public Result<String> readMessage(@PathVariable String id,@RequestParam(value="read") boolean read, HttpServletRequest request) throws Exception{
		inboxService.refreshReadStatus(id.split(","), read);
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
