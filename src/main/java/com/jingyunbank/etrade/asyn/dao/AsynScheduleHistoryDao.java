package com.jingyunbank.etrade.asyn.dao;

import com.jingyunbank.etrade.asyn.entity.AsynScheduleHistoryEntity;

public interface AsynScheduleHistoryDao {
	
	boolean insert(AsynScheduleHistoryEntity entity);

	boolean insertFromAsynSchedule(String scheduleID);
}
