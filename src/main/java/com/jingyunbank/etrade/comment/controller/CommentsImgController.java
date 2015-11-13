package com.jingyunbank.etrade.comment.controller;

import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jingyunbank.core.Result;
import com.jingyunbank.etrade.api.comment.service.ICommentImgService;
import com.jingyunbank.etrade.comment.bean.CommentsImgVO;

@Controller
public class CommentsImgController{
	
	@Autowired
	private ICommentImgService commentImgService;
	
	@RequestMapping(value="/api/commentsImg/getImg/{imgid}")
	@ResponseBody
	public Result selectById(@PathVariable String imgid,HttpSession session){
		
		return Result.ok(commentImgService.getById(imgid)
				.stream().map(bo -> {
				CommentsImgVO vo = new CommentsImgVO();
				BeanUtils.copyProperties(bo, vo);
				return vo;
		}).collect(Collectors.toList()));
	}
}
