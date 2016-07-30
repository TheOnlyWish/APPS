package com.king.loadnote.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.king.loadnote.R;
import com.king.loadnote.dao.NoteBookDao;
import com.king.loadnote.dao.NoteDao;
import com.king.loadnote.domain.Attachment;
import com.king.loadnote.domain.Note;
import com.king.loadnote.domain.NoteBook;
import com.king.loadnote.util.DefaultAlertDialogUtil;
import com.king.loadnote.util.DefaultAlertDialogUtil.MyDialogInterface;
import com.king.loadnote.util.FileUtils;
import com.lidroid.xutils.BitmapUtils;

public class AddNoteTextActicity extends Activity implements OnClickListener {

	private ImageView save;
	private ImageView photo;
	private ImageView attachment;
	private ImageView more;
	private EditText noteTitle;
	private TextView chooseNoteBook;
	private EditText noteBody;
	private ListView listView_attachment;

	private final int FILE_SELECT_CODE = 1;

	private final int REFLESH_ATTACHMENT_ADAPTER = 2;

	private final int REFLASH_NOTEBOOK_ADAPTER = 3;

	private final int INIT_ALERTDIALOG_TITLE_TEXT = 4;
	private AttachmentAdapter adapter;

	// �Ƿ�ѡ���˱���ıʼǱ� Ĭ��û��
	private boolean isChooseNoteBook = false;

	// ��һ���ʼǱ�
	// private NoteBook firstNoteBook;
	// ��ѡ��ıʼǱ�
	private int checkedNoteBook = 0;

	// ��������
	private List<Attachment> list = new ArrayList<Attachment>();
	// ��׺��
	private static List<String> suffixes = new ArrayList<String>();

	// popu�ı�����
	String[] texts = new String[] { "����", "��������..." };
	static {
		suffixes.add(".jpg");
		suffixes.add(".png");
		suffixes.add(".jpeg");
		suffixes.add(".gif");
		suffixes.add(".bmp");
		suffixes.add(".wbmp");
	}
	private DialogAdapter dAdapter;
	private Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case REFLESH_ATTACHMENT_ADAPTER:
				if (adapter == null) {
					adapter = new AttachmentAdapter(new BitmapUtils(
							AddNoteTextActicity.this));
					listView_attachment.setAdapter(adapter);
				} else {
					adapter.notifyDataSetChanged();
				}
				break;
			case REFLASH_NOTEBOOK_ADAPTER:
				if (dAdapter == null) {
					dAdapter = new DialogAdapter();
					listViewFunction.setAdapter(dAdapter);
				} else {
					dAdapter.notifyDataSetChanged();
				}
				break;
			case INIT_ALERTDIALOG_TITLE_TEXT:
				chooseNoteBook.setText(allNoteBooks.get(0).bookName);
				if (dAdapter == null) {
					dAdapter = new DialogAdapter();
					listViewFunction.setAdapter(dAdapter);
				} else {
					dAdapter.notifyDataSetChanged();
				}
				break;
			default:
				break;
			}

		};
	};
	private View ppwView;
	private PopupWindow ppw;
	private ListView ppListView;
	private View headerView;
	private View footerView;
	private View dialogView;
	private TextView title;
	private ListView listViewFunction;

	private boolean isEditing = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		Intent intent = getIntent();

		initView();

		if (intent.getBooleanExtra("edit", false)) {
			note = (Note) intent.getExtras().get("note");
			System.out.println("��ת֮���create���������:" + note.toString());
			isEditing = true;
		}

		initListener();
		initData();

	}

	private void initView() {
		setContentView(R.layout.activity_addnotetext);
		save = (ImageView) findViewById(R.id.save);
		photo = (ImageView) findViewById(R.id.photo);
		attachment = (ImageView) findViewById(R.id.attachment);
		more = (ImageView) findViewById(R.id.more);

		listView_attachment = (ListView) findViewById(R.id.listView_attachment);

		ppwView = View.inflate(this, R.layout.view_pop, null);

		ppListView = (ListView) ppwView.findViewById(R.id.pop_listView);

		headerView = View.inflate(this, R.layout.view_add_header, null);

		footerView = View.inflate(this, R.layout.view_add_footer, null);

		noteTitle = (EditText) headerView.findViewById(R.id.noteTitle);
		chooseNoteBook = (TextView) headerView
				.findViewById(R.id.chooseNoteBook);

		noteBody = (EditText) footerView.findViewById(R.id.noteBody);

		// alertdialog
		dialogView = View.inflate(this, R.layout.view_notebook_ad, null);
		title = (TextView) dialogView.findViewById(R.id.title_ad);
		addNoteBooks = (ImageView) dialogView.findViewById(R.id.addNoteBooks);
		listViewFunction = (ListView) dialogView
				.findViewById(R.id.listView_function);

		functionView = View.inflate(this, R.layout.add_note_book_ad, null);
		name = (EditText) functionView.findViewById(R.id.name);
		cancel = (TextView) functionView.findViewById(R.id.cancel);
		ok = (TextView) functionView.findViewById(R.id.ok);

		View ppView = View.inflate(this, R.layout.view_pop, null);
		ListView popListView = (ListView) ppView
				.findViewById(R.id.pop_listView);

	}

	private void initListener() {
		// ����
		save.setOnClickListener(this);

		// ��Ӹ���
		attachment.setOnClickListener(this);

		// ���ͼƬ
		photo.setOnClickListener(this);

		// ����
		more.setOnClickListener(this);

		// ѡ��ʼǱ�
		chooseNoteBook.setOnClickListener(this);

		addNoteBooks.setOnClickListener(this);

		ppListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> abAdapter, View view, int position,
					long id) {
				switch (position) {
				case 0:
					// ����
					saveDataToDataBase();
					break;
				case 1:
					// ��������...
					finish();
				default:
					break;
				}
			}
		});
		
		
		listViewFunction.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				checkedNoteBook = position;
				handler.sendEmptyMessage(REFLASH_NOTEBOOK_ADAPTER);
				chooseNoteBook.setText(allNoteBooks.get(position).bookName);
				dialog.dismiss();
			}
		});

		listView_attachment
				.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
							View view, final int position, long id) {

						if (dUtil == null) {
							dUtil = DefaultAlertDialogUtil
									.getInstant(AddNoteTextActicity.this);
						}
						Attachment att = list.get(position-1);
						dUtil.show(att.fileName, "�Ƿ��Ƴ�����", "�Ƴ�", "ȡ��");
						dUtil.setMyDialogInterface(new MyDialogInterface() {
							@Override
							public void clickPositiveButton() {
								list.remove(position);
								handler.sendEmptyMessage(REFLESH_ATTACHMENT_ADAPTER);
							}

							@Override
							public void clickNegativeButton() {
								// ��
							}
						});
						
						if(id == R.id.more){
							return false;
						}
						
						return true;
					}
				});

	}

	/**
	 * ��ʼ��note�����еļ���
	 * 
	 * @param note
	 */
	private void ParseAndInitList(Note note) {
		if (uris != null) {
			for (int i = 0; i < uris.length; i++) {
				note.attachment_uri.add(uris[i]);
				note.attachment_name.add(names[i]);
				note.attachment_type.add(types[i]);
			}
		}
	}

	private void ParseNote(Note note) {

		String uri = note.getAttachment_uri_str();
		String type = note.getAttachment_type_str();
		String name = note.getAttachment_name_str();

		if (!TextUtils.isEmpty(uri) && !TextUtils.isEmpty(type)
				&& !TextUtils.isEmpty(name)) {
			uris = uri.split(",");
			types = type.split(",");
			names = name.split(",");

			if (!TextUtils.isEmpty(uri.trim()) && uris.length <= 0) {
				System.out.println("ֻ��һ������");
				uris = new String[] { uri };
				types = new String[] { type };
				names = new String[] { name };

			}

			System.out.println("��ת֮��test:uris:" + uris.length + "|" + uris[0]);
			System.out.println("��ת֮��test:types:" + types.length + "|"
					+ types[0]);
			System.out.println("��ת֮��test:names:" + names.length + "|"
					+ names[0]);

		}

	}

	private void initAttachmentList() {
		if (uris != null && uris.length > 0) {
			// ��ȡ�ļ�
			for (int i = 0; i < uris.length; i++) {
				File file = new File(uris[i]);
				Attachment attachment = new Attachment();
				attachment.fileName = names[i];
				attachment.filePath = uris[i];
				attachment.size = file.length();
				attachment.type = types[i];
				boolean isImage = suffixes.contains(attachment.fileName
						.substring(attachment.fileName.lastIndexOf(".")));
				attachment.isImage = isImage;
				list.add(attachment);
			}
		}
	}

	private void initData() {
		if (isEditing) {
			noteTitle.setText(note.title);
			isEditingNote_book = note.outKeyNoteBook;
			noteBody.setText(note.body);
			// ����
			ParseNote(note);
			ParseAndInitList(note);
			initAttachmentList();

			System.out.println("��ת֮���initdata���������:" + note.toString());
		}

		adapter = new AttachmentAdapter(new BitmapUtils(this));
		// ���headerView
		listView_attachment.addHeaderView(headerView);
		// ���footerView
		listView_attachment.addFooterView(footerView);

		listView_attachment.setAdapter(adapter);

		PPAdapter pAdapter = new PPAdapter();
		ppListView.setAdapter(pAdapter);

		// init dialog
		title.setText("ѡ��ʼǱ�");
		// ��ѯ���бʼǱ�
		getAllNoteBooks();

		/**
		 * ��ʾ
		 */

	}

	private List<NoteBook> allNoteBooks;
	private ImageView addNoteBooks;
	private AlertDialog dialog;
	private AlertDialog.Builder builder;
	private Note note;
	private int isEditingNote_book;
	private String[] uris;
	private String[] types;
	private String[] names;
	private View functionView;
	private EditText name;
	private TextView cancel;
	private TextView ok;

	private void getAllNoteBooks() {
		new Thread() {
			@Override
			public void run() {
				NoteBookDao dao = new NoteBookDao(AddNoteTextActicity.this);
				// ��ȡ���бʼǱ�
				allNoteBooks = dao.getAllNoteBooks();

				// ��ʼ���ʼǱ�����chooseNoteBook
				if (allNoteBooks.size() > 0 && allNoteBooks != null) {

					if (isEditing) {
						for (int i = 0; i < allNoteBooks.size(); i++) {
							if (allNoteBooks.get(i)._id == isEditingNote_book) {
								checkedNoteBook = i;
								break;
							}
						}
					}

					handler.sendEmptyMessage(INIT_ALERTDIALOG_TITLE_TEXT);
					isChooseNoteBook = true;
				} else {
					chooseNoteBook.setText("δ�����ʼǱ�");
					isChooseNoteBook = false;
				}

			}
		}.start();
	}

	private AlertDialog.Builder nbuilder;
	private AlertDialog ndialog;

	private DefaultAlertDialogUtil dUtil;

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.save:
			// ѡ����һ���ʼǱ�
			saveDataToDataBase();
			break;
		case R.id.attachment:
			showFileChooser();
			break;
		case R.id.photo:
			showFileChooser();
			break;
		case R.id.more:
			if (ppw == null) {
				ppw = new PopupWindow(ppwView, 400, LayoutParams.WRAP_CONTENT,
						true);
				ppw.setBackgroundDrawable(this.getResources().getDrawable(
						R.drawable.popup_background));
				ppw.setOutsideTouchable(true);

				// �����Զ���Ķ�����ʽ
				ppw.setAnimationStyle(R.style.showPopupAnimation);
			}

			ppw.showAtLocation(
					View.inflate(this, R.layout.activity_addnotetext, null),
					Gravity.RIGHT + Gravity.TOP, 5, 35);

			break;
		case R.id.chooseNoteBook:

			if (builder == null) {
				// ����alertDialog
				builder = new AlertDialog.Builder(new ContextThemeWrapper(this,
						R.style.Theme_Transparent));
				dialog = builder.create();
			}
			// ���ò����ļ�
			dialog.setView(dialogView);
			dialog.show();

			break;
		case R.id.addNoteBooks:
			// ��ת���ʼǱ�����ҳ

			name.setText("");

			if (nbuilder == null) {
				nbuilder = new AlertDialog.Builder(new ContextThemeWrapper(
						this, R.style.Theme_Transparent));
				ndialog = builder.create();
			}
			// ���ò����ļ�
			ndialog.setView(functionView);
			ndialog.show();

			// ad ȡ��
			cancel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					if (ndialog != null) {
						ndialog.dismiss();
					}
				}
			});

			// ad ȷ��
			ok.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					String value = name.getText().toString();
					if (!TextUtils.isEmpty(value.trim())) {
						NoteBookDao dao = new NoteBookDao(
								AddNoteTextActicity.this);
						NoteBook book = new NoteBook();
						book.is_usable = 1;
						book.bookName = value;
						dao.addToNoteBook(book);

						getAllNoteBooks();
					}
					ndialog.dismiss();

					dialog.dismiss();
				}
			});

		}
	}

	/**
	 * ��ʾpop
	 */
	public void showAttachmentPop() {

	}

	/**
	 * �������ݵ����ݿ�
	 */
	private void saveDataToDataBase() {
		// �����ʼǶ���
		Note note = null;
		if (isEditing) {
			note = this.note;
		} else {
			note = new Note();
		}
		note.title = noteTitle.getText().toString();
		note.body = noteBody.getText().toString();

		// ��ȡѡ�еıʼǱ�����
		if (allNoteBooks.size() > 0) {
			NoteBook book = allNoteBooks.get(checkedNoteBook);
			note.outKeyNoteBook = book._id;
			note.is_usable = 1;
			note.updated = 1;
			note.writeTime = new Date().getTime();
			ArrayList<String> uris = new ArrayList<String>();
			ArrayList<String> types = new ArrayList<String>();
			ArrayList<String> names = new ArrayList<String>();
			for (Attachment att : list) {
				uris.add(att.filePath);
				types.add(att.type);
				names.add(att.fileName);
			}
			note.attachment_name = names;
			note.attachment_type = types;
			note.attachment_uri = uris;
		} else {
			chooseNoteBook.setText("δ�����ʼǱ�");
		}

		// У��
		if (TextUtils.isEmpty(note.title.trim())) {
			Toast.makeText(this, "����Ϊ��", 0).show();
			return;
		}
		if (!isChooseNoteBook) {
			Toast.makeText(this, "δ�����ʼǱ�", 0).show();
			return;
		}
		boolean flag = false;
		NoteDao dao = new NoteDao(this);

		if (isEditing) {
			flag = dao.updateNotes(note);
		} else {
			flag = dao.addToNotes(note);
		}
		if (flag) {
			Toast.makeText(this, "�ʼǱ���ɹ�", 0).show();
			// ���͹㲥ʹAllNotesContentPagerˢ��listView
			sendBroadcast(new Intent("REFLASH_NOTE_LIST"));
		} else {
			Toast.makeText(this, "�ʼǱ���ʧ��", 0).show();
		}
		finish();
	}

	/** �����ļ�ѡ�������ѡ���ļ� **/
	private void showFileChooser() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("*/*");
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		try {
			startActivityForResult(Intent.createChooser(intent, "��ѡ��һ��Ҫ�ϴ����ļ�"),
					FILE_SELECT_CODE);
		} catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(this, "�밲װ�ļ�������", Toast.LENGTH_SHORT).show();
		}
	}

	/** ���ݷ���ѡ����ļ����������ϴ����� **/
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			Uri uri = data.getData();
			String url;
			try {
				// ��ȡ�ļ�
				String path = FileUtils.getPath(this, uri);
				String fileName = path.substring(path.lastIndexOf("/") + 1);
				File file = new File(path);
				Attachment attachment = new Attachment();
				attachment.fileName = fileName;
				attachment.filePath = path;
				attachment.size = file.length();
				attachment.type = fileName.substring(fileName.lastIndexOf("."));
				boolean isImage = suffixes.contains(attachment.type);
				attachment.isImage = isImage;
				list.add(attachment);
				// ����ˢ��adapter ����Ϣ
				handler.sendEmptyMessage(REFLESH_ATTACHMENT_ADAPTER);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	class AttachmentAdapter extends BaseAdapter {

		private BitmapUtils util;

		public AttachmentAdapter(BitmapUtils util) {
			this.util = util;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View view = null;
			Holder holder = null;
			if (convertView == null) {
				view = View.inflate(AddNoteTextActicity.this,
						R.layout.view_attachment_item, null);
				holder = new Holder();
				holder.file = (LinearLayout) view.findViewById(R.id.file);
				holder.fileName = (TextView) view.findViewById(R.id.fileName);
				holder.fileSize = (TextView) view.findViewById(R.id.fileSize);
				holder.more = (ImageView) view.findViewById(R.id.more);
				holder.llImage = (LinearLayout) view
						.findViewById(R.id.ll_image);
				holder.image = (ImageView) view.findViewById(R.id.image);
				view.setTag(holder);
			} else {
				view = convertView;
				holder = (Holder) view.getTag();

			}
			final Attachment atta = list.get(position);
			if (atta.isImage) {
				holder.file.setVisibility(View.GONE);
				holder.llImage.setVisibility(View.VISIBLE);

				util.display(holder.image, atta.filePath);
			} else {
				holder.llImage.setVisibility(View.GONE);
				holder.file.setVisibility(View.VISIBLE);

				holder.fileName.setText(atta.fileName);
				long size = atta.size;
				double countSize = 0;
				String suffix = null;
				if (size >= 1024) {
					// K
					countSize = size / 1024.0;
					suffix = "KB";
				} else if (size >= 1024 * 1024) {
					// M
					countSize = size / (1024 * 1024.0);
					suffix = "MB";
				} else if (size >= 1024 * 1024 * 1024) {
					// G
					countSize = size / (1024 * 1024 * 1024.0);
					suffix = "GB";
				} else {
					countSize = size;
					suffix = "Bytes";
				}
				holder.fileSize.setText(countSize + " " + suffix);

				holder.more.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						System.out.println("position:" + position);
						final Attachment att = list.get(position);
						// ����
						if (dUtil == null) {
							dUtil = DefaultAlertDialogUtil
									.getInstant(AddNoteTextActicity.this);
						}
						dUtil.show(atta.fileName, "�Ƿ��Ƴ�����", "�Ƴ�", "ȡ��");
						dUtil.setMyDialogInterface(new MyDialogInterface() {
							@Override
							public void clickPositiveButton() {
								list.remove(att);
								handler.sendEmptyMessage(REFLESH_ATTACHMENT_ADAPTER);
							}

							@Override
							public void clickNegativeButton() {
								// ��
							}
						});
					}
				});

			}
			return view;
		}

	}

	class Holder {

		public ImageView image;
		public LinearLayout llImage;
		public android.widget.TextView fileSize;
		public ImageView more;
		public android.widget.TextView fileName;
		public LinearLayout file;

	}

	class PPAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return texts.length;
		}

		@Override
		public Object getItem(int position) {
			return texts[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			PPWHolder holder = null;
			if (convertView == null) {
				holder = new PPWHolder();
				view = View.inflate(AddNoteTextActicity.this,
						R.layout.view_pop_item_list, null);
				holder.text = (TextView) view.findViewById(R.id.pop_text);
				view.setTag(holder);
			} else {
				view = convertView;
				holder = (PPWHolder) view.getTag();
			}
			holder.text.setText(texts[position]);
			return view;
		}

	}

	class PPWHolder {

		public TextView text;

	}

	class DialogAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return allNoteBooks.size();
		}

		@Override
		public Object getItem(int position) {
			return allNoteBooks.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			DialogHolder holder = null;
			if (convertView == null) {
				holder = new DialogHolder();
				view = View.inflate(AddNoteTextActicity.this,
						R.layout.view_notebook_item, null);
				holder.text = (TextView) view.findViewById(R.id.noteBookName);
				view.setTag(holder);
			} else {
				view = convertView;
				holder = (DialogHolder) view.getTag();
			}
			NoteBook noteBook = allNoteBooks.get(position);
			holder.text.setText(noteBook.bookName);
			// �ж��Ƿ����
			if (checkedNoteBook == position) {
				holder.text.setEnabled(false);
			} else {
				holder.text.setEnabled(true);
			}
			return view;
		}

	}

	class DialogHolder {

		public TextView text;

	}

}
