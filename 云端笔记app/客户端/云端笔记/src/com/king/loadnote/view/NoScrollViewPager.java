package com.king.loadnote.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
/**
 * 不能滑动的PagerView
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
	
	
	// 重写触碰拦截分发事件方法
	// 不拦截
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return false;
	}

	//不消费
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		return false;
	}
	
}
