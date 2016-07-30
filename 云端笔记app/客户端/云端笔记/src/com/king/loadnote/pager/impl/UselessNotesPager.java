package com.king.loadnote.pager.impl;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.king.loadnote.R;
import com.king.loadnote.pager.BaseContentPager;
import com.king.loadnote.pager.content.AllNotesContentPager;
import com.king.loadnote.pager.content.UselessNoteContentPager;
import com.king.loadnote.pager.impl.AllNotesPager.PopAdapter;
import com.king.loadnote.pager.impl.AllNotesPager.PopHolder;
import com.king.loadnote.util.WindowDataUtil;

/**
 * 所有笔记的pager
 * 
 * @author Administrator
 * 
 */
public class UselessNotesPager extends BaseContentPager {

	public UselessNotesPager(Activity activity) {
		super(activity);
	}

	private int[] resIds;
	private LinearLayout communication;
	private LinearLayout search;
	private List<String> popStr;
	private UselessNoteContentPager pager;

	public void initView() {
		// 父类View数据初始化
		super.initView();
		// 子类View数据初始化
		initChildView();
		// 初始化监听
		initListener();
		// 初始化该pager的数据
		// initData();
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
		// 初始化标题
		baseTitleText.setText("废纸篓");

		// 初始化ppView
		popStr = new ArrayList<String>();
		popStr.add("选择笔记");
		popStr.add("删除");
		popStr.add("清空废纸篓");
		popStr.add("还原笔记");
		popStr.add("设置");

		PopAdapter adapter = new PopAdapter();
		popListView.setAdapter(adapter);

		pager = new UselessNoteContentPager(mActivity);
		// 提前设置view 生命期，在initView之后，initData之前
		pager.setViews(btnMenuToggle, imageMenuToggle, baseTitleText);
		// 初始化initData
		pager.initData();
		// 将其布局文件添加到framelayout
		baseContent.removeAllViews();
		baseContent.addView(pager.rootView);

	}

	public ListView getPopListView() {
		return popListView;
	}

	/**
	 * 获取AllNotesContentPager对象
	 * 
	 * @return
	 */
	public UselessNoteContentPager getUselessNoteContentPager() {
		return pager;
	}

	class PopAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return popStr.size();
		}

		@Override
		public Object getItem(int position) {
			return popStr.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			PopHolder holder = null;
			if (convertView == null) {
				view = View.inflate(mActivity, R.layout.view_pop_item_list,
						null);
				holder = new PopHolder();
				holder.popText = (TextView) view.findViewById(R.id.pop_text);
				view.setTag(holder);
			} else {
				view = convertView;
				holder = (PopHolder) view.getTag();
			}
			String string = popStr.get(position);
			holder.popText.setText(string);
			return view;
		}

	}

	class PopHolder {
		public TextView popText;

	}

}
