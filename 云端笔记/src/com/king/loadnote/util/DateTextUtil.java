package com.king.loadnote.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * ʱ���ı�������
 * @author Administrator
 *
 */
public class DateTextUtil {

	// һ��ĺ�����
	private static final long FULLDAYMILLISCENDS = 86400000;
	// ǰ��
	private static final String YESTERDAY = "����";
	// ����
	private static final String THE_DAY_BEFORE_YESTERDAY = "ǰ��";
	// ����
	private static final String TODAY = "����";
	
	/**
	 * ��ȡʱ���ı�
	 * @return
	 */
	public static String getDateString(long time){
		long currentTime = System.currentTimeMillis();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// ����ʵ������
		Calendar cal = Calendar.getInstance();
		// ���õ�ǰ�Ǻ�����
		cal.set(Calendar.HOUR, -12);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		// ��ȡ�����賿0��ĺ�����
		long toDayZero = cal.getTimeInMillis();
		// ��ȡ�����賿0��ĺ�����
		long yesDayZero = toDayZero - FULLDAYMILLISCENDS;
		// ��ȡǰ���賿0��ĺ�����
		long beforeDayZero = yesDayZero - FULLDAYMILLISCENDS;
		
		// �ж�
		if(time >= toDayZero){// ����
			return TODAY;
		}else if(time > yesDayZero){ // ����
			return YESTERDAY;
		}else if(time > beforeDayZero){ // ǰ��
			return THE_DAY_BEFORE_YESTERDAY;
		}else{ // ���ڱ�ʾ
			return sdf.format(new Date(time));
		}
	}
}
