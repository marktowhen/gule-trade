package com.jingyunbank.etrade.goods.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.Page;
import com.jingyunbank.core.Range;
import com.jingyunbank.core.Result;
import com.jingyunbank.etrade.api.goods.bo.SalesRecord;
import com.jingyunbank.etrade.api.goods.service.ISalesRecordsService;
import com.jingyunbank.etrade.goods.bean.SaleRecordVO;

@RestController
public class SalesRecordsController {

	@Autowired
	private ISalesRecordsService salesRecordsService;
	
	/**
	 * 获取商品的购买记录
	 * 
	 * @param gid
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/api/goods/{gid}/sales/list", method = RequestMethod.GET)
	public Result<List<SaleRecordVO>> querySalesRecords(@PathVariable String gid, Page page) throws Exception {
		Range range = new Range();
		range.setFrom(page.getOffset());
		range.setTo(page.getSize());
		List<SalesRecord> salelist = salesRecordsService.listRange(gid, range);
		List<SaleRecordVO> list = salelist.stream().map(bo -> {
			SaleRecordVO vo = new SaleRecordVO();
			BeanUtils.copyProperties(bo, vo);
			return vo;
		}).collect(Collectors.toList());
		return Result.ok(list);
	}
	
	/**
	 * 销量
	 * @param gid
	 * @param year
	 * @param month
	 * @return
	 * @throws Exception
	 * 2016年2月14日 qxs
	 */
	@RequestMapping(value = "/api/goods/{gid}/sales/{year}/{month}", method = RequestMethod.GET)
	public Result<Integer> countSalesRecords(@PathVariable String gid, @PathVariable String year,@PathVariable String month) throws Exception {
		
		return Result.ok(salesRecordsService.count(gid, year, month));
	}

}
