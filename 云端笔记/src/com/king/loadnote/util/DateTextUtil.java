package com.king.loadnote.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间文本工具类
 * @author Administrator
 *
 */
public class DateTextUtil {

	// 一天的毫秒数
	private static final long FULLDAYMILLISCENDS = 86400000;
	// 前天
	private static final String YESTERDAY = "昨天";
	// 昨天
	private static final String THE_DAY_BEFORE_YESTERDAY = "前天";
	// 今天
	private static final String TODAY = "今天";
	
	/**
	 * 获取时间文本
	 * @return
	 */
	public static String getDateString(long time){
		long currentTime = System.currentTimeMillis();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// 创建实例对象
		Calendar cal = Calendar.getInstance();
		// 设置当前是毫秒数
		cal.set(Calendar.HOUR, -12);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		// 获取今天凌晨0点的毫秒数
		long toDayZero = cal.getTimeInMillis();
		// 获取昨天凌晨0点的毫秒数
		long yesDayZero = toDayZero - FULLDAYMILLISCENDS;
		// 获取前天凌晨0点的毫秒数
		long beforeDayZero = yesDayZero - FULLDAYMILLISCENDS;
		
		// 判断
		if(time >= toDayZero){// 今天
			return TODAY;
		}else if(time > yesDayZero){ // 昨天
			return YESTERDAY;
		}else if(time > beforeDayZero){ // 前天
			return THE_DAY_BEFORE_YESTERDAY;
		}else{ // 日期表示
			return sdf.format(new Date(time));
		}
	}
}
