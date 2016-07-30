package com.king.loadnote.fragment.content.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.king.loadnote.R;
import com.king.loadnote.activity.MainActivity;
import com.king.loadnote.dao.NoteDao;
import com.king.loadnote.domain.Ids;
import com.king.loadnote.domain.Item;
import com.king.loadnote.domain.Note;
import com.king.loadnote.fragment.BaseFragment;
import com.king.loadnote.util.SynchronDataUtil;
import com.king.loadnote.util.SynchronDataUtil.SetOnUpdateListener;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;

public class MenuFragment extends BaseFragment {

	private LinearLayout title;
	private ImageView portrait;
	private TextView tel;
	private ListView listView;
	private LinearLayout sync;
	private ImageView syncImage;
	private TextView synText;

	private List<Item> items;
	// 默认是第一个--所有笔记
	private int lastClickedItem = 0;
	private final int REFLESH_ADAPTER = 1;

	/**
	 * 消息处理
	 */
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case REFLESH_ADAPTER:
				if (adapter != null) {
					adapter.notifyDataSetChanged();
				} else {
					adapter = new MenuAdapter();
					listView.setAdapter(adapter);
				}
				break;

			default:
				break;
			}

		};
	};
	private MenuAdapter adapter;

	@Override
	public void initView() {
		// 初始化根布局
		rootView = View.inflate(mActivity, R.layout.fragment_menu, null);

		title = (LinearLayout) rootView.findViewById(R.id.menu_title);
		portrait = (ImageView) rootView.findViewById(R.id.menu_title_portrait);
		tel = (TextView) rootView.findViewById(R.id.menu_title_tel);
		listView = (ListView) rootView.findViewById(R.id.menu_list);
		sync = (LinearLayout) rootView.findViewById(R.id.menu_sync);
		syncImage = (ImageView) rootView.findViewById(R.id.sync_image);
		synText = (TextView) rootView.findViewById(R.id.sync_text);

		initListener();
		initData();
	}

	private void initListener() {
		// 当menu的item被点击时调用，调用contentFragment的setViewPager方法
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				MainActivity mainActivity = (MainActivity) mActivity;
				ContentFragment contentFragment = mainActivity
						.getContentFragment();
				// 更替contentFragment
				contentFragment.setPager(position, false);
				// 收起左侧滑栏
				mainActivity.closeLeftDrawer();

				// // 更该上一个被点击的item
				// changeItemView(listView.getChildAt(lastClickedItem), true);
				// 记录被点击的item
				lastClickedItem = position;
				// // 更新
				// changeItemView(view, false);
				adapter.notifyDataSetChanged();
			}
		});

		// 同步图标
		syncImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				// 动画
				RotateAnimation rotateAnim = new RotateAnimation(0, 360,
						Animation.RELATIVE_TO_SELF, 0.5f,
						Animation.RELATIVE_TO_SELF, 0.5f);
				rotateAnim.setDuration(1000);
				rotateAnim.setRepeatMode(Animation.INFINITE);
				syncImage.startAnimation(rotateAnim);

				PostOrReceive();

			}
		});

	}

	// 云端同步
	private void PostOrReceive() {

		SynchronDataUtil syncUtil = SynchronDataUtil.getInstant(mActivity);
		syncUtil.updateData();

		syncUtil.setOnUpdateListener(new SetOnUpdateListener() {

			@Override
			public void onSuccess(ResponseInfo<String> result,
					List<Note> listNote) {

				// 成功后
				NoteDao dao = new NoteDao(mActivity);
				// 修改list集合
				for (Note note : listNote) {
					// 写入数据库,修改updated的值
					dao.updateUpdateValue(note._id, 0);
				}

				System.out.println("ListIds:" + result.result);

				List<Ids> ids = new ArrayList<Ids>();

				JSONArray jarr = null;
				try {
					jarr = new JSONArray(result.result);
					for (int i = 0; i < jarr.length(); i++) {
						Ids id = new Ids();
						String idStr = jarr.getString(i);
						JSONObject jobj = new JSONObject(idStr);
						id.server_id = jobj.getInt("server_id");
						id.client_id = jobj.getInt("client_id");
						ids.add(id);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

				for (Ids id : ids) {
					// 写入数据库,修改服务端传来的server_note_id的值
					dao.updateServerId(id.server_id, id.client_id);
					
				}
				
				// 结束动画
				syncImage.getAnimation().setRepeatMode(Animation.ABSOLUTE);
				syncImage.getAnimation().setRepeatCount(0);
				
				// 关闭侧拉菜单
				MainActivity mainActivity = (MainActivity) mActivity;
				mainActivity.closeLeftDrawer();
				mainActivity.getContentFragment().getPagers().get(0).initData();
			}

			@Override
			public void onStart() {
				
			}

			@Override
			public void onNoteDataToUpdate() {

			}

			@Override
			public void onLoading(long total, long current, boolean isUploading) {

			}

			@Override
			public void onFailure(HttpException e, String errorText) {
			}

			@Override
			public void onCancelled() {

			}
		});
	}

	@Override
	public void initData() {
		// 初始化listView的数据
		items = new ArrayList<Item>();
		items.add(new Item("所有笔记", R.drawable.selector_item_list_menu_image_01));
		items.add(new Item("笔记夹", R.drawable.selector_item_list_menu_image_02));
		items.add(new Item("废纸篓", R.drawable.selector_item_list_menu_image_03));
		items.add(new Item("设置", R.drawable.selector_item_list_menu_image_06));

		adapter = new MenuAdapter();
		listView.setAdapter(adapter);

		// 默认主页内容是所有笔记
		setContentPager(0);
	}

	/**
	 * 对外暴露，设置页面方法
	 * 
	 * @param pagerIndex
	 */
	public void setContentPager(int pagerIndex) {
		MainActivity mainActivity = (MainActivity) mActivity;
		ContentFragment contentFragment = mainActivity.getContentFragment();
		contentFragment.setPager(pagerIndex, false);
		// 改变Menu的选中view--默认第一个itemView
		adapter.notifyDataSetChanged();
	}

	public void changeItemView(View view, boolean enable) {

		ImageView itemIcon = (ImageView) view.findViewById(R.id.item_icon);
		TextView itemText = (TextView) view.findViewById(R.id.item_text);
		view.setEnabled(enable);
		itemIcon.setEnabled(enable);
		itemText.setEnabled(enable);
	}

	class MenuAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return items.size();
		}

		@Override
		public Object getItem(int position) {
			return items.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			Holder holder = null;
			if (convertView == null) {
				view = View.inflate(mActivity, R.layout.view_item_menu_list,
						null);
				holder = new Holder();
				holder.itemIcon = (ImageView) view.findViewById(R.id.item_icon);
				holder.itemText = (TextView) view.findViewById(R.id.item_text);
				view.setTag(holder);
			} else {
				view = convertView;
				holder = (Holder) view.getTag();
			}
			Item item = items.get(position);
			if (lastClickedItem == position) {
				holder.itemIcon.setEnabled(false);
				holder.itemText.setEnabled(false);
			} else {
				holder.itemIcon.setEnabled(true);
				holder.itemText.setEnabled(true);
			}
			holder.itemIcon.setImageResource(item.resId);
			holder.itemText.setText(item.itemStr);

			return view;
		}

	}

	class Holder {
		public TextView itemText;
		public ImageView itemIcon;
	}

}
