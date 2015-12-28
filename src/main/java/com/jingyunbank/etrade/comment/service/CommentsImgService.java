package com.jingyunbank.etrade.comment.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.comment.bo.CommentsImg;
import com.jingyunbank.etrade.api.comment.service.ICommentImgService;
import com.jingyunbank.etrade.api.exception.DataRemovingException;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.comment.dao.CommentsImgDao;
import com.jingyunbank.etrade.comment.entity.CommentsImgEntity;
@Service
public class CommentsImgService implements ICommentImgService{
	@Autowired
	private CommentsImgDao commentsImgDao;

	@Override
	public boolean save(CommentsImg commentsImg) throws DataSavingException {
		boolean flag;
		int result=0;
		CommentsImgEntity commentsImgEntity=new CommentsImgEntity();
		BeanUtils.copyProperties(commentsImg, commentsImgEntity);
		try {
			result=commentsImgDao.insert(commentsImgEntity);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new DataSavingException(e);
		}
		if(result>0){
			 flag=true;
		}else{
			flag=false;
			}
		return flag;
	}

	@Override
	public List<CommentsImg> getById(String id) {
		return commentsImgDao.selectById(id)
				.stream().map(entity -> {
				CommentsImg bo=new CommentsImg();
				BeanUtils.copyProperties(entity, bo);
				return bo;
		}).collect(Collectors.toList());
		
		/*return commentsImgDao.selectById(id)
				.stream().map(entity -> {
					CommentsImg bo=new CommentsImg();
					BeanUtils.copyProperties(entity, bo);
					return bo;
				}).collect(Collectors.toList());*/
		
	}

	@Override
	public void remove(String id) throws DataRemovingException {
		
		try {
			commentsImgDao.delete(id);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}

	@Override
	public Optional<CommentsImg> singleById(String id) {
		CommentsImgEntity commentsImgEntity=	commentsImgDao.getById(id);
		CommentsImg bo = new CommentsImg();
		BeanUtils.copyProperties(commentsImgEntity, bo);
		return Optional.of(bo);
	}

}
