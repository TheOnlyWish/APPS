package com.king.loadnote.fragment.splash.impl;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.king.loadnote.R;
import com.king.loadnote.base.BasePager;
import com.king.loadnote.base.impl.LoginPager;
import com.king.loadnote.base.impl.RegistPager;
import com.king.loadnote.fragment.BaseFragment;
import com.viewpagerindicator.TabPageIndicator;

/**
 * Splash_bottom的框架
 * @author Administrator
 *
 */
public class SplashBottomFragment extends BaseFragment {
	
	private TabPageIndicator indicator;
	private ViewPager viewPager;
	
	// 数据pagers
	private List<BasePager> pagers;

	@Override
	public void initView() {
		rootView = View.inflate(mActivity, R.layout.framelayout_splash_bottom, null);
		indicator = (TabPageIndicator) rootView.findViewById(R.id.splash_indicator);
		viewPager = (ViewPager) rootView.findViewById(R.id.splash_pager);
		
		initData();
	}
	
	@Override
	public void initData() {
		// 初始化数据View集合
		pagers = new ArrayList<BasePager>();
		pagers.add(new LoginPager(mActivity));
		pagers.add(new RegistPager(mActivity));
		
		LRAdapter adapter = new LRAdapter();
		viewPager.setAdapter(adapter);
		
		indicator.setViewPager(viewPager);
		
	}
	
	
	class LRAdapter extends PagerAdapter{

		@Override
		public int getCount() {
			return pagers.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
		
		@Override
		public CharSequence getPageTitle(int position) {
			return pagers.get(position).title;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			BasePager pager = pagers.get(position);
			View rootView = pager.rootView;
			container.addView(rootView);
			return rootView;
		}
		
		
	}
	
}
