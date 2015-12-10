package com.jingyunbank.etrade.comment.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jingyunbank.etrade.api.comment.bo.CommentsImg;
import com.jingyunbank.etrade.user.bean.UserInfoVO;
import com.jingyunbank.etrade.user.bean.UserVO;

public class CommentsVO {
	private String ID;
	private String UID;
	private String OID;
	private String replyUID;//对于评论进行回复的人
	private String GID;
	private String ImgID;
	private String replyComment;//回复人回复的内容
	private String goodsComment;//商品的评价
	private int commentGrade;//商品评价的等级
	private String goodsService;//商品的服务
	private int serviceGrade;//服务级别
	@JsonFormat(pattern="yyyy-MM-dd HH:mm" ,locale="zh", timezone="GMT+8")
	private Date addtime;//添加评价的时间
	private int commentStatus;//评价的是否的状态1:已评价2：未评价
	private int orders;
	private float zongjibie;
	private int personCount;
	private int allLevel;
	private UserVO userVO; 
	private UserInfoVO userInfoVO;
	private List<CommentsImg> imgs=new ArrayList<CommentsImg>();
	private List<String> picture;
	
	
	
	
	public int getAllLevel() {
		return allLevel;
	}
	public void setAllLevel(int allLevel) {
		this.allLevel = allLevel;
	}
	public int getPersonCount() {
		return personCount;
	}
	public void setPersonCount(int personCount) {
		this.personCount = personCount;
	}
	public float getZongjibie() {
		return zongjibie;
	}
	public void setZongjibie(float zongjibie) {
		this.zongjibie = zongjibie;
	}
	public List<String> getPicture() {
		return picture;
	}
	public void setPicture(List<String> picture) {
		this.picture = picture;
	}

	public List<CommentsImg> getImgs() {
		return imgs;
	}
	
	public String getOID() {
		return OID;
	}
	public void setOID(String oID) {
		OID = oID;
	}

	public void setImgs(List<CommentsImg> imgs) {
		this.imgs = imgs;
	}
	public UserInfoVO getUserInfoVO() {
		return userInfoVO;
	}
	public void setUserInfoVO(UserInfoVO userInfoVO) {
		this.userInfoVO = userInfoVO;
	}
	public UserVO getUserVO() {
		return userVO;
	}
	public void setUserVO(UserVO userVO) {
		this.userVO = userVO;
	}
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getUID() {
		return UID;
	}
	public void setUID(String uID) {
		UID = uID;
	}
	public String getReplyUID() {
		return replyUID;
	}
	public void setReplyUID(String replyUID) {
		this.replyUID = replyUID;
	}
	public String getGID() {
		return GID;
	}
	public void setGID(String gID) {
		GID = gID;
	}
	public String getImgID() {
		return ImgID;
	}
	public void setImgID(String imgID) {
		ImgID = imgID;
	}
	public Date getAddtime() {
		return addtime;
	}
	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}
	
	public String getReplyComment() {
		return replyComment;
	}
	public void setReplyComment(String replyComment) {
		this.replyComment = replyComment;
	}
	public String getGoodsComment() {
		return goodsComment;
	}
	public void setGoodsComment(String goodsComment) {
		this.goodsComment = goodsComment;
	}
	public int getCommentGrade() {
		return commentGrade;
	}
	public void setCommentGrade(int commentGrade) {
		this.commentGrade = commentGrade;
	}
	public String getGoodsService() {
		return goodsService;
	}
	public void setGoodsService(String goodsService) {
		this.goodsService = goodsService;
	}
	public int getServiceGrade() {
		return serviceGrade;
	}
	public void setServiceGrade(int serviceGrade) {
		this.serviceGrade = serviceGrade;
	}
	public int getCommentStatus() {
		return commentStatus;
	}
	public void setCommentStatus(int commentStatus) {
		this.commentStatus = commentStatus;
	}
	public int getOrders() {
		return orders;
	}
	public void setOrders(int orders) {
		this.orders = orders;
	}
	
}
