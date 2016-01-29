package com.jingyunbank.etrade.goods.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.jingyunbank.core.Range;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.goods.bo.SalesRecord;
import com.jingyunbank.etrade.api.goods.service.ISalesRecordsService;
import com.jingyunbank.etrade.goods.dao.SalesRecordsDao;
import com.jingyunbank.etrade.goods.entity.SalesRecordEntity;

@Service("salesRecordsService")
public class SalesRecordsService implements ISalesRecordsService {

	@Autowired
	private SalesRecordsDao salesRecordsDao;
	
	@Override
	@CacheEvict(value="salesRecordCache",allEntries=true)
	public void save(SalesRecord record) throws DataSavingException {
		SalesRecordEntity entity = new SalesRecordEntity();
		BeanUtils.copyProperties(record, entity);
		try {
			salesRecordsDao.insert(entity);
		} catch (Exception e) {
			throw new DataSavingException(e);
		}
	}

	@Override
	@Cacheable(cacheNames = "salesRecordCache", keyGenerator = "CustomKG")
	public List<SalesRecord> listRange(String gid, Range range)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("gid", gid);
		map.put("from", (int) range.getFrom());
		map.put("size", (int) range.getTo());
		List<SalesRecord> saleList = salesRecordsDao.select(map)
								.stream().map(dao -> {
			SalesRecord bo = new SalesRecord();
			BeanUtils.copyProperties(dao, bo);
			return bo;
		}).collect(Collectors.toList());
		return saleList;
	}

}
