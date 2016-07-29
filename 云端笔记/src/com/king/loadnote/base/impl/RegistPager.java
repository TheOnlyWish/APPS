package com.king.loadnote.base.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

import com.king.loadnote.R;
import com.king.loadnote.activity.MainActivity;
import com.king.loadnote.base.BasePager;
import com.king.loadnote.globle.Globle;
import com.king.loadnote.util.AlertDialogUtil;
import com.king.loadnote.util.AlertDialogUtil.OnDismissListener;
import com.king.loadnote.util.DateUtil;
import com.king.loadnote.util.FormatterUtil;
import com.king.loadnote.util.Md5Util;
import com.king.loadnote.util.SharedPUtil;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class RegistPager extends BasePager implements OnClickListener {

	private EditText telNumber;
	private EditText password;
	private CheckBox isShowPass;
	private Button regist;
	private Button btn_checkCode;
	private boolean isRegistOK;

	public RegistPager(Activity activity) {
		super(activity);
		title = "ע��";
	}

	@Override
	public void initView() {
		rootView = View.inflate(mActivity, R.layout.pager_regist, null);
		telNumber = (EditText) rootView.findViewById(R.id.telNumber);
		password = (EditText) rootView.findViewById(R.id.password);
		checkCode = (EditText) rootView.findViewById(R.id.checkCode);
		isShowPass = (CheckBox) rootView.findViewById(R.id.isShowPass);
		btn_checkCode = (Button) rootView.findViewById(R.id.btn_checkCode);
		regist = (Button) rootView.findViewById(R.id.regist);
		initListener();
		initData();
	}

	public void initListener() {
		// ���"��ȡ��֤��"��ť
		btn_checkCode.setOnClickListener(this);
		regist.setOnClickListener(this);
		isShowPass.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					// ��ʾ����
					password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
				} else {
					// ��������
					password.setInputType(InputType.TYPE_CLASS_TEXT
							| InputType.TYPE_TEXT_VARIATION_PASSWORD);
				}
			}
		});
	}

	@Override
	public void initData() {

	}

	/**
	 * �¼��������
	 */
	@Override
	public void onClick(View v) {

		System.out.println("RegistPager");

		final String tel = telNumber.getText().toString();
		final String pass = password.getText().toString();
		String code = checkCode.getText().toString();

		AlertDialogUtil adUtil = AlertDialogUtil
				.instantAlertDialogUtil(mActivity);

		switch (v.getId()) {
		// ���"ע��"��ť
		case R.id.regist:
			/**
			 * 1. �жϵ绰�����Ƿ�Ϊ�� 2. ����绰�����ʽ�Ƿ���ȷ 3. ��������Ƿ�Ϊ�� 4. ��������Ƿ񳬹�12λ
			 */
			// ������ʾ��
			if (TextUtils.isEmpty(tel)) {
				adUtil.create("�绰����Ϊ��", "������绰����");
				break;
			} else if (!FormatterUtil.isPhoneNumberValid(tel)) {
				adUtil.create("�绰�������", "���޸ĵ绰����");
				break;
			} else if (TextUtils.isEmpty(pass)) {
				adUtil.create("����Ϊ��", "����������");
				break;
			} else if (pass.trim().length() > 12) {
				adUtil.create("���볤�ȳ���12λ", "���޸�����");
				break;
			} else if (TextUtils.isEmpty(code)) {
				adUtil.create("��֤��Ϊ��", "��������֤��");
			} else {
				// ע�Ὺʼ
				userRegist(tel, pass, code);

				// ��ʾ�Ե�ģ��
				adUtil.create();
			}
			break;
		// ���"��ȡ��֤��"��ť
		case R.id.btn_checkCode:
			// ���ð�ť���ɵ��
			if (TextUtils.isEmpty(tel)) {
				adUtil.create("�绰����Ϊ��", "������绰����");
				break;
			} else if (!FormatterUtil.isPhoneNumberValid(tel)) {
				adUtil.create("�绰�������", "���޸ĵ绰����");
				break;
			} else {
				// ���������ɹ�
				adUtil.create("��ȴ���֤��", "������...");
				/**
				 * �������������������ȡ��֤�� ����: 1. �绰���� 2. ʱ���ַ��� 3. ʱ�����
				 */
				btn_checkCode.setEnabled(false);
				btn_checkCode.setBackgroundColor(Color.GRAY);
				requestCheckCode(tel);
				// ˢ��UI
				reflashCheckCodeBtn();
			}
			break;
		default:
			break;
		}
	}

	// ��Ϣ����
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			// �жϽ���
			if (msg.what <= 0) {
				// ȡ����ʱ��
				timer.cancel();
				btn_checkCode.setEnabled(true);
				btn_checkCode.setBackgroundResource(R.drawable.select_btn_lr);
				btn_checkCode.setText("��ȡ��֤��");
				return;
			}
			// ˢ��UI
			btn_checkCode.setText("��ʣ" + msg.what + "��");
		};
	};
	// ��ʱ������
	private Timer timer;
	private EditText checkCode;

	/**
	 * ˢ�¼��UI60��
	 */
	private void reflashCheckCodeBtn() {
		TimerTask task = new TimerTask() {
			int amount = 60;

			// ִ�еĴ���
			@Override
			public void run() {
				// ����ˢ��UI����Ϣ
				handler.sendEmptyMessage(--amount);
			}
		};
		timer = new Timer();
		timer.schedule(task, 0, 1000);

	}

	/**
	 * �û�ע��
	 * 
	 * @param tel
	 * @param pass
	 * @param code
	 */
	private void userRegist(final String tel, final String pass,
			final String code) {
		String registUrl = Globle.REGIST_URL + "?telNumber=" + tel
				+ "&password=" + Md5Util.encoder(pass) + "&code="
				+ code;
		
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.GET, registUrl, new RequestCallBack<String>() {

			@Override
			public void onLoading(long total, long current, boolean isUploading) {
				super.onLoading(total, current, isUploading);
			}
			@Override
			public void onStart() {
				super.onStart();
			}
			
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				AlertDialogUtil adUtil = AlertDialogUtil
						.instantAlertDialogUtil(mActivity);
				// ��ȡ������
				int respCode = Integer.parseInt(arg0.result);
				System.out.println("test:regist:respCode:" + respCode);
				// ȡ���Ե�ģ��
				adUtil.dismissWaiting();
				if (respCode == 200) {
					adUtil.dismissWaiting();
					System.out.println("test:regist:200:");
					
					// ע��ɹ�
					isRegistOK = true;
					adUtil.create("ע��ɹ�", "   ");
					System.out.println("test:adUtil.create(ע��ɹ�)");
				} else {
					// ע��ʧ��
					if (respCode == 300) {
						adUtil.dismissWaiting();
						System.out.println("test:regist ��������300");
						adUtil.create("���û��Ѿ�ע��", "������!");
					} else {
						System.out.println("test:regist ��������400");
						adUtil.create("ע��ʧ��", "��֤���ʱ��ʧЧ������!");
					}
					isRegistOK = false;
				}
				adUtil.setOnDismissListener(new OnDismissListener() {
					@Override
					public void afterDismiss() {
						if (isRegistOK) {
							isRegistOK = false;
							/**
							 * 1. �����û����Լ����� 2. ��תҳ��
							 */
							SharedPUtil sp = SharedPUtil
									.getInstant(mActivity);
							sp.putString("telNumber", tel);
							sp.putString("password", pass);
							Intent intent = new Intent(mActivity,
									MainActivity.class);
							mActivity.startActivity(intent);
							mActivity.finish();
						}
					}
				});
			}
		});
		
		
		
		////////////////////////////////////////////////////
//		new Thread() {
//			public void run() {
//				String registUrl = Globle.REGIST_URL + "?telNumber=" + tel
//						+ "&password=" + Md5Util.encoder(pass) + "&code="
//						+ code;
//				BufferedReader reader = null;
//				HttpURLConnection conn = null;
//				try {
//					URL url = new URL(registUrl);
//					conn = (HttpURLConnection) url.openConnection();
//					conn.setRequestMethod("GET");
//					conn.setReadTimeout(5000);
//					conn.setConnectTimeout(5000);
//					conn.connect();
//					System.out.println("test:regist:getResponseCode:"
//							+ conn.getResponseCode());
//					if (conn.getResponseCode() == 200) {
//						InputStream in = conn.getInputStream();
//						reader = new BufferedReader(new InputStreamReader(in));
//						StringBuffer sb = new StringBuffer();
//						String value = "";
//						while ((value = reader.readLine()) != null) {
//							sb.append(value);
//						}
//						AlertDialogUtil adUtil = AlertDialogUtil
//								.instantAlertDialogUtil(mActivity);
//						// ��ȡ������
//						int respCode = Integer.parseInt(sb.toString());
//						System.out.println("test:regist:respCode:" + respCode);
//						// ȡ���Ե�ģ��
//						adUtil.dismissWaiting();
//						if (respCode == 200) {
//							adUtil.dismissWaiting();
//							System.out.println("test:regist:200:");
//							
//							// ע��ɹ�
//							isRegistOK = true;
//							adUtil.create("ע��ɹ�", "   ", "��");
//							System.out.println("test:adUtil.create(ע��ɹ�)");
//						} else {
//							// ע��ʧ��
//							if (respCode == 300) {
//								adUtil.dismissWaiting();
//								System.out.println("test:regist ��������300");
//								adUtil.create("���û��Ѿ�ע��", "������!", "��");
//							} else {
//								System.out.println("test:regist ��������400");
//								adUtil.create("ע��ʧ��", "��֤���ʱ��ʧЧ������!", "��");
//							}
//							isRegistOK = false;
//						}
//						adUtil.setOnDismissListener(new OnDismissListener() {
//							@Override
//							public void afterDismiss() {
//								if (isRegistOK) {
//									isRegistOK = false;
//									/**
//									 * 1. �����û����Լ����� 2. ��תҳ��
//									 */
//									SharedPUtil sp = SharedPUtil
//											.getInstant(mActivity);
//									sp.putString("telNumber", tel);
//									sp.putString("password", pass);
//									Intent intent = new Intent(mActivity,
//											MainActivity.class);
//									mActivity.startActivity(intent);
//									mActivity.finish();
//								}
//							}
//						});
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				} finally {
//					if (reader != null) {
//						try {
//							reader.close();
//						} catch (IOException e) {
//							e.printStackTrace();
//						}
//					}
//				}
//			};
//		}.start();
	}

	/**
	 * ����checkCode���� ����: ��ǰʱ���ַ��� ��ǰʱ�����ֵ
	 * 
	 * @param tel
	 */
	private void requestCheckCode(final String tel) {
		System.out.println("test:requestCheckCode");
		// ������֤������
		new Thread() {
			public void run() {
				/**
				 * ����:�绰���룬��ǰʱ��
				 */
				System.out.println("test:requestCheckCode_1");
				Date date = new Date();
				String nowTime = DateUtil.dateToStr(date,
						DateUtil.DATE_TIME_NO_SLASH);
				long clickTime = date.getTime();
				String netUrl = Globle.REQUEST_CHECKCODE + "?telNumber=" + tel
						+ "&nowTime=" + nowTime + "&clickTime=" + clickTime;
				System.out.println("test:" + netUrl);
				BufferedReader reader = null;
				HttpURLConnection conn = null;
				try {
					URL url = new URL(netUrl);
					conn = (HttpURLConnection) url.openConnection();
					System.out.println("test:requestCheckCode_2");
					conn.setRequestMethod("GET");
					System.out.println("test:requestCheckCode_3");
					conn.setReadTimeout(5000);
					conn.setConnectTimeout(5000);
					System.out.println("test:******getResponseCode��"
							+ conn.getResponseCode());
					conn.connect();
					if (conn.getResponseCode() == 200) {
						InputStream in = conn.getInputStream();
						reader = new BufferedReader(new InputStreamReader(in,
								"UTF-8"));
						StringBuffer sb = new StringBuffer();
						String value = "";
						while ((value = reader.readLine()) != null) {
							sb.append(value);
						}
						System.out.println("test:getResponseCode:�õ��ķ��������ص�����:"
								+ sb.toString());
						// ��ȡ������
						int respCode = Integer.parseInt(sb.toString());
						System.out.println("test:respCode��" + respCode);
						if (respCode == 200) {

						} else {
							AlertDialogUtil adUtil = AlertDialogUtil
									.instantAlertDialogUtil(mActivity);
							// ���������ʧ��
							adUtil.create("��֤������ʧ��", "��֤�뱻����ʱ������ˣ�������");
							// handler.removeCallbacksAndMessages(null);
							timer.cancel();
							handler.sendEmptyMessage(-1);
						}

					}

				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (reader != null) {
						try {
							reader.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			};
		}.start();
	}
}
