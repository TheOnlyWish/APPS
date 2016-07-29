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
		title = "登录";
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
					// 显示密码
					password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
				} else {
					// 隐藏密码
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
	 * 点击事件的重写
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
			// 点击登录按钮
			// 弹出提示框
			if (TextUtils.isEmpty(tel)) {
				adUtil.create("电话号码为空", "请输入电话号码");
				break;
			} else if (!FormatterUtil.isPhoneNumberValid(tel)) {
				adUtil.create("电话号码错误", "请修改电话号码");
				break;
			} else if (TextUtils.isEmpty(pass)) {
				adUtil.create("密码为空", "请输入密码");
				break;
			} else if (pass.trim().length() > 12) {
				adUtil.create("密码长度超过12位", "请修改密码");
				break;
			} else {
				// 访问网络是否登录成功
				userLogin(tel, pass);

				// 稍等对话框
				adUtil.create();

			}

			break;

		default:
			break;
		}

	}

	/**
	 * 用户登录
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
			 * 访问成功
			 */
			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				int respCode = Integer.parseInt(arg0.result);
				AlertDialogUtil adUtils = AlertDialogUtil
						.instantAlertDialogUtil(mActivity);
				System.out.println("test:respCode:" + respCode);
				// 取消稍等模版
				adUtils.dismissWaiting();
				if (respCode == 200) {
					System.out.println("test:登录成功");
					// 登录成功
					isLoginOK = true;
					adUtils.create("登录成功", "");
				} else {
					System.out.println("test:登录失败");
					// 登录失败
					adUtils.create("登录失败", "请重试!");
					isLoginOK = false;
				}
				adUtils.setOnDismissListener(new OnDismissListener() {
					@Override
					public void afterDismiss() {
						if (isLoginOK) {
							isLoginOK = false;
							/**
							 * 1. 保存用户名以及密码 2. 跳转页面
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
//						// 获取返回码,并解析--int
//						int respCode = Integer.parseInt(sb.toString());
//						AlertDialogUtil adUtils = AlertDialogUtil
//								.instantAlertDialogUtil(mActivity);
//						System.out.println("test:respCode:" + respCode);
//						// 取消稍等模版
//						adUtils.dismissWaiting();
//						if (respCode == 200) {
//							System.out.println("test:登录成功");
//							// 登录成功
//							isLoginOK = true;
//							adUtils.create("登录成功", "", "好");
//						} else {
//							System.out.println("test:登录失败");
//							// 登录失败
//							adUtils.create("登录失败", "请重试!", "好");
//							isLoginOK = false;
//						}
//						adUtils.setOnDismissListener(new OnDismissListener() {
//							@Override
//							public void afterDismiss() {
//								if (isLoginOK) {
//									isLoginOK = false;
//									/**
//									 * 1. 保存用户名以及密码 2. 跳转页面
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
