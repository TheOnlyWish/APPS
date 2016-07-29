package com.king.loadnote.pager;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.king.loadnote.R;
import com.king.loadnote.activity.MainActivity;

/**
 * ����pager�Ļ���
 * 
 * @author Administrator
 * 
 */
public class BaseContentPager implements OnClickListener {

	// ������
	public View rootView;
	// �����Ķ���
	public Activity mActivity;
	// �˵���ť
	public LinearLayout btnMenuToggle;
	// ����
	public TextView baseTitleText;
	// �Զ���Ĺ���ģ��
	public LinearLayout functions;
	// ����
	public LinearLayout btnMore;
	// �����image
	public ImageView imageMenuToggle;
	// frameLayout ����
	public FrameLayout baseContent;
	private RotateAnimation open;
	private RotateAnimation close;
	// popuWindow �� View
	public View ppView;
	public ListView popListView;
	public ImageView whiteBg;
	public PopupWindow ppw;

	public BaseContentPager(Activity activity) {
		this.mActivity = activity;
		initView();
	}

	/**
	 * ��ʼ��View
	 */
	public void initView() {
		rootView = View.inflate(mActivity, R.layout.pager_base_content, null);
		ppView = View.inflate(mActivity, R.layout.view_pop, null);
		popListView = (ListView) ppView.findViewById(R.id.pop_listView);

		btnMenuToggle = (LinearLayout) rootView
				.findViewById(R.id.base_btn_menu_toggle);
		baseTitleText = (TextView) rootView.findViewById(R.id.base_title_text);
		functions = (LinearLayout) rootView.findViewById(R.id.functions);
		btnMore = (LinearLayout) rootView.findViewById(R.id.btn_more);
		baseContent = (FrameLayout) rootView.findViewById(R.id.base_content_pager);
		imageMenuToggle = (ImageView) rootView
				.findViewById(R.id.image_menu_toggle);
		whiteBg = (ImageView) rootView.findViewById(R.id.whilte_bg);
		
		ppw = new PopupWindow(ppView, 300,
				LayoutParams.WRAP_CONTENT, true);
		ppw.setBackgroundDrawable(mActivity.getResources().getDrawable(
				R.drawable.popup_background));
		ppw.setOutsideTouchable(true);

		// �����Զ���Ķ�����ʽ
		ppw.setAnimationStyle(R.style.showPopupAnimation);

		
		// ��ʼ������
		initAnimation();
		// ��ʼ��������
		initListener();

	}
	
	public PopupWindow getPopuWindow(){
		return ppw;
	}

	private void initListener() {
		// �໬�˵���ť
		btnMenuToggle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// ����򿪲����
				MainActivity mainActivity = (MainActivity) mActivity;
				mainActivity.openLeftDrawer();
			}
		});

		// ���� ��ť
		btnMore.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ppw.showAtLocation(rootView, Gravity.RIGHT, 20, 70);
				
			}
		});

		btnMenuToggle.setOnClickListener(this);
		btnMore.setOnClickListener(this);

	}

	private void initAnimation() {

		open = new RotateAnimation(0, 90, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		open.setDuration(500);

		close = new RotateAnimation(90, 0, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		open.setDuration(500);

	}
	
	public void dissmissPPw(){
		if(ppw.isShowing()){
			System.out.println("ppw:listener:" + ppw.isShowing());
			ppw.dismiss();
		}
	}

	/**
	 * ��ʼ������
	 */
	public void initData() {

	}

	/**
	 * ���ò˵�����ʱ�˵���ť���� �������
	 */
	public void setMenuOpenAnimation() {
		close.cancel();
		imageMenuToggle.startAnimation(open);
	}

	/**
	 * ���ò˵����ر��ǲ˵���ť����
	 */
	public void setMenuCloseAnimation() {
		open.cancel();
		imageMenuToggle.startAnimation(close);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.base_btn_menu_toggle:

			// ����򿪲����
			MainActivity mainActivity = (MainActivity) mActivity;
			mainActivity.openLeftDrawer();

			break;
		case R.id.btn_more:

//			System.out.println("click btnMore");
//			// ����popuWindow����
//			PopupWindow ppw = new PopupWindow(ppView, 250,
//					LayoutParams.WRAP_CONTENT, true);
//			ppw.setBackgroundDrawable(mActivity.getResources().getDrawable(
//					R.drawable.popu_white_bg_shadow));
//			ppw.setOutsideTouchable(true);
//
//			// �����Զ���Ķ�����ʽ
//			ppw.setAnimationStyle(R.style.showPopupAnimation);

			// ppw.showAsDropDown(ppView, 0, 0);
			ppw.showAtLocation(rootView, Gravity.RIGHT + Gravity.TOP, 20, 55);

			break;

		default:
			break;
		}

	}

	public LinearLayout getBtnMenuToggle() {
		return btnMenuToggle;
	}

	public TextView getBaseTitleText() {
		return baseTitleText;
	}

	public ImageView getImageMenuToggle() {
		return imageMenuToggle;
	}

}
