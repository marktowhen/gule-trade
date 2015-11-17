package com.jingyunbank.etrade.comment.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.jingyunbank.core.web.ServletBox;
import com.jingyunbank.etrade.TestCaseBase;

public class CommentsImgControllerTest extends TestCaseBase{
	
	
	/**
	 * 通过产品的id得到产品对应的多张图片
	 * @throws Exception
	 */
	@Test
	public void selectById() throws Exception{
		getMockMvc().perform(
				 get("/api/commentsImg/getImg/7")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
			.andDo(MockMvcResultHandlers.print())
			.andDo(print());
	}
}
