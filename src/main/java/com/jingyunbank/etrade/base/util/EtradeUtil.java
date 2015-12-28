package com.jingyunbank.etrade.base.util;

import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpSession;

import com.jingyunbank.etrade.user.controller.UserController;

public class EtradeUtil {
	
	/**
	 * 验证认证身份是否超时
	 * @param session
	 * @return
	 * 2015年12月1日 gyx
	 */
	public static boolean effectiveTime(HttpSession session){
		Calendar now=Calendar.getInstance();
		now.setTime(new Date());
		Object sessionDate=session.getAttribute(UserController.CHECK_CODE_PASS_DATE);
		if(sessionDate!=null && sessionDate instanceof Date ){
			Calendar checkDate  = Calendar.getInstance();
			checkDate.setTime((Date)sessionDate);
			//+2
			checkDate.add(Calendar.MINUTE, SystemConfigProperties.getInt(SystemConfigProperties.EFFECTIVE_TIME) );
			if(checkDate.after(now)){
				return true;
			}
		}
		return false;
		
	}
	
}
