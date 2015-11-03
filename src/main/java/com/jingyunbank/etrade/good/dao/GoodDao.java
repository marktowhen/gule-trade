package com.jingyunbank.etrade.good.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.exception.DataUpdatingException;
import com.jingyunbank.etrade.good.entity.GoodsEntity;
/**
 * @author liug
 *
 */
@Repository("goodDao")
public interface GoodDao{

	public boolean insertGood(GoodsEntity good) throws DataSavingException ;

	public boolean updateGood(GoodsEntity good) throws DataUpdatingException ;

	public List<GoodsEntity> select(String uid) ;

}
