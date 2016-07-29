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
 * AlertDialog������ ����ģʽ
 * 
 * @author Administrator
 * 
 */
public class AlertDialogUtil implements OnClickListener {

	// Ψһ����
	private static AlertDialogUtil util = new AlertDialogUtil();
	// ������
	private static Activity mActivity;
	// �����ļ�id
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
		
		
		// ��ȡ�������
		Window window = ad.getWindow();
		// �ֻ��������
		Display display = mActivity.getWindowManager().getDefaultDisplay();
		DisplayMetrics out = new DisplayMetrics();
		display.getMetrics(out);
		// �����ܶ�
		float density = out.density;
		// �ֻ���Ļ���
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
		// ��ʼ��tip��ʾ��
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
		// �û��Զ�����
		/**
		 * 1. �����û����Լ����� 2. ��תҳ��
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
	 * ���Ⱪ¶ 1. ��ȡutil���� 2. ��ȡ������Ҫ���ݶ���
	 * 
	 * @return
	 */
	public static AlertDialogUtil instantAlertDialogUtil(Activity activity) {
		mActivity = activity;
		return util;
	}

	/**
	 * ����alertDialog�Ի���
	 * 
	 * @param title
	 * @param content
	 * @param btn
	 */
	public void create(String title_, String content_) {
		// ��ʼ��tip��ʾ��
		view = View.inflate(mActivity, R.layout.view_alertdialog, null);

		title = (TextView) view.findViewById(R.id.ad_title);
		content = (TextView) view.findViewById(R.id.ad_content);
		ok = (TextView) view.findViewById(R.id.ad_ok);

		this.title.setText(title_);
		this.content.setText(content_);

		ok.setOnClickListener(this);

		// ����AlertDialog����
		AlertDialog.Builder builder = new Builder(new ContextThemeWrapper(
				mActivity, R.style.Theme_Transparent));
		ad = builder.create();
		
		// ��ȡ�������
		Window window = ad.getWindow();

		System.out.println();
		ad.setView(view, 0, 0, 0, 0);

		ad.show();

		window.setLayout(width, 390);
		window.setGravity(Gravity.CENTER);

	}

	/**
	 * �Եȿ�
	 */
	public void create() {
		// ��ʼ��tip��ʾ��
		view = View.inflate(mActivity, R.layout.view_alertdialog_waiting, null);

		// ����AlertDialog����
		AlertDialog.Builder builder = new Builder(new ContextThemeWrapper(
				mActivity, R.style.Theme_Transparent));
		ad = builder.create();
		// ��ȡ�������
		Window window = ad.getWindow();

		ad.setView(view, 0, 0, 0, 0);

		ad.show();

		ad.setCanceledOnTouchOutside(false);
		
		window.setLayout(width, 390);
		window.setGravity(Gravity.CENTER);

	}
	
	/**
	 * ȡ��waiting
	 */
	public void dismissWaiting(){
		ad.dismiss();
	}

}
