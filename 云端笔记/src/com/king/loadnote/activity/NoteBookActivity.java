package com.king.loadnote.activity;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.king.loadnote.R;
import com.king.loadnote.dao.NoteDao;
import com.king.loadnote.domain.Ids;
import com.king.loadnote.domain.Note;
import com.king.loadnote.domain.ServerNote;
import com.king.loadnote.globle.Globle;
import com.king.loadnote.util.DateTextUtil;
import com.king.loadnote.util.SharedPUtil;
import com.king.loadnote.util.SynchronDataUtil;
import com.king.loadnote.util.SynchronDataUtil.SetOnUpdateListener;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class NoteBookActivity extends Activity {

	// view
	private TextView tvAllnoteTime;
	private ListView allnoteListView;

	// ����
	private List<Note> notes;
	// ��ѡ�е�notes��λ�ü���
	private HashMap<Integer, Note> checkedNotes = new HashMap<Integer, Note>();
	// �Ƿ���ʾcheckbox
	public boolean isShowCheckBox = false;
	private int firstIndex = 0;
	private NoteAdapter adapter;
	private final String BLACK_PLACEHOLDER_SHORT = "          ";
	private final String BLACK_PLACEHOLDER_LONG = "                          ";
	// �������ݶ�ȡ�ɹ�
	private final int LOAD_LOCAL_DATA_FINISH = 1;
	private final int REFLASH_LISTVIEW_FUNCTION = 2;
	private final int REFLASH_TITLE_TIME = 3;
	private final int JUMP_TO_PAINT = 50;

	private FunctionAdapter funAdapter;

	private static List<String> suffixes = new ArrayList<String>();

	static {
		suffixes.add(".jpg");
		suffixes.add(".png");
		suffixes.add(".jpeg");
		suffixes.add(".gif");
		suffixes.add(".bmp");
		suffixes.add(".wbmp");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		rootView = View.inflate(this, R.layout.activity_notebook_list, null);

		setContentView(rootView);

		Intent intent = getIntent();
		clickBookId = intent.getIntExtra("bookIndex", -1);
		bookName = intent.getStringExtra("bookName");

		initView();
		initListener();
		initData();
	}

	// ȡ��
	private OnClickListener cancel = new OnClickListener() {
		@Override
		public void onClick(View v) {
			clearCheckedMap();
		}
	};

	// �򿪲˵�
	private OnClickListener back = new OnClickListener() {
		@Override
		public void onClick(View v) {
			finish();
		}
	};

	private Handler handler = new Handler() {
		private Note note = null;

		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case LOAD_LOCAL_DATA_FINISH:
				if (adapter == null) {
					adapter = new NoteAdapter();

					System.out.println("test:Adapter:ListSize:"
							+ adapter.getCount());

					// ����adapter
					allnoteListView.setAdapter(adapter);
				} else {
					adapter.notifyDataSetChanged();
				}

				// ȡ���ȴ�
				if (notes.size() > 0) {
					note = notes.get(0);
					tvAllnoteTime.setText(DateTextUtil.getDateString(new Date(
							note.writeTime).getTime()));
				} else {
					tvAllnoteTime.setText("�տ���Ҳ");
				}

				// ˢ������
				base_note_sum.setText(noteCount + " �ʼ�");

				break;
			case REFLASH_LISTVIEW_FUNCTION:
				if (funAdapter == null) {
					funAdapter = new FunctionAdapter();
					listViewFunction.setAdapter(funAdapter);
				} else {
					funAdapter.notifyDataSetChanged();
				}

				break;
			case REFLASH_TITLE_TIME:
				if (notes.size() > 0) {
					int index = msg.arg1;
					note = notes.get(index);
					tvAllnoteTime.setText(DateTextUtil.getDateString(new Date(
							note.writeTime).getTime()));
				} else {
					tvAllnoteTime.setText("�տ���Ҳ");
				}

				break;
			default:
				break;
			}
		};
	};
	private RelativeLayout addNoteCircle;
	private ImageView circle;
	private ImageView add;

	private View ppwView;
	private ScaleAnimation scale;
	private RotateAnimation rotate;

	private PopupWindow ppw;
	private AnimationSet set;

	private boolean animationIsFinished = true;

	private AlertDialog.Builder builder;
	private View functionView;

	private String[] items = new String[] { "���ֱʼ�", "�ֻ�ʼ�", "����" };
	private int[] ids = new int[] { R.drawable.selector_text_ad,
			R.drawable.selector_write_ad, R.drawable.selector_alarm_ad,
			R.drawable.selector_file_ad };
	private ListView listViewFunction;

	private List<String> popStr;

	private AlertDialog alertDialog;
	private ImageView lineShadow;
	private ListView popListView;
	private PopupWindow ppwList;

	public void initView() {

		// ���ذ�ť
		btn_back = (LinearLayout) findViewById(R.id.btn_back);
		image_back = (ImageView) findViewById(R.id.image_back);

		// ����
		btn_more = (LinearLayout) findViewById(R.id.btn_more);

		// ����
		base_title_text = (TextView) findViewById(R.id.base_title_text);

		// �ʼ�����
		base_note_sum = (TextView) findViewById(R.id.base_note_sum);

		tvAllnoteTime = (TextView) findViewById(R.id.f_allnote_time_text);

		lineShadow = (ImageView) findViewById(R.id.line_shadow);

		allnoteListView = (ListView) findViewById(R.id.f_allnote_listview);

		addNoteCircle = (RelativeLayout) findViewById(R.id.add_note_circle);
		circle = (ImageView) findViewById(R.id.circle);
		add = (ImageView) findViewById(R.id.add);

		functionView = View.inflate(this, R.layout.view_dialog_function_area,
				null);

		listViewFunction = (ListView) functionView
				.findViewById(R.id.listView_function);

		ppView = View.inflate(this, R.layout.view_pop, null);
		popListView = (ListView) ppView.findViewById(R.id.pop_listView);

		ppw = new PopupWindow(ppView, 300, LayoutParams.WRAP_CONTENT, true);
		ppw.setBackgroundDrawable(this.getResources().getDrawable(
				R.drawable.popup_background));
		ppw.setOutsideTouchable(true);

		// �����Զ���Ķ�����ʽ
		ppw.setAnimationStyle(R.style.showPopupAnimation);

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

	public void initListener() {
		// �˳���ҳ��
		btn_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

		// item��������
		allnoteListView
				.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
							View view, int position, long id) {
						if (!isShowCheckBox) {
							CheckBox cb = (CheckBox) view
									.findViewById(R.id.choose_note);
							cb.setChecked(true);
							checkedNotes.put(position, notes.get(position));
							// �޸ı���Ĳ˵���ͼƬ�ͱ���
							image_back
									.setImageResource(R.drawable.ic_navigation_cancel);
							base_title_text.setText("��ѡ��" + checkedNotes.size()
									+ "��");
							// �����ʼǱ�����
							base_note_sum.setText("");
							// ����ȡ������
							btn_back.setOnClickListener(cancel);

							// �������е�checkbox��ʾ����
							isShowCheckBox = true;
							adapter.notifyDataSetChanged();

							System.out.println("test:longCLick:"
									+ checkedNotes.size());
							for (Integer i : checkedNotes.keySet()) {
								System.out.println("test:click:position:" + i);
							}
							return true;
						}
						return false;
					}
				});

		// item�������
		allnoteListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// �ж��Ƿ�checkBox�Ѿ���ʾ
				if (isShowCheckBox) {
					// ���ѡ�л�ȡ��
					CheckBox cb = (CheckBox) view
							.findViewById(R.id.choose_note);
					if (cb.isChecked()) {
						cb.setChecked(false);
						checkedNotes.remove(position);
					} else {
						checkedNotes.put(position, notes.get(position));
						cb.setChecked(true);
					}
					base_title_text.setText("��ѡ��" + checkedNotes.size() + "��");
					if (checkedNotes.size() == 0) {
						clearCheckedMap();
					}

					System.out.println("test:click:" + checkedNotes.size());
					for (Integer i : checkedNotes.keySet()) {
						System.out.println("test:click:position:" + i);
					}

				} else {
					// ����ʼ�ҳ��
					System.out.println("test:����ʼ�ҳ��");
					Intent intent = new Intent(NoteBookActivity.this,
							AddNoteEditActivity.class);
					Bundle extras = new Bundle();
					Note note = notes.get(position);
					extras.putParcelable("note", note);
					intent.putExtras(extras);
					NoteBookActivity.this.startActivityForResult(intent, 200);
				}
			}
		});

		// function ��item�ĵ���¼�
		listViewFunction.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				System.out.println("�����" + funAdapter.getItem(position));

				if (alertDialog != null) {
					Intent intent = null;
					switch (position) {
					case 0:
						// ���ֱʼ�
						intent = new Intent(NoteBookActivity.this,
								AddNoteTextActicity.class);
						NoteBookActivity.this.startActivityForResult(intent,
								200);
						break;
					case 1:
						// �ֻ�ʼ�
						intent = new Intent(NoteBookActivity.this,
								AddNoteTextActicity.class);
						intent.putExtra("jump", JUMP_TO_PAINT);
						NoteBookActivity.this.startActivityForResult(
								new Intent(NoteBookActivity.this,
										AddNoteTextActicity.class), 200);
						break;
					case 2:
						// ����
						intent = new Intent(NoteBookActivity.this,
								AddNoteTextActicity.class);
						NoteBookActivity.this.startActivityForResult(intent,
								200);
						break;

					default:
						break;
					}
					alertDialog.dismiss();
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
					System.out.println("scroll:" + msg.arg1);
					handler.sendMessage(msg);

				}

			}

		});
		// ���� �������¼�
		btn_more.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ppw.showAtLocation(rootView, Gravity.RIGHT + Gravity.TOP, 1, 30);
			}
		});

		// �����popuWindowʱ����
		popListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (ppw.isShowing()) {
					ppw.dismiss();
				}
				System.out.println("click_popListView");
				NoteDao dao = null;
				switch (position) {
				case 0:

					// ѡ��ʼ�
					if (!isShowCheckBox && notes.size() > 0) {
						isShowCheckBox = true;

						System.out.println("******isShowCheckBox:"
								+ checkedNotes == null);

						checkedNotes.put(0, notes.get(0));

						System.out.println("******isShowCheckBox:"
								+ checkedNotes.size());

						handler.sendEmptyMessage(LOAD_LOCAL_DATA_FINISH);
					}
					break;
				case 1:
					// �ƶ�ͬ��
					System.out.println("test:�ƶ�ͬ��");
					PostOrReceive();
					break;
				case 2:
					// ���ݻָ�
					recoverLocalData();

					break;

				case 3:
					// ɾ��
					/**
					 * 1. ������ѡ�е�note 2. ���ı�����--��is_usable
					 */
					if (dao == null) {
						dao = new NoteDao(NoteBookActivity.this);
					}
					Set<Integer> keySet = checkedNotes.keySet();
					Iterator<Integer> iterator = keySet.iterator();

					for (Integer i : checkedNotes.keySet()) {
						Note note = checkedNotes.get(iterator.next());
						// �ĳ�0����������
						dao.updateNotes(note._id, 0, note.outKeyNoteBook);

					}
					clearCheckedMap();
					getLocalData();
					break;
				default:
					break;
				}
			}

		});

		// ���----ͼƬ��ť
		// ̧��ʱ����
		addNoteCircle.setOnTouchListener(new OnTouchListener() {
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

						}
					});
					if (animationIsFinished) {
						addNoteCircle.startAnimation(set);
					}

					if (builder == null) {
						// ����alertDialog
						builder = new AlertDialog.Builder(
								new ContextThemeWrapper(NoteBookActivity.this,
										R.style.Theme_Transparent));
						alertDialog = builder.create();
					}
					// ���ò����ļ�
					alertDialog.setView(functionView);
					// handler.sendEmptyMessage(REFLASH_LISTVIEW_FUNCTION);
					alertDialog.show();

					break;

				default:
					break;
				}

				return false;
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		/**
		 * �����һ�ݱʼǣ�����ˢ������
		 */
		getLocalData();

	}

	public void initData() {
		// ���ݿ��ȡ������Դ
		getLocalData();

		if (funAdapter == null) {
			funAdapter = new FunctionAdapter();
			listViewFunction.setAdapter(funAdapter);
		}

		// // ��ʼ������
		base_title_text.setText(bookName);
		base_note_sum.setText(noteCount + " �ʼ�");

		// ��ʼ��ppView
		popStr = new ArrayList<String>();
		popStr.add("ѡ��ʼ�");
		popStr.add("Զ��ͬ��");
		popStr.add("���ݻָ�");
		popStr.add("ɾ��");

		PopAdapter adapter = new PopAdapter();
		popListView.setAdapter(adapter);

	}

	private HttpUtils utils;
	private LinearLayout btn_back;
	private View ppView;
	private int clickBookId;
	private String bookName;
	private int noteCount;
	private TextView base_title_text;
	private TextView base_note_sum;
	private ImageView image_back;
	private LinearLayout btn_more;
	private View rootView;

	// ���ݻָ�
	private void recoverLocalData() {
		new Thread() {
			public void run() {
				/**
				 * ���رʼ�
				 */
				downLoadNote();

			};
		}.start();

	}

	/**
	 * �ָ�����(���رʼ�)
	 */
	public void downLoadNote() {

		/**
		 * 1. ��ȡ���б��ص��Ѹ��¹��ıʼ�note��server_note_id�ļ��� 2. ת����json��������������user(�绰����)
		 * 3. ��������ѯ���д�user�µ�����note���ϣ����Ƴ�server_note_id���ϵ�ƥ��Ԫ�� 4.
		 * ��ȡ����˷��ص�json�������� 5. д�����ݿ�
		 */
		NoteDao noteDao = new NoteDao(NoteBookActivity.this);
		List<Integer> ids = noteDao.getServerNoteId();
		Gson gson = new Gson();
		String idsJson = gson.toJson(ids, new TypeToken<List<Integer>>() {
		}.getType());

		final HttpUtils httpUtils = new HttpUtils();
		RequestParams params = new RequestParams();

		SharedPUtil pUtil = SharedPUtil.getInstant(NoteBookActivity.this);
		String user = pUtil.getString("telNumber");

		params.addBodyParameter("user", user);
		params.addBodyParameter("ids", idsJson);

		String url = Globle.DOWNLOAD_URL;

		httpUtils.send(HttpMethod.POST, url, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {

					}

					@Override
					public void onSuccess(ResponseInfo<String> result) {
						try {
							String resultValue = URLDecoder.decode(
									result.result, "utf-8");

							/**
							 * д�����ݿ�
							 */
							Gson gson = new Gson();
							List<ServerNote> listNotes = gson.fromJson(
									resultValue,
									new TypeToken<List<ServerNote>>() {
									}.getType());

							System.out
									.println("return json : result : ListNote:"
											+ listNotes);
							// ���ʼ�д������д�����ݿ�
							NoteDao noteDao = new NoteDao(NoteBookActivity.this);
							for (ServerNote sn : listNotes) {
								Note note = new Note();
								note._id = sn.client_id;
								note.server_id = sn._id;
								note.outKeyNoteBook = sn.outKeyNoteBook;
								note.title = sn.title;
								note.writeTime = sn.writeTime;
								note.body = sn.body;
								note.attachment_name_str = sn.attachment_name_str;
								note.attachment_type_str = sn.attachment_type_str;
								note.attachment_uri_str = sn.attachment_uri_str;
								note.updated = sn.updated;
								note.is_usable = sn.is_usable;
								note.isPaint = sn.isPaint;
								noteDao.addToNotes(note);
								// �жϲ�Ϊ��
								if (!TextUtils
										.isEmpty(sn.server_attachment_url)) {
									// �����ļ���Դ
									String[] uris = sn.server_attachment_url
											.split(",");
									String[] localUris = note.attachment_uri_str
											.split(",");
									// �����ļ�
									if (uris.length > 0) {
										for (int i = 0; i < uris.length; i++) {
											httpUtils
													.download(
															Globle.SERVLER_URL
																	+ uris[i],
															localUris[i],
															true,
															new RequestCallBack<File>() {

																@Override
																public void onFailure(
																		HttpException arg0,
																		String arg1) {

																}

																@Override
																public void onSuccess(
																		ResponseInfo<File> arg0) {
																	System.out
																			.println("���سɹ�");
																}

															});

										}
									}
								}
							}
							// ���¶�ȡ�ʼǱ����ݿ�
							getLocalData();

						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}

					}
				});

	}

	// �ƶ�ͬ��
	private void PostOrReceive() {

		SynchronDataUtil syncUtil = SynchronDataUtil
				.getInstant(NoteBookActivity.this);
		syncUtil.updateData();

		syncUtil.setOnUpdateListener(new SetOnUpdateListener() {

			@Override
			public void onSuccess(ResponseInfo<String> result,
					List<Note> listNote) {
				// �ɹ���
				System.out.println("json:OK");
				NoteDao dao = new NoteDao(NoteBookActivity.this);
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

	private void getLocalData() {
		// ��ʼ��listView���õ�notes����
		NoteDao dao = new NoteDao(NoteBookActivity.this);
		notes = dao.getNotesByBookId(clickBookId);
		noteCount = dao.getNotesCount(clickBookId);
		// ����������
		ListSort(notes);

		// ����Ϣ
		handler.sendEmptyMessage(LOAD_LOCAL_DATA_FINISH);
	}

	/**
	 * ��������
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
				view = View.inflate(NoteBookActivity.this,
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

			System.out.println("test:getView:position:" + position);

			Note note = notes.get(position);
			if (note.updated == 1) {
				holder.updated.setVisibility(View.VISIBLE);
			} else {
				holder.updated.setVisibility(View.GONE);
			}

			holder.title.setText(note.title);
			// ��ͼƬĬ��ѡ��һ��ͼƬ
			if (note.attachment_uri.size() != 0) {
				holder.ivShow.setVisibility(View.VISIBLE);
				if (suffixes.contains(note.attachment_type.get(0))) {
					// ͼƬ֮���
					BitmapUtils util = new BitmapUtils(NoteBookActivity.this);
					util.display(holder.ivShow, note.attachment_uri.get(0));
				} else {
					// ����
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

	class FunctionAdapter extends BaseAdapter {

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
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			FunHolder holder = null;
			if (convertView == null) {
				view = View.inflate(NoteBookActivity.this,
						R.layout.view_dialog_function_item, null);
				holder = new FunHolder();
				holder.icon = (ImageView) view.findViewById(R.id.icon);
				holder.text = (TextView) view.findViewById(R.id.text);
				view.setTag(holder);
			} else {
				view = convertView;
				holder = (FunHolder) view.getTag();
			}
			holder.icon.setImageResource(ids[position]);
			holder.text.setText(items[position]);
			return view;
		}

	}

	class FunHolder {

		public ImageView icon;
		public TextView text;

	}

	/**
	 * ˢ��adater
	 */
	public void reflashAdapter() {
		handler.sendEmptyMessage(LOAD_LOCAL_DATA_FINISH);
	}

	/**
	 * ���map,��ԭ����
	 */
	public void clearCheckedMap() {
		// ���map
		checkedNotes.clear();

		// �����Ƿ���ʾ
		isShowCheckBox = false;
		// ����ͼ��
		image_back.setImageResource(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
		// ���ı���
		base_title_text.setText(bookName);
		base_note_sum.setText(noteCount + " �ʼ�");
		// ˢ��adapter
		adapter.notifyDataSetChanged();
		btn_back.setOnClickListener(back);
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
				view = View.inflate(NoteBookActivity.this,
						R.layout.view_pop_item_list, null);
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

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		if (isShowCheckBox) {
			clearCheckedMap();
		} else {
			// ���͹㲥��ˢ��UI
			sendBroadcast(new Intent("REFLASH_NOTE_BOOK_LIST"));
			finish();
		}

	}

}
