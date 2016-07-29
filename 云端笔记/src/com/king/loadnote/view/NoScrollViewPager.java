package com.king.loadnote.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
/**
 * ���ܻ�����PagerView
 * @author Administrator
 *
 */
public class NoScrollViewPager extends ViewPager {

	public NoScrollViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public NoScrollViewPager(Context context) {
		super(context);
	}
	
	
	// ��д�������طַ��¼�����
	// ������
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return false;
	}

	//������
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		return false;
	}
	
}
