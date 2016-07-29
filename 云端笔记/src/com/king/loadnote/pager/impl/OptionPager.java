package com.king.loadnote.pager.impl;

import android.app.Activity;
import android.view.View.OnClickListener;

import com.king.loadnote.pager.BaseContentPager;
import com.king.loadnote.pager.content.OptionContentPager;

/**
 * 设置的pager
 * 
 * @author Administrator
 * 
 */
public class OptionPager extends BaseContentPager implements
		OnClickListener {

	public OptionPager(Activity activity) {
		super(activity);
	}

	public void initView() {
		// 父类View数据初始化
		super.initView();

		// 子类View数据初始化
		//initChildView();

		//initListener();
		// 初始化该pager的数据
//		initData();
	}

	private void initChildView() {

	}

	/**
	 * 初始化监听器
	 */
	private void initListener() {

	}

	/**
	 * 外部创建本类对象时调用 1. 从数据库读数据 2. 为listView初始化
	 */
	public void initData() {
		// 跳activity
		baseTitleText.setText("设置");

		OptionContentPager pager = new OptionContentPager(mActivity);

		baseContent.removeAllViews();
		baseContent.addView(pager.rootView);

	}


}
