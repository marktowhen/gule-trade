package com.jingyunbank.etrade.goods.dao;

import java.util.List;
import java.util.Map;

import com.jingyunbank.etrade.goods.entity.GoodsDaoEntity;
import com.jingyunbank.etrade.goods.entity.GoodsDetailEntity;
import com.jingyunbank.etrade.goods.entity.GoodsEntity;
import com.jingyunbank.etrade.goods.entity.GoodsImgEntity;
import com.jingyunbank.etrade.goods.entity.GoodsOperationEntity;

/**
 * 
 * Title: 商品的操作DAO
 * 
 * @author duanxf
 * @date 2015年11月13日
 */
public interface GoodsOperationDao {
	/**
	 * 保存商品信息
	 * 
	 * @param goodsEntity
	 * @return
	 * @throws Exception
	 */
	public int insertGoods(GoodsEntity goodsEntity) throws Exception;

	/**
	 * 保存商品详细信息
	 * 
	 * @param goodsDetailEntity
	 * @return
	 * @throws Exception
	 */
	public int insertGoodsDetail(GoodsDetailEntity goodsDetailEntity) throws Exception;

	/**
	 * 保存商品图片
	 * 
	 * @param goodsImgEntity
	 * @return
	 * @throws Exception
	 */
	public int insertGoodsImg(GoodsImgEntity goodsImgEntity) throws Exception;

	/**
	 * 根据gid 获取商品详细信息
	 * 
	 * @param gid
	 * @return
	 * @throws Exception
	 */
	public GoodsOperationEntity selectOne(String gid) throws Exception;

	/**
	 * 修改商品信息
	 * 
	 * @param goodsEntity
	 * @return
	 * @throws Exception
	 */
	public int updateGoods(GoodsEntity goodsEntity) throws Exception;

	/**
	 * 修改商品详细信息
	 * 
	 * @param goodsDetailEntity
	 * @return
	 * @throws Exception
	 */
	public int updateGoodsDetail(GoodsDetailEntity goodsDetailEntity) throws Exception;

	/**
	 * 修改商品图片信息
	 * 
	 * @param goodsImgEntity
	 * @return
	 * @throws Exception
	 */
	public int updateGoodsImg(GoodsImgEntity goodsImgEntity) throws Exception;

	/**
	 * 更改商品的库存和销量
	 * 
	 * @param GID
	 *            商品ID
	 * @param count
	 *            销售数量
	 * @return
	 * @throws Exception
	 */
	public int updateGoodsVolume(Map<String, Object> map) throws Exception;

	/**
	 * 商品上架
	 * 
	 * @param gid
	 * @return
	 * @throws Exception
	 */
	public int updateGoodsUp(String gid) throws Exception;

	/**
	 * 商品下架
	 * 
	 * @param gid
	 * @return
	 * @throws Exception
	 */
	public int updateGoodsDown(String gid) throws Exception;

	/**
	 * 获取所有的店铺信息
	 * @return
	 * @throws Exception
	 */
	public List<GoodsOperationEntity> selectMerchant() throws Exception;

	/**
	 * 根据MID 查询所属的品牌
	 * 
	 * @param mid
	 * @return
	 * @throws Exception
	 */
	public List<GoodsOperationEntity> selectBrandsByMid(String mid) throws Exception;
}
