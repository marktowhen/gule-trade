package com.jingyunbank.etrade.comment.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.jingyunbank.core.web.ServletBox;
import com.jingyunbank.etrade.TestCaseBase;

public class CommentsControllerTest extends TestCaseBase{
	/**
	 * 测试保存物品的评价和对应上传的图片
	 * @throws Exception
	 */
	@Test
	public void test1() throws Exception{
		getMockMvc().perform(
				 put("/api/comments/list")
				 .sessionAttr(ServletBox.LOGIN_ID, "14")
				 	.param("gid", "2")
				 	.param("imgid", "9")
				 	.param("goodsComment", "挺好")
				 	.param("commentGrade", "1")
				 	.param("goodsService", "挺好的")
				 	.param("serviceGrade", "1")
				 	/*.param("picture", "c:/bulid/b.jpg")*/
				 	.param("commentStatus", "1")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
			.andDo(MockMvcResultHandlers.print())
			.andDo(print());
	}
	/**
	 * 测试通过产品的id查询其评价信息
	 * @throws Exception
	 */
	@Test
	public void test2() throws Exception{
		getMockMvc().perform(
				 get("/api/comments/getbyid/1")
				 .sessionAttr(ServletBox.LOGIN_ID, "1")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
			.andDo(MockMvcResultHandlers.print())
			.andDo(print());
	}
	/**
	 * 测试通过id删除评价一起对应的照片
	 * @throws Exception
	 */
	@Test
	public void test3() throws Exception{
		getMockMvc().perform(
				 delete("/api/comments/delete/a1ginJ3sTJS1JoMr5Ws9fA")
				 .sessionAttr(ServletBox.LOGIN_ID, "10")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
			.andDo(MockMvcResultHandlers.print())
			.andDo(print());
	}
}