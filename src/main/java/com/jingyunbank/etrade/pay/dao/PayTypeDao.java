package com.jingyunbank.etrade.pay.dao;

import java.util.List;

import com.jingyunbank.etrade.pay.entity.PayTypeEntity;

public interface PayTypeDao {

	public List<PayTypeEntity> select();
}
