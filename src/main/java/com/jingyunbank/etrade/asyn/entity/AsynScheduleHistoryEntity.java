package com.jingyunbank.etrade.asyn.entity;

import java.util.Date;

public class AsynScheduleHistoryEntity extends AsynScheduleEntity {


	private Date finishtime;
	public Date getFinishtime() {
		return finishtime;
	}
	public void setFinishtime(Date finishtime) {
		this.finishtime = finishtime;
	}
	
}
