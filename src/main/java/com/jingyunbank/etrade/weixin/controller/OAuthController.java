package com.jingyunbank.etrade.weixin.controller;


import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.KeyGen;
import com.jingyunbank.core.web.Login;
import com.jingyunbank.core.web.Security;
import com.jingyunbank.etrade.api.cart.bo.Cart;
import com.jingyunbank.etrade.api.cart.service.ICartService;
import com.jingyunbank.etrade.api.weixin.bo.SNSUserInfoBo;
import com.jingyunbank.etrade.api.weixin.service.IWeiXinUserService;
import com.jingyunbank.etrade.weixin.bean.SNSUserInfoVo;
import com.jingyunbank.etrade.weixin.entity.Constants;
import com.jingyunbank.etrade.weixin.entity.TEA;
import com.jingyunbank.etrade.weixin.entity.WeixinOauth2Token;
import com.jingyunbank.etrade.weixin.util.GetAccessToken;
import com.jingyunbank.etrade.weixin.util.StringUtilss;

@RestController
public class OAuthController {
	
	@Autowired
	private IWeiXinUserService weixinUserService;
	@Autowired
	private  ICartService cartService;
	
	/**
	 * 保存信息
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/api/get/user")
	public void getUserInfo(HttpServletRequest request,HttpServletResponse resp,HttpSession session){
		try {
			HttpSession rsession = request.getSession();
			System.out.println(session);
			
			System.out.println(rsession);
			request.setCharacterEncoding("utf-8");
			resp.setCharacterEncoding("utf-8");
			//用户同意授权后，能获取到code
			 String code = request.getParameter("code");
		     /*String state = request.getParameter("state");*/
		     //用户同意授权
		     if(!"authdeny".equals(code)){
		    	 //获取授权的access_token
		    	 WeixinOauth2Token weixinOauth2Token = GetAccessToken.getOauth2AccessToken("wx3f0da4d18b8e3ff8", "eca90c942400a6a3c6a70099bb7958d7", code);
		    	 //网页授权接口访问凭证
		    	 String accessToken=weixinOauth2Token.getAccessToken();
		    	 //用户标识
		    	 String openId = weixinOauth2Token.getOpenId();
		    	 //获取用户信息
		    	
		    	 Optional<SNSUserInfoBo> bo=weixinUserService.getUsers(openId);
		    	 if(!bo.isPresent()){
		    		 SNSUserInfoVo snsUserInfoVo=GetAccessToken.getUserInfo(accessToken, openId);
			    	 SNSUserInfoBo snsUserInfoBo = new SNSUserInfoBo();
			    	 snsUserInfoVo.setId(KeyGen.uuid());
			    	 BeanUtils.copyProperties(snsUserInfoVo, snsUserInfoBo);
		    		 weixinUserService.addUser(snsUserInfoBo);
		    		 Optional<Cart> candidatecart = cartService.singleCart(snsUserInfoVo.getId());
		    			String cartID = null;
		    			if(candidatecart.isPresent()){
		    				cartID = candidatecart.get().getID();
		    			}else{
		    				cartService.save(new Cart(KeyGen.uuid(), snsUserInfoVo.getId()));
		    			}
		    			request.getSession().setAttribute(Constants.IDBYSESSION, "abcdasfafd");
		    			/*loginSuccessed(snsUserInfoVo.getId(), snsUserInfoVo.getNickname(),cartID,session,response);*/
						session.setAttribute(Constants.IDBYSESSION,
								TEA.Encrypt(snsUserInfoVo.getId()));
						session.setAttribute(Constants.USERNAMEBYSESSION,
								TEA.Encrypt(snsUserInfoVo.getNickname()));
						session.setAttribute(Constants.CARTIDBYSESSION,
								TEA.Encrypt(cartID));
		    		 /*System.out.println("uid:"+Login.UID(request));*/
		    		 resp.sendRedirect("http://zhongpai.legu.co/#/");
		    	 }else{
		    		 
		    		 request.getSession().setAttribute(Constants.IDBYSESSION, "abcdasfafd");
		    		 Optional<Cart> candidatecart = cartService.singleCart(Login.UID(request));
		    			String cartID = null;
		    			if(candidatecart.isPresent()){
		    				cartID = candidatecart.get().getID();
		    			}
		    			/*loginSuccessed(bo.get().getId(), bo.get().getNickname(),cartID,session,response);*/
		    			session.setAttribute(Constants.IDBYSESSION,
								TEA.Encrypt(bo.get().getId()));
						session.setAttribute(Constants.USERNAMEBYSESSION,
								TEA.Encrypt(bo.get().getNickname()));
						session.setAttribute(Constants.CARTIDBYSESSION,
								TEA.Encrypt(cartID));
		    		 /*System.out.println("得到用户的信息:"+snsUserInfoVo.getNickname());*/
			    	 //设置传递参数
						System.out.println(request.getSession().getAttribute(Constants.IDBYSESSION));
						String iii=String.valueOf(request.getSession().getAttribute(Constants.IDBYSESSION));
					String uid = StringUtilss.getSessionId(request,resp);
		    		 System.out.println("uid:"+uid);
		    		 
		    		 resp.sendRedirect("http://zhongpai.legu.co/#/");
		    		 return;
		    	 }
		    	
		    	
		     }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void loginSuccessed(String uid, String username, String cartID, HttpSession session,
			HttpServletResponse response){
		Security.authenticate(session);
		Login.UID(session, uid);
		Login.uname(session, username);
		
		if(!StringUtils.isEmpty(cartID)){
			Login.cartID(session, cartID);
		}
		
		//将uid写入cookie
		Cookie cookie = new Cookie(Login.LOGIN_USER_ID, uid);
		cookie.setPath("/");
		cookie.setMaxAge(session.getMaxInactiveInterval());
		response.addCookie(cookie);
		
	}
	
}
