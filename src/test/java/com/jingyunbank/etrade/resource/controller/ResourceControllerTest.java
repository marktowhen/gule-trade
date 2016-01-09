package com.jingyunbank.etrade.resource.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.FileInputStream;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import com.jingyunbank.core.web.Login;
import com.jingyunbank.etrade.TestCaseBase;

public class ResourceControllerTest extends TestCaseBase {

	@Test
	public void test0()throws Exception{
		
		File f = new File("C:\\Users\\Cao\\Pictures\\18091441691216121.jpg");
		File f1 = new File("C:\\Users\\Cao\\Pictures\\BingWallpaper-2015-07-08.jpg");
        System.out.println(f.isFile()+"  "+f.getName()+f.exists());
        FileInputStream fi1 = new FileInputStream(f);
        FileInputStream fi2 = new FileInputStream(f1);
        MockMultipartFile fstmp = new MockMultipartFile("file", f.getName(), "image/jpeg", fi1);
        MockMultipartFile secmp = new MockMultipartFile("file", "Tulips.jpg","image/jpeg", fi2); 
		
		getMockMvc().perform(
					fileUpload("/api/resource/upload/multiple")
					.file(fstmp).file(secmp)
					.param("param", "123").param("param", "456")
					.sessionAttr(Login.LOGIN_USER_ID, "123321")
					.accept(MediaType.APPLICATION_JSON)
				)
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(jsonPath("$.code").value("200"))
				.andDo(print());
	}
}
