package com.jingyunbank.etrade.comment.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jingyunbank.etrade.comment.entity.CommentsEntity;

public interface CommentsDao {
	
	public boolean insert(CommentsEntity commentsEntity) throws Exception;
	
	public List<CommentsEntity> selectCommentByGid(String gid);
	
	public List<CommentsEntity> selectCommentGradeByGid(@Param(value="GID") String gid,@Param(value="commentGrade") int commentGrade,@Param(value="existsImg") boolean existsImg,@Param(value="offset")long offset, @Param(value="size")long size);
	
	public CommentsEntity selectById(String id);
	
	public void delete(String id) throws Exception;
	
	
	public int count(String gid);
	
	public CommentsEntity selectCommentByOid(@Param(value="oid") String oid,@Param(value="gid") String gid);
	
	public List<CommentsEntity> selectComment();
}
