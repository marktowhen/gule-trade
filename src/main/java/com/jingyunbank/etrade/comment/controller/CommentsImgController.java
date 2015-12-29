package com.jingyunbank.etrade.comment.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.Result;
import com.jingyunbank.core.web.AuthBeforeOperation;
import com.jingyunbank.etrade.api.comment.bo.CommentsImg;
import com.jingyunbank.etrade.api.comment.service.ICommentImgService;
import com.jingyunbank.etrade.comment.bean.CommentsImgVO;

@RestController
public class CommentsImgController{
	
	@Autowired
	private ICommentImgService commentImgService;
	
	@AuthBeforeOperation
	@RequestMapping(value="/api/commentsImg/getImg/{imgid}")
	@ResponseBody
	public Result<List<CommentsImgVO>> selectById(@PathVariable String imgid,HttpSession session) throws Exception{
		
		return Result.ok(commentImgService.list(imgid)
				.stream().map(bo -> {
				CommentsImgVO vo = new CommentsImgVO();
				BeanUtils.copyProperties(bo, vo);
				return vo;
		}).collect(Collectors.toList()));
	}
	
	@RequestMapping(value="/api/comments/img/{id}",method=RequestMethod.GET)
	public Result<CommentsImgVO> getCommentImg(@PathVariable String id){
		Optional<CommentsImg> optinal=commentImgService.singleById(id);
		CommentsImgVO vo = new CommentsImgVO();
		BeanUtils.copyProperties(optinal.get(), vo);
		return Result.ok(vo);
		
	}
}
