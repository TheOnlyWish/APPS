package com.king.loadnote.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPreference的封装工具类
 * @author Administrator
 */
public class SharedPUtil {

	private static SharedPUtil spUtil;
	private static Activity mActivity;
	private SharedPreferences sp;
	
	private SharedPUtil(){
		sp = mActivity.getSharedPreferences("config", Context.MODE_PRIVATE);
	}

	
	public static SharedPUtil getInstant(Activity activity){
		mActivity = activity;
		if(spUtil == null){
			spUtil = new SharedPUtil();
		}
		return spUtil;
	}
	
	
	public void putString(String key, String value){
		sp.edit().putString(key, value).commit();
	}
	
	
	public String getString(String key){
		return sp.getString(key, null);
	}
	
	
	
}
