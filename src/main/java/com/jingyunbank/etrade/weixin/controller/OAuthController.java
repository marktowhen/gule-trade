package com.jingyunbank.etrade.weixin.controller;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.KeyGen;
import com.jingyunbank.core.web.Login;
import com.jingyunbank.core.web.Security;
import com.jingyunbank.etrade.api.cart.bo.Cart;
import com.jingyunbank.etrade.api.cart.service.ICartService;
import com.jingyunbank.etrade.api.weixin.bo.SNSUserInfoBo;
import com.jingyunbank.etrade.api.weixin.service.IWeiXinUserService;
import com.jingyunbank.etrade.weixin.bean.SNSUserInfoVo;
import com.jingyunbank.etrade.weixin.entity.WeixinOauth2Token;
import com.jingyunbank.etrade.weixin.util.GetAccessToken;

@RestController
public class OAuthController {

	@Autowired
	private IWeiXinUserService weixinUserService;
	@Autowired
	private ICartService cartService;

	/**
	 * 保存信息
	 * 
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws Exception
	 */
	@RequestMapping("/api/get/user")
	public void getUserInfo(HttpServletRequest request,
			HttpServletResponse resp, HttpSession session,
			@RequestParam String code, @RequestParam String state) throws IOException {
		try {
			request.setCharacterEncoding("utf-8");
			resp.setCharacterEncoding("utf-8");
			// 用户同意授权
			if (!"authdeny".equals(code)) {
				// 获取授权的access_token
				WeixinOauth2Token weixinOauth2Token = GetAccessToken
						.getOauth2AccessToken("wx3f0da4d18b8e3ff8",
								"eca90c942400a6a3c6a70099bb7958d7", code);
				// 网页授权接口访问凭证
				String accessToken = weixinOauth2Token.getAccessToken();
				// 用户标识
				String openId = weixinOauth2Token.getOpenId();
				// 获取用户信息

				
				if (weixinUserService.getUsers(openId)==null) {
					
					SNSUserInfoVo snsUserInfoVo = GetAccessToken.getUserInfo(
							accessToken, openId);
					SNSUserInfoBo snsUserInfoBo = new SNSUserInfoBo();
					snsUserInfoVo.setId(KeyGen.uuid());
					BeanUtils.copyProperties(snsUserInfoVo, snsUserInfoBo);
					weixinUserService.addUser(snsUserInfoBo);
					Optional<Cart> candidatecart = cartService
							.singleCart(snsUserInfoVo.getId());
					String cartID = null;
					if (candidatecart.isPresent()) {
						cartID = candidatecart.get().getID();
					} else {
						cartID = KeyGen.uuid();
						cartService.save(new Cart(cartID, snsUserInfoVo
								.getId()));
					}
					
					loginSuccessed(snsUserInfoVo.getId(), snsUserInfoVo.getNickname(), cartID, session, resp);

					resp.sendRedirect("http://zhongpai.legu.co/#/");
					return;
				} else {
					Optional<SNSUserInfoBo> bo = weixinUserService.getUsers(openId);
					Optional<Cart> candidatecart = cartService.singleCart(Login
							.UID(request));
					String cartID = null;
					if (candidatecart.isPresent()) {
						cartID = candidatecart.get().getID();
					}
					loginSuccessed(bo.get().getId(), bo.get().getNickname(), cartID, session, resp);

					resp.sendRedirect("http://zhongpai.legu.co/#/");
					return;
				}
			/*}else{
				resp.sendRedirect("http://xiaoxue.tunnel.qydev.com/#/?code="+code);
				return;*/
			}
		} catch (Exception e) {
			e.printStackTrace();
			resp.sendRedirect("http://zhongpai.legu.co/#/?error="+e.getMessage());
			return;
		}
	}

	public static void loginSuccessed(String uid, String username,
			String cartID, HttpSession session, HttpServletResponse response) {
		Security.authenticate(session);
		Login.UID(session, uid);
		Login.uname(session, username);

		if (!StringUtils.isEmpty(cartID)) {
			Login.cartID(session, cartID);
		}

		// 将uid写入cookie
		Cookie cookie = new Cookie(Login.LOGIN_USER_ID, uid);
		cookie.setPath("/");
		cookie.setMaxAge(session.getMaxInactiveInterval());
		response.addCookie(cookie);

	}

}
