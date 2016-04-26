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
import com.jingyunbank.etrade.wap.goods.bean.GoodsOperationVO;
import com.jingyunbank.etrade.wap.goods.bean.GoodsSkuVO;
import com.jingyunbank.etrade.wap.goods.bean.GoodsVO;

public class WapGoodsOperationControllerTest extends TestCaseBase {
	@Test
	public void testAddGoods() throws Exception {
		GoodsOperationVO vo = new GoodsOperationVO();
		vo.setName("3测试增加商品-421");
		vo.setAddtime(new Date());
		vo.setPath("3测试商品展示图片-421");

		
		//-------img---------------
		List<GoodsImgVO> imgList = new ArrayList<GoodsImgVO>();
		GoodsImgVO imgVO1 = new GoodsImgVO();
		imgVO1.setPath("3测试增加---0001-421.jpg");
		GoodsImgVO imgVO2 = new GoodsImgVO();
		imgVO2.setPath("3测试增加---0002-421.jpg");
		
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
		s1.setPropertiesValue("红色2@大大2");
		GoodsSkuVO s2 = new GoodsSkuVO();
		s2.setPropertiesValue("黄色2@小小2");
		
		skuList.add(s1);
		skuList.add(s2);
		//----------------------------------
		//----------info---------------------
		List<GoodsInfoVO> infoList = new ArrayList<GoodsInfoVO>();
		GoodsInfoVO info1= new GoodsInfoVO();
		info1.setKey("测试info1-421");
		info1.setValue("测试info1-421");
		GoodsInfoVO info2= new GoodsInfoVO();
		info2.setKey("测试info1-421");
		info2.setValue("测试info1-421");
		infoList.add(info1);
		infoList.add(info2);
		
		vo.setImgList(imgList);
		vo.setAttrValueList(attrValueList);
		vo.setSkuList(skuList);
		vo.setInfoList(infoList);
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
		GoodsOperationVO vo = new GoodsOperationVO();
		vo.setName("421增加商品-421");
		vo.setAddtime(new Date());
		vo.setPath("421商品展示图片-421");

		
		//-------img---------------
		List<GoodsImgVO> imgList = new ArrayList<GoodsImgVO>();
		GoodsImgVO imgVO1 = new GoodsImgVO();
		imgVO1.setPath("421增加---0001-421.jpg");
		GoodsImgVO imgVO2 = new GoodsImgVO();
		imgVO2.setPath("421增加---0002-421.jpg");
		
		imgList.add(imgVO1);
		imgList.add(imgVO2);
		//----------------------------------
		//-------attrValue---------------
		List<GoodsAttrValueVO> attrValueList = new ArrayList<GoodsAttrValueVO>();
		GoodsAttrValueVO a1 = new GoodsAttrValueVO();
		a1.setAttrName("颜色3");
		a1.setValue("红色3");
		GoodsAttrValueVO a2 = new GoodsAttrValueVO();
		a2.setAttrName("颜色3");
		a2.setValue("黄色3");
		GoodsAttrValueVO a3 = new GoodsAttrValueVO();
		a3.setAttrName("规格3");
		a3.setValue("大大3");
		GoodsAttrValueVO a4 = new GoodsAttrValueVO();
		a4.setAttrName("规格3");
		a4.setValue("小小3");
		
		attrValueList.add(a1);
		attrValueList.add(a2);
		attrValueList.add(a3);
		attrValueList.add(a4);
		//----------------------------------
		//-------sku---------------
		List<GoodsSkuVO> skuList = new ArrayList<GoodsSkuVO>();
		GoodsSkuVO s1 = new GoodsSkuVO();
		s1.setProperties("421测试sku-1-421");
		GoodsSkuVO s2 = new GoodsSkuVO();
		s2.setProperties("421测试sku-2-421");
		
		skuList.add(s1);
		skuList.add(s2);
		//----------------------------------
		//----------info---------------------
		List<GoodsInfoVO> infoList = new ArrayList<GoodsInfoVO>();
		GoodsInfoVO info1= new GoodsInfoVO();
		info1.setKey("421测试info1-421");
		info1.setValue("421测试info1-421");
		GoodsInfoVO info2= new GoodsInfoVO();
		info2.setKey("421测试info1-421");
		info2.setValue("421测试info1-421");
		infoList.add(info1);
		infoList.add(info2);
		
		vo.setImgList(imgList);
		vo.setAttrValueList(attrValueList);
		vo.setSkuList(skuList);
		vo.setInfoList(infoList);
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(vo);
		getMockMvc()
				.perform(post("/api/goods/operation/update/JBJrsjnbRvyecjsa-gRT5Q").content(json).contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andDo(MockMvcResultHandlers.print()).andReturn();

	}
	
	
	
	
	
	
	@Test
	public void a2() throws Exception {
		getMockMvc()
				.perform(get("/api/goods/operation/view/JBJrsjnbRvyecjsa-gRT5Q").contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andDo(MockMvcResultHandlers.print()).andReturn();

	}


}
