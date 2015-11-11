package com.jingyunbank.etrade.user.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.FileInputStream;

import net.minidev.json.JSONObject;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.jingyunbank.core.web.ServletBox;
import com.jingyunbank.etrade.TestCaseBase;

public class UserControllerTest extends TestCaseBase {
	/**
	 * 测试登录
	 * @throws Exception
	 */
	@Test
	public void test0() throws Exception{
		JSONObject json = new JSONObject();
		json.put("loginfo", "loginfo");
		json.put("password", "password");
		getMockMvc()
			.perform(post("/api/user/login").param("loginfo", "qxstest").param("password", "123456")
					.content(json.toJSONString())
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
			.andDo(MockMvcResultHandlers.print())  
		            .andReturn();
		//System.out.println(restTemplate.getForEntity("http://localhost:8080/user", String.class).getBody());
		
	}
	//测试注册
	@Test
	public void test1() throws Exception{
		getMockMvc().perform(
				 put("/api/user/register/send")
				.param("mobile", "18766169803")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
			.andDo(MockMvcResultHandlers.print())
			.andDo(print());
	}
	/**
	 * 测试验证校验是否成功和保存信息是否成功
	 * @throws Exception
	 */
	@Test
	public void test2() throws Exception{
		getMockMvc().perform(
				 post("/api/user/register/checkCode")
				.param("username", "xiaoyuIiii")
				.param("mobile", "18766169803")
				.param("code", "4278")
			/*	.param("email", "555901118888@qq.com")*/
				.param("password", "123490090")
				.sessionAttr(ServletBox.SMS_MESSAGE, "427")
				.param("nickname", "xiaoxue")
				.param("locked", "true")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
			/*.andExpect(jsonPath("$.code").value("500"))*/
			.andDo(MockMvcResultHandlers.print())
			.andDo(print());
	}
	/**
	 * 测试修改登录密码
	 * @throws Exception
	 */
	@Test
	public void testUpdatePassword() throws Exception{
		getMockMvc().perform(
				post("/api/user/update/password")
				.sessionAttr(ServletBox.LOGIN_ID, "1")
				.param("password", "77777777777")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(jsonPath("$.code").value("200"))
				.andDo(MockMvcResultHandlers.print())  
				.andReturn();
	}
	/**
	 * 测试修改交易密码
	 * @throws Exception
	 */
	@Test
	public void testTradepwd() throws Exception{
		getMockMvc().perform(
				post("/api/user/update/tradepwd")
				.sessionAttr(ServletBox.LOGIN_ID, "1")
				.param("tradepwd", "77777777777")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andDo(MockMvcResultHandlers.print())  
				.andReturn();
	}
	/**
	 * 测试设置交易密码
	 * @throws Exception
	 */
	@Test
	public void testInstallTradepwd() throws Exception{
		getMockMvc().perform(
				post("/api/user/install/tradepwd")
				.sessionAttr(ServletBox.LOGIN_ID, "vcAxfKg-TlOB4Ql5AB3k-w")
				.param("tradepwd", "77777777788")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andDo(MockMvcResultHandlers.print())  
				.andReturn();
	}

	
	/**
	 * 测试发送当前手机号的验证码
	 * 
	 * @throws Exception
	 */
		
	@Test
	public void testCurrentSendPhone() throws Exception{
		getMockMvc().perform(
				get("/api/user/send/message")
				.sessionAttr(ServletBox.LOGIN_ID, "1")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andDo(MockMvcResultHandlers.print())  
				.andReturn();
	}
	/**
	 * 测试当前手机号的验证码输入和发送的是否一致
	 * 
	 * @throws Exception
	 */
	
	@Test
	public void testCurrentCheckCode() throws Exception{
		getMockMvc().perform(
				post("/api/user/send/message")
				.param("mobile", "18766169803")
				.param("code", "9650")
				.sessionAttr(ServletBox.LOGIN_ID, "123")
			/*	.sessionAttr("UNCHECK_MOBILE", "18766169803")*/
				.sessionAttr(ServletBox.SMS_MESSAGE, "9650")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andDo(MockMvcResultHandlers.print())  
				.andReturn();
	}
	
	/**
	 * 测试发送修改后手机号的验证码
	 * 
	 * @throws Exception
	 */
	@Test
	public void testUpdatePhone() throws Exception{
		getMockMvc().perform(
				get("/api/user/update/phone")
				.sessionAttr(ServletBox.LOGIN_ID, "123")
				.param("mobile", "18766169803")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				/*.andExpect(jsonPath("$.code").value("200"))*/
				.andDo(MockMvcResultHandlers.print())  
				.andReturn();
	}
	/**
	 * 检验修改后的手机号和验证码是否输入正确
	 * @throws Exception
	 */
	@Test
	public void testCheckCode() throws Exception{
		getMockMvc().perform(
				post("/api/user/update/phone")
				.param("mobile", "18766169803")
				.param("code", "9650")
				.sessionAttr(ServletBox.LOGIN_ID, "123")
				/*.sessionAttr("UNCHECK_MOBILE", "18766169803")*/
				.sessionAttr(ServletBox.SMS_MESSAGE, "9650")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andDo(MockMvcResultHandlers.print())  
				.andReturn();
	}
	//----------------------------邮箱验证 start---------------------------------
	/**
	 * 测试发短信到绑定手机
	 * @throws Exception
	 */
	@Test
	public void testMessage() throws Exception{
		getMockMvc().perform(
				get("/api/user/smsMessage")
				/*.param("mobile", "15853166853")*/
				.sessionAttr(ServletBox.LOGIN_ID, "1")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andDo(MockMvcResultHandlers.print())  
				.andReturn();
	}
	/**
	 * 测试发邮箱验证链接
	 * @throws Exception
	 */
	@Test
	public void testSendEmail() throws Exception{
		getMockMvc().perform(
				get("/api/user/email-link")
				.sessionAttr(ServletBox.LOGIN_ID, "1")
				.param("email", "627956245@qq.com")
				.param("code", "1234")
				.sessionAttr("session_code", "1234")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code").value("200"))
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andDo(MockMvcResultHandlers.print())  
				.andReturn();
			
	}
	
	/**
	 * 测试验证邮件
	 * @throws Exception
	 */
	@Test
	public void testCheckEmail() throws Exception{
		getMockMvc().perform(
				get("/api/user/ckemail-link")
				.sessionAttr(ServletBox.LOGIN_ID, "1")
				.param("d", "1")
				.param("u", "NjI3OTU2MjQ1QHFxLmNvbX4xNDQ3MjExODI0NjQ4")
				.param("m", "EAC57D28C8D9C3D52C0EF1E66727AE8B")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andDo(MockMvcResultHandlers.print())  
				.andExpect(jsonPath("$.code").value("500"))
				.andReturn();
			
	}
	//----------------------------邮箱验证 end---------------------------------
	
	
	//----------------------------手机认证start---------------------------------
	/**
	 * 发送验证码到注册邮箱
	 * @throws Exception
	 */
	@Test
	public void testSendEmailCode() throws Exception{
		getMockMvc().perform(
				get("/api/user/email-message")
				.sessionAttr(ServletBox.LOGIN_ID, "1")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code").value("200"))
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andDo(MockMvcResultHandlers.print())  
				.andReturn();
			
	}
	
	/**
	 * 测试绑定手机
	 * @throws Exception
	 */
	@Test
	public void testCheckMobile() throws Exception{
		getMockMvc().perform(
				post("/api/user/message")
				.param("mobile", "15853166853")
				.param("code", "894")
				.sessionAttr(ServletBox.LOGIN_ID, "1")
				.sessionAttr("UNCHECK_MOBILE", "15853166853")
				.sessionAttr(ServletBox.SMS_MESSAGE, "894")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code").value("200"))
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andDo(MockMvcResultHandlers.print())  
				.andReturn();
			
	}
	//----------------------------手机认证end---------------------------------
	
/**
	 * 上传文件
	 * @throws Exception
	 */
	@Test
	public void testUploadFile() throws Exception{
		String fileName = new File("").getAbsolutePath()+"/pom.xml";
		MockMultipartFile file = new MockMultipartFile(
				"file"//接收时的参数名
				,"pom"//原文件名
				,"xml"//扩展名
				, new FileInputStream(new File(fileName)));
		getMockMvc().perform(fileUpload("/api/user/picture")
				.file(file)
				.sessionAttr(ServletBox.LOGIN_ID, "1")
				
				)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code").value("200"))
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andDo(MockMvcResultHandlers.print())  
				.andReturn();
			
	}
	
	

	
}
