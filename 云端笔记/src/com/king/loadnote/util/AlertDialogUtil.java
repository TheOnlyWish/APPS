package com.king.loadnote.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.util.DisplayMetrics;
import android.view.ContextThemeWrapper;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.king.loadnote.R;

/**
 * AlertDialog工具类 单例模式
 * 
 * @author Administrator
 * 
 */
public class AlertDialogUtil implements OnClickListener {

	// 唯一对象
	private static AlertDialogUtil util = new AlertDialogUtil();
	// 上下文
	private static Activity mActivity;
	// 布局文件id
	private View view;
	private TextView title;
	private TextView content;
	private TextView ok;
	public OnDismissListener onDismissListener;
	private int width;
	private Window adWindow;
	private static AlertDialog ad;

	private AlertDialogUtil() {

		initView();
		initAdlWindowData();
		initListener();

	}

	private void initAdlWindowData() {
		AlertDialog.Builder builder = new Builder(new ContextThemeWrapper(
				mActivity, R.style.Theme_Transparent));
		ad = builder.create();
		
		
		// 获取窗体对象
		Window window = ad.getWindow();
		// 手机窗体对象
		Display display = mActivity.getWindowManager().getDefaultDisplay();
		DisplayMetrics out = new DisplayMetrics();
		display.getMetrics(out);
		// 像素密度
		float density = out.density;
		// 手机屏幕宽高
		width = display.getWidth();
		int height = display.getHeight();

	}

	public boolean isShowDialog(){
		if(ad != null){
			return ad.isShowing();
		}
		return false;
	}
	
	private void initView() {
		// 初始化tip提示框
		view = View.inflate(mActivity, R.layout.view_alertdialog, null);

		title = (TextView) view.findViewById(R.id.ad_title);
		content = (TextView) view.findViewById(R.id.ad_content);
		ok = (TextView) view.findViewById(R.id.ad_ok);

	}

	public void initListener() {

	}

	@Override
	public void onClick(View v) {
		System.out.println("Click:initListener");
		ad.dismiss();
		// 用户自定操作
		/**
		 * 1. 保存用户名以及密码 2. 跳转页面
		 */
		if (onDismissListener != null) {
			onDismissListener.afterDismiss();
		}
	}

	public void setOnDismissListener(OnDismissListener listener) {
		this.onDismissListener = listener;
	}

	public interface OnDismissListener {
		public void afterDismiss();
	}

	/**
	 * 对外暴露 1. 获取util对象 2. 获取外来必要数据对象
	 * 
	 * @return
	 */
	public static AlertDialogUtil instantAlertDialogUtil(Activity activity) {
		mActivity = activity;
		return util;
	}

	/**
	 * 创建alertDialog对话框
	 * 
	 * @param title
	 * @param content
	 * @param btn
	 */
	public void create(String title_, String content_) {
		// 初始化tip提示框
		view = View.inflate(mActivity, R.layout.view_alertdialog, null);

		title = (TextView) view.findViewById(R.id.ad_title);
		content = (TextView) view.findViewById(R.id.ad_content);
		ok = (TextView) view.findViewById(R.id.ad_ok);

		this.title.setText(title_);
		this.content.setText(content_);

		ok.setOnClickListener(this);

		// 创建AlertDialog对象
		AlertDialog.Builder builder = new Builder(new ContextThemeWrapper(
				mActivity, R.style.Theme_Transparent));
		ad = builder.create();
		
		// 获取窗体对象
		Window window = ad.getWindow();

		System.out.println();
		ad.setView(view, 0, 0, 0, 0);

		ad.show();

		window.setLayout(width, 390);
		window.setGravity(Gravity.CENTER);

	}

	/**
	 * 稍等框
	 */
	public void create() {
		// 初始化tip提示框
		view = View.inflate(mActivity, R.layout.view_alertdialog_waiting, null);

		// 创建AlertDialog对象
		AlertDialog.Builder builder = new Builder(new ContextThemeWrapper(
				mActivity, R.style.Theme_Transparent));
		ad = builder.create();
		// 获取窗体对象
		Window window = ad.getWindow();

		ad.setView(view, 0, 0, 0, 0);

		ad.show();

		ad.setCanceledOnTouchOutside(false);
		
		window.setLayout(width, 390);
		window.setGravity(Gravity.CENTER);

	}
	
	/**
	 * 取消waiting
	 */
	public void dismissWaiting(){
		ad.dismiss();
	}

}
