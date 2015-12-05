package com.jingyunbank.etrade.message.bean;

import java.io.Serializable;
import java.util.Date;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jingyunbank.etrade.user.bean.UserVO;

public class MessageVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4323383200012547451L;
	private String ID;
	private String sentUID;//发送者
	@NotEmpty(message="收件人不能为空")
	private String receiveUID;//接收者
	@NotEmpty(message="标题不能为空")
	private String title;//标题
	@NotEmpty(message="内容不能为空")
	private String content;//内容
	private String type;//类型
	private int status;//状态 1:成功 2:失败 3:删除
	@JsonFormat(pattern="yyyy-MM-dd HH:mm" ,locale="zh", timezone="GMT+8")
	private Date addtime;//添加时间
	private String addip;//添加人的ip
	private boolean hasRead;//是否已读
	
	private UserVO sendUser = new UserVO();//发送者
	private UserVO receiveUser = new UserVO();//接收者
	
	public UserVO getSendUser() {
		return sendUser;
	}
	public void setSendUser(UserVO sendUser) {
		this.sendUser = sendUser;
	}
	public UserVO getReceiveUser() {
		return receiveUser;
	}
	public void setReceiveUser(UserVO receiveUser) {
		this.receiveUser = receiveUser;
	}
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
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
