package com.king.loadnote.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Fragment�Ļ���
 * @author Administrator
 *
 */
public abstract class BaseFragment extends Fragment {

	// ������View
	public View rootView;
	// acitivity
	public Activity mActivity;
	


	// �൱��Activity��oncreate
	// ���ڳ�ʼ��Views
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
	 * ��ʼ��Views
	 */
	public abstract void initView();
	
	/**
	 * ��ʼ������
	 */
	public void initData(){
		
	}
	
	
}
