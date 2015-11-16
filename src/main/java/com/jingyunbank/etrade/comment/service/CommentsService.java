package com.jingyunbank.etrade.comment.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.comment.bo.Comments;
import com.jingyunbank.etrade.api.comment.bo.CommentsImg;
import com.jingyunbank.etrade.api.comment.service.ICommentService;
import com.jingyunbank.etrade.api.exception.DataRemovingException;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.order.bo.Orders;
import com.jingyunbank.etrade.comment.dao.CommentsDao;
import com.jingyunbank.etrade.comment.dao.CommentsImgDao;
import com.jingyunbank.etrade.comment.entity.CommentsEntity;
import com.jingyunbank.etrade.comment.entity.CommentsImgEntity;
@Service("commentService")
public class CommentsService implements ICommentService{
	
	@Autowired
	private CommentsDao commentsDao;
	@Autowired
	private CommentsImgDao commentsImgDao;
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
	public List<Comments> getCommentsByGid(String gid) {
/*		List<CommentsEntity> lists=commentsDao.selectCommentByGid(gid);
			Comments bo=new Comments();
			BeanUtils.copyProperties(lists, bo);
			List<Comments> comments=(List<Comments>) bo;
				return comments;*/
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
	public Comments getById(String id) {
		// TODO Auto-generated method stub
		CommentsEntity commentsEntity=commentsDao.selectById(id);
		Comments comments=new Comments();
		BeanUtils.copyProperties(commentsEntity, comments);
		return comments;
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
	
}