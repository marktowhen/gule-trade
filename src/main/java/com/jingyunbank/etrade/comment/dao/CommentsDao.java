package com.jingyunbank.etrade.comment.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jingyunbank.etrade.comment.entity.CommentsEntity;

public interface CommentsDao {
	
	public int insert(CommentsEntity commentsEntity) throws Exception;
	
	public List<CommentsEntity> selectCommentByGid(String gid);
	
	public List<CommentsEntity> selectCommentGradeByGid(@Param(value="GID") String gid,@Param(value="commentGrade") int commentGrade,@Param(value="picture") int picture,@Param(value="offset")long offset, @Param(value="size")long size);
	
	public CommentsEntity selectById(String id);
	
	public void delete(String id) throws Exception;
	
	public boolean updateStatus(CommentsEntity commentsEntity) throws Exception;
	
	public boolean refreshReadStatus(CommentsEntity commentsEntity) throws Exception;
	
	public int commentCount(String gid);
	
	public CommentsEntity selectCommentByOid(String oid);
	
	public List<CommentsEntity> selectComment();
}
