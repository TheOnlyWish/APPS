package com.king.loadnote.pager.content;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.king.loadnote.R;
import com.king.loadnote.activity.AddNoteEditActivity;
import com.king.loadnote.activity.MainActivity;
import com.king.loadnote.dao.NoteBookDao;
import com.king.loadnote.dao.NoteDao;
import com.king.loadnote.domain.Note;
import com.king.loadnote.domain.NoteBook;
import com.king.loadnote.pager.BaseCPager;
import com.king.loadnote.pager.impl.UselessNotesPager;
import com.king.loadnote.util.DateTextUtil;
import com.lidroid.xutils.BitmapUtils;

public class UselessNoteContentPager extends BaseCPager {

	// view
	private LinearLayout linearLayoutAllnoteTime;
	private TextView tvAllnoteTime;
	private ImageView shadow;
	private ListView allnoteListView;

	private TextView baseTitle;
	private ImageView imageMenuToggle;
	private LinearLayout btnMenuToggle;
	// 数据
	private List<Note> notes;
	// 被选中的notes的位置集合
	private HashMap<Integer, Note> checkedNotes = new HashMap<Integer, Note>();
	// 是否显示checkbox
	public boolean isShowCheckBox = false;
	private int firstIndex = 0;
	private int count = 20;
	private NoteAdapter adapter;
	private final String BLACK_PLACEHOLDER_SHORT = "          ";
	private final String BLACK_PLACEHOLDER_LONG = "                          ";
	// 本地数据读取成功
	private final int LOAD_LOCAL_DATA_FINISH = 1;
	private final int REFLASH_LISTVIEW_FUNCTION = 2;
	private final int REFLASH_TITLE_TIME = 3;

	private static List<String> suffixes = new ArrayList<String>();

	static {
		suffixes.add(".jpg");
		suffixes.add(".png");
		suffixes.add(".jpeg");
		suffixes.add(".gif");
		suffixes.add(".bmp");
		suffixes.add(".wbmp");
	}

	// 取消
	private OnClickListener cancel = new OnClickListener() {
		@Override
		public void onClick(View v) {
			clearCheckedMap();
		}
	};

	// 打开菜单
	private OnClickListener openMenu = new OnClickListener() {
		@Override
		public void onClick(View v) {
			mainActivity.openLeftDrawer();
		}
	};

	public UselessNoteContentPager(Activity activity) {
		super(activity);
	}

	private Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case LOAD_LOCAL_DATA_FINISH:
				if (adapter == null) {
					adapter = new NoteAdapter();
					// 设置adapter
					allnoteListView.setAdapter(adapter);
				} else {
					adapter.notifyDataSetChanged();
				}
				// 取消等待
				if (notes.size() > 0) {
					int index = msg.arg1;
					Note note = notes.get(index);
					String showTime = DateTextUtil.getDateString(new Date(
							note.writeTime).getTime());
					tvAllnoteTime.setText(showTime);
				}else{
					tvAllnoteTime.setText("空空如也");
				}
				break;
			case REFLASH_TITLE_TIME:
				if (notes.size() > 0) {
					int index = msg.arg1;
					Note note = notes.get(index);
					String showTime = DateTextUtil.getDateString(new Date(
							note.writeTime).getTime());
					tvAllnoteTime.setText(showTime);
				}else{
					tvAllnoteTime.setText("空空如也");
				}
				break;
			default:
				break;
			}
		};
	};
	private MainActivity mainActivity;

	private View ppwView;
	private ScaleAnimation scale;
	private RotateAnimation rotate;

	private PopupWindow ppw;
	private AnimationSet set;

	private View functionView;

	private AlertDialog alertDialog;
	private ImageView lineShadow;
	private ListView popListView;
	private PopupWindow ppwList;
	private UselessNotesPager useless;

	@Override
	public void initView() {
		rootView = View.inflate(mActivity, R.layout.view_useless, null);
		linearLayoutAllnoteTime = (LinearLayout) rootView
				.findViewById(R.id.f_allnote_time);
		tvAllnoteTime = (TextView) rootView
				.findViewById(R.id.f_allnote_time_text);

		// shadow = (ImageView) rootView.findViewById(R.id.line);
		lineShadow = (ImageView) rootView.findViewById(R.id.line_shadow);

		allnoteListView = (ListView) rootView
				.findViewById(R.id.f_allnote_listview);

		initAnimation();
	}

	private void initAnimation() {
		scale = new ScaleAnimation(1, 0.3f, 1, 0.3f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		scale.setDuration(400);
		scale.setRepeatMode(Animation.REVERSE);

		rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		rotate.setDuration(400);

		set = new AnimationSet(false);
		set.addAnimation(scale);
		set.addAnimation(rotate);

	}

	@Override
	public void initListener() {
		// 取消按钮的点击事件设置

		// 事件阴影条的改变触发

		// item长按触发
		allnoteListView
				.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
							View view, int position, long id) {
						if (!isShowCheckBox) {
							// 设置侧滑菜单不可用
							mainActivity.setDrawerDisable();
							CheckBox cb = (CheckBox) view
									.findViewById(R.id.choose_note);
							cb.setChecked(true);
							checkedNotes.put(position, notes.get(position));
							// 修改标题的菜单栏图片和标题
							imageMenuToggle
									.setImageResource(R.drawable.ic_cancel);
							baseTitle.setText("已选中" + checkedNotes.size() + "个");
							// 设置取消监听
							btnMenuToggle.setOnClickListener(cancel);

							// 设置所有的checkbox显示出来
							isShowCheckBox = true;
							adapter.notifyDataSetChanged();
							return true;
						}
						return false;
					}
				});

		// item点击触发
		allnoteListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 判断是否checkBox已经显示
				if (isShowCheckBox) {
					// 点击选中或取消
					CheckBox cb = (CheckBox) view
							.findViewById(R.id.choose_note);
					if (cb.isChecked()) {
						cb.setChecked(false);
						checkedNotes.remove(position);
					} else {
						checkedNotes.put(position, notes.get(position));
						cb.setChecked(true);
					}
					baseTitle.setText("已选中" + checkedNotes.size() + "个");
					if (checkedNotes.size() == 0) {
						clearCheckedMap();
					}

				} else {
					// 进入笔记页面
					System.out.println("test:进入笔记页面");
					Intent intent = new Intent(mActivity,
							AddNoteEditActivity.class);
					Bundle extras = new Bundle();
					Note note = notes.get(position);
					ArrayList<String> attachment_uri = note.getAttachment_uri();
					ArrayList<String> attachment_type = note
							.getAttachment_type();
					ArrayList<String> ttachment_name = note
							.getAttachment_name();
					extras.putParcelable("note", note);
					intent.putExtras(extras);
					mActivity.startActivityForResult(intent, 200);

				}

			}
		});

		allnoteListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				allnoteListView.getFirstVisiblePosition();
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if (notes != null && notes.size() > 0) {

					Message msg = handler.obtainMessage();
					msg.arg1 = firstVisibleItem;
					msg.what = REFLASH_TITLE_TIME;
					handler.sendMessage(msg);

				}

			}

		});

	}

	public void dissmissPPw() {
		useless.dissmissPPw();
	}

	public void initData() {
		mainActivity = (MainActivity) mActivity;
		// 数据库获取数据资源
		getLocalData();

		useless = (UselessNotesPager) mainActivity.getContentFragment()
				.getPagers().get(2);
		popListView = useless.getPopListView();

		ppwList = useless.getPopuWindow();

		// 当点击popuWindow时调用
		popListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				NoteDao dao = null;
				Set<Integer> keySet = null;
				Iterator<Integer> iterator = null;
				dissmissPPw();
				switch (position) {
				case 0:
					// 选择笔记
					if (!isShowCheckBox && notes.size()>0) {
						isShowCheckBox = true;
						checkedNotes.put(0, notes.get(0));
						handler.sendEmptyMessage(LOAD_LOCAL_DATA_FINISH);
					}
					break;
				case 1:
					// 删除
					/**
					 * 真正删除某条记录
					 */
					if (!isShowCheckBox) {
						isShowCheckBox = true;
						checkedNotes.put(0, notes.get(0));
						handler.sendEmptyMessage(LOAD_LOCAL_DATA_FINISH);
					} else {
						dao = new NoteDao(mActivity);
						keySet = checkedNotes.keySet();
						iterator = keySet.iterator();
						while (iterator.hasNext()) {
							Note note = checkedNotes.get(iterator.next());
							// 直接删除数据
							dao.deleteFromNotesRealy(String.valueOf(note._id));
						}
						clearCheckedMap();
						getLocalData();
						handler.sendEmptyMessage(LOAD_LOCAL_DATA_FINISH);
					}
					break;
				case 2:
					// 清空废纸篓
					dao = new NoteDao(mActivity);
					dao.clearRubbishBox();
					clearCheckedMap();
					getLocalData();
					handler.sendEmptyMessage(LOAD_LOCAL_DATA_FINISH);
					break;
				case 3:
					// 还原笔记
					/**
					 * 判断是否选中item 没选中，就默认选中第一个
					 */
					if (!isShowCheckBox) {
						isShowCheckBox = true;
						checkedNotes.put(0, notes.get(0));
						handler.sendEmptyMessage(LOAD_LOCAL_DATA_FINISH);
					} else {
						dao = new NoteDao(mActivity);

						keySet = checkedNotes.keySet();
						iterator = keySet.iterator();
						while (iterator.hasNext()) {
							Note note = checkedNotes.get(iterator.next());
							/**
							 * 判断笔记的笔记本外键是否存在
							 */
							NoteBookDao bookDao = new NoteBookDao(mActivity);
							NoteBook book = bookDao.getNoteBook(String.valueOf(note.outKeyNoteBook));
							if(book == null){
								
							}
							int firstBookId = bookDao.getFirstBookId();
							// 修改成可用
							dao.updateNotes(note._id, 1, firstBookId);
						}
						clearCheckedMap();
						getLocalData();
					}

					break;
				case 4:
					// 设置
					// 跳转设置页面
					mainActivity.getMenuFragment().setContentPager(5);
					break;

				default:
					break;
				}
			}
		});

	}

	private void getLocalData() {
		// 初始化listView所用到notes集合
		NoteDao dao = new NoteDao(mActivity);
		notes = dao.getUselessNotes(count);
		// 集合派排序
		ListSort(notes);
		// 发消息
		handler.sendEmptyMessage(LOAD_LOCAL_DATA_FINISH);
	}

	/**
	 * 集合排序
	 */
	private void ListSort(List<Note> list) {
		Collections.sort(list);
	};

	class NoteAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return notes.size();
		}

		@Override
		public Object getItem(int position) {
			return notes.get(position);
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
				view = View.inflate(mActivity,
						R.layout.view_item_list_allnotes, null);
				holder = new Holder();
				holder.checkBox = (CheckBox) view
						.findViewById(R.id.choose_note);
				holder.title = (TextView) view.findViewById(R.id.title);
				holder.time = (TextView) view.findViewById(R.id.time);
				holder.body = (TextView) view.findViewById(R.id.body_text);
				holder.ivShow = (ImageView) view.findViewById(R.id.iv_show);
				holder.updated = (ImageView) view.findViewById(R.id.updated);
				view.setTag(holder);
			} else {
				view = convertView;
				holder = (Holder) view.getTag();
			}
			AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT, 180);
			view.setLayoutParams(lp);

			if (isShowCheckBox) {
				holder.checkBox.setVisibility(View.VISIBLE);
				if (checkedNotes.containsKey(position)) {
					holder.checkBox.setChecked(true);
				} else {
					holder.checkBox.setChecked(false);
				}
			} else {
				holder.checkBox.setChecked(false);
				holder.checkBox.setVisibility(View.GONE);
			}
			System.out.println("noteSize:" + notes.size());
			System.out.println("noteSize:position:" + notes.size());
			Note note = notes.get(position);
			if (note.updated == 1) {
				holder.updated.setVisibility(View.VISIBLE);
			} else {
				holder.updated.setVisibility(View.GONE);
			}

			holder.title.setText(note.title);
			// 有图片默认选第一张图片
			if (note.attachment_uri.size() != 0) {
				holder.ivShow.setVisibility(View.VISIBLE);
				if (suffixes.contains(note.attachment_type.get(0))) {
					// 图片之类的
					BitmapUtils util = new BitmapUtils(mActivity);
					util.display(holder.ivShow, note.attachment_uri.get(0));
				} else {
					// 附件
					System.out.println("size:" + note.attachment_uri.size());
					holder.ivShow
							.setImageResource(R.drawable.ic_attachment_file);
				}

			} else {
				holder.ivShow.setVisibility(View.INVISIBLE);
			}
			String showTime = DateTextUtil.getDateString(new Date(
					note.writeTime).getTime());
			if (showTime.length() > 2) {
				holder.body.setText(BLACK_PLACEHOLDER_LONG + note.body);
			} else {
				holder.body.setText(BLACK_PLACEHOLDER_SHORT + note.body);
			}
			holder.time.setText(showTime);
			return view;
		}

	}

	class Holder {

		public ImageView updated;
		public TextView time;
		public TextView title;
		public TextView body;
		public ImageView ivShow;
		public CheckBox checkBox;

	}

	public void setViews(LinearLayout btnMenuToggle2,
			ImageView imageMenuToggle2, TextView baseTitleText,
			ImageView whiteBg) {
		btnMenuToggle = btnMenuToggle2;
		imageMenuToggle = imageMenuToggle2;
		baseTitle = baseTitleText;
		// this.whiteBg = whiteBg;
	}

	/**
	 * 刷新adater
	 */
	public void reflashAdapter() {
		handler.sendEmptyMessage(LOAD_LOCAL_DATA_FINISH);
	}

	/**
	 * 清空map,还原标题
	 */
	public void clearCheckedMap() {
		// 清空map
		checkedNotes.clear();
		// 更改是否显示
		isShowCheckBox = false;
		// 更改图标
		imageMenuToggle.setImageResource(R.drawable.icon_menu_btn);
		// 更改标题
		baseTitle.setText("废纸篓");
		// 刷新adapter
		adapter.notifyDataSetChanged();
		btnMenuToggle.setOnClickListener(openMenu);
		// 设置侧滑菜单可用
		mainActivity.setDrawerEnable();
	}

	// 标题view
	public void setViews(LinearLayout btnMenuToggle2,
			ImageView imageMenuToggle2, TextView baseTitleText) {
		this.btnMenuToggle = btnMenuToggle2;
		this.imageMenuToggle = imageMenuToggle2;
		this.baseTitle = baseTitleText;
	}

}
