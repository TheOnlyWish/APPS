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
		title = "注册";
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
		// 点击"获取验证码"按钮
		btn_checkCode.setOnClickListener(this);
		regist.setOnClickListener(this);
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
	 * 事件点击监听
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
		// 点击"注册"按钮
		case R.id.regist:
			/**
			 * 1. 判断电话号码是否为空 2. 检验电话好码格式是否正确 3. 检查密码是否为空 4. 检查密码是否超过12位
			 */
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
			} else if (TextUtils.isEmpty(code)) {
				adUtil.create("验证码为空", "请输入验证码");
			} else {
				// 注册开始
				userRegist(tel, pass, code);

				// 显示稍等模版
				adUtil.create();
			}
			break;
		// 点击"获取验证码"按钮
		case R.id.btn_checkCode:
			// 设置按钮不可点击
			if (TextUtils.isEmpty(tel)) {
				adUtil.create("电话号码为空", "请输入电话号码");
				break;
			} else if (!FormatterUtil.isPhoneNumberValid(tel)) {
				adUtil.create("电话号码错误", "请修改电话号码");
				break;
			} else {
				// 服务端请求成功
				adUtil.create("请等待验证码", "发送中...");
				/**
				 * 发送请求给服务器，获取验证码 参数: 1. 电话号码 2. 时间字符串 3. 时间毫秒
				 */
				btn_checkCode.setEnabled(false);
				btn_checkCode.setBackgroundColor(Color.GRAY);
				requestCheckCode(tel);
				// 刷新UI
				reflashCheckCodeBtn();
			}
			break;
		default:
			break;
		}
	}

	// 消息处理
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			// 判断结束
			if (msg.what <= 0) {
				// 取消计时器
				timer.cancel();
				btn_checkCode.setEnabled(true);
				btn_checkCode.setBackgroundResource(R.drawable.select_btn_lr);
				btn_checkCode.setText("获取验证码");
				return;
			}
			// 刷新UI
			btn_checkCode.setText("还剩" + msg.what + "秒");
		};
	};
	// 计时器对象
	private Timer timer;
	private EditText checkCode;

	/**
	 * 刷新间隔UI60秒
	 */
	private void reflashCheckCodeBtn() {
		TimerTask task = new TimerTask() {
			int amount = 60;

			// 执行的代码
			@Override
			public void run() {
				// 发送刷新UI的消息
				handler.sendEmptyMessage(--amount);
			}
		};
		timer = new Timer();
		timer.schedule(task, 0, 1000);

	}

	/**
	 * 用户注册
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
				// 获取返回码
				int respCode = Integer.parseInt(arg0.result);
				System.out.println("test:regist:respCode:" + respCode);
				// 取消稍等模版
				adUtil.dismissWaiting();
				if (respCode == 200) {
					adUtil.dismissWaiting();
					System.out.println("test:regist:200:");
					
					// 注册成功
					isRegistOK = true;
					adUtil.create("注册成功", "   ");
					System.out.println("test:adUtil.create(注册成功)");
				} else {
					// 注册失败
					if (respCode == 300) {
						adUtil.dismissWaiting();
						System.out.println("test:regist 返回码是300");
						adUtil.create("该用户已经注册", "请重试!");
					} else {
						System.out.println("test:regist 返回码是400");
						adUtil.create("注册失败", "验证码过时，失效请重试!");
					}
					isRegistOK = false;
				}
				adUtil.setOnDismissListener(new OnDismissListener() {
					@Override
					public void afterDismiss() {
						if (isRegistOK) {
							isRegistOK = false;
							/**
							 * 1. 保存用户名以及密码 2. 跳转页面
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
//						// 获取返回码
//						int respCode = Integer.parseInt(sb.toString());
//						System.out.println("test:regist:respCode:" + respCode);
//						// 取消稍等模版
//						adUtil.dismissWaiting();
//						if (respCode == 200) {
//							adUtil.dismissWaiting();
//							System.out.println("test:regist:200:");
//							
//							// 注册成功
//							isRegistOK = true;
//							adUtil.create("注册成功", "   ", "好");
//							System.out.println("test:adUtil.create(注册成功)");
//						} else {
//							// 注册失败
//							if (respCode == 300) {
//								adUtil.dismissWaiting();
//								System.out.println("test:regist 返回码是300");
//								adUtil.create("该用户已经注册", "请重试!", "好");
//							} else {
//								System.out.println("test:regist 返回码是400");
//								adUtil.create("注册失败", "验证码过时，失效请重试!", "好");
//							}
//							isRegistOK = false;
//						}
//						adUtil.setOnDismissListener(new OnDismissListener() {
//							@Override
//							public void afterDismiss() {
//								if (isRegistOK) {
//									isRegistOK = false;
//									/**
//									 * 1. 保存用户名以及密码 2. 跳转页面
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
	 * 请求checkCode数据 附加: 当前时间字符串 当前时间毫秒值
	 * 
	 * @param tel
	 */
	private void requestCheckCode(final String tel) {
		System.out.println("test:requestCheckCode");
		// 请求验证码数据
		new Thread() {
			public void run() {
				/**
				 * 传参:电话号码，当前时间
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
					System.out.println("test:******getResponseCode："
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
						System.out.println("test:getResponseCode:得到的服务器返回的数据:"
								+ sb.toString());
						// 获取返回码
						int respCode = Integer.parseInt(sb.toString());
						System.out.println("test:respCode：" + respCode);
						if (respCode == 200) {

						} else {
							AlertDialogUtil adUtil = AlertDialogUtil
									.instantAlertDialogUtil(mActivity);
							// 服务端请求失败
							adUtil.create("验证码请求失败", "验证码被吸进时空隧道了，请重试");
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
