package com.jingyunbank.etrade.message.entity;

import java.io.Serializable;
import java.util.Date;

import com.jingyunbank.etrade.user.entity.UserEntity;

public class MessageEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3552899576011642079L;
	private String ID;
	private String [] IDs;
	private String title;//标题
	private String content;//内容
	private String type;//类型
	private int status;//状态 1:成功 2:失败 3:删除
	private Date addtime;//添加时间
	private String addip;//添加人的ip
	private boolean hasRead;//是否已读
	private String sentUID;//发送者
	private String receiveUID;//接收者
	private String[] receiveUIDs;//接收者
	private UserEntity sendUser;//发送者
	private UserEntity receiveUser;//接收者
	
	private long offset ;//偏移量
	private long size;//开始点
	
	private boolean valid = true; //查询时的状态true->查询未删除的
	private boolean needReadStatus = false;//查询时是否需要加上读取状态
	
	public boolean isNeedReadStatus() {
		return needReadStatus;
	}
	public void setNeedReadStatus(boolean needReadStatus) {
		this.needReadStatus = needReadStatus;
	}
	public boolean isValid() {
		return valid;
	}
	public void setValid(boolean valid) {
		this.valid = valid;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public String getSentUID() {
		return sentUID;
	}
	public void setSentUID(String sentUID) {
		this.sentUID = sentUID;
	}
	public String getReceiveUID() {
		return receiveUID;
	}
	public void setReceiveUID(String receiveUID) {
		this.receiveUID = receiveUID;
	}
	public String[] getReceiveUIDs() {
		return receiveUIDs;
	}
	public void setReceiveUIDs(String[] receiveUIDs) {
		this.receiveUIDs = receiveUIDs;
	}
	public String[] getIDs() {
		return IDs;
	}
	public void setIDs(String[] iDs) {
		IDs = iDs;
	}
	
	public long getOffset() {
		return offset;
	}
	public void setOffset(long offset) {
		this.offset = offset;
	}
	public UserEntity getSendUser() {
		return sendUser;
	}
	public void setSendUser(UserEntity sendUser) {
		this.sendUser = sendUser;
	}
	public UserEntity getReceiveUser() {
		return receiveUser;
	}
	public void setReceiveUser(UserEntity receiveUser) {
		this.receiveUser = receiveUser;
	}
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Date getAddtime() {
		return addtime;
	}
	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}
	public String getAddip() {
		return addip;
	}
	public void setAddip(String addip) {
		this.addip = addip;
	}
	public boolean isHasRead() {
		return hasRead;
	}
	public void setHasRead(boolean hasRead) {
		this.hasRead = hasRead;
	}
	
}
