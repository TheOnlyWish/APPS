package com.king.loadnote.pager.impl;

import android.app.Activity;
import android.view.View.OnClickListener;

import com.king.loadnote.pager.BaseContentPager;
import com.king.loadnote.pager.content.OptionContentPager;

/**
 * ���õ�pager
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
		// ����View���ݳ�ʼ��
		super.initView();

		// ����View���ݳ�ʼ��
		//initChildView();

		//initListener();
		// ��ʼ����pager������
//		initData();
	}

	private void initChildView() {

	}

	/**
	 * ��ʼ��������
	 */
	private void initListener() {

	}

	/**
	 * �ⲿ�����������ʱ���� 1. �����ݿ������ 2. ΪlistView��ʼ��
	 */
	public void initData() {
		// ��activity
		baseTitleText.setText("����");

		OptionContentPager pager = new OptionContentPager(mActivity);

		baseContent.removeAllViews();
		baseContent.addView(pager.rootView);

	}


}
