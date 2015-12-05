package com.jingyunbank.etrade.pay.dao;

import java.util.List;

import com.jingyunbank.etrade.pay.entity.PayPipelineEntity;

public interface PayPipelineDao {

	public List<PayPipelineEntity>  selectAll();
	
	public PayPipelineEntity selectOne(String pipelineCode);
	
}
