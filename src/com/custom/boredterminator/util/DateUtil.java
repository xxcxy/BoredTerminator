package com.custom.boredterminator.util;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	
	public static Date getTodayLastTime(){
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY,23);
		cal.set(Calendar.MINUTE,59);
		cal.set(Calendar.SECOND,59);
		return cal.getTime();
	}
	public static Date getNow(){
		Calendar cal = Calendar.getInstance();
		return cal.getTime();
	}
}
