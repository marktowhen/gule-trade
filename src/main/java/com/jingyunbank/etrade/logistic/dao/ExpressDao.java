package com.jingyunbank.etrade.logistic.dao;

import java.util.List;

import com.jingyunbank.etrade.logistic.entity.ExpressEntity;

/**
 * 
 * Title: 快递DAO
 * 
 * @author duanxf
 * @date 2016年1月21日
 */
public interface ExpressDao {

	public List<ExpressEntity> selectExpress() throws Exception;

}
