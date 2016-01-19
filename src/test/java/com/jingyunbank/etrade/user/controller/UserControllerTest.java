package com.jingyunbank.etrade.user.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jingyunbank.core.web.Login;
import com.jingyunbank.core.web.ServletBox;
import com.jingyunbank.etrade.TestCaseBase;
import com.jingyunbank.etrade.user.bean.LoginUserPwdVO;

public class UserControllerTest extends TestCaseBase {
	/**
	 * 测试登录
	 * @throws Exception
	 */
	@Test
	public void testLogin() throws Exception{
		LoginUserPwdVO user = new LoginUserPwdVO();
		user.setKey("username");
		user.setPassword("12345678");
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(user);
		System.out.println(" ================= " + json);
		getMockMvc()
			.perform(post("/api/user/login")
					.content(json)
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
			.andDo(MockMvcResultHandlers.print())  
		            .andReturn();
		//System.out.println(restTemplate.getForEntity("http://localhost:8080/user", String.class).getBody());
		
	}
	/**
	 * 测试登出
	 * @throws Exception
	 */
	@Test
	public void testLogout() throws Exception{
		getMockMvc()
			.perform(get("/api/user/logout")
					.sessionAttr(Login.LOGIN_USER_ID, "1")
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
	//测试注册
	/**
	 * 邮箱注册或是手机注册
	 * 测试验证校验是否成功和保存信息是否成功
	 * @throws Exception
	 */
	@Test
	public void test2() throws Exception{
		getMockMvc().perform(
				 post("/api/user/register/checkcode")
				.param("username", "xiaoyuIiii")
				.param("mobile", "18766169803")
				.param("code", "4278")
				/*.param("email", "5559018888@qq.com")*/
				.param("password", "123490090")
				.sessionAttr(ServletBox.SMS_CODE_KEY_IN_SESSION, "4278")
				.param("nickname", "xiaoxue")
				.param("locked", "true")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
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
				.sessionAttr(Login.LOGIN_USER_ID, "1")
				.param("password", "77777777777")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andDo(MockMvcResultHandlers.print())  
				.andReturn();
	}
	
	//测试忘记密码
	/**
	 * 给手机或邮箱发送验证码的过程
	 * @throws Exception
	 */
	
	@Test
	public void testForgetpwdSend() throws Exception{
		getMockMvc().perform(
				get("/api/user/forgetpwd/send")
				.param("loginfo", "18766169803")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andDo(MockMvcResultHandlers.print())  
				.andReturn();
	}
	//测试忘记密码
	/**
	 * 测试忘记密码页面是修改密码用户信息和验证验证码是否正确
	 * @throws Exception
	 */
	@Test
	public void testForgetpwdCheck() throws Exception{
		getMockMvc().perform(
				post("/api/user/forgetpwd/check")
				.param("loginfo", "18766169803")
				.param("password", "1234455555")
				.param("code", "4278")
				.sessionAttr(ServletBox.SMS_CODE_KEY_IN_SESSION, "4278")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
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
				.sessionAttr(Login.LOGIN_USER_ID, "1")
				.param("tradepwd", "4568787989888")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andDo(MockMvcResultHandlers.print())  
				.andReturn();
	}
	/**
	 * 只有在交易密码为空的时候才会设置成功！
	 * 测试设置交易密码
	 * @throws Exception
	 */
	@Test
	public void testInstallTradepwd() throws Exception{
		getMockMvc().perform(
				post("/api/user/install/tradepwd")
				.sessionAttr(Login.LOGIN_USER_ID, "vcAxfKg-TlOB4Ql5AB3k-w")
				.param("tradepwd", "777777777888")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andDo(MockMvcResultHandlers.print())  
				.andReturn();
	}

	//为当前手机发送验证码
	/**
	 * 测试发送当前手机号的验证码
	 * 
	 * @throws Exception
	 */
		
	@Test
	public void testCurrentSendPhone() throws Exception{
		getMockMvc().perform(
				get("/api/user/send/message")
				.sessionAttr(Login.LOGIN_USER_ID, "1")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andDo(MockMvcResultHandlers.print())  
				.andReturn();
	}
	//当前手机检验验证码是否正确
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
				.sessionAttr(Login.LOGIN_USER_ID, "123")
				.sessionAttr(ServletBox.SMS_CODE_KEY_IN_SESSION, "9650")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andDo(MockMvcResultHandlers.print())  
				.andReturn();
	}
	//更换手机号
	/**
	 * 测试发送修改后手机号的验证码
	 * 
	 * @throws Exception
	 */
	@Test
	public void testUpdatePhone() throws Exception{
		getMockMvc().perform(
				get("/api/user/update/phone")
				.param("mobile", "18766169803")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andDo(MockMvcResultHandlers.print())  
				.andReturn();
	}
	//更换手机号
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
				.sessionAttr(Login.LOGIN_USER_ID, "123")
				.sessionAttr(ServletBox.SMS_CODE_KEY_IN_SESSION, "9650")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andDo(MockMvcResultHandlers.print())  
				.andReturn();
	}
	/**
	 * 测试发短信
	 * @throws Exception
	 */
	@Test
	public void testMessage() throws Exception{
		getMockMvc().perform(
				get("/api/user/smsMessage")
				/*.param("mobile", "15853161111")*/
				.sessionAttr(Login.LOGIN_USER_ID, "1")
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
				.sessionAttr(Login.LOGIN_USER_ID, "1")
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
				.sessionAttr(Login.LOGIN_USER_ID, "1")
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
				.sessionAttr(Login.LOGIN_USER_ID, "1")
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
				.param("mobile", "15853161111")
				.param("code", "894")
				.sessionAttr(Login.LOGIN_USER_ID, "1")
				.sessionAttr("UNCHECK_MOBILE", "15853161111")
				.sessionAttr(ServletBox.SMS_CODE_KEY_IN_SESSION, "894")
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
	 * 上传头像
	 * @throws Exception
	 */
	/*@Test
	public void testUploadFile() throws Exception{
		String fileName = new File("").getAbsolutePath()+"/pom.xml";
		MockMultipartFile file = new MockMultipartFile(
				"file"//接收时的参数名
				,"pom"//原文件名
				,"xml"//扩展名
				, new FileInputStream(new File(fileName)));
		getMockMvc().perform(fileUpload("/api/resource/upload/multiple")
				.file(file)
				.sessionAttr(Login.LOGIN_ID, "1")
				
				)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code").value("200"))
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andDo(MockMvcResultHandlers.print())  
				.andReturn();
			
	}*/
	
	
	/**
	 * 测试根据key查询用户
	 * @throws Exception
	 */
	@Test
	public void testQuery() throws Exception{
		getMockMvc().perform(
				get("/api/user/query")
				.sessionAttr(Login.LOGIN_USER_ID, "1")
				.param("key", "15853161111")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andDo(MockMvcResultHandlers.print())  
				.andExpect(jsonPath("$.code").value("200"))
				.andReturn();
			
	}
	
}
