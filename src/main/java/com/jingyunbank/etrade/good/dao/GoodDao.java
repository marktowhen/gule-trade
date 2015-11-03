package com.jingyunbank.etrade.good.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.exception.DataUpdatingException;
import com.jingyunbank.etrade.good.entity.GoodEntity;
/**
 * @author liug
 *
 */
@Repository("goodDao")
public interface GoodDao{

	public boolean insertGood(GoodEntity good) throws DataSavingException ;

	public boolean updateGood(GoodEntity good) throws DataUpdatingException ;

	public List<GoodEntity> select(String uid) ;

}
