package com.king.loadnote.pager.content;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Handler;
import android.sax.StartElementListener;
import android.text.TextUtils;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.king.loadnote.R;
import com.king.loadnote.activity.MainActivity;
import com.king.loadnote.activity.NoteBookActivity;
import com.king.loadnote.dao.NoteBookDao;
import com.king.loadnote.dao.NoteDao;
import com.king.loadnote.domain.Ids;
import com.king.loadnote.domain.Note;
import com.king.loadnote.domain.NoteBook;
import com.king.loadnote.pager.BaseCPager;
import com.king.loadnote.pager.content.AllNotesContentPager.NoteAdapter;
import com.king.loadnote.pager.impl.NoteBooksPager;
import com.king.loadnote.util.DateTextUtil;
import com.king.loadnote.util.SynchronDataUtil;
import com.king.loadnote.util.SynchronDataUtil.SetOnUpdateListener;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;

public class NoteBooksContentPager extends BaseCPager {

	private ListView listViewNoteBooks;
	private List<NoteBook> books;
	private List<Integer> booksCount;

	// 本地数据读取成功
	private final int LOAD_LOCAL_DATA_FINISH = 1;

	// 刷新内容adapter，若为null,则为其初始化
	private final int REFLASH_CONTENT_ADAPTER = 2;

	private Handler handler = new Handler() {
		private ContentAdapter contentAdapter;

		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case REFLASH_CONTENT_ADAPTER:
				if (contentAdapter == null) {
					contentAdapter = new ContentAdapter();
					listViewNoteBooks.setAdapter(contentAdapter);
					System.out.println("test:handler");
				} else {
					contentAdapter.notifyDataSetChanged();
				}
				break;
			case LOAD_LOCAL_DATA_FINISH:
				// 略
				
				break;
			default:
				break;
			}

		};
	};
	private LinearLayout functions;
	private View functionView;
	private TextView cancel;
	private EditText name;
	private TextView ok;

	private boolean isRename = false;
	private int renameBook = -1;
	private int clickBook = -1;
	private boolean isMove = false;

	public NoteBooksContentPager(Activity activity) {
		super(activity);
	}

	@Override
	public void initView() {
		rootView = View.inflate(mActivity, R.layout.view_notebook, null);
		listViewNoteBooks = (ListView) rootView
				.findViewById(R.id.listViewNoteBooks);

		MainActivity mainActivity = (MainActivity) mActivity;
		NoteBooksPager noteBooksPager = (NoteBooksPager) mainActivity
				.getContentFragment().getPagers().get(1);
		functions = noteBooksPager.getFunctions();

		functionView = View.inflate(mActivity, R.layout.add_note_book_ad, null);
		name = (EditText) functionView.findViewById(R.id.name);
		cancel = (TextView) functionView.findViewById(R.id.cancel);
		ok = (TextView) functionView.findViewById(R.id.ok);

		noteAd = View.inflate(mActivity, R.layout.view_note_ad, null);
		listView = (ListView) noteAd.findViewById(R.id.book_listView_function);

	}

	private AlertDialog.Builder builder;
	private AlertDialog alertDialog;

	private String[] items = new String[] { "重命名笔记本", "移至新笔记本", "删除" };
	private View noteAd;
	private ListView listView;
	private AlertDialogAdapter adAdapter;

	private OnDismissListener dismissListener = new OnDismissListener() {
		@Override
		public void onDismiss(DialogInterface dialog) {
			// 清理子view
			((ViewGroup) noteAd.getParent()).removeView(noteAd);
		}
	};

	public void initListener() {

		/**
		 * 当noteDialog的item点击时调用
		 * 
		 * @param dialog
		 * @param which
		 */
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view,
					int position, long id) {
				alertDialog.dismiss();
				NoteBookDao dao = null;
				NoteDao noteDao = null;
				switch (position) {
				case 0:
					// 重命名笔记本
					isRename = true;
					String oldName = books.get(position).bookName;
					renameNoteBook(oldName);
					renameBook = books.get(position)._id;
					break;
				case 1:
					// 一移至新笔记本
					isMove = true;
					// 弹框
					addNoteBook();
					break;
				case 2:
					// 删除
					/**
					 * 修改该笔记本下的所有笔记的is_usable = 0， out_key_notebook = -1 注意：
					 * 还原笔记时，添加默认还原到第一本笔记中 删除笔记本
					 */
					dao = new NoteBookDao(mActivity);
					noteDao = new NoteDao(mActivity);
					// 点击的笔记本
					int idClicked = books.get(clickBook)._id;
					List<Integer> list = noteDao
							.getNotesByOutKeyBookId(idClicked);
					for (Integer _id : list) {
						noteDao.deleteFromNotesVirtual(String.valueOf(_id));
					}
					// 删除笔记本
					dao.deleteFromNoteBookByIdRealy(String.valueOf(idClicked));
					// 重新刷新books
					LoadData();
					break;

				default:
					break;
				}

			}
		});

		listViewNoteBooks
				.setOnItemLongClickListener(new OnItemLongClickListener() {
					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
							View view, int position, long id) {
						// 设置点击项的索引
						clickBook = position;

						// 弹框
						// 创建alertDialog
						builder = new AlertDialog.Builder(
								new ContextThemeWrapper(mActivity,
										R.style.Theme_Transparent));
						builder.setView(noteAd);
						alertDialog = builder.create();
						alertDialog.show();

						alertDialog.setOnDismissListener(dismissListener);

						return true;
					}
				});

		// 笔记本点击
		listViewNoteBooks.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 进入一个activity 详情页
				// 设置点击项的索引
				clickBook = position;
				/**
				 * 1. 跳转到一个笔记集合的activity 2. 携带笔记本的id
				 */
				Intent intent = new Intent(mActivity, NoteBookActivity.class);
				intent.putExtra("bookIndex", books.get(position)._id);
				intent.putExtra("bookName", books.get(position).bookName);
				mActivity.startActivity(intent);

			}
		});

		// 添加笔记本
		functions.getChildAt(0).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				addNoteBook();

			}

		});

		// ad 取消
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				System.out.println("value:cancel");

				if (dialog != null) {
					dialog.dismiss();
				}

			}
		});

		// ad 确定
		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String value = name.getText().toString();
				System.out.println("value:" + value);
				if (isRename) {
					isRename = false;
					NoteBookDao dao = new NoteBookDao(mActivity);
					dao.updateNoteBook(String.valueOf(renameBook), value);
					LoadData();
				} else {
					if (!TextUtils.isEmpty(value.trim())) {
						NoteBookDao dao = new NoteBookDao(mActivity);
						NoteBook book = new NoteBook();
						book.is_usable = 1;
						book.bookName = value;
						dao.addToNoteBook(book);

						if (isMove) {
							moveTonewBook();
							isMove = false;
						}

						LoadData();
					}
				}
				dialog.dismiss();
			}
		});

		/**
		 * 获取pager对象
		 */
		mainActivity = (MainActivity) mActivity;

		NoteBooksPager noteBooksPager = (NoteBooksPager) mainActivity
				.getContentFragment().getPagers().get(1);
		popListView = noteBooksPager.getPopListView();
		
		popListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View parent,
					int position, long id) {
				switch (position) {
				case 0:
					// 同步
					// 云端同步
					System.out.println("test:云端同步");
					PostOrReceive();

					break;
				case 1:
					// 设置
					// 跳转设置页面
					mainActivity.getMenuFragment().setContentPager(3);

					break;

				default:
					break;
				}

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
				getLocalData();

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

	private List<Note> notes;

	private void getLocalData() {
		// 初始化listView所用到notes集合
		NoteDao dao = new NoteDao(mActivity);
		notes = dao.getReorderNotes(dao.getCount());
		// 集合派排序
		ListSort(notes);

		System.out.println("test:getLocalData:" + notes.size());

		// 发消息
		handler.sendEmptyMessage(LOAD_LOCAL_DATA_FINISH);
	}

	/**
	 * 集合排序
	 */
	private void ListSort(List<Note> list) {
		Collections.sort(list);
	};

	public void moveTonewBook() {
		/**
		 * 查询刚创建的笔记本的id
		 */
		NoteBookDao dao = new NoteBookDao(mActivity);
		NoteDao noteDao = new NoteDao(mActivity);

		NoteBook newBook = dao.getLastNoteBook();
		System.out.println("test:新笔记本的id:" + newBook._id);
		int idNew = newBook._id;
		// 点击的笔记本
		int idOld = books.get(clickBook)._id;
		System.out
				.println("test:点击的笔记本的id:" + idOld + "：position:" + clickBook);
		System.out.println("test:输出books：" + books);
		/**
		 * 修改该笔记本下的笔记的笔记本外键 如果该笔记本下的笔记数量大于0，就修改 修改内容为：updated， outkeyBookid
		 */
		// 查询到旧笔记的id集合
		List<Integer> listIds = noteDao.getNotesByOutKeyBookId(idOld);
		if (listIds.size() > 0) {
			for (int i = 0; i < listIds.size(); i++) {
				noteDao.updateNoteOutkeyBookIdById(listIds.get(i), idNew);
			}
		}

		// 重新刷新books
		LoadData();
	}

	private AlertDialog.Builder funBuilder;
	private AlertDialog dialog;
	private ListView popListView;
	private MainActivity mainActivity;

	/**
	 * 重命名笔记本
	 */
	private void renameNoteBook(String oldName) {
		name.setText(oldName);
		if (funBuilder == null) {
			funBuilder = new AlertDialog.Builder(new ContextThemeWrapper(
					mActivity, R.style.Theme_Transparent));
			dialog = funBuilder.create();
		}
		// 设置布局文件
		dialog.setView(functionView);
		dialog.show();
	}

	/**
	 * 添加笔记本
	 */
	private void addNoteBook() {
		name.setText("");
		if (funBuilder == null) {
			funBuilder = new AlertDialog.Builder(new ContextThemeWrapper(
					mActivity, R.style.Theme_Transparent));
			dialog = funBuilder.create();
		}
		// 设置布局文件
		dialog.setView(functionView);
		dialog.show();
	}

	public void initData() {
		// 获取数据从数据库
		LoadData();
		// 初始化笔记本的弹框listView
		adAdapter = new AlertDialogAdapter();
		listView.setAdapter(adAdapter);
	}

	/**
	 * 从数据库获取list集合
	 */
	private void LoadData() {
		new Thread() {
			public void run() {
				NoteBookDao dao = new NoteBookDao(mActivity);
				books = dao.getAllNoteBooks();
				booksCount = new ArrayList<Integer>();
				// 获取每本笔记本的笔记数
				NoteDao noteDao = new NoteDao(mActivity);
				for (int i = 0; i < books.size(); i++) {
					books.get(i).noteCount = noteDao
							.getNotesCount(books.get(i)._id);
				}
				handler.sendEmptyMessage(REFLASH_CONTENT_ADAPTER);
			};
		}.start();
	}

	class ContentAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return books.size();
		}

		@Override
		public Object getItem(int position) {
			return books.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View view = null;
			Holder holder = null;
			if (convertView == null) {
				view = View.inflate(mActivity,
						R.layout.view_notebook_list_item, null);
				holder = new Holder();
				holder.bookName = (TextView) view.findViewById(R.id.bookName);
				holder.bookCount = (TextView) view.findViewById(R.id.bookCount);
				holder.more = (ImageView) view.findViewById(R.id.more);
				view.setTag(holder);
			} else {
				view = convertView;
				holder = (Holder) view.getTag();
			}
			NoteBook book = books.get(position);
			holder.bookName.setText(book.bookName);
			holder.bookCount.setText(book.noteCount + " 笔记");
			holder.more.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// 设置点击项的索引
					clickBook = position;
					// 弹框
					if (builder == null) {
						// 创建alertDialog
						builder = new AlertDialog.Builder(
								new ContextThemeWrapper(mActivity,
										R.style.Theme_Transparent));
						alertDialog = builder.create();
					}
					alertDialog = builder.create();
					alertDialog.setView(noteAd);
					alertDialog.show();

					alertDialog.setOnDismissListener(dismissListener);
				}
			});
			return view;
		}

	}

	class Holder {

		public ImageView more;
		public TextView bookCount;
		public TextView bookName;

	}

	class AlertDialogAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return items.length;
		}

		@Override
		public Object getItem(int position) {
			return items[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View view = null;
			DHolder holder = null;
			if (convertView == null) {
				view = View
						.inflate(mActivity, R.layout.view_item_note_ad, null);
				holder = new DHolder();
				holder.item = (TextView) view.findViewById(R.id.item);
				view.setTag(holder);
			} else {
				view = convertView;
				holder = (DHolder) view.getTag();
			}
			holder.item.setText(items[position]);

			return view;
		}

	}

	class DHolder {

		public TextView item;

	}

}
