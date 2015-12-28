package com.jingyunbank.etrade.base.util;

import java.util.Calendar;
import java.util.Date;

public class EtradeUtil {
	
	/**
	 * 验证认证身份是否超时
	 * @param sessionDate
	 * @return
	 * 2015年12月1日 gyx
	 */
	public static boolean effectiveTime(Object sessionDate){
		Calendar now=Calendar.getInstance();
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
