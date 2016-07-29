package com.king.loadnote.pager.impl;

import java.util.ArrayList;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.king.loadnote.R;
import com.king.loadnote.pager.BaseContentPager;
import com.king.loadnote.pager.content.NoteBooksContentPager;
import com.king.loadnote.util.WindowDataUtil;

/**
 * ���бʼǵ�pager
 * 
 * @author Administrator
 * 
 */
public class NoteBooksPager extends BaseContentPager{

	private int[] resIds;
	private View contentView;
	private ListView listView;

	private ArrayList<String> popStr;
	private NoteBooksContentPager booksContentPager;
	
	public NoteBooksPager(Activity activity) {
		super(activity);
	}

	public void initView() {
		// ����View���ݳ�ʼ��
		super.initView();

		// ����View���ݳ�ʼ��
		initChildView();

		initListener();
		// ��ʼ����pager������
//		initData();
	}

	private void initChildView() {
		resIds = new int[] { R.drawable.icon_add_folder
				};
		for (int i = 0; i < resIds.length; i++) {
			LinearLayout linearLayout = new LinearLayout(mActivity);
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					75, 75);
			layoutParams.gravity = Gravity.CENTER_VERTICAL;
			linearLayout.setOrientation(LinearLayout.VERTICAL);

			linearLayout.setClickable(true);
			linearLayout.setFocusable(true);

			// ���ñ���
			linearLayout
					.setBackgroundResource(R.drawable.selector_title_btn_bg);
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

		// contentListView
		contentView = View.inflate(mActivity, R.layout.view_notebook, null);
		listView = (ListView) contentView.findViewById(R.id.listViewNoteBooks);

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
		baseTitleText.setText("�ʼǱ�");

		// ��ʼ��ppView
		popStr = new ArrayList<String>();
		popStr.add("ͬ��");
		popStr.add("����");

		PopAdapter popAdapter = new PopAdapter();
		popListView.setAdapter(popAdapter);
		
		booksContentPager = new NoteBooksContentPager(mActivity);
		booksContentPager.initData();
		
		baseContent.removeAllViews();
		baseContent.addView(booksContentPager.rootView);
		
		
		loadDataFromDataBase();

	}

	
	public ListView getPopListView(){
		return popListView;
	}
	
	
	private void loadDataFromDataBase() {

	}

//	@Override
//	public void onClick(View v) {
//		if (v.getTag() != null) {
//			int tag = (Integer) v.getTag();
//			switch (tag) {
//			case 0:
//
//				break;
//			case 1:
//
//				break;
//			default:
//				break;
//			}
//		} else {
//			super.onClick(v);
//		}
//	}
	
	/**
	 * ��ȡ������������
	 */
	public LinearLayout getFunctions(){
		return functions;
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

	public NoteBooksContentPager getNoteBookContentPager() {
		return booksContentPager;
	}

}
