package com.jingyunbank.etrade.user.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.Result;
import com.jingyunbank.core.web.AuthBeforeOperation;
import com.jingyunbank.core.web.ServletBox;
import com.jingyunbank.etrade.api.user.bo.UserInfo;
import com.jingyunbank.etrade.api.user.service.IUserInfoService;
import com.jingyunbank.etrade.user.bean.UserInfoVO;

/**
 * @author Administrator 
 * @date 2015年11月6日
	@todo TODO
 */
@RestController
@RequestMapping("/api/users")
public class UserInfoController {
	@Autowired
	private IUserInfoService userInfoService;

	
	/**
	 * 个人资料的添加
	 * @param session
	 * @param userInfoVO
	 * @return
	 * @throws Exception
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/",method=RequestMethod.PUT)
	public Result addUserInfo(HttpSession session,UserInfoVO userInfoVO,HttpServletRequest request) throws Exception{
		
		UserInfo userInfo=new UserInfo();
		BeanUtils.copyProperties(userInfoVO, userInfo);
		String id = ServletBox.getLoginUID(request);
		userInfoVO.setUID(id);;
		if(userInfoService.UidExists(userInfoVO.getUID())>0){
			return Result.fail("该uid已经存在！");
		}
		if(userInfoService.save(userInfo)){
			return Result.ok(userInfoVO);
		}
		return Result.fail("重试！");
	}
	
	/**
	 * 通过uid查询个人信息	
	 * @param session
	 * @param request
	 * @param uid
	 * @return
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/userinfo",method=RequestMethod.GET)
	public Result selectUserInfo(HttpSession session,HttpServletRequest request) throws Exception{
		String uid = ServletBox.getLoginUID(request);
		Optional<UserInfo> userinfo= userInfoService.getByUid(uid);
		/*System.out.println(userinfo.get().getBirthday());*/
		
		if(userinfo.isPresent()){
			
			UserInfo userInfo=userinfo.get();
			UserInfoVO userInfoVO=new UserInfoVO();
			BeanUtils.copyProperties(userInfo, userInfoVO);
			if(!StringUtils.isEmpty(userinfo.get().getBirthday())){
				String birthdayStr= new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(userinfo.get().getBirthday());
				userInfoVO.setBirthdayStr(birthdayStr);
			}
			return Result.ok(userInfoVO);
		}
		return Result.fail("重试");
	}
	
	/**
	 * 修改个人资料信息
	 * @param session
	 * @param request
	 * @param userInfoVO
	 * @return
	 * @throws Exception
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/info",method=RequestMethod.PUT)
	public Result updateUserInfo(@RequestBody UserInfoVO userInfoVO,HttpSession session,HttpServletRequest request) throws Exception {
		if(!StringUtils.isEmpty(userInfoVO.getBirthdayStr())){
			Date date = new SimpleDateFormat("yyyy-MM-dd").parse(userInfoVO.getBirthdayStr());
			userInfoVO.setBirthday(date);
		}
		UserInfo userInfo=new UserInfo();
		String id = ServletBox.getLoginUID(request);
		userInfoVO.setUID(id);
		BeanUtils.copyProperties(userInfoVO, userInfo);
		
		if(userInfoService.refresh(userInfo)){
			
			return Result.ok(userInfoVO);
		}
		return	Result.fail("修改失败");
	}
}
