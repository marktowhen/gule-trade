package com.jingyunbank.etrade.wap.goods.bean;

import java.util.ArrayList;
import java.util.List;

import com.jingyunbank.etrade.api.wap.goods.bo.BaseGoodsDeatil;

/**
 * 
 * Title: GoodsDeatilVO 商品详情展示
 * 
 * @author duanxf
 * @date 2016年4月7日
 */
public class GoodsDeatilVO extends BaseGoodsDeatil {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<GoodsImgVO> imgList = new ArrayList<GoodsImgVO>();

	public List<GoodsImgVO> getImgList() {
		return imgList;
	}

	public void setImgList(List<GoodsImgVO> imgList) {
		this.imgList = imgList;
	}

}
