package com.jingyunbank.etrade.comment.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.jingyunbank.core.web.Login;
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
				 .sessionAttr(Login.LOGIN_ID, "21")
				 	.param("GID", "3")
				 	.param("ImgID", "7")
				 	.param("goodsComment", "挺好")
				 	.param("commentGrade", "1")//级别应该是自动生成的
				 	.param("goodsService", "挺好的")
				 	.param("serviceGrade", "1")
				 	.param("replyUID", "21")
				 	/*.param("picture", "c:/bulid/b.jpg")*/
				 	/*.param("commentStatus", "1")*/
				 	.param("orders", "3")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
			.andDo(MockMvcResultHandlers.print())
			.andDo(print());
	}
	/**
	 * 测试通过产品的Gid查询其所有的评价信息
	 * @throws Exception
	 */
	@Test
	public void test2() throws Exception{
		getMockMvc().perform(
				 get("/api/comments/getbyid/3")
				 .sessionAttr(Login.LOGIN_ID, "12")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
			.andDo(MockMvcResultHandlers.print())
			.andDo(print());
	}
	/**
	 * 测试通过id删除评价一起对应的照片
	 * (只能删除自己的评价)
	 * @throws Exception
	 */
	@Test
	public void test3() throws Exception{
		getMockMvc().perform(
				 delete("/api/comments/delete/rNa4bwnwQvOF0PaRGlY6VQ")
				 .sessionAttr(Login.LOGIN_ID, "12")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
			.andDo(MockMvcResultHandlers.print())
			.andDo(print());
	}
	/**
	 * 测试修改状态的
	 * @throws Exception
	 */
	@Test
	public void test4() throws Exception{
		getMockMvc().perform(
				 post("/api/comments/update/status")
				 .param("ID", "9zzH2f9FRiicAo0Sg10zgw")
				 .sessionAttr(Login.LOGIN_ID, "13")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
			.andDo(MockMvcResultHandlers.print())
			.andDo(print());
	}
}
