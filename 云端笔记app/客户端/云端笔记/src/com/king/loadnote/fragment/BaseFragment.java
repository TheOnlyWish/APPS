package com.king.loadnote.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Fragment的基类
 * @author Administrator
 *
 */
public abstract class BaseFragment extends Fragment {

	// 跟布局View
	public View rootView;
	// acitivity
	public Activity mActivity;
	


	// 相当于Activity的oncreate
	// 用于初始化Views
	@Override
	public void onCreate(Bundle savedInstanceState) {
		mActivity = getActivity();
		initView();
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return rootView;
	}
	
	/**
	 * 初始化Views
	 */
	public abstract void initView();
	
	/**
	 * 初始化数据
	 */
	public void initData(){
		
	}
	
	
}
