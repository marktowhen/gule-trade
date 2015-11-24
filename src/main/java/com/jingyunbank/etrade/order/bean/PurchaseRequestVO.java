package com.jingyunbank.etrade.order.bean;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 用户购买商品的请求VO<br>
 * 该请求VO中应当包含收货人地址，支付方式，是否开具发票（发票信息）
 * 是否使用优惠券，是否使用积分等信息<br>
 * 另外还应当包含有哪些商品.<br>
 * <strong>
 * 	注：该请求VO中可能包含多个商家的多个商品，而对于每个商家都会生成一个订单。<br>
 * 	每个订单中包含收件人，收货地址，支付方式，配送方式，发票开据，优惠活动等信息。<br>
 * 	以及该订单中的商品详情。<br>
 * 	买家<---->订单{商品1， 商品2， 商品3}<---->商家
 * </strong>
 * 
 */
public class PurchaseRequestVO {

	@NotNull
	private String receiver;//收货人姓名
	@Size(min=22, max=22, message="收货地址错误。")
	@NotNull
	private String addressID;//收货地址->address id
	private String UID;//下单人
	@Size(min=1, max=12, message="支付类型错误。")
	@NotNull
	private String paytypeCode;//支付方式 code
	@NotNull
	private String paytypeName;
	
	private boolean requireInvoice = false;//是否需要发票, 默认不需要
	private boolean businessReceipt;//是否是单位发票
	private String invoiceTitle;//发票抬头
	
	@NotNull
	@Size(min=1)
	private List<PurchaseOrderVO> orders = new ArrayList<>();
	
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public String getAddressID() {
		return addressID;
	}
	public void setAddressID(String addressID) {
		this.addressID = addressID;
	}
	public String getUID() {
		return UID;
	}
	public void setUID(String uID) {
		UID = uID;
	}
	public String getPaytypeCode() {
		return paytypeCode;
	}
	public void setPaytypeCode(String paytypeCode) {
		this.paytypeCode = paytypeCode;
	}
	public String getPaytypeName() {
		return paytypeName;
	}
	public void setPaytypeName(String paytypeName) {
		this.paytypeName = paytypeName;
	}
	public boolean isRequireInvoice() {
		return requireInvoice;
	}
	public void setRequireInvoice(boolean requireInvoice) {
		this.requireInvoice = requireInvoice;
	}
	public boolean isBusinessReceipt() {
		return businessReceipt;
	}
	public void setBusinessReceipt(boolean businessReceipt) {
		this.businessReceipt = businessReceipt;
	}
	public String getInvoiceTitle() {
		return invoiceTitle;
	}
	public List<PurchaseOrderVO> getOrders() {
		return orders;
	}
	public void setOrders(List<PurchaseOrderVO> orders) {
		this.orders = orders;
	}
	public void setInvoiceTitle(String invoiceTitle) {
		this.invoiceTitle = invoiceTitle;
	}
	
}
