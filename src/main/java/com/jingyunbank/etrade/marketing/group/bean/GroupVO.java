package com.jingyunbank.etrade.marketing.group.bean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jingyunbank.etrade.api.user.bo.Users;

public class GroupVO {
	
	private GroupGoodsVO goods;
	private String leaderUID;
	private String ID;
	private Users leader;
	@JsonFormat(pattern="yyyy-MM-dd HH:mm" ,locale="zh", timezone="GMT+8")
	private Date start;
	private String status;
	private String groupGoodsID;
	private List<GroupUserVO> buyers = new ArrayList<GroupUserVO>();
	private String path;
	private String propertyValue;
	private String gname;
	
	
	private String orderno;
	@JsonFormat(pattern="yyyy-MM-dd HH:mm" ,locale="zh", timezone="GMT+8")
	private Date ordertime;
	private BigDecimal postage;
	private double totalPrice;
	
	
	
	/**
	 * @return the totalPrice
	 */
	public double getTotalPrice() {
		return totalPrice;
	}
	/**
	 * @param totalPrice the totalPrice to set
	 */
	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}
	/**
	 * @return the postage
	 */
	public BigDecimal getPostage() {
		return postage;
	}
	/**
	 * @param postage the postage to set
	 */
	public void setPostage(BigDecimal postage) {
		this.postage = postage;
	}
	/**
	 * @return the orderno
	 */
	public String getOrderno() {
		return orderno;
	}
	/**
	 * @param orderno the orderno to set
	 */
	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}
	
	/**
	 * @return the ordertime
	 */
	public Date getOrdertime() {
		return ordertime;
	}
	/**
	 * @param ordertime the ordertime to set
	 */
	public void setOrdertime(Date ordertime) {
		this.ordertime = ordertime;
	}
	/**
	 * @return the gname
	 */
	public String getGname() {
		return gname;
	}
	/**
	 * @param gname the gname to set
	 */
	public void setGname(String gname) {
		this.gname = gname;
	}
	/**
	 * @return the propertyValue
	 */
	public String getPropertyValue() {
		return propertyValue;
	}
	/**
	 * @param propertyValue the propertyValue to set
	 */
	public void setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
	}
	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}
	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the leaderUID
	 */
	public String getLeaderUID() {
		return leaderUID;
	}
	/**
	 * @param leaderUID the leaderUID to set
	 */
	public void setLeaderUID(String leaderUID) {
		this.leaderUID = leaderUID;
	}
	/**
	 * @return the buyers
	 */
	public List<GroupUserVO> getBuyers() {
		return buyers;
	}
	/**
	 * @param buyers the buyers to set
	 */
	public void setBuyers(List<GroupUserVO> buyers) {
		this.buyers = buyers;
	}
	public GroupGoodsVO getGoods() {
		return goods;
	}
	public void setGoods(GroupGoodsVO goods) {
		this.goods = goods;
	}
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public Users getLeader() {
		return leader;
	}
	public void setLeader(Users leader) {
		this.leader = leader;
	}
	public Date getStart() {
		return start;
	}
	public void setStart(Date start) {
		this.start = start;
	}
	/**
	 * @return the groupGoodsID
	 */
	public String getGroupGoodsID() {
		return groupGoodsID;
	}
	/**
	 * @param groupGoodsID the groupGoodsID to set
	 */
	public void setGroupGoodsID(String groupGoodsID) {
		this.groupGoodsID = groupGoodsID;
	}
	
	
}
