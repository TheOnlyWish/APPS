package com.king.loadnote.base;

import com.king.loadnote.R;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;

/**
 * ViewPager�� Pager�Ļ���
 * 
 * @author Administrator
 * 
 */
public class BasePager {

	// activity����
	public Activity mActivity;
	// �����ֶ���
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
