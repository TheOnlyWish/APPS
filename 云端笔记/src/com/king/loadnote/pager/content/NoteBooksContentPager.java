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

	// �������ݶ�ȡ�ɹ�
	private final int LOAD_LOCAL_DATA_FINISH = 1;

	// ˢ������adapter����Ϊnull,��Ϊ���ʼ��
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
				// ��
				
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

	private String[] items = new String[] { "�������ʼǱ�", "�����±ʼǱ�", "ɾ��" };
	private View noteAd;
	private ListView listView;
	private AlertDialogAdapter adAdapter;

	private OnDismissListener dismissListener = new OnDismissListener() {
		@Override
		public void onDismiss(DialogInterface dialog) {
			// ������view
			((ViewGroup) noteAd.getParent()).removeView(noteAd);
		}
	};

	public void initListener() {

		/**
		 * ��noteDialog��item���ʱ����
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
					// �������ʼǱ�
					isRename = true;
					String oldName = books.get(position).bookName;
					renameNoteBook(oldName);
					renameBook = books.get(position)._id;
					break;
				case 1:
					// һ�����±ʼǱ�
					isMove = true;
					// ����
					addNoteBook();
					break;
				case 2:
					// ɾ��
					/**
					 * �޸ĸñʼǱ��µ����бʼǵ�is_usable = 0�� out_key_notebook = -1 ע�⣺
					 * ��ԭ�ʼ�ʱ�����Ĭ�ϻ�ԭ����һ���ʼ��� ɾ���ʼǱ�
					 */
					dao = new NoteBookDao(mActivity);
					noteDao = new NoteDao(mActivity);
					// ����ıʼǱ�
					int idClicked = books.get(clickBook)._id;
					List<Integer> list = noteDao
							.getNotesByOutKeyBookId(idClicked);
					for (Integer _id : list) {
						noteDao.deleteFromNotesVirtual(String.valueOf(_id));
					}
					// ɾ���ʼǱ�
					dao.deleteFromNoteBookByIdRealy(String.valueOf(idClicked));
					// ����ˢ��books
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
						// ���õ���������
						clickBook = position;

						// ����
						// ����alertDialog
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

		// �ʼǱ����
		listViewNoteBooks.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// ����һ��activity ����ҳ
				// ���õ���������
				clickBook = position;
				/**
				 * 1. ��ת��һ���ʼǼ��ϵ�activity 2. Я���ʼǱ���id
				 */
				Intent intent = new Intent(mActivity, NoteBookActivity.class);
				intent.putExtra("bookIndex", books.get(position)._id);
				intent.putExtra("bookName", books.get(position).bookName);
				mActivity.startActivity(intent);

			}
		});

		// ��ӱʼǱ�
		functions.getChildAt(0).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				addNoteBook();

			}

		});

		// ad ȡ��
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				System.out.println("value:cancel");

				if (dialog != null) {
					dialog.dismiss();
				}

			}
		});

		// ad ȷ��
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
		 * ��ȡpager����
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
					// ͬ��
					// �ƶ�ͬ��
					System.out.println("test:�ƶ�ͬ��");
					PostOrReceive();

					break;
				case 1:
					// ����
					// ��ת����ҳ��
					mainActivity.getMenuFragment().setContentPager(3);

					break;

				default:
					break;
				}

			}
		});

	}

	// �ƶ�ͬ��
	private void PostOrReceive() {

		SynchronDataUtil syncUtil = SynchronDataUtil.getInstant(mActivity);
		syncUtil.updateData();

		syncUtil.setOnUpdateListener(new SetOnUpdateListener() {

			@Override
			public void onSuccess(ResponseInfo<String> result,
					List<Note> listNote) {

				// �ɹ���
				NoteDao dao = new NoteDao(mActivity);
				// �޸�list����
				for (Note note : listNote) {
					// д�����ݿ�,�޸�updated��ֵ
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
					// д�����ݿ�,�޸ķ���˴�����server_note_id��ֵ
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
		// ��ʼ��listView���õ�notes����
		NoteDao dao = new NoteDao(mActivity);
		notes = dao.getReorderNotes(dao.getCount());
		// ����������
		ListSort(notes);

		System.out.println("test:getLocalData:" + notes.size());

		// ����Ϣ
		handler.sendEmptyMessage(LOAD_LOCAL_DATA_FINISH);
	}

	/**
	 * ��������
	 */
	private void ListSort(List<Note> list) {
		Collections.sort(list);
	};

	public void moveTonewBook() {
		/**
		 * ��ѯ�մ����ıʼǱ���id
		 */
		NoteBookDao dao = new NoteBookDao(mActivity);
		NoteDao noteDao = new NoteDao(mActivity);

		NoteBook newBook = dao.getLastNoteBook();
		System.out.println("test:�±ʼǱ���id:" + newBook._id);
		int idNew = newBook._id;
		// ����ıʼǱ�
		int idOld = books.get(clickBook)._id;
		System.out
				.println("test:����ıʼǱ���id:" + idOld + "��position:" + clickBook);
		System.out.println("test:���books��" + books);
		/**
		 * �޸ĸñʼǱ��µıʼǵıʼǱ���� ����ñʼǱ��µıʼ���������0�����޸� �޸�����Ϊ��updated�� outkeyBookid
		 */
		// ��ѯ���ɱʼǵ�id����
		List<Integer> listIds = noteDao.getNotesByOutKeyBookId(idOld);
		if (listIds.size() > 0) {
			for (int i = 0; i < listIds.size(); i++) {
				noteDao.updateNoteOutkeyBookIdById(listIds.get(i), idNew);
			}
		}

		// ����ˢ��books
		LoadData();
	}

	private AlertDialog.Builder funBuilder;
	private AlertDialog dialog;
	private ListView popListView;
	private MainActivity mainActivity;

	/**
	 * �������ʼǱ�
	 */
	private void renameNoteBook(String oldName) {
		name.setText(oldName);
		if (funBuilder == null) {
			funBuilder = new AlertDialog.Builder(new ContextThemeWrapper(
					mActivity, R.style.Theme_Transparent));
			dialog = funBuilder.create();
		}
		// ���ò����ļ�
		dialog.setView(functionView);
		dialog.show();
	}

	/**
	 * ��ӱʼǱ�
	 */
	private void addNoteBook() {
		name.setText("");
		if (funBuilder == null) {
			funBuilder = new AlertDialog.Builder(new ContextThemeWrapper(
					mActivity, R.style.Theme_Transparent));
			dialog = funBuilder.create();
		}
		// ���ò����ļ�
		dialog.setView(functionView);
		dialog.show();
	}

	public void initData() {
		// ��ȡ���ݴ����ݿ�
		LoadData();
		// ��ʼ���ʼǱ��ĵ���listView
		adAdapter = new AlertDialogAdapter();
		listView.setAdapter(adAdapter);
	}

	/**
	 * �����ݿ��ȡlist����
	 */
	private void LoadData() {
		new Thread() {
			public void run() {
				NoteBookDao dao = new NoteBookDao(mActivity);
				books = dao.getAllNoteBooks();
				booksCount = new ArrayList<Integer>();
				// ��ȡÿ���ʼǱ��ıʼ���
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
			holder.bookCount.setText(book.noteCount + " �ʼ�");
			holder.more.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// ���õ���������
					clickBook = position;
					// ����
					if (builder == null) {
						// ����alertDialog
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
