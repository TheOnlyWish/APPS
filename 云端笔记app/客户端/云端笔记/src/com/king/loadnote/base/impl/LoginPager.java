package com.king.loadnote.base.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.content.Intent;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.king.loadnote.R;
import com.king.loadnote.activity.MainActivity;
import com.king.loadnote.base.BasePager;
import com.king.loadnote.globle.Globle;
import com.king.loadnote.util.AlertDialogUtil;
import com.king.loadnote.util.FormatterUtil;
import com.king.loadnote.util.Md5Util;
import com.king.loadnote.util.SharedPUtil;
import com.king.loadnote.util.AlertDialogUtil.OnDismissListener;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * LoginPager
 * 
 * @author Administrator
 * 
 */
public class LoginPager extends BasePager implements OnClickListener {

	private EditText telNumber;
	private EditText password;
	private CheckBox isShowPass;
	private Button login;

	private boolean isLoginOK;

	public LoginPager(Activity activity) {
		super(activity);
		title = "��¼";
	}

	@Override
	public void initView() {
		rootView = View.inflate(mActivity, R.layout.pager_login, null);
		telNumber = (EditText) rootView.findViewById(R.id.telNumber);
		password = (EditText) rootView.findViewById(R.id.password);
		isShowPass = (CheckBox) rootView.findViewById(R.id.isShowPass);
		login = (Button) rootView.findViewById(R.id.login);

		initListener();
		initData();
	}

	private void initListener() {
		login.setOnClickListener(this);
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
	 * ����¼�����д
	 * 
	 * @param v
	 */
	@Override
	public void onClick(View v) {
		final String tel = telNumber.getText().toString();
		final String pass = password.getText().toString();
		switch (v.getId()) {
		case R.id.login:
			AlertDialogUtil adUtil = AlertDialogUtil
					.instantAlertDialogUtil(mActivity);
			System.out.println("loginPager");
			// �����¼��ť
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
			} else {
				// ���������Ƿ��¼�ɹ�
				userLogin(tel, pass);

				// �ԵȶԻ���
				adUtil.create();

			}

			break;

		default:
			break;
		}

	}

	/**
	 * �û���¼
	 * 
	 * @param tel
	 * @param pass
	 */
	private void userLogin(final String tel, final String pass) {
		String loginUrl = Globle.LOGIN_URL + "?telNumber=" + tel
				+ "&password=" + Md5Util.encoder(pass);
		System.out.println(loginUrl);
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.GET, loginUrl, new RequestCallBack<String>() {

			@Override
			public void onLoading(long total, long current, boolean isUploading) {
				super.onLoading(total, current, isUploading);
			}
			
			
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				
			}

			/**
			 * ���ʳɹ�
			 */
			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				int respCode = Integer.parseInt(arg0.result);
				AlertDialogUtil adUtils = AlertDialogUtil
						.instantAlertDialogUtil(mActivity);
				System.out.println("test:respCode:" + respCode);
				// ȡ���Ե�ģ��
				adUtils.dismissWaiting();
				if (respCode == 200) {
					System.out.println("test:��¼�ɹ�");
					// ��¼�ɹ�
					isLoginOK = true;
					adUtils.create("��¼�ɹ�", "");
				} else {
					System.out.println("test:��¼ʧ��");
					// ��¼ʧ��
					adUtils.create("��¼ʧ��", "������!");
					isLoginOK = false;
				}
				adUtils.setOnDismissListener(new OnDismissListener() {
					@Override
					public void afterDismiss() {
						if (isLoginOK) {
							isLoginOK = false;
							/**
							 * 1. �����û����Լ����� 2. ��תҳ��
							 */
							SharedPUtil sp = SharedPUtil
									.getInstant(mActivity);
							sp.putString("telNumber", tel);
							sp.putString("password",
									Md5Util.encoder(pass));

							Intent intent = new Intent(mActivity,
									MainActivity.class);
							mActivity.startActivity(intent);
							mActivity.finish();
						}
					}
				});
				
				
			}
			
			@Override
			public void onStart() {
				super.onStart();
			}

			
		});
		
		
		
//		//////////////////////////////
//		new Thread() {
//			public void run() {
//				String registUrl = Globle.LOGIN_URL + "?telNumber=" + tel
//						+ "&password=" + Md5Util.encoder(pass);
//				BufferedReader reader = null;
//				HttpURLConnection conn = null;
//				System.out.println("test:registUrl:" + registUrl);
//				try {
//					URL url = new URL(registUrl);
//					conn = (HttpURLConnection) url.openConnection();
//					conn.setRequestMethod("GET");
//					conn.setReadTimeout(5000);
//					conn.setConnectTimeout(5000);
//					conn.connect();
//					System.out.println("test:connect");
//					if (conn.getResponseCode() == 200) {
//						InputStream in = conn.getInputStream();
//						reader = new BufferedReader(new InputStreamReader(in,
//								"UTF-8"));
//						StringBuffer sb = new StringBuffer();
//						String value = "";
//						while ((value = reader.readLine()) != null) {
//							sb.append(value);
//						}
//
//						System.out
//								.println("test:respCode:str:" + sb.toString());
//						// ��ȡ������,������--int
//						int respCode = Integer.parseInt(sb.toString());
//						AlertDialogUtil adUtils = AlertDialogUtil
//								.instantAlertDialogUtil(mActivity);
//						System.out.println("test:respCode:" + respCode);
//						// ȡ���Ե�ģ��
//						adUtils.dismissWaiting();
//						if (respCode == 200) {
//							System.out.println("test:��¼�ɹ�");
//							// ��¼�ɹ�
//							isLoginOK = true;
//							adUtils.create("��¼�ɹ�", "", "��");
//						} else {
//							System.out.println("test:��¼ʧ��");
//							// ��¼ʧ��
//							adUtils.create("��¼ʧ��", "������!", "��");
//							isLoginOK = false;
//						}
//						adUtils.setOnDismissListener(new OnDismissListener() {
//							@Override
//							public void afterDismiss() {
//								if (isLoginOK) {
//									isLoginOK = false;
//									/**
//									 * 1. �����û����Լ����� 2. ��תҳ��
//									 */
//									SharedPUtil sp = SharedPUtil
//											.getInstant(mActivity);
//									sp.putString("telNumber", tel);
//									sp.putString("password",
//											Md5Util.encoder(pass));
//
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
//					throw new RuntimeException(e);
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

}
