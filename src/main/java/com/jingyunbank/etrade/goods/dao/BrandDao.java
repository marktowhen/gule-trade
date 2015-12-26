package com.jingyunbank.etrade.goods.dao;

import java.util.Collection;
import java.util.List;

import com.jingyunbank.etrade.api.goods.bo.Brand;
import com.jingyunbank.etrade.goods.entity.GoodsBrandEntity;

/**
 * 
 * Title: 品牌管理DAO
 * 
 * @author duanxf
 * @date 2015年12月15日
 */
public interface BrandDao {

	/**
	 * 保存品牌
	 * 
	 * @param brandEntity
	 * @return
	 * @throws Exception
	 */
	public int insertBrand(GoodsBrandEntity brandEntity) throws Exception;

	/**
	 * 根据ID 查询品牌
	 * 
	 * @param bid
	 * @return
	 * @throws Exception
	 */
	public GoodsBrandEntity selectOne(String bid) throws Exception;

	/**
	 * 修改品牌
	 * 
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	public int updateBrand(GoodsBrandEntity entity) throws Exception;

	/**
	 * 根据MID 查询brand
	 * 
	 * @param mid
	 * @return
	 * @throws Exception
	 */
	public List<GoodsBrandEntity> selectbrand(String mid) throws Exception;

	/**
	 * 查询所有brands
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<GoodsBrandEntity> selectAllBrands() throws Exception;

	/**
	 * 删除品牌
	 * @param bid
	 * @return
	 * @throws Exception
	 */
	public int delBrand(String bid) throws Exception;
}
