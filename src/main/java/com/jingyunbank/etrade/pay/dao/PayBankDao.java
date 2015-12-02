package com.jingyunbank.etrade.pay.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jingyunbank.etrade.pay.entity.PayBankEntity;

public interface PayBankDao {

	public List<PayBankEntity> select(
								@Param("gc") boolean gateCredit, 
								@Param("gd") boolean gateDebit, 
								@Param("fc") boolean fastCredit, 
								@Param("fd") boolean fastDebit);
	
	public List<PayBankEntity> selectRange(
								@Param("gc") boolean gateCredit, 
								@Param("gd") boolean gateDebit, 
								@Param("fc") boolean fastCredit, 
								@Param("fd") boolean fastDebit,
								@Param("from") long from, @Param("size") long size);
}
