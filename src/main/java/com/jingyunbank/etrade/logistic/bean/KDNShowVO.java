package com.jingyunbank.etrade.logistic.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 
* Title: 	快递VO
* @author    duanxf
* @date      2016年1月21日
 */
public class KDNShowVO implements Serializable{


	private static final long serialVersionUID = 1L;

	private String ebusinessid; //电商用户id
	private String ordercode;	//订单编号
	private String shippercode; //快递公司编码
	private String logisticcode; //物流运单号
	private boolean  success;  //成功与否
	private String reason; //失败原因
	private String state; // 物流状态: 2-在途中，3-签收,4-问题件
	private List<KDNContentVO> traces; //traces/物流轨迹详情
	private String callback; //用户标识
	public String getEbusinessid() {
		return ebusinessid;
	}
	public void setEbusinessid(String ebusinessid) {
		this.ebusinessid = ebusinessid;
	}
	public String getOrdercode() {
		return ordercode;
	}
	public void setOrdercode(String ordercode) {
		this.ordercode = ordercode;
	}
	public String getShippercode() {
		return shippercode;
	}
	public void setShippercode(String shippercode) {
		this.shippercode = shippercode;
	}
	public String getLogisticcode() {
		return logisticcode;
	}
	public void setLogisticcode(String logisticcode) {
		this.logisticcode = logisticcode;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public List<KDNContentVO> getTraces() {
		return traces;
	}
	public void setTraces(List<KDNContentVO> traces) {
		this.traces = traces;
	}
	public String getCallback() {
		return callback;
	}
	public void setCallback(String callback) {
		this.callback = callback;
	}

	
	
	
	
}
