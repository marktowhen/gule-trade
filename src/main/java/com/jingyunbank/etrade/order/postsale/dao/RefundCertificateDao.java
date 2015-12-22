package com.jingyunbank.etrade.order.postsale.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jingyunbank.etrade.order.postsale.entity.RefundCertificateEntity;

public interface RefundCertificateDao {

	public void insertMany(@Param("certs") List<RefundCertificateEntity> entities) throws Exception;

	public List<String> selectMany(String rid);

	public void delete(String rid) throws Exception;
	
}
