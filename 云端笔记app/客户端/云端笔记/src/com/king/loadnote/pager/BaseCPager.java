package com.king.loadnote.pager;

import android.app.Activity;
import android.view.View;

public class BaseCPager {

	public Activity mActivity;
	public View rootView;
	
	public BaseCPager(Activity activity) {
		mActivity = activity;
		initView();
		initListener();
	}
	
	
	public void initView(){
		
	}
	
	
	public void initListener(){
		
	}
}
