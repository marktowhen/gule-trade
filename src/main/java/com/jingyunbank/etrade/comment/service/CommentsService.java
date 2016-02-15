package com.jingyunbank.etrade.comment.service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jingyunbank.core.Range;
import com.jingyunbank.etrade.api.comment.bo.Comments;
import com.jingyunbank.etrade.api.comment.bo.CommentsImg;
import com.jingyunbank.etrade.api.comment.service.ICommentImgService;
import com.jingyunbank.etrade.api.comment.service.ICommentService;
import com.jingyunbank.etrade.api.exception.DataRemovingException;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.order.presale.bo.OrderStatusDesc;
import com.jingyunbank.etrade.api.order.presale.service.IOrderGoodsService;
import com.jingyunbank.etrade.api.order.presale.service.IOrderService;
import com.jingyunbank.etrade.comment.dao.CommentsDao;
import com.jingyunbank.etrade.comment.entity.CommentsEntity;
import com.jingyunbank.etrade.config.CacheConfig;
@Service("commentService")
public class CommentsService implements ICommentService{
	
	@Autowired
	private CommentsDao commentsDao;
	@Autowired
	private ICommentImgService commentImgService;
	@Autowired
	private IOrderGoodsService orderGoodsService;
	@Autowired
	private IOrderService orderService;

	/**
	 * 保存评论的信息和图片
	 */
	@Override
	@Transactional
	@CacheEvict(cacheNames="commentCache", allEntries=true)
	public boolean save(Comments comments, List<CommentsImg> imgList) throws DataSavingException {
		CommentsEntity commentsEntity=new CommentsEntity();
		BeanUtils.copyProperties(comments, commentsEntity);
		try {
			commentsDao.insert(commentsEntity);
			if(Objects.nonNull(imgList)){
				commentImgService.save(imgList);
			}
			//修改订单商品的状态
			orderGoodsService.refreshGoodStatus(comments.getOID(), comments.getGID(), OrderStatusDesc.COMMENTED);
			
			//若订单商品都已评价 修改订单的状态
			if(orderGoodsService.count(comments.getOID(), OrderStatusDesc.RECEIVED)==0){
				orderService.refreshStatus(Arrays.asList(comments.getOID()), OrderStatusDesc.COMMENTED);
			}
			return true;
		} catch (Exception e) {
			throw new DataSavingException(e);
		}
	}
	/**
	 * 通过gid查询产品的平评论
	 */
	@Override
	@Cacheable(cacheNames="commentCache", keyGenerator=CacheConfig.CUSTOM_CACHE_KEY_GENERATOR)
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
		CommentsEntity commentsEntity=commentsDao.selectById(id);
		Comments comments=new Comments();
		BeanUtils.copyProperties(commentsEntity, comments);
		return Optional.of(comments);
	}

	/**
	 * 通过id删除评论
	 */
	@Override
	@CacheEvict(cacheNames="commentCache", allEntries=true)
	public void remove(String id) throws DataRemovingException {
		try {
			commentsDao.delete(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 通过gid查出总共的评论条数
	 */
	@Override
	@Cacheable(cacheNames="commentCache", keyGenerator=CacheConfig.CUSTOM_CACHE_KEY_GENERATOR)
	public int commentCount(String gid) {
		return commentsDao.count(gid);
	}
	/**
	 * 通过gid和评论的级别查询好评或中评或差评
	 */
	@Override
	@Cacheable(cacheNames="commentCache", keyGenerator=CacheConfig.CUSTOM_CACHE_KEY_GENERATOR)
	public List<Comments> list(String gid, int commentGrade,boolean existsImg, Range range) {
		return commentsDao.selectCommentGradeByGid(gid,commentGrade,existsImg,range.getFrom(),range.getTo()-range.getFrom())
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
	@Cacheable(cacheNames="commentCache", keyGenerator=CacheConfig.CUSTOM_CACHE_KEY_GENERATOR)
	public List<Comments> list() {
		return	commentsDao.selectComment().stream().map(entity ->{
			Comments bo=new Comments();
			BeanUtils.copyProperties(entity, bo);
			return bo;
		}).collect(Collectors.toList());
		 
	}
	@Override
	public Optional<Comments> singleByOid(String oid,String gid) {
		
		CommentsEntity commentsEntity=commentsDao.selectCommentByOid(oid,gid);
		Comments comments=new Comments();
		BeanUtils.copyProperties(commentsEntity, comments);
		return Optional.of(comments);
	}
	
	
}
