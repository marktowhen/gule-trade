package com.jingyunbank.etrade.comment.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.core.Range;
import com.jingyunbank.etrade.api.comment.bo.Comments;
import com.jingyunbank.etrade.api.comment.service.ICommentService;
import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.exception.DataRemovingException;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.comment.dao.CommentsDao;
import com.jingyunbank.etrade.comment.entity.CommentsEntity;
@Service("commentService")
public class CommentsService implements ICommentService{
	
	@Autowired
	private CommentsDao commentsDao;

	/**
	 * 保存评论的信息和图片
	 */
	public boolean save(Comments comments) throws DataSavingException {
		boolean flag=false;
		int result=0;
		CommentsEntity commentsEntity=new CommentsEntity();
		BeanUtils.copyProperties(comments, commentsEntity);
		try {
			result=commentsDao.insert(commentsEntity);
		} catch (Exception e) {
			throw new DataSavingException(e);
		}
		if(result>0){
			 flag=true;
		}else{
			flag=false;
			}
		return flag;
	}
	/**
	 * 通过gid查询产品的平评论
	 */
	@Override
	public List<Comments> list(String gid) {
		return commentsDao.selectCommentByGid(gid)
				.stream().map(entity -> {
					Comments bo=new Comments();
					BeanUtils.copyProperties(entity, bo);
					return bo;
				}).collect(Collectors.toList());
	}
	/**
	 * 通过id查出对应的评论信息
	 */
	@Override
	public Optional<Comments> single(String id) {
		// TODO Auto-generated method stub
		
		CommentsEntity commentsEntity=commentsDao.selectById(id);
		Comments comments=new Comments();
		BeanUtils.copyProperties(commentsEntity, comments);
		return Optional.of(comments);
	}

	/**
	 * 通过id删除评论
	 */
	@Override
	public void remove(String id) throws DataRemovingException {
		// TODO Auto-generated method stub
		try {
			commentsDao.delete(id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public void refreshStatus(String[] ids, Comments comments) throws DataRefreshingException {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 通过gid查出总共的评论条数
	 */
	@Override
	public int commentCount(String gid) {
		return commentsDao.commentCount(gid);
	}
	/**
	 * 通过gid和评论的级别查询好评或中评或差评
	 */
	@Override
	public List<Comments> list(String gid, int commentGrade,int picture, Range range) {
		return commentsDao.selectCommentGradeByGid(gid,commentGrade,picture,range.getFrom(),range.getTo()-range.getFrom())
				.stream().map(entity -> {
					Comments bo=new Comments();
					BeanUtils.copyProperties(entity, bo);
					return bo;
				}).collect(Collectors.toList());
	}
	/**
	 * 查出所有的评价信息
	 */
	@Override
	public List<Comments> list() {
		return	commentsDao.selectComment().stream().map(entity ->{
			Comments bo=new Comments();
			BeanUtils.copyProperties(entity, bo);
			return bo;
		}).collect(Collectors.toList());
		 
	}
	@Override
	public Optional<Comments> singleByOid(String oid) {
		
		CommentsEntity commentsEntity=commentsDao.selectCommentByOid(oid);
		Comments comments=new Comments();
		BeanUtils.copyProperties(commentsEntity, comments);
		return Optional.of(comments);
	}
	
	
}
