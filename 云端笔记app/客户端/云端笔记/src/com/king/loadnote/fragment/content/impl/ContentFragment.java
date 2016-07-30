package com.king.loadnote.fragment.content.impl;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.king.loadnote.R;
import com.king.loadnote.fragment.BaseFragment;
import com.king.loadnote.pager.BaseContentPager;
import com.king.loadnote.pager.impl.AllNotesPager;
import com.king.loadnote.pager.impl.NoteBooksPager;
import com.king.loadnote.pager.impl.OptionPager;
import com.king.loadnote.pager.impl.UselessNotesPager;

public class ContentFragment extends BaseFragment {

	private List<BaseContentPager> pagers;
	private ViewPager viewPager;
	
	@Override
	public void initView() {
		// 初始化根布局
		rootView = View.inflate(mActivity, R.layout.fragment_content, null);
		viewPager = (ViewPager) rootView.findViewById(R.id.content_viewPager);
		
		initListener();
		initData();
	}

	private void initListener() {
		
	}
	
	@Override
	public void initData() {
		// 初始化viewPager的pager数据
		pagers = new ArrayList<BaseContentPager>();
		pagers.add(new AllNotesPager(mActivity));
		pagers.add(new NoteBooksPager(mActivity));
		pagers.add(new UselessNotesPager(mActivity));
		pagers.add(new OptionPager(mActivity));
		
		ContentAdapter adapter = new ContentAdapter();
		viewPager.setAdapter(adapter);
	}

	class ContentAdapter extends PagerAdapter{

		@Override
		public int getCount() {
			return pagers.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			BaseContentPager pager = pagers.get(position);
			View view = pager.rootView;
			container.addView(view);
			return view;
		}
		
	}
	
	/**
	 * 设置当前contentPager页面
	 * @param index
	 */
	public void setPager(int index, boolean smoothScroll){
		viewPager.setCurrentItem(index, smoothScroll);
		pagers.get(index).initData();
	}
	
	public List<BaseContentPager> getPagers(){
		return pagers;
	}
	
	
}
