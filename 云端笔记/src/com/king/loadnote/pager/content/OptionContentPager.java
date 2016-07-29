package com.king.loadnote.pager.content;

import android.app.Activity;
import android.view.View;

import com.king.loadnote.R;
import com.king.loadnote.pager.BaseCPager;

public class OptionContentPager extends BaseCPager {

	public OptionContentPager(Activity activity) {
		super(activity);
		
		
	}
	
	@Override
	public void initView() {
		rootView = View.inflate(mActivity, R.layout.view_director, null);
	}
	
	@Override
	public void initListener() {
		
	}
	
	
	private void initdata() {
		
	}

}
