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
 * ���бʼǵ�pager
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
		// ����View���ݳ�ʼ��
		super.initView();
		// ����View���ݳ�ʼ��
		initChildView();
		// ��ʼ������
		initListener();
		// ��ʼ����pager������
		// initData();
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
		// ��ʼ������
		baseTitleText.setText("��ֽ¨");

		// ��ʼ��ppView
		popStr = new ArrayList<String>();
		popStr.add("ѡ��ʼ�");
		popStr.add("ɾ��");
		popStr.add("��շ�ֽ¨");
		popStr.add("��ԭ�ʼ�");
		popStr.add("����");

		PopAdapter adapter = new PopAdapter();
		popListView.setAdapter(adapter);

		pager = new UselessNoteContentPager(mActivity);
		// ��ǰ����view �����ڣ���initView֮��initData֮ǰ
		pager.setViews(btnMenuToggle, imageMenuToggle, baseTitleText);
		// ��ʼ��initData
		pager.initData();
		// ���䲼���ļ���ӵ�framelayout
		baseContent.removeAllViews();
		baseContent.addView(pager.rootView);

	}

	public ListView getPopListView() {
		return popListView;
	}

	/**
	 * ��ȡAllNotesContentPager����
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
