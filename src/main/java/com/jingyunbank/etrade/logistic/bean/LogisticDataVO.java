package com.jingyunbank.etrade.logistic.bean;

import java.io.Serializable;

public class LogisticDataVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

		private String time;
		private String content;
		private String remark;
		public String getTime() {
			return time;
		}
		public void setTime(String time) {
			this.time = time;
		}
		public String getContent() {
			return content;
		}
		public void setContent(String content) {
			this.content = content;
		}
		public String getRemark() {
			return remark;
		}
		public void setRemark(String remark) {
			this.remark = remark;
		}
		
		
		
	}
