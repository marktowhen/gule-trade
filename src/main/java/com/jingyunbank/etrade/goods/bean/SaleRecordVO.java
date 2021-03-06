package com.jingyunbank.etrade.goods.bean;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 商品交易记录VO Title: SaleRecordsVO
 * 
 * @author duanxf
 * @date 2015年12月18日
 */
public class SaleRecordVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String ID;
	private String UID;
	private String uname;
	private String GID;
	private int count;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
	private Date salesDate;

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

	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public String getGID() {
		return GID;
	}
	public void setGID(String gID) {
		GID = gID;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}

	public Date getSalesDate() {
		return salesDate;
	}

	public void setSalesDate(Date salesDate) {
		this.salesDate = salesDate;
	}
}
