package com.jingyunbank.etrade.award.dao;

import com.jingyunbank.etrade.award.entity.SalesUserrelationshipEntity;

public interface SalesUserrelationshipDao {

	boolean insert(SalesUserrelationshipEntity entity);

	SalesUserrelationshipEntity selectBySID(String sID);

	SalesUserrelationshipEntity selectByUID(String uID);

}
