package com.king.loadnote.base;

import com.king.loadnote.R;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;

/**
 * ViewPager的 Pager的基类
 * 
 * @author Administrator
 * 
 */
public class BasePager {

	// activity对象
	public Activity mActivity;
	// 根布局对象
	public View rootView;
	// title
	public String title;

	public BasePager(Activity activity) {
		mActivity = activity;
		initView();
	}

	public void initView() {

	}

	public void initData() {
	}

}
