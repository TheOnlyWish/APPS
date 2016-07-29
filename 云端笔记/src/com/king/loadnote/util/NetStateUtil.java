package com.king.loadnote.util;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * �ж������Ƿ����
 * @author Administrator
 */
public class NetStateUtil {
	
	public static NetStateUtil util;
	public static Activity mActivity;
	private ConnectivityManager cm;
	private NetworkInfo activeNetworkInfo;
	
	private NetStateUtil() {
		cm = (ConnectivityManager) mActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
	}
	
	
	public static NetStateUtil instantNetStateUtil(Activity activity){
		mActivity = activity;
		if(util == null){
			util = new NetStateUtil();
		}
		return util;
	}
	
	/**
	 * �ж������Ƿ����
	 * @return
	 */
	public boolean NetUsable(){
		activeNetworkInfo = cm.getActiveNetworkInfo();
		if(activeNetworkInfo != null){
			return true;
		}
		return false;
	}
	
	/**
	 * ��ȡ��������
	 * @return
	 */
	public String getNetType(){
		return activeNetworkInfo.getTypeName();
	}
	
}
