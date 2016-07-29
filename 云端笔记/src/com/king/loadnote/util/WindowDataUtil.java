package com.king.loadnote.util;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

/**
 * 获取window参数的工具类
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
	 * 实例化WindowDataUtil工具类
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
	 * 返回屏幕高
	 * @return
	 */
	public static int getWindowHeight(){
		return display.getHeight();
	}
	
	/**
	 * 返回屏幕宽
	 * @return
	 */
	public static int getWindowWidth(){
		return display.getWidth();
	}
	
	/**
	 * 返回屏幕度量
	 * 例如:屏幕密度
	 * @return
	 */
	public DisplayMetrics getDisplayMetrics(){
		return metrics;
	}
	
}
