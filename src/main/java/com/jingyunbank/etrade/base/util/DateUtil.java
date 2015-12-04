package com.jingyunbank.etrade.base.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	
	public static final String DATE_FORMAT_DATE_TIME = "yyyy-MM-dd";
	
	/**
	 * 将date格式化为yyyy-MM-dd HH:mm:dd字符串
	 * @param date
	 * @return
	 * 2015年12月4日 qxs
	 */
	public static String formatDate(Date date){
		if(date!=null){
			return new SimpleDateFormat(DATE_FORMAT_DATE_TIME).format(date);
		}
		return null;
	}
	
	/**
	 * 将date格式化为指定格式的字符串
	 * @param date
	 * @param pattern 日志格式
	 * @return
	 * 2015年12月4日 qxs
	 */
	public static String formatDate(Date date, String pattern){
		if(date!=null){
			return new SimpleDateFormat(pattern).format(date);
		}
		return null;
	}

}
