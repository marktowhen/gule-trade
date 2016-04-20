package com.jingyunbank.etrade.goods.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jingyunbank.etrade.TestCaseBase;
import com.jingyunbank.etrade.wap.goods.bean.GoodsAttrValueVO;
import com.jingyunbank.etrade.wap.goods.bean.GoodsImgVO;
import com.jingyunbank.etrade.wap.goods.bean.GoodsInfoVO;
import com.jingyunbank.etrade.wap.goods.bean.GoodsSkuVO;
import com.jingyunbank.etrade.wap.goods.bean.GoodsVO;

public class WapGoodsOperationControllerTest extends TestCaseBase {
	@Test
	public void testAddGoods() throws Exception {
		GoodsVO vo = new GoodsVO();
		vo.setName("测试增加商品");
		vo.setAddtime(new Date());
		vo.setPath("测试商品展示图片");
		
		//-------img---------------
		List<GoodsImgVO> imgList = new ArrayList<GoodsImgVO>();
		GoodsImgVO imgVO1 = new GoodsImgVO();
		imgVO1.setPath("测试增加---0001.jpg");
		GoodsImgVO imgVO2 = new GoodsImgVO();
		imgVO2.setPath("测试增加---0002.jpg");
		
		imgList.add(imgVO1);
		imgList.add(imgVO2);
		//----------------------------------
		//-------attrValue---------------
		List<GoodsAttrValueVO> attrValueList = new ArrayList<GoodsAttrValueVO>();
		GoodsAttrValueVO a1 = new GoodsAttrValueVO();
		a1.setAttrName("颜色");
		a1.setValue("红色");
		GoodsAttrValueVO a2 = new GoodsAttrValueVO();
		a2.setAttrName("颜色");
		a2.setValue("黄色");
		GoodsAttrValueVO a3 = new GoodsAttrValueVO();
		a3.setAttrName("规格");
		a3.setValue("大大");
		GoodsAttrValueVO a4 = new GoodsAttrValueVO();
		a4.setAttrName("规格");
		a4.setValue("小小");
		
		attrValueList.add(a1);
		attrValueList.add(a2);
		attrValueList.add(a3);
		attrValueList.add(a4);
		//----------------------------------
		//-------sku---------------
		List<GoodsSkuVO> skuList = new ArrayList<GoodsSkuVO>();
		GoodsSkuVO s1 = new GoodsSkuVO();
		s1.setProperties("测试sku-1");
		GoodsSkuVO s2 = new GoodsSkuVO();
		s2.setProperties("测试sku-2");
		
		skuList.add(s1);
		skuList.add(s2);
		//----------------------------------
		vo.setImgList(imgList);
		vo.setAttrValueList(attrValueList);
		vo.setSkuList(skuList);

		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(vo);
		getMockMvc()
				.perform(post("/api/goods/operation/save").content(json).contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andDo(MockMvcResultHandlers.print()).andReturn();

	}
	
	
	@Test
	public void testUpdateGoods() throws Exception {
		GoodsVO vo = new GoodsVO();
		vo.setName("测试增加商品--修改--测试");
		vo.setAddtime(new Date());
		vo.setPath("测试商品展示图片--修改--测试");
		
		//-------img---------------
		List<GoodsImgVO> imgList = new ArrayList<GoodsImgVO>();
		GoodsImgVO imgVO1 = new GoodsImgVO();
		imgVO1.setPath("测试修改---0001.jpg");
		GoodsImgVO imgVO2 = new GoodsImgVO();
		imgVO2.setPath("测试修改---0002.jpg");
		
		imgList.add(imgVO1);
		imgList.add(imgVO2);
		//----------------------------------
		//-------attrValue---------------
		List<GoodsAttrValueVO> attrValueList = new ArrayList<GoodsAttrValueVO>();
		GoodsAttrValueVO a1 = new GoodsAttrValueVO();
		a1.setAttrName("颜色2");
		a1.setValue("红色2");
		GoodsAttrValueVO a2 = new GoodsAttrValueVO();
		a2.setAttrName("颜色2");
		a2.setValue("黄色2");
		GoodsAttrValueVO a3 = new GoodsAttrValueVO();
		a3.setAttrName("规格2");
		a3.setValue("大大2");
		GoodsAttrValueVO a4 = new GoodsAttrValueVO();
		a4.setAttrName("规格2");
		a4.setValue("小小2");
		
		attrValueList.add(a1);
		attrValueList.add(a2);
		attrValueList.add(a3);
		attrValueList.add(a4);
		//----------------------------------
		//-------sku---------------
		List<GoodsSkuVO> skuList = new ArrayList<GoodsSkuVO>();
		GoodsSkuVO s1 = new GoodsSkuVO();
		s1.setProperties("测试-修改-sku-1");
		GoodsSkuVO s2 = new GoodsSkuVO();
		s2.setProperties("测试-修改-sku-2");
		
		skuList.add(s1);
		skuList.add(s2);
		//----------------------------------
		vo.setImgList(imgList);
		vo.setAttrValueList(attrValueList);
		vo.setSkuList(skuList);

		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(vo);
		getMockMvc()
				.perform(post("/api/goods/operation/update/zMK5wllGSKa2RNznqS1Xrw").content(json).contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andDo(MockMvcResultHandlers.print()).andReturn();

	}
	@Test
	public void testAddGoodsInfo() throws Exception {
		GoodsInfoVO vo1 = new GoodsInfoVO();
		vo1.setGID("g001");
		vo1.setKey("1111");
		vo1.setValue("2222");

		GoodsInfoVO vo2 = new GoodsInfoVO();
		vo2.setGID("g001");
		vo2.setKey("3333");
		vo2.setValue("4444");

		List<GoodsInfoVO> list = new ArrayList<GoodsInfoVO>();
		list.add(vo1);
		list.add(vo2);

		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(list);
		getMockMvc()
				.perform(post("/api/goods/operation/info/save").content(json).contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andDo(MockMvcResultHandlers.print()).andReturn();

	}
	
	
	@Test
	public void testupdateGoodsInfo() throws Exception {
		GoodsInfoVO vo1 = new GoodsInfoVO();
		vo1.setGID("g001");
		vo1.setKey("55");
		vo1.setValue("66");

		GoodsInfoVO vo2 = new GoodsInfoVO();
		vo2.setGID("g001");
		vo2.setKey("77");
		vo2.setValue("88");

		List<GoodsInfoVO> list = new ArrayList<GoodsInfoVO>();
		list.add(vo1);
		list.add(vo2);

		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(list);
		getMockMvc()
				.perform(post("/api/goods/operation/info/update/g001").content(json).contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andDo(MockMvcResultHandlers.print()).andReturn();

	}
	
	
	@Test
	public void a2() throws Exception {
		getMockMvc()
				.perform(get("/api/goods/operation/view/zMK5wllGSKa2RNznqS1Xrw").contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andDo(MockMvcResultHandlers.print()).andReturn();

	}


}
