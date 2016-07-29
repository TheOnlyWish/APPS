package com.king.loadnote.util;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

/**
 * ��ȡwindow�����Ĺ�����
 * @author Administrator
 *
 */
public class WindowDataUtil {

	private static WindowDataUtil util;
	
	private static Activity mActivity;

	private static WindowManager windowManager;

	private static Display display;

	private static DisplayMetrics metrics;
	
	private WindowDataUtil() {
		windowManager = mActivity.getWindowManager();
		display = windowManager.getDefaultDisplay();
		metrics = mActivity.getResources().getDisplayMetrics();
	}

	/**
	 * ʵ����WindowDataUtil������
	 * @param activity
	 * @return
	 */
	public static WindowDataUtil getInstant(Activity activity){
		mActivity = activity;
		if(util == null){
			util = new WindowDataUtil();
		}
		return util;
	}
	
	/**
	 * ������Ļ��
	 * @return
	 */
	public static int getWindowHeight(){
		return display.getHeight();
	}
	
	/**
	 * ������Ļ��
	 * @return
	 */
	public static int getWindowWidth(){
		return display.getWidth();
	}
	
	/**
	 * ������Ļ����
	 * ����:��Ļ�ܶ�
	 * @return
	 */
	public DisplayMetrics getDisplayMetrics(){
		return metrics;
	}
	
}
