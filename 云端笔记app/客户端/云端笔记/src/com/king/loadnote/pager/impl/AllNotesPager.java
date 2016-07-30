package com.king.loadnote.pager.impl;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.king.loadnote.R;
import com.king.loadnote.pager.BaseContentPager;
import com.king.loadnote.pager.content.AllNotesContentPager;
import com.king.loadnote.util.WindowDataUtil;

/**
 * ���бʼǵ�pager
 * 
 * @author Administrator
 * 
 */
public class AllNotesPager extends BaseContentPager {

	private int[] resIds;
	private LinearLayout communication;
	private LinearLayout search;
	private List<String> popStr;
	private AllNotesContentPager pager;

	public AllNotesPager(Activity activity) {
		super(activity);
	}

	public void initView() {
		// ����View���ݳ�ʼ��
		super.initView();
		// ����View���ݳ�ʼ��
		// initChildView();
		// ��ʼ������
		//initListener();
		// ��ʼ����pager������
//		initData();
	}

	private void initChildView() {
		resIds = new int[] { R.drawable.icon_communication,
				R.drawable.icon_search };
		for (int i = 0; i < resIds.length; i++) {
			LinearLayout linearLayout = new LinearLayout(mActivity);
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					75, 75);
			layoutParams.gravity = Gravity.CENTER_VERTICAL;
			linearLayout.setOrientation(LinearLayout.VERTICAL);

			linearLayout.setClickable(true);
			linearLayout.setFocusable(true);
			
			// ���ñ���
			linearLayout.setBackgroundResource(R.drawable.selector_title_btn_bg);
			// ����Viewʱ�õ�
			linearLayout.setTag(i);

			linearLayout.setLayoutParams(layoutParams);

			ImageView child = new ImageView(mActivity);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
			float density = WindowDataUtil.getInstant(mActivity)
					.getDisplayMetrics().density;
			// ���ÿ��
			params.width = (int) (density * 20 + 0.5f);
			params.height = (int) (density * 20 + 0.5f);
			
			params.topMargin = 20;
			// ����λ��
			params.gravity = Gravity.CENTER;
			child.setLayoutParams(params);
			// ����ͼƬ
			child.setImageResource(resIds[i]);

			linearLayout.addView(child);

			functions.addView(linearLayout);

		}

		communication = (LinearLayout) rootView.findViewWithTag(0);
		search = (LinearLayout) rootView.findViewWithTag(1);

	}

	/**
	 * ��ʼ��������
	 */
	private void initListener() {
		communication.setOnClickListener(this);
		search.setOnClickListener(this);
		
	}

	
	
	/**
	 * �ⲿ�����������ʱ���� 1. �����ݿ������ 2. ΪlistView��ʼ��
	 */
	public void initData() {
		// ��ʼ������
		baseTitleText.setText("���бʼ�");

		// ��ʼ��ppView
		popStr = new ArrayList<String>();
		popStr.add("ѡ��ʼ�");
		popStr.add("�ƶ�ͬ��");
		popStr.add("���ݻָ�");
		popStr.add("ɾ��");
		popStr.add("����");

		PopAdapter adapter = new PopAdapter();
		popListView.setAdapter(adapter);

		pager = new AllNotesContentPager(mActivity);
		// ��ǰ����view	�����ڣ���initView֮��initData֮ǰ
		pager.setViews(btnMenuToggle, imageMenuToggle, baseTitleText, whiteBg);
		// ��ʼ��initData
		pager.initData();
		// ���䲼���ļ���ӵ�framelayout
		baseContent.removeAllViews();
		baseContent.addView(pager.rootView);

	}

	
	
	public ListView getPopListView(){
		return popListView;
	}
	
	
	/**
	 * ��ȡAllNotesContentPager����
	 * 
	 * @return
	 */
	public AllNotesContentPager getAllNotesContentPager() {
		return pager;
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		if (v.getTag() != null) {
			int tag = (Integer) v.getTag();
			switch (tag) {
			case 0:

				break;
			case 1:

				break;
			default:
				break;
			}
		}

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
