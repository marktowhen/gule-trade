package com.jingyunbank.etrade.comment.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.comment.bo.CommentsImg;
import com.jingyunbank.etrade.api.comment.service.ICommentImgService;
import com.jingyunbank.etrade.api.exception.DataRemovingException;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.comment.dao.CommentsImgDao;
import com.jingyunbank.etrade.comment.entity.CommentsImgEntity;
import com.jingyunbank.etrade.config.CacheConfig;
@Service
public class CommentsImgService implements ICommentImgService{
	@Autowired
	private CommentsImgDao commentsImgDao;

	@Override
	@CacheEvict(cacheNames="commentCache", allEntries=true)
	public boolean save(CommentsImg commentsImg) throws DataSavingException {
		boolean flag;
		int result=0;
		CommentsImgEntity commentsImgEntity=new CommentsImgEntity();
		BeanUtils.copyProperties(commentsImg, commentsImgEntity);
		try {
			result=commentsImgDao.insert(commentsImgEntity);
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

	@Override
	@Cacheable(cacheNames="commentCache", keyGenerator=CacheConfig.CUSTOM_CACHE_KEY_GENERATOR)
	public List<CommentsImg> list(String commentID) {
		return commentsImgDao.selectById(commentID)
				.stream().map(entity -> {
				CommentsImg bo=new CommentsImg();
				BeanUtils.copyProperties(entity, bo);
				return bo;
		}).collect(Collectors.toList());
	}

	@Override
	@CacheEvict(cacheNames="commentCache", allEntries=true)
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
