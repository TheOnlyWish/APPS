package com.king.loadnote.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.ContextThemeWrapper;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.Animation.AnimationListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.king.loadnote.R;
import com.king.loadnote.dao.NoteBookDao;
import com.king.loadnote.domain.Attachment;
import com.king.loadnote.domain.Note;
import com.king.loadnote.domain.NoteBook;
import com.lidroid.xutils.BitmapUtils;

/**
 * 编辑添加笔记页面
 * 
 * @author Administrator
 * 
 */
public class AddNoteEditActivity extends Activity implements OnClickListener {

	private ImageView back;
	private ImageView share;
	private ImageView more;
	private ListView listViewAttachment;
	private RelativeLayout editCircle;
	private View header;
	private View footer;
	private EditText noteTitle;
	private TextView chooseNoteBook;
	private EditText body;

	private View dialogView;
	private TextView title;
	private ListView listViewFunction;
	private ImageView addNoteBooks;

	private final int FILE_SELECT_CODE = 1;

	private final int REFLESH_ATTACHMENT_ADAPTER = 2;

	private final int REFLASH_NOTEBOOK_ADAPTER = 3;

	private final int INIT_ALERTDIALOG_TITLE_TEXT = 4;

	// 是否选择了保存的笔记本 默认没有
	private boolean isChooseNoteBook = false;

	// 被选择的笔记本
	private int checkedNoteBook = 0;

	// 附件数据
	private String[] uris;
	private String[] types;
	private String[] names;

	private static List<String> suffixes = new ArrayList<String>();

	private DialogAdapter dAdapter;

	static {
		suffixes.add(".jpg");
		suffixes.add(".png");
		suffixes.add(".jpeg");
		suffixes.add(".gif");
		suffixes.add(".bmp");
		suffixes.add(".wbmp");
	}

	private Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_addnoteedit);

		intent = getIntent();
		// 被点击的note对象
		note = (Note) intent.getExtras().get("note");
		// 为向list集合赋值
		System.out.println("note:" + note.toString());

		initView();
		initListener();
		initData();

	}

	private void initView() {

		back = (ImageView) findViewById(R.id.back_image);
		share = (ImageView) findViewById(R.id.share);
		more = (ImageView) findViewById(R.id.more);
		
		// 初始化不显示
		share.setVisibility(View.GONE);
		more.setVisibility(View.GONE);
		
		

		listViewAttachment = (ListView) findViewById(R.id.listView_attachment);
		editCircle = (RelativeLayout) findViewById(R.id.edit_note_circle);

		header = View.inflate(this, R.layout.view_add_header, null);
		footer = View.inflate(this, R.layout.view_add_footer, null);

		noteTitle = (EditText) header.findViewById(R.id.noteTitle);
		chooseNoteBook = (TextView) header.findViewById(R.id.chooseNoteBook);

		body = (EditText) footer.findViewById(R.id.noteBody);

		// alertDialog view
		dialogView = View.inflate(this, R.layout.view_notebook_ad, null);
		title = (TextView) dialogView.findViewById(R.id.title_ad);
		addNoteBooks = (ImageView) dialogView.findViewById(R.id.addNoteBooks);
		listViewFunction = (ListView) dialogView
				.findViewById(R.id.listView_function);
		
		
		
		
		initAnimation();
	}

	

	private void initAnimation() {
		scale = new ScaleAnimation(1, 0.3f, 1, 0.3f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		scale.setDuration(200);
		scale.setRepeatMode(Animation.REVERSE);

		rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		rotate.setDuration(200);

		set = new AnimationSet(false);
		set.addAnimation(scale);
		set.addAnimation(rotate);

	}

	private AlertDialog.Builder builder;
	private AlertDialog dialog;

	private boolean animationIsFinished = true;

	private void initListener() {
		// 笔记本选择的点击事件
		// chooseNoteBook.setOnClickListener(this);

		back.setOnClickListener(this);
		
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

		// 编辑笔记
		// 抬起时触发
		editCircle.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					break;
				case MotionEvent.ACTION_MOVE:
					break;
				case MotionEvent.ACTION_UP:
					scale.setAnimationListener(new AnimationListener() {
						@Override
						public void onAnimationStart(Animation animation) {
							animationIsFinished = false;
						}

						@Override
						public void onAnimationRepeat(Animation animation) {
						}

						@Override
						public void onAnimationEnd(Animation animation) {
							animationIsFinished = true;

							/**
							 * 跳转页面
							 */

							Intent intent = new Intent(
									AddNoteEditActivity.this,
									AddNoteTextActicity.class);
							Bundle extras = new Bundle();
							// 初始化对象中的集合， 不然无法获取路径在下一个页面
							// ParseAndInitList(note);
							System.out.println("跳转页面之前note:" + note.toString());
							extras.putParcelable("note", note);
							intent.putExtras(extras);
							intent.putExtra("edit", true);
							startActivity(intent);
							finish();
						}

					});
					if (animationIsFinished) {
						editCircle.startAnimation(set);
					}
					break;

				default:
					break;
				}

				return false;
			}
		});

	}

	/**
	 * 初始化note对象中的集合
	 * 
	 * @param note
	 */
	private void ParseAndInitList(Note note) {

		if(uris != null){
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
				System.out.println("只有一条数据");
				uris = new String[] { uri };
				types = new String[] { type };
				names = new String[] { name };

			}
			
			
			
			System.out.println("test:uris:" + uris.length + "|" + uris[0]);
			System.out.println("test:types:" + types.length + "|" + types[0]);
			System.out.println("test:names:" + names.length + "|" + names[0]);

		}

	}
	
	private void initAttachmentList() {
		if (uris != null && uris.length > 0) {
			// 获取文件
			for (int i = 0; i < uris.length; i++) {
				File file = new File(uris[i]);
				Attachment attachment = new Attachment();
				attachment.fileName = names[i];
				attachment.filePath = uris[i];
				attachment.size = file.length();
				boolean isImage = suffixes.contains(attachment.fileName
						.substring(attachment.fileName.lastIndexOf(".")));
				attachment.isImage = isImage;
				list.add(attachment);
			}
		}
	}


	private void initData() {

		ParseNote(note);
		ParseAndInitList(note);
		
		System.out.println("test:note:" + note.toString());
		
		getAllNoteBooks();

		initAttachmentList();

		// 添加header view和footer view
		listViewAttachment.addHeaderView(header);
		listViewAttachment.addFooterView(footer);

		// 未进入编辑状态
		noteTitle.setFocusable(false);
		noteTitle.setClickable(false);
		noteTitle.setFocusableInTouchMode(false);
		noteTitle.setText(note.title);

		body.setFocusable(false);
		body.setClickable(false);
		body.setFocusableInTouchMode(false);
		body.setText(note.body == null ? "" : note.body);

		AttachmentAdapter adapter = new AttachmentAdapter(new BitmapUtils(this));
		listViewAttachment.setAdapter(adapter);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.chooseNoteBook:
			if (builder == null) {
				builder = new AlertDialog.Builder(new ContextThemeWrapper(
						AddNoteEditActivity.this, R.style.Theme_Transparent));
				dialog = builder.create();
			}
			// 设置布局文件
			dialog.setView(dialogView);
			dialog.show();
			break;
		case R.id.back_image:
			// 关闭页面
			finish();
			break;
		default:
			break;
		}
	}

	private List<NoteBook> allNoteBooks;
	// 被点击的note数据对象
	private Note note;
	// 附件集合
	private List<Attachment> list = new ArrayList<Attachment>();
	private Intent intent;
	private ScaleAnimation scale;
	private RotateAnimation rotate;
	private AnimationSet set;

	private void getAllNoteBooks() {
		new Thread() {
			@Override
			public void run() {
				NoteBookDao dao = new NoteBookDao(AddNoteEditActivity.this);
				// 获取所有笔记本
				allNoteBooks = dao.getAllNoteBooks();

				// 初始化笔记本数据chooseNoteBook
				if (allNoteBooks.size() > 0 && allNoteBooks != null) {
					handler.sendEmptyMessage(INIT_ALERTDIALOG_TITLE_TEXT);
					isChooseNoteBook = true;
				} else {
					chooseNoteBook.setText("未创建笔记本");
					isChooseNoteBook = false;
				}

			}
		}.start();
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
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			Holder holder = null;
			if (convertView == null) {
				view = View.inflate(AddNoteEditActivity.this,
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
			if (list != null && list.size() > 0) {

				Attachment atta = list.get(position);
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
				}
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
				view = View.inflate(AddNoteEditActivity.this,
						R.layout.view_notebook_item, null);
				holder.text = (TextView) view.findViewById(R.id.noteBookName);
				view.setTag(holder);
			} else {
				view = convertView;
				holder = (DialogHolder) view.getTag();
			}
			NoteBook noteBook = allNoteBooks.get(position);
			holder.text.setText(noteBook.bookName);
			// 判断是否可用
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
