package com.jingyunbank.etrade.order.postsale.entity;

/**
 * 退单的证明图片
 */
public class RefundCertificateEntity {

	private String ID;
	private String RID;
	private String path;
	
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getRID() {
		return RID;
	}
	public void setRID(String rID) {
		RID = rID;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
}
